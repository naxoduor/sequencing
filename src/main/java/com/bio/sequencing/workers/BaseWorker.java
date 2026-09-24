package com.bio.sequencing.workers;

import com.bio.sequencing.port.Port;
import com.bio.sequencing.sequence.Sequence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public abstract class BaseWorker implements Worker {

	protected final Port input;
	protected final Port output;
	protected boolean done;
	protected Process process;
	protected BufferedWriter writer;
	protected BufferedReader reader;
	protected BufferedReader errorReader;

	protected BaseWorker(Port input, Port output) {
		this.input = input;
		this.output = output;
	}

	protected BaseWorker() {
		this(null, null);
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

	protected void outputSequences() throws IOException, InterruptedException {
		CompletableFuture<String> errors = CompletableFuture.supplyAsync(() -> {
			try (BufferedReader outputErrorReader = errorReader) {
				return outputErrorReader.lines()
						.collect(Collectors.joining(System.lineSeparator()));
			} catch (IOException exception) {
				throw new UncheckedIOException(exception);
			}
		});
	
		String currentId = null;
		StringBuilder currentRecord = null;
		try (BufferedReader outputReader = reader) {
			String line;
			while ((line = outputReader.readLine()) != null) {
				if (line.startsWith(">")) {

					if (currentRecord != null) {
						Sequence resultSequence = new Sequence(currentId, currentRecord.toString());
						System.out.println(resultSequence.getData());
						output.put(resultSequence);
					}
					currentId = line.substring(1).trim();
					currentRecord = new StringBuilder();
				}

				if (currentRecord != null) {
					currentRecord.append(line).append(System.lineSeparator());
				}

			}
		}

		if (currentRecord != null) {
			Sequence resultSequence = new Sequence(currentId, currentRecord.toString());
			System.out.println(resultSequence.getData());
			output.put(resultSequence);
		}

		int exitCode = process.waitFor();
		String errorOutput = errors.join();
		if (exitCode != 0) {
			System.out.println(errorOutput);
			throw new RuntimeException(
					getClass().getSimpleName() + " failed with exit code "
							+ exitCode + ": " + errorOutput);
		}
	}

	public void flush() throws IOException {
		writer.flush();
	}

	public void align(String fasta) throws Exception {
		if (process == null) {
			throw new IllegalStateException(
					getClass().getSimpleName() + " has not been initialized");
		}

		writer.write(alignmentInput(fasta));
	}

	protected String alignmentInput(String fasta) {
		return fasta + System.lineSeparator();
	}

	
}
