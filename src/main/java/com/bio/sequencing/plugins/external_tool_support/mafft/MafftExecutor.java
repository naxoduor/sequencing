package com.bio.sequencing.plugins.external_tool_support.mafft;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class MafftExecutor {

    public static String execute(
            String fasta,
            MAFFTSettings settings)
            throws IOException, InterruptedException {

        ProcessBuilder processBuilder =
                new ProcessBuilder(
                        settings.getMafftExecutable(),
                        settings.getAlgorithm(),
                        "-"
                );

        Process process =
                processBuilder.start();

        /*
         * Write FASTA to MAFFT stdin.
         */
        try (OutputStream output =
                     process.getOutputStream()) {

            output.write(
                    fasta.getBytes(
                            StandardCharsets.UTF_8
                    )
            );
        }

        /*
         * Read MAFFT stdout.
         */
        String output;

        try (InputStream input =
                     process.getInputStream()) {

            output = new String(
                    input.readAllBytes(),
                    StandardCharsets.UTF_8
            );
        }

        /*
         * Read stderr.
         */
        String error;

        try (InputStream errorStream =
                     process.getErrorStream()) {

            error = new String(
                    errorStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );
        }

        int exitCode =
                process.waitFor();

        if (exitCode != 0) {

            throw new IOException(
                    "MAFFT failed with exit code "
                            + exitCode
                            + ": "
                            + error
            );
        }

        return output;
    }
}