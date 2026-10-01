package com.bio.sequencing.plugins.external_tool_support.cufflinks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

public class CufflinksTask implements Callable<Void> {

    private final String taskId = UUID.randomUUID().toString();
    private final CufflinksTaskSettings settings;

    private final List<TaskListener> listeners =
            new CopyOnWriteArrayList<>();

    public CufflinksTask(CufflinksTaskSettings settings) {
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

        command.add(settings.cufflinksExecutable());
        command.add("-o");
        command.add(settings.outputDirectory());
        command.add("-p");
        command.add(String.valueOf(settings.threads()));

        if (settings.referenceGtf() != null
                && !settings.referenceGtf().isBlank()) {
            command.add("-G");
            command.add(settings.referenceGtf());
        }

        if (settings.referenceSequence() != null
                && !settings.referenceSequence().isBlank()) {
            command.add("-b");
            command.add(settings.referenceSequence());
        }

        command.add("-u");
        command.add(settings.inputBam());

        return command;
    }

    @Override
    public Void call() throws Exception {
        notifyStarted();

        try {
            Path outputDir = Path.of(settings.outputDirectory());
            Files.createDirectories(outputDir);

            Path bam = Path.of(settings.inputBam());
            if (!Files.isRegularFile(bam)) {
                throw new IllegalArgumentException(
                        "Input BAM file does not exist: " + bam
                );
            }

            List<String> command = buildCommand();

            notifyLog("Executing: " + String.join(" ", command));

            ProcessBuilder builder = new ProcessBuilder(command);
            builder.directory(
                    Path.of(settings.workingDirectory()).toFile()
            );
            builder.redirectErrorStream(true);

            Process process = builder.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    notifyLog(line);
                }
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException(
                        "Cufflinks failed with exit code: " + exitCode
                );
            }

            notifyCompleted();
            return null;

        } catch (Exception e) {
            notifyFailed(e);
            throw e;
        }
    }

    public String getTaskId() {
        return taskId;
    }
}