package com.bio.sequencing.workers;


import com.bio.sequencing.port.Port;
import com.bio.sequencing.sequence.Sequence;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileWriterWorker implements Worker{
    private final StringBuilder pendingFasta = new StringBuilder();
    private  Path output;
    private Port input;
    private BufferedWriter writer;

    private boolean done = false;

    public FileWriterWorker(
            Path output,
            Port input) {
                this.output = output;
                this.input = input;
    }

    @Override
    public void init() {
        try {
            writer = Files.newBufferedWriter(output);
        } catch (IOException e) {
            throw new RuntimeException("Failed to open output file: " + output, e);
        }
    }

    @Override
    public void tick() throws Exception {
        Object inputData = input.get();
        if (inputData == null) {
            if (input.isClosed()) {
                if (pendingFasta.length() > 0) {
                    writer.write(pendingFasta.toString());
                    pendingFasta.setLength(0);
                }
                writer.close();
                done = true;
            }
            return;
        }

        Sequence sequence = (Sequence) inputData;

        String fasta = sequenceData(sequence, "MAFFT");

        pendingFasta.append(fasta).append(System.lineSeparator());
    }

    protected String sequenceData(Sequence sequence, String workerName) {
		String data = sequence.getData();
		if (data == null || data.isBlank()) {
			throw new IllegalArgumentException(
					workerName + " input sequence '" + sequence.getId()
							+ "' has no residues");
		}
		return data;
	}

    @Override
    public boolean isDone() {
        return done;
    }
}