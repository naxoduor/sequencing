package com.bio.sequencing.plugins.external_tool_support.clustalw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ClustalWExecutor {

    public static String execute(
            String inputFile,
            String outputFile,
            ClustalWSettings settings)
            throws IOException, InterruptedException {

        List<String> command =
                new ArrayList<>();

        command.add(
                settings.getClustalWExecutable()
        );

        /*
         * ClustalW input.
         *
         * Example:
         *
         * clustalw2
         *   -INFILE=/tmp/input.fasta
         */
        command.add(
                "-INFILE=" + inputFile
        );

        /*
         * Output file.
         */
        command.add(
                "-OUTFILE=" + outputFile
        );

        /*
         * Ask ClustalW for FASTA output.
         */
        command.add(
                "-OUTPUT="
                        + settings.getOutputFormat()
        );

        /*
         * Preserve aligned ordering.
         */
        if (settings.isOutputOrderAligned()) {

            command.add(
                    "-OUTPUTORDER=ALIGNED"
            );
        }

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
                                     process.getInputStream()
                             ))) {

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
                    "ClustalW failed. "
                            + "Exit code: "
                            + exitCode
                            + "\n"
                            + log
            );
        }

        return log.toString();
    }
}