package com.bio.sequencing.plugins.external_tool_support.clustalw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.Callable;

public class ClustalWTask implements Callable<ClustalWTaskResult> {

    private final String taskId;
    private final ClustalWTaskSettings settings;
    private final List<TaskListener> listeners = new ArrayList<>();

    public ClustalWTask(
            String taskId,
            ClustalWTaskSettings settings) {
        this.taskId = taskId;
        this.settings = settings;
    }

    public void addListener(TaskListener listener) {
        listeners.add(listener);
    }

    @Override
    public ClustalWTaskResult call() throws Exception {
        notifyStarted();

        Path input = Path.of(settings.inputFasta()).toAbsolutePath();
        Path output = Path.of(settings.outputFasta()).toAbsolutePath();
        Path outputDirectory = output.getParent();

        if (!Files.isRegularFile(input)) {
            throw new IOException("Input FASTA file not found: " + input);
        }

        if (outputDirectory != null) {
            Files.createDirectories(outputDirectory);
        }

        List<String> command = buildCommand(input, output);

        notifyLog("Starting ClustalW: " + String.join(" ", command));

        ProcessBuilder processBuilder = new ProcessBuilder(command)
                .redirectErrorStream(true);

        if (settings.workingDirectory() != null
                && !settings.workingDirectory().isBlank()) {
            processBuilder.directory(
                    Path.of(settings.workingDirectory()).toFile()
            );
        }

        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        process.getInputStream(),
                        StandardCharsets.UTF_8
                ))) {
            String line;
            while ((line = reader.readLine()) != null) {
                notifyLog(line);
            }
        } catch (IOException e) {
            process.destroyForcibly();
            throw e;
        }

        int exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            process.destroyForcibly();
            Thread.currentThread().interrupt();
            throw e;
        }

        if (exitCode != 0) {
            throw new IOException(
                    "ClustalW failed with exit code: " + exitCode
            );
        }

        validateOutput(output);

        ClustalWTaskResult result = new ClustalWTaskResult(
                taskId,
                input.toString(),
                output.toString(),
                normalizeFormat(settings.outputFormat()),
                outputDirectory == null ? "" : outputDirectory.toString()
        );

        notifyCompleted();
        return result;
    }

    private List<String> buildCommand(Path input, Path output) {
        List<String> command = new ArrayList<>();

        command.add(settings.clustalwExecutable());
        command.add("-INFILE=" + input);
        command.add("-OUTFILE=" + output);
        command.add("-OUTPUT=" + normalizeFormat(settings.outputFormat()));

        if (settings.outputOrder()) {
            command.add("-OUTORDER=INPUT");
        }

        return command;
    }

    private String normalizeFormat(String format) {
        if (format == null || format.isBlank()) {
            return "FASTA";
        }

        return switch (format.trim().toUpperCase(Locale.ROOT)) {
            case "FASTA", "FA", "PEARSON" -> "FASTA";
            case "CLUSTAL", "CLUSTALW" -> "CLUSTAL";
            case "PHYLIP", "PHYLIP3" -> "PHYLIP";
            case "NEXUS" -> "NEXUS";
            default -> throw new IllegalArgumentException(
                    "Unsupported ClustalW output format: " + format
            );
        };
    }

    private void validateOutput(Path output) throws IOException {
        if (!Files.isRegularFile(output)
                || Files.size(output) == 0) {
            throw new IOException(
                    "ClustalW did not produce an output file: " + output
            );
        }

        String format = normalizeFormat(settings.outputFormat());

        if (format.equals("FASTA")) {
            try (BufferedReader reader = Files.newBufferedReader(output)) {
                boolean hasHeader = reader.lines()
                        .anyMatch(line -> line.startsWith(">"));

                if (!hasHeader) {
                    throw new IOException(
                            "Output does not contain FASTA headers"
                    );
                }
            }
        }
    }

    private void notifyStarted() {
        listeners.forEach(l -> l.onStarted(taskId));
    }

    private void notifyLog(String message) {
        listeners.forEach(l -> l.onLog(taskId, message));
    }

    private void notifyCompleted() {
        listeners.forEach(l -> l.onCompleted(taskId));
    }
}