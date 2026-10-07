package com.bio.sequencing.plugins.external_tool_support.kalign;

import com.bio.sequencing.plugins.external_tool_support.clustalw.TaskListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class KalignTask implements Callable<KalignTaskResult> {

    private final String taskId;
    private final KalignTaskSettings settings;

    private final List<TaskListener> listeners =
            new java.util.concurrent.CopyOnWriteArrayList<>();

    public KalignTask(
            String taskId,
            KalignTaskSettings settings) {

        this.taskId = taskId;
        this.settings = settings;
    }

    public void addListener(TaskListener listener) {
        listeners.add(listener);
    }

    @Override
    public KalignTaskResult call() throws Exception {

        notifyStarted();

        try {
            KalignTaskResult result = execute();

            notifyCompleted();

            return result;

        } catch (Exception e) {

            notifyFailed(e);

            throw e;
        }
    }

    private KalignTaskResult execute() throws Exception {

        Path input =
                Path.of(settings.inputFasta()).toAbsolutePath();

        Path output =
                Path.of(settings.outputFasta()).toAbsolutePath();

        validateInput(input);

        createOutputDirectory(output);

        if (Files.exists(output) && !settings.overwrite()) {
            throw new IOException(
                    "Output file already exists: " + output
            );
        }

        List<String> command =
                buildCommand(input, output);

        notifyLog(
                "Starting Kalign: "
                        + String.join(" ", command)
        );

        ProcessBuilder processBuilder =
                new ProcessBuilder(command);

        /*
         * Kalign writes alignment to the output file,
         * while stdout/stderr are used for logging.
         */
        processBuilder.redirectErrorStream(true);

        if (settings.workingDirectory() != null
                && !settings.workingDirectory().isBlank()) {

            processBuilder.directory(
                    Path.of(
                            settings.workingDirectory()
                    ).toFile()
            );
        }

        Process process = processBuilder.start();

        readProcessOutput(process);

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
                    "Kalign failed with exit code: "
                            + exitCode
            );
        }

        validateOutput(output);

        Path outputDirectory =
                output.getParent();

        return new KalignTaskResult(
                taskId,
                input.toString(),
                output.toString(),
                settings.outputFormat(),
                outputDirectory == null
                        ? ""
                        : outputDirectory.toString()
        );
    }

    private List<String> buildCommand(
            Path input,
            Path output) {

        List<String> command =
                new ArrayList<>();

        command.add(settings.kalignExecutable());

        /*
         * Kalign input
         */
        command.add("-i");
        command.add(input.toString());

        /*
         * Kalign output
         */
        command.add("-o");
        command.add(output.toString());

        /*
         * Output format.
         *
         * FASTA is the format expected by the
         * React MSA Viewer.
         */
        if (settings.outputFormat() != null
                && !settings.outputFormat().isBlank()) {

            command.add("--format");
            command.add(
                    settings.outputFormat()
            );
        }

        if (settings.overwrite()) {
            command.add("--overwrite");
        }

        if (settings.verbose()) {
            command.add("--verbose");
        }

        return command;
    }

    private void readProcessOutput(
            Process process) throws IOException {

        try (
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        process.getInputStream(),
                                        StandardCharsets.UTF_8
                                )
                        )
        ) {

            String line;

            while ((line = reader.readLine()) != null) {

                notifyLog(line);
            }
        }
    }

    private void validateInput(Path input)
            throws IOException {

        if (!Files.isRegularFile(input)) {

            throw new IOException(
                    "Input FASTA file does not exist: "
                            + input
            );
        }

        if (Files.size(input) == 0) {

            throw new IOException(
                    "Input FASTA file is empty: "
                            + input
            );
        }
    }

    private void createOutputDirectory(
            Path output) throws IOException {

        Path parent = output.getParent();

        if (parent != null) {

            Files.createDirectories(parent);
        }
    }

    private void validateOutput(Path output)
            throws IOException {

        if (!Files.isRegularFile(output)) {

            throw new IOException(
                    "Kalign did not create output file: "
                            + output
            );
        }

        if (Files.size(output) == 0) {

            throw new IOException(
                    "Kalign created an empty output file: "
                            + output
            );
        }

        boolean hasFastaHeader = false;

        try (
                BufferedReader reader =
                        Files.newBufferedReader(
                                output,
                                StandardCharsets.UTF_8
                        )
        ) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.startsWith(">")) {

                    hasFastaHeader = true;
                    break;
                }
            }
        }

        if (!hasFastaHeader) {

            throw new IOException(
                    "Kalign output does not appear to be FASTA"
            );
        }
    }

    private void notifyStarted() {

        listeners.forEach(
                listener ->
                        listener.onStarted(taskId)
        );
    }

    private void notifyLog(String message) {

        listeners.forEach(
                listener ->
                        listener.onLog(
                                taskId,
                                message
                        )
        );
    }

    private void notifyCompleted() {

        listeners.forEach(
                listener ->
                        listener.onCompleted(taskId)
        );
    }

    private void notifyFailed(
            Throwable error) {

        listeners.forEach(
                listener ->
                        listener.onFailed(
                                taskId,
                                error
                        )
        );
    }
}