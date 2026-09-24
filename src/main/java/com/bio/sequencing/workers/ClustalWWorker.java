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
        Object inputData = input.get();
        if (inputData == null) {
            if (input.isClosed()) {
                flush();
                writer.close();
                outputSequences();
                System.out.println("ClustalW aligned result");
                output.close();
                done = true;
            }
            return;
        }

        Sequence sequence = (Sequence) inputData;

        String fasta = sequenceData(sequence, "ClustalW");
        align(fasta);
    }

    @Override
    public boolean isDone() {
        return done;
    }
}




