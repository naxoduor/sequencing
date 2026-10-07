package com.bio.sequencing.plugins.external_tool_support.bowtie2;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Bowtie2Executor {

    public Bowtie2Result execute(
            Bowtie2Settings settings)
            throws IOException, InterruptedException {

        validate(settings);

        List<String> command =
                buildCommand(settings);

        ProcessBuilder processBuilder =
                new ProcessBuilder(command);

        processBuilder.redirectErrorStream(true);

        Process process =
                processBuilder.start();

        StringBuilder log =
                new StringBuilder();

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     process.getInputStream()))) {

            String line;

            while ((line = reader.readLine()) != null) {

                log.append(line)
                        .append(System.lineSeparator());
            }
        }

        int exitCode =
                process.waitFor();

        if (exitCode != 0) {

            throw new IOException(
                    "Bowtie2 failed with exit code "
                            + exitCode
                            + "\n"
                            + log
            );
        }

        Path output =
                Paths.get(
                        settings.getOutputSamFile()
                );

        if (!Files.exists(output)) {

            throw new IOException(
                    "Bowtie2 completed but output SAM "
                            + "was not created: "
                            + output
            );
        }

        Bowtie2Result result =
                new Bowtie2Result();

        result.setSamFile(
                output.toString()
        );

        result.setLog(
                log.toString()
        );

        return result;
    }

    private List<String> buildCommand(
            Bowtie2Settings settings) {

        List<String> command =
                new ArrayList<>();

        command.add(
                settings.getBowtie2Executable()
        );

        /*
         * Reference index.
         *
         * bowtie2 -x reference/index
         */
        command.add("-x");

        command.add(
                settings.getReferenceIndex()
        );

        /*
         * Threads.
         */
        command.add("-p");

        command.add(
                String.valueOf(
                        settings.getThreads()
                )
        );

        /*
         * Input reads.
         */
        if (settings.getInputMode()
                == Bowtie2Settings.InputMode.PAIRED_END) {

            command.add("-1");

            command.add(
                    settings.getRead1File()
            );

            command.add("-2");

            command.add(
                    settings.getRead2File()
            );

        } else {

            command.add("-U");

            command.add(
                    settings.getRead1File()
            );
        }

        /*
         * Local alignment.
         */
        if (settings.isLocalAlignment()) {

            command.add("--local");
        }

        /*
         * Don't output unaligned reads.
         */
        if (settings.isNoUnal()) {

            command.add("--no-unal");
        }

        /*
         * Don't output SAM header.
         */
        if (settings.isNoHead()) {

            command.add("--no-head");
        }

        /*
         * Don't output @SQ records.
         */
        if (settings.isNoSQ()) {

            command.add("--no-sq");
        }

        /*
         * Output SAM.
         */
        command.add("-S");

        command.add(
                settings.getOutputSamFile()
        );

        /*
         * Additional Bowtie2 arguments.
         */
        if (settings.getExtraArguments() != null &&
                !settings.getExtraArguments().isBlank()) {

            command.addAll(
                    Arrays.asList(
                            settings
                                    .getExtraArguments()
                                    .trim()
                                    .split("\\s+")
                    )
            );
        }

        return command;
    }

    private void validate(
            Bowtie2Settings settings)
            throws IOException {

        if (settings == null) {

            throw new IllegalArgumentException(
                    "Bowtie2 settings cannot be null"
            );
        }

        if (settings.getReferenceIndex() == null ||
                settings.getReferenceIndex().isBlank()) {

            throw new IllegalArgumentException(
                    "Bowtie2 reference index is required"
            );
        }

        if (settings.getRead1File() == null ||
                settings.getRead1File().isBlank()) {

            throw new IllegalArgumentException(
                    "Read 1 file is required"
            );
        }

        if (settings.getInputMode()
                == Bowtie2Settings.InputMode.PAIRED_END) {

            if (settings.getRead2File() == null ||
                    settings.getRead2File().isBlank()) {

                throw new IllegalArgumentException(
                        "Read 2 file is required "
                                + "for paired-end alignment"
                );
            }
        }

        if (settings.getOutputSamFile() == null ||
                settings.getOutputSamFile().isBlank()) {

            throw new IllegalArgumentException(
                    "Output SAM file is required"
            );
        }

        if (settings.getThreads() <= 0) {

            throw new IllegalArgumentException(
                    "Threads must be greater than zero"
            );
        }
    }
}