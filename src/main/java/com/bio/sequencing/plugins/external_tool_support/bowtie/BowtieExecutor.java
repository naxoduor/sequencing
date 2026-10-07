package com.bio.sequencing.plugins.external_tool_support.bowtie;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BowtieExecutor {

    public BowtieResult execute(
            BowtieSettings settings)
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
                    "Bowtie failed with exit code "
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
                    "Bowtie completed but SAM output "
                            + "was not created: "
                            + output
            );
        }

        BowtieResult result =
                new BowtieResult();

        result.setSamFile(
                output.toString()
        );

        result.setLog(
                log.toString()
        );

        return result;
    }

    private List<String> buildCommand(
            BowtieSettings settings) {

        List<String> command =
                new ArrayList<>();

        command.add(
                settings.getBowtieExecutable()
        );

        /*
         * Bowtie2
         */
        if (settings.getVersion()
                == BowtieSettings.Version.BOWTIE2) {

            command.add("-x");
            command.add(
                    settings.getReferenceIndex()
            );

            /*
             * Threads
             */
            command.add("-p");
            command.add(
                    String.valueOf(
                            settings.getThreads()
                    )
            );

            /*
             * Input reads
             */
            if (settings.getInputMode()
                    == BowtieSettings.InputMode.PAIRED_END) {

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
             * Output SAM
             */
            command.add("-S");

            command.add(
                    settings.getOutputSamFile()
            );

            /*
             * Don't report unaligned reads.
             */
            if (settings.isNoUnaligned()) {
                command.add("--no-unal");
            }

            /*
             * Don't output SAM header.
             */
            if (settings.isNoHead()) {
                command.add("--no-head");
            }

            /*
             * Don't output SQ header.
             */
            if (settings.isNoSQ()) {
                command.add("--no-sq");
            }

        } else {

            /*
             * Bowtie 1
             */
            command.add(
                    settings.getReferenceIndex()
            );

            /*
             * Number of mismatches.
             */
            command.add("-v");

            command.add(
                    String.valueOf(
                            settings.getMaxMismatches()
                    )
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

            if (settings.getInputMode()
                    == BowtieSettings.InputMode.PAIRED_END) {

                command.add("-1");

                command.add(
                        settings.getRead1File()
                );

                command.add("-2");

                command.add(
                        settings.getRead2File()
                );

            } else {

                command.add(
                        settings.getRead1File()
                );
            }

            /*
             * SAM output.
             */
            command.add(
                    settings.getOutputSamFile()
            );
        }

        /*
         * Extra user arguments.
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
            BowtieSettings settings)
            throws IOException {

        if (settings == null) {

            throw new IllegalArgumentException(
                    "Bowtie settings cannot be null"
            );
        }

        if (settings.getReferenceIndex() == null ||
                settings.getReferenceIndex().isBlank()) {

            throw new IllegalArgumentException(
                    "Reference index is required"
            );
        }

        if (settings.getRead1File() == null ||
                settings.getRead1File().isBlank()) {

            throw new IllegalArgumentException(
                    "Read 1 FASTQ file is required"
            );
        }

        if (settings.getInputMode()
                == BowtieSettings.InputMode.PAIRED_END) {

            if (settings.getRead2File() == null ||
                    settings.getRead2File().isBlank()) {

                throw new IllegalArgumentException(
                        "Read 2 FASTQ file is required "
                                + "for paired-end alignment"
                );
            }
        }

        if (settings.getThreads() <= 0) {

            throw new IllegalArgumentException(
                    "Threads must be greater than zero"
            );
        }

        if (settings.getOutputSamFile() == null ||
                settings.getOutputSamFile().isBlank()) {

            throw new IllegalArgumentException(
                    "Output SAM file is required"
            );
        }
    }
}