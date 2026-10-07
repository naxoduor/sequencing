package com.bio.sequencing.plugins.external_tool_support.hmmer;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class HMMERExecutor {

    public HMMERExecutionResult execute(
            HMMERSettings settings)
            throws IOException, InterruptedException {

        validate(settings);

        Files.createDirectories(
                Paths.get(settings.getOutputDirectory()));

        long start =
                System.currentTimeMillis();

        List<String> command =
                buildCommand(settings);

        ProcessBuilder processBuilder =
                new ProcessBuilder(command);

        processBuilder.directory(
                Paths.get(
                                settings.getOutputDirectory())
                        .toFile());

        processBuilder.redirectErrorStream(false);

        Process process =
                processBuilder.start();

        StringBuilder stdout =
                new StringBuilder();

        StringBuilder stderr =
                new StringBuilder();

        Thread stdoutThread =
                createReaderThread(
                        process.getInputStream(),
                        stdout);

        Thread stderrThread =
                createReaderThread(
                        process.getErrorStream(),
                        stderr);

        stdoutThread.start();
        stderrThread.start();

        int exitCode =
                process.waitFor();

        stdoutThread.join();
        stderrThread.join();

        if (exitCode != 0) {

            throw new IllegalStateException(
                    "HMMER failed with exit code "
                            + exitCode
                            + "\n"
                            + stderr);
        }

        long executionTime =
                System.currentTimeMillis()
                        - start;

        return new HMMERExecutionResult(
                settings.getOutputFile(),
                settings.getTblOutputFile(),
                settings.getDomTblOutputFile(),
                settings.getAlignmentOutputFile(),
                stderr.toString(),
                executionTime);
    }

    private List<String> buildCommand(
            HMMERSettings settings) {

        List<String> command =
                new ArrayList<>();

        switch (settings.getOperation()) {

            case HMMSEARCH ->
                    buildHmmSearchCommand(
                            command,
                            settings);

            case HMMSCAN ->
                    buildHmmScanCommand(
                            command,
                            settings);
        }

        if (settings.getExtraArguments() != null
                && !settings.getExtraArguments()
                .isBlank()) {

            command.addAll(
                    Arrays.asList(
                            settings.getExtraArguments()
                                    .trim()
                                    .split("\\s+")));
        }

        return command;
    }

    private void buildHmmSearchCommand(
            List<String> command,
            HMMERSettings settings) {

        command.add(
                executable(
                        settings,
                        "hmmsearch"));

        /*
         * Number of CPUs.
         */
        command.add("--cpu");
        command.add(
                String.valueOf(
                        settings.getThreads()));

        /*
         * Sequence reporting threshold.
         */
        command.add("-E");
        command.add(
                String.valueOf(
                        settings.getSequenceEvalue()));

        /*
         * Domain reporting threshold.
         */
        command.add("--domE");
        command.add(
                String.valueOf(
                        settings.getDomainEvalue()));

        /*
         * Per-target table.
         */
        if (settings.getTblOutputFile() != null) {

            command.add("--tblout");

            command.add(
                    settings.getTblOutputFile());
        }

        /*
         * Per-domain table.
         */
        if (settings.getDomTblOutputFile() != null) {

            command.add("--domtblout");

            command.add(
                    settings.getDomTblOutputFile());
        }

        /*
         * Alignment output.
         */
        if (settings.getAlignmentOutputFile() != null) {

            command.add("-A");

            command.add(
                    settings.getAlignmentOutputFile());
        }

        if (settings.isNoAlignment()) {

            command.add("--noali");
        }

        /*
         * HMMER positional arguments:
         *
         * hmmsearch [options] <hmmfile> <seqdb>
         */
        command.add(
                settings.getHmmFile());

        command.add(
                settings.getSequenceFile());
    }

    private void buildHmmScanCommand(
            List<String> command,
            HMMERSettings settings) {

        command.add(
                executable(
                        settings,
                        "hmmscan"));

        command.add("--cpu");

        command.add(
                String.valueOf(
                        settings.getThreads()));

        /*
         * Sequence reporting threshold.
         */
        command.add("-E");

        command.add(
                String.valueOf(
                        settings.getSequenceEvalue()));

        /*
         * Domain reporting threshold.
         */
        command.add("--domE");

        command.add(
                String.valueOf(
                        settings.getDomainEvalue()));

        if (settings.getTblOutputFile() != null) {

            command.add("--tblout");

            command.add(
                    settings.getTblOutputFile());
        }

        if (settings.getDomTblOutputFile() != null) {

            command.add("--domtblout");

            command.add(
                    settings.getDomTblOutputFile());
        }

        if (settings.isNoAlignment()) {

            command.add("--noali");
        }

        /*
         * hmmscan [options] <hmmdb> <seqfile>
         */
        command.add(
                settings.getHmmDatabase());

        command.add(
                settings.getSequenceFile());
    }

    private String executable(
            HMMERSettings settings,
            String defaultExecutable) {

        if (settings.getHmmerExecutable() != null
                && !settings.getHmmerExecutable()
                .isBlank()) {

            return settings.getHmmerExecutable();
        }

        return defaultExecutable;
    }

    private Thread createReaderThread(
            InputStream inputStream,
            StringBuilder output) {

        return new Thread(() -> {

            try (BufferedReader reader =
                         new BufferedReader(
                                 new InputStreamReader(
                                         inputStream))) {

                String line;

                while ((line =
                        reader.readLine()) != null) {

                    output.append(line)
                            .append(System.lineSeparator());
                }

            } catch (IOException e) {

                output.append(e.getMessage());
            }
        });
    }

    private void validate(
            HMMERSettings settings) {

        if (settings.getSequenceFile() == null
                || settings.getSequenceFile().isBlank()) {

            throw new IllegalArgumentException(
                    "HMMER sequence file is required");
        }

        if (settings.getOutputDirectory() == null
                || settings.getOutputDirectory()
                .isBlank()) {

            throw new IllegalArgumentException(
                    "HMMER output directory is required");
        }

        if (settings.getOperation()
                == HMMERSettings.Operation.HMMSEARCH) {

            if (settings.getHmmFile() == null
                    || settings.getHmmFile().isBlank()) {

                throw new IllegalArgumentException(
                        "HMM file is required for hmmsearch");
            }
        }

        if (settings.getOperation()
                == HMMERSettings.Operation.HMMSCAN) {

            if (settings.getHmmDatabase() == null
                    || settings.getHmmDatabase().isBlank()) {

                throw new IllegalArgumentException(
                        "HMM database is required for hmmscan");
            }
        }
    }
}