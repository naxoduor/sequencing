package com.bio.sequencing.plugins.external_tool_support.stringtie;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class StringTieExecutor {

    public StringTieResult execute(StringTieSettings settings)
            throws IOException, InterruptedException {

        validate(settings);

        Files.createDirectories(
                Paths.get(settings.getOutputDirectory())
        );

        String outputGtf =
                Paths.get(
                        settings.getOutputDirectory(),
                        "transcripts.gtf"
                ).toString();

        List<String> command = new ArrayList<>();

        command.add(settings.getStringTieExecutable());

        /*
         * Number of threads
         *
         * stringtie -p <threads>
         */
        command.add("-p");
        command.add(String.valueOf(settings.getThreads()));

        /*
         * Minimum transcript abundance
         *
         * -f <fraction>
         */
        command.add("-f");
        command.add(
                String.valueOf(settings.getMinIsoformAbundance())
        );

        /*
         * Minimum transcript length
         *
         * -m <length>
         */
        command.add("-m");
        command.add(
                String.valueOf(
                        (int) settings.getMinTranscriptLength()
                )
        );

        /*
         * Reference annotation
         */
        if (settings.getReferenceGtf() != null &&
                !settings.getReferenceGtf().isBlank()) {

            command.add("-G");
            command.add(settings.getReferenceGtf());
        }

        /*
         * Ballgown output
         */
        if (settings.isBallgown()) {
            command.add("-B");
        }

        /*
         * Estimate abundance
         *
         * StringTie performs abundance estimation during
         * normal assembly.
         *
         * This flag is therefore represented here as
         * configuration rather than a CLI flag.
         */

        /*
         * Input BAM
         */
        command.add(settings.getInputBamFile());

        /*
         * Output GTF
         */
        command.add("-o");
        command.add(outputGtf);

        ProcessBuilder processBuilder =
                new ProcessBuilder(command);

        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        String log;

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     process.getInputStream()))) {

            StringBuilder output = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line)
                        .append(System.lineSeparator());
            }

            log = output.toString();
        }

        int exitCode = process.waitFor();

        if (exitCode != 0) {

            throw new IOException(
                    "StringTie failed with exit code "
                            + exitCode
                            + "\n"
                            + log
            );
        }

        if (!Files.exists(Paths.get(outputGtf))) {

            throw new IOException(
                    "StringTie completed successfully but "
                            + "output GTF was not created: "
                            + outputGtf
            );
        }

        StringTieResult result =
                new StringTieResult();

        result.setTranscriptGtf(outputGtf);
        result.setLog(log);

        return result;
    }

    private void validate(StringTieSettings settings)
            throws IOException {

        if (settings.getInputBamFile() == null ||
                settings.getInputBamFile().isBlank()) {

            throw new IllegalArgumentException(
                    "Input BAM file is required"
            );
        }

        if (!Files.exists(
                Paths.get(settings.getInputBamFile()))) {

            throw new FileNotFoundException(
                    "Input BAM file does not exist: "
                            + settings.getInputBamFile()
            );
        }

        if (settings.getOutputDirectory() == null ||
                settings.getOutputDirectory().isBlank()) {

            throw new IllegalArgumentException(
                    "Output directory is required"
            );
        }

        if (settings.getThreads() <= 0) {

            throw new IllegalArgumentException(
                    "Threads must be greater than zero"
            );
        }
    }
}