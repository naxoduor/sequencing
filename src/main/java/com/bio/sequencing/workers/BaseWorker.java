package com.bio.sequencing.workers;

import com.bio.sequencing.port.Port;
import com.bio.sequencing.sequence.Sequence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

	protected void outputSequences(String fasta) {
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

	public String align(String fasta) throws Exception {
		if (process == null) {
			throw new IllegalStateException(
					getClass().getSimpleName() + " has not been initialized");
		}

		try (BufferedWriter inputWriter = writer) {
			inputWriter.write(alignmentInput(fasta));
			inputWriter.flush();
		}

		String output;
		try (BufferedReader outputReader = reader) {
			output = outputReader.lines()
					.collect(Collectors.joining(System.lineSeparator()));
		}

		String errors;
		try (BufferedReader outputErrorReader = errorReader) {
			errors = outputErrorReader.lines()
					.collect(Collectors.joining(System.lineSeparator()));
		}

		int exitCode = process.waitFor();
		if (exitCode != 0) {
			throw new RuntimeException(
					getClass().getSimpleName() + " failed with exit code "
							+ exitCode + ": " + errors);
		}

		return output;
	}

	protected String alignmentInput(String fasta) {
		return fasta + System.lineSeparator();
	}

	
}
