package com.bio.sequencing.workers;

import com.bio.sequencing.port.Port;
import com.bio.sequencing.sequence.Sequence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

public class ClustalWWorker extends BaseWorker {

    private final StringBuilder pendingFasta = new StringBuilder();
    private String firstSequenceId;
    public ClustalWWorker(
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
                System.out.println("clustalw aligned");
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

        fasta = sequenceData(sequence, "Clustalw");

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




