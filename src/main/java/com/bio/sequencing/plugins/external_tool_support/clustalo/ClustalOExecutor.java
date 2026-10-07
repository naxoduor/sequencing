package com.bio.sequencing.plugins.external_tool_support.clustalo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ClustalOExecutor {

    public static String execute(
            String inputFile,
            String outputFile,
            ClustalOSettings settings)
            throws IOException, InterruptedException {

        List<String> command =
                new ArrayList<>();

        command.add(
                settings.getClustalOExecutable()
        );

        command.add("-i");
        command.add(inputFile);

        command.add("-o");
        command.add(outputFile);

        if (settings.isForce()) {
            command.add("--force");
        }

        command.add("--threads");
        command.add(
                String.valueOf(
                        settings.getThreads()
                )
        );

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
                    "Clustal Omega failed. "
                            + "Exit code: "
                            + exitCode
                            + "\n"
                            + log
            );
        }

        return log.toString();
    }
}