package com.bio.sequencing.plugins.external_tool_support.kalign;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class KalignExecutor {

    public void execute(
            String inputFile,
            String outputFile,
            KalignSettings settings
    ) throws Exception {

        ProcessBuilder processBuilder = new ProcessBuilder();

        /*
         * Example:
         *
         * kalign -i input.fasta -o output.fasta -f fasta
         */

        processBuilder.command(
                settings.getKalignExecutable(),
                "-i",
                inputFile,
                "-o",
                outputFile,
                "-f",
                settings.getOutputFormat()
        );

        processBuilder.redirectErrorStream(true);

        System.out.println(
                "Starting Kalign: "
                        + String.join(" ", processBuilder.command())
        );

        Process process = processBuilder.start();

        String log;

        try (InputStream inputStream = process.getInputStream()) {
            log = new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );
        }

        int exitCode = process.waitFor();

        System.out.println("Kalign output:");
        System.out.println(log);

        if (exitCode != 0) {
            throw new RuntimeException(
                    "Kalign failed with exit code "
                            + exitCode
                            + ". Output: "
                            + log
            );
        }
    }
}