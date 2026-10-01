package com.bio.sequencing.plugins.external_tool_support.stringtie;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

public class StringTieTask implements Callable<Void> {

    private final String taskId = UUID.randomUUID().toString();
    private final StringTieTaskSettings settings;

    private final List<TaskListener> listeners =
            new CopyOnWriteArrayList<>();

    public StringTieTask(StringTieTaskSettings settings) {
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

        command.add(settings.stringTieExecutable());
        command.add(settings.inputBam());

        command.add("-p");
        command.add(String.valueOf(settings.threads()));

        command.add("-o");
        command.add(settings.outputGtf());

        command.add("-l");
        command.add("sample_" + taskId.substring(0, 8));

        command.add("-m");
        command.add(String.valueOf(
                settings.minimumTranscriptLength()
        ));

        command.add("-c");
        command.add(String.valueOf(
                settings.minimumAbundance()
        ));

        if (settings.referenceGtf() != null
                && !settings.referenceGtf().isBlank()) {
            command.add("-G");
            command.add(settings.referenceGtf());
        }

        if (settings.estimateAbundance()) {
            command.add("-e");
        }

        return command;
    }

    @Override
    public Void call() throws Exception {
        notifyStarted();

        try {
            validateInputs();

            Path outputDirectory =
                    Path.of(settings.outputDirectory());

            Files.createDirectories(outputDirectory);

            Path outputGtf = Path.of(settings.outputGtf());

            if (outputGtf.getParent() != null) {
                Files.createDirectories(outputGtf.getParent());
            }

            List<String> command = buildCommand();

            notifyLog("Executing StringTie: "
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
                        "StringTie failed with exit code: " + exitCode
                );
            }

            if (!Files.isRegularFile(outputGtf)
                    || Files.size(outputGtf) == 0) {
                throw new RuntimeException(
                        "StringTie completed without producing "
                                + "a non-empty GTF file: " + outputGtf
                );
            }

            notifyLog("StringTie GTF: " + outputGtf);
            notifyCompleted();

            return null;

        } catch (Exception e) {
            notifyFailed(e);
            throw e;
        }
    }

    private void validateInputs() {
        requireFile(settings.inputBam(), "Input BAM");

        if (settings.referenceGtf() != null
                && !settings.referenceGtf().isBlank()) {
            requireFile(settings.referenceGtf(), "Reference GTF");
        }

        if (settings.threads() < 1) {
            throw new IllegalArgumentException(
                    "Thread count must be at least 1"
            );
        }

        if (settings.minimumTranscriptLength() <= 0) {
            throw new IllegalArgumentException(
                    "Minimum transcript length must be positive"
            );
        }

        if (settings.minimumAbundance() < 0) {
            throw new IllegalArgumentException(
                    "Minimum coverage cannot be negative"
            );
        }

        if (settings.estimateAbundance()
                && (settings.referenceGtf() == null
                || settings.referenceGtf().isBlank())) {
            throw new IllegalArgumentException(
                    "StringTie -e requires a reference GTF"
            );
        }

        if (settings.mergeMode()) {
            throw new UnsupportedOperationException(
                    "Merge mode requires a separate merge task "
                            + "with transcript GTF inputs"
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