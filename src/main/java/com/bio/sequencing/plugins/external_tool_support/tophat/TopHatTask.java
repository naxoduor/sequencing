package com.bio.sequencing.plugins.external_tool_support.tophat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

public class TopHatTask implements Callable<Void> {

    private final String taskId = UUID.randomUUID().toString();
    private final TopHatTaskSettings settings;

    private final List<TaskListener> listeners =
            new CopyOnWriteArrayList<>();

    public TopHatTask(TopHatTaskSettings settings) {
        this.settings = settings;
    }

    public void addListeners(List<TaskListener> newListeners) {
        listeners.addAll(newListeners);
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

    private void notifyFailed(Throwable error) {
        listeners.forEach(l -> l.onFailed(taskId, error));
    }

    private List<String> buildCommand() {
        List<String> command = new ArrayList<>();

        command.add(settings.tophatExecutable());
        command.add("-o");
        command.add(settings.outputDirectory());
        command.add("-p");
        command.add(String.valueOf(settings.threads()));

        command.add("--read-mismatches");
        command.add(String.valueOf(settings.readMismatches()));

        command.add("--read-gap-length");
        command.add(String.valueOf(settings.readGapLength()));

        command.add("--read-edit-dist");
        command.add(String.valueOf(settings.readEditDistance()));

        if (settings.annotationGtf() != null
                && !settings.annotationGtf().isBlank()) {
            command.add("-G");
            command.add(settings.annotationGtf());
        }

        command.add(settings.referenceIndex());
        command.add(settings.inputFastq1());

        if (settings.isPairedEnd()) {
            command.add(settings.inputFastq2());
        }

        return command;
    }

    @Override
    public Void call() throws Exception {
        notifyStarted();

        try {
            validateInputs();

            Path outputDir =
                    Path.of(settings.outputDirectory());

            Files.createDirectories(outputDir);

            List<String> command = buildCommand();

            notifyLog("Executing TopHat: "
                    + String.join(" ", command));

            ProcessBuilder builder =
                    new ProcessBuilder(command);

            builder.directory(
                    Path.of(settings.workingDirectory()).toFile()
            );

            builder.redirectErrorStream(true);

            Process process = builder.start();

            try (BufferedReader reader =
                         new BufferedReader(
                                 new InputStreamReader(
                                         process.getInputStream()))) {

                String line;

                while ((line = reader.readLine()) != null) {
                    notifyLog(line);
                }
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException(
                        "TopHat failed with exit code: " + exitCode
                );
            }

            Path acceptedHits =
                    outputDir.resolve("accepted_hits.bam");

            if (!Files.isRegularFile(acceptedHits)
                    || Files.size(acceptedHits) == 0) {
                throw new RuntimeException(
                        "TopHat completed without producing "
                                + "a non-empty accepted_hits.bam"
                );
            }

            notifyLog("BAM output: " + acceptedHits);
            notifyCompleted();

            return null;

        } catch (Exception e) {
            notifyFailed(e);
            throw e;
        }
    }

    private void validateInputs() {
        requireFile(settings.inputFastq1(), "FASTQ input 1");

        if (settings.isPairedEnd()) {
            requireFile(settings.inputFastq2(), "FASTQ input 2");
        }

        if (settings.annotationGtf() != null
                && !settings.annotationGtf().isBlank()) {
            requireFile(settings.annotationGtf(), "Annotation GTF");
        }

        if (settings.referenceIndex() == null
                || settings.referenceIndex().isBlank()) {
            throw new IllegalArgumentException(
                    "Reference genome index is required"
            );
        }

        if (settings.threads() < 1) {
            throw new IllegalArgumentException(
                    "Thread count must be at least 1"
            );
        }
    }

    private void requireFile(String file, String description) {
        if (file == null || file.isBlank()
                || !Files.isRegularFile(Path.of(file))) {
            throw new IllegalArgumentException(
                    description + " does not exist: " + file
            );
        }
    }

    public String getTaskId() {
        return taskId;
    }
}