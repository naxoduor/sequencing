package com.bio.sequencing.plugins.external_tool_support.trimmomatic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

public class TrimmomaticTask implements Callable<Void> {

    private final String taskId = UUID.randomUUID().toString();
    private final TrimmomaticTaskSettings settings;
    private final List<TaskListener> listeners = new java.util.concurrent.CopyOnWriteArrayList<>();

    public TrimmomaticTask(TrimmomaticTaskSettings settings) {
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

    @Override
    public Void call() throws Exception {
        notifyStarted();

        try {
            List<String> command = List.of(
                    "java",
                    "-jar", settings.trimmomaticJar(),
                    "SE",
                    "-threads", String.valueOf(settings.threads()),
                    "-phred33",
                    settings.inputFile(),
                    settings.outputFile(),
                    "ILLUMINACLIP:" + settings.adaptersFile() + ":2:30:10",
                    "LEADING:3",
                    "TRAILING:3",
                    "SLIDINGWINDOW:4:15",
                    "MINLEN:36"
            );

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(
                    Path.of(settings.workingDirectory()).toFile()
            );
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

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
                        "Trimmomatic failed with exit code: " + exitCode
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