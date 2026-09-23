package com.bio.sequencing.workers;

import com.bio.sequencing.port.Port;
import com.bio.sequencing.sequence.Sequence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.stream.Collectors;

public class ClustalWWorker implements Worker {

    private final Port input;
    private final Port output;

    private Process process;
    private BufferedWriter writer;
    private BufferedReader reader;
    private BufferedReader errorReader;

    private final StringBuilder pendingFasta = new StringBuilder();
    private String firstSequenceId;
    private boolean done = false;

    public ClustalWWorker(
            Port input,
            Port output) {

        this.input = input;
        this.output = output;
    }

    @Override
    public void init() {
        System.out.println("initialize clustalo");
        if (process != null) {
            return;
        }

        List<String> command = List.of(
                "docker",
                "run",
                "--rm",
                "-i",
                "jcuhpc/clustalw",
                "sh",
                "-c",
                "cat > /tmp/input.fasta && clustalw -INFILE=/tmp/input.fasta -OUTFILE=/dev/stdout -OUTPUT=FASTA -QUIET"

        );


        try {
            process = new ProcessBuilder(command).start();
            writer = new BufferedWriter(
                    new OutputStreamWriter(process.getOutputStream()));
            reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));
            System.out.println("Aligner initialized");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize MAFFT", e);
        }
    }

    @Override
    public void tick() throws Exception {
        if (done) {
            return;
        }

        Object inputData = input.get();
        if (inputData == null) {
            if (input.isClosed() && pendingFasta.length() > 0) {
                String fasta = pendingFasta.toString();
                pendingFasta.setLength(0);
                String result = align(fasta);
                outputSequences(result);
                output.close();
                done = true;
            }
            return;
        }

        Sequence sequence = (Sequence) inputData;
        System.out.println(sequence.getData());
        String fasta = sequence.getData();

        if (fasta == null || fasta.isBlank()) {
            throw new IllegalArgumentException(
                    "Clustalw input sequence '" + sequence.getId()
                            + "' has no residues");
        }

        if (firstSequenceId == null) {
            firstSequenceId = sequence.getId();
        }
        pendingFasta.append(fasta).append(System.lineSeparator());
    }

    static String normalizeSequence(String data) {
        if (data == null) {
            return "";
        }

        return data.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .filter(line -> !line.startsWith(">"))
                .collect(Collectors.joining());
    }

    public String align(String fasta) throws Exception {
        System.out.println("clustalw align");
        System.out.println(fasta);
        if (process == null) {
            throw new IllegalStateException("ClustalO worker has not been initialized");
        }

        // Write all FASTA records, then close stdin so ClustalO can finish.
        try (BufferedWriter inputWriter = writer) {
            inputWriter.write(fasta);
            inputWriter.flush();
        }

        // Read aligned FASTA from stdout
        String output;
        try (BufferedReader outputReader = reader) {
            output = outputReader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        }
        System.out.println("collected outpputtttttt in clustalworker");
        System.out.println(output);

        // Read MAFFT errors
        String errors;
        try (BufferedReader outputErrorReader = errorReader) {
            errors = outputErrorReader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        }

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException(
                    "Clustalw failed with exit code " + exitCode +
                            ": " + errors
            );
        }

        return output;
    }

    private void outputSequences(String fasta) {
        StringBuilder currentRecord = null;
        String currentId = null;

        for (String line : fasta.lines().toList()) {
            if (line.startsWith(">")) {
                if (currentRecord != null) {
                    output.put(new Sequence(currentId, currentRecord.toString()));
                }
                currentId = line.substring(1).trim();
                currentRecord = new StringBuilder();
            }

            if (currentRecord != null) {
                currentRecord.append(line).append(System.lineSeparator());
            }
        }

        if (currentRecord != null) {
            output.put(new Sequence(currentId, currentRecord.toString()));
        }
    }

    @Override
    public boolean isDone() {
        return done;
    }
}




