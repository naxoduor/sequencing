package com.bio.sequencing.plugins.external_tool_support.cap3;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Cap3Executor {

    public Cap3Result execute(
            Cap3Settings settings)
            throws IOException, InterruptedException {

        Path input =
                Paths.get(settings.getInputFasta());

        if (!Files.exists(input)) {
            throw new FileNotFoundException(
                    "CAP3 input file does not exist: "
                            + input);
        }

        Path outputDirectory =
                Paths.get(settings.getOutputDirectory());

        Files.createDirectories(outputDirectory);

        /*
         * CAP3 generates output files relative to
         * the input filename. Therefore copy the
         * input into the requested output directory
         * if necessary.
         */
        Path workingInput =
                outputDirectory.resolve(input.getFileName());

        if (!input.toAbsolutePath()
                .equals(workingInput.toAbsolutePath())) {

            Files.copy(
                    input,
                    workingInput,
                    StandardCopyOption.REPLACE_EXISTING);
        }

        List<String> command =
                buildCommand(settings, workingInput);

        ProcessBuilder processBuilder =
                new ProcessBuilder(command);

        processBuilder.directory(
                outputDirectory.toFile());

        processBuilder.redirectErrorStream(false);

        Process process =
                processBuilder.start();

        StringBuilder stdout =
                new StringBuilder();

        StringBuilder stderr =
                new StringBuilder();

        Thread stdoutReader =
                new Thread(() -> {

                    try (BufferedReader reader =
                                 new BufferedReader(
                                         new InputStreamReader(
                                                 process.getInputStream()))) {

                        String line;

                        while ((line = reader.readLine())
                                != null) {

                            stdout.append(line)
                                    .append(System.lineSeparator());
                        }

                    } catch (IOException e) {

                        stderr.append(e.getMessage());
                    }
                });

        Thread stderrReader =
                new Thread(() -> {

                    try (BufferedReader reader =
                                 new BufferedReader(
                                         new InputStreamReader(
                                                 process.getErrorStream()))) {

                        String line;

                        while ((line = reader.readLine())
                                != null) {

                            stderr.append(line)
                                    .append(System.lineSeparator());
                        }

                    } catch (IOException e) {

                        stderr.append(e.getMessage());
                    }
                });

        stdoutReader.start();
        stderrReader.start();

        int exitCode =
                process.waitFor();

        stdoutReader.join();
        stderrReader.join();

        if (exitCode != 0) {

            throw new IllegalStateException(
                    "CAP3 failed with exit code "
                            + exitCode
                            + "\n"
                            + stderr);
        }

        return collectResult(
                workingInput,
                stderr.toString());
    }

    private List<String> buildCommand(
            Cap3Settings settings,
            Path input) {

        List<String> command =
                new ArrayList<>();

        command.add(
                settings.getCap3Executable());

        /*
         * CAP3 takes the FASTA file as its
         * primary argument.
         */
        command.add(input.toString());

        /*
         * Add CAP3-specific options here when
         * they are enabled.
         *
         * Keep these mappings version-specific.
         */

        if (settings.getExtraArguments() != null
                && !settings.getExtraArguments().isBlank()) {

            command.addAll(
                    Arrays.asList(
                            settings.getExtraArguments()
                                    .split("\\s+")));
        }

        return command;
    }

    private Cap3Result collectResult(
            Path input,
            String log) {

        String base =
                input.toString();

        return new Cap3Result(
                findExisting(base + ".cap.contigs"),
                findExisting(base + ".cap.singlets"),
                findExisting(base + ".cap"),
                findExisting(base + ".cap.qual"),
                findExisting(base + ".cap.ace"),
                findExisting(base + ".cap.info"),
                log);
    }

    private String findExisting(
            String filename) {

        Path path =
                Paths.get(filename);

        return Files.exists(path)
                ? path.toString()
                : null;
    }
}