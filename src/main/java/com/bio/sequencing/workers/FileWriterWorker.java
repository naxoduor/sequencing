package com.bio.sequencing.workers;


import com.bio.sequencing.port.Port;
import com.bio.sequencing.sequence.Sequence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileWriterWorker implements Worker {

    private final Port input;
    private final Path output;

    private Process process;
    private BufferedWriter writer;
    private BufferedReader reader;
    private BufferedReader errorReader;

    private boolean done = false;

    public FileWriterWorker(
            Path output,
            Port input) {

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
                "openeuler/kalign",
                "kalign",
                "-i", "-",
                "-o", "-"
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

        if (!input.hasData()) {
            return;
        }

        Sequence sequence =
                (Sequence) input.get();

        String result=align(sequence.getData());
        outputSequences(result);

        System.out.println(
                "Kalign Aligning: " + sequence.getData()
        );

        Sequence resultSequence = new Sequence(sequence.getId(), result);

//        output.put(resultSequence);

        done = true;
    }
    public String align(String fasta) throws Exception {
        System.out.println("mafft align");
        if (process == null) {
            throw new IllegalStateException("MAFFT worker has not been initialized");
        }

        // Write FASTA sequence to MAFFT stdin
        BufferedWriter inputWriter = writer;
        try (inputWriter) {
            inputWriter.write(">sequence1");
            inputWriter.newLine();
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
                    "MAFFT failed with exit code " + exitCode +
                            ": " + errors
            );
        }

        return output;
    }

    private void outputSequences(String fasta) {

    }
    @Override
    public boolean isDone() {
        return done;
    }
}