package com.bio.sequencing.plugins.external_tool_support;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ExternalToolRunTask extends Task {

    private final String executable;
    private final List<String> arguments;

    private int exitCode;

    public ExternalToolRunTask(
            String executable,
            List<String> arguments) {

        super("Run external tool: " + executable);

        this.executable = executable;
        this.arguments = arguments;
    }

    @Override
    protected void execute() throws Exception {

        List<String> command = new ArrayList<>();

        command.add(executable);
        command.addAll(arguments);

        ProcessBuilder processBuilder =
                new ProcessBuilder(command);

        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     process.getInputStream()))) {

            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(
                        "[" + getName() + "] " + line
                );
            }
        }

        exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException(
                    "External tool failed. Exit code: "
                            + exitCode
            );
        }
    }

    public int getExitCode() {
        return exitCode;
    }
}