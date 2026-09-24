package com.bio.sequencing.workers;

import com.bio.sequencing.port.Port;
import com.bio.sequencing.sequence.Sequence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

public class ClustalOWorker extends BaseWorker {

    private final StringBuilder pendingFasta = new StringBuilder();
    private String firstSequenceId;
    public ClustalOWorker(
            Port input,
            Port output) {

        super(input, output);
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
                "pegi3s/clustalomega",
                "--auto",
                "--infile=-"
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
                System.out.println("clustalo aligned result");
                System.out.println(result);
                outputSequences(result);
                output.close();
                done = true;
            }
            return;
        }

        Sequence sequence = (Sequence) inputData;
        System.out.println(sequence.getData());
        String fasta = sequence.getData();

        fasta = sequenceData(sequence, "ClustalO");

        if (firstSequenceId == null) {
            firstSequenceId = sequence.getId();
        }
        pendingFasta.append(fasta).append(System.lineSeparator());
    }

    @Override
    public boolean isDone() {
        return done;
    }
}




