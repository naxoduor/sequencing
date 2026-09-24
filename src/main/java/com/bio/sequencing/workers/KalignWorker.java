package com.bio.sequencing.workers;


import com.bio.sequencing.port.Port;
import com.bio.sequencing.sequence.Sequence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

public class KalignWorker extends BaseWorker {

    public KalignWorker(
            Port input,
            Port output) {

        super(input, output);
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
        Object inputData = input.get();
        if (inputData == null) {
            if (input.isClosed()) {
                flush();
                writer.close();
                outputSequences();
                System.out.println("Kalign aligned result");
                output.close();
                done = true;
            }
            return;
        }

        Sequence sequence = (Sequence) inputData;

        String fasta = sequenceData(sequence, "Kalign");
        align(fasta);
    }

    @Override
    protected String alignmentInput(String fasta) {
        return ">sequence1" + System.lineSeparator() + super.alignmentInput(fasta);
    }

    @Override
    public boolean isDone() {
        return done;
    }
}