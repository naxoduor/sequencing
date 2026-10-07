package com.bio.sequencing.plugins.external_tool_support.bwa;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BwaMemExecutor {

    public BwaMemResult execute(
            BwaMemSettings settings)
            throws IOException, InterruptedException {

        validate(settings);

        List<String> command =
                buildCommand(settings);

        ProcessBuilder processBuilder =
                new ProcessBuilder(command);

        processBuilder.redirectErrorStream(true);

        Process process =
                processBuilder.start();

        Path output =
                Paths.get(
                        settings.getOutputSamFile()
                );

        Files.createDirectories(
                output.getParent()
        );

        StringBuilder log =
                new StringBuilder();

        /*
         * BWA-MEM writes SAM to stdout.
         *
         * Therefore we redirect the process
         * stdout directly into the SAM file.
         */
        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     process.getInputStream()));

             BufferedWriter writer =
                     Files.newBufferedWriter(
                             output)) {

            String line;

            while ((line = reader.readLine()) != null) {

                writer.write(line);
                writer.newLine();
            }
        }

        int exitCode =
                process.waitFor();

        if (exitCode != 0) {

            throw new IOException(
                    "BWA-MEM failed with exit code "
                            + exitCode
                            + "\n"
                            + log
            );
        }

        if (!Files.exists(output)) {

            throw new IOException(
                    "BWA-MEM did not create SAM: "
                            + output
            );
        }

        BwaMemResult result =
                new BwaMemResult();

        result.setSamFile(
                output.toString()
        );

        result.setLog(
                log.toString()
        );

        return result;
    }

    private List<String> buildCommand(
            BwaMemSettings settings) {

        List<String> command =
                new ArrayList<>();

        command.add(
                settings.getBwaExecutable()
        );

        command.add("mem");

        /*
         * Number of worker threads.
         */
        command.add("-t");

        command.add(
                String.valueOf(
                        settings.getThreads()
                )
        );

        /*
         * Minimum seed length.
         */
        command.add("-k");

        command.add(
                String.valueOf(
                        settings.getMinSeedLength()
                )
        );

        /*
         * Band width.
         */
        command.add("-w");

        command.add(
                String.valueOf(
                        settings.getBandWidth()
                )
        );

        /*
         * Gap open penalty.
         */
        command.add("-O");

        command.add(
                String.valueOf(
                        settings.getGapOpenPenalty()
                )
        );

        /*
         * Gap extension penalty.
         */
        command.add("-E");

        command.add(
                String.valueOf(
                        settings.getGapExtensionPenalty()
                )
        );

        /*
         * Mismatch penalty.
         */
        command.add("-B");

        command.add(
                String.valueOf(
                        settings.getMismatchPenalty()
                )
        );

        /*
         * Clipping penalty.
         */
        command.add("-L");

        command.add(
                String.valueOf(
                        settings.getClippingPenalty()
                )
        );

        /*
         * Optional read group.
         */
        if (settings.getReadGroup() != null &&
                !settings.getReadGroup().isBlank()) {

            command.add("-R");

            command.add(
                    settings.getReadGroup()
            );
        }

        /*
         * Reference index.
         */
        command.add(
                settings.getReferenceIndex()
        );

        /*
         * Reads.
         */
        command.add(
                settings.getRead1File()
        );

        if (settings.getInputMode()
                == BwaMemSettings.InputMode.PAIRED_END) {

            command.add(
                    settings.getRead2File()
            );
        }

        /*
         * Extra BWA arguments.
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
            BwaMemSettings settings)
            throws IOException {

        if (settings == null) {

            throw new IllegalArgumentException(
                    "BWA settings cannot be null"
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
                    "Read 1 file is required"
            );
        }

        if (settings.getInputMode()
                == BwaMemSettings.InputMode.PAIRED_END) {

            if (settings.getRead2File() == null ||
                    settings.getRead2File().isBlank()) {

                throw new IllegalArgumentException(
                        "Read 2 file is required "
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