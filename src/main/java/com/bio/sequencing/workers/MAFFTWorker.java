package com.bio.sequencing.workers;

import com.bio.sequencing.port.Port;
import com.bio.sequencing.sequence.Sequence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.stream.Collectors;

public class MAFFTWorker implements Worker {

    private final Port input;
    private final Port output;

    private Process process;
    private BufferedWriter writer;
    private BufferedReader reader;
    private BufferedReader errorReader;

    private final StringBuilder pendingFasta = new StringBuilder();
    private boolean done = false;

    public MAFFTWorker(
            Port input,
            Port output) {

        this.input = input;
        this.output = output;
    }

    @Override
    public void init() {
        if (process != null) {
            return;
        }

        List<String> command = List.of(
            "docker",
            "run",
            "--rm",
            "-i",
            "pegi3s/mafft",
            "mafft",
            "--auto",
            "-"
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
        Object inputData = input.get();
        if (inputData == null) {
            if (input.isClosed() && pendingFasta.length() > 0) {
                String result = align(pendingFasta.toString());
                outputSequences(result);
                output.close();
                done = true;
            }
            return;
        }

        Sequence sequence = (Sequence) inputData;

        String fasta = sequence.getData();
        if (fasta == null || fasta.isBlank()) {
            throw new IllegalArgumentException(
                "MAFFT input sequence '" + sequence.getId()
                    + "' has no residues");
        }

        pendingFasta.append(fasta).append(System.lineSeparator());
    }
    public String align(String fasta) throws Exception {
        System.out.println("mafft align");
        if (process == null) {
            throw new IllegalStateException("MAFFT worker has not been initialized");
        }

        // Write all FASTA records, then close stdin so MAFFT can finish.
        try (BufferedWriter inputWriter = writer) {
            // inputWriter.write(">sequence1");
            // inputWriter.newLine();
            inputWriter.write(fasta);
            inputWriter.newLine();
            inputWriter.flush();
        }

        // Read aligned FASTA from stdout
        String output;
        try (BufferedReader outputReader = reader) {
            output = outputReader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        }
        System.out.println("collected outpput");
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
                    "Clustal failed with exit code " + exitCode +
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