package com.bio.sequencing.plugins.external_tool_support.trimmomatic;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TrimmomaticExecutor {

    public TrimmomaticResult execute(
            TrimmomaticSettings settings
    ) throws Exception {

        validate(settings);

        List<String> command =
                new ArrayList<>();

        command.add(
                settings.getTrimmomaticExecutable()
        );

        /*
         * PE / SE
         */
        command.add(
                settings.getMode()
        );

        /*
         * Quality encoding.
         */
        if (settings.isPhred33()) {
            command.add("-phred33");
        } else {
            command.add("-phred64");
        }

        /*
         * Threads.
         */
        if (settings.getThreads() > 1) {

            command.add("-threads");

            command.add(
                    String.valueOf(
                            settings.getThreads()
                    )
            );
        }

        /*
         * ------------------------------------------------
         * PAIRED END
         * ------------------------------------------------
         */
        if ("PE".equalsIgnoreCase(
                settings.getMode()
        )) {

            command.add(
                    settings.getInputRead1()
            );

            command.add(
                    settings.getInputRead2()
            );

            command.add(
                    settings.getOutputPairedRead1()
            );

            command.add(
                    settings.getOutputUnpairedRead1()
            );

            command.add(
                    settings.getOutputPairedRead2()
            );

            command.add(
                    settings.getOutputUnpairedRead2()
            );
        }

        /*
         * ------------------------------------------------
         * SINGLE END
         * ------------------------------------------------
         */
        else {

            command.add(
                    settings.getInputRead1()
            );

            command.add(
                    settings.getOutputPairedRead1()
            );
        }

        /*
         * ------------------------------------------------
         * TRIMMING STEPS
         * ------------------------------------------------
         */
        command.addAll(
                settings.getTrimmingSteps()
        );

        System.out.println(
                "Starting Trimmomatic:"
        );

        System.out.println(
                String.join(
                        " ",
                        command
                )
        );

        ProcessBuilder processBuilder =
                new ProcessBuilder(command);

        processBuilder.redirectErrorStream(true);

        Process process =
                processBuilder.start();

        String log;

        try (InputStream stream =
                     process.getInputStream()) {

            log = new String(
                    stream.readAllBytes(),
                    StandardCharsets.UTF_8
            );
        }

        int exitCode =
                process.waitFor();

        System.out.println(
                "Trimmomatic output:"
        );

        System.out.println(log);

        if (exitCode != 0) {

            throw new RuntimeException(
                    "Trimmomatic failed with exit code "
                            + exitCode
                            + "\n"
                            + log
            );
        }

        return createResult(
                settings,
                log
        );
    }

    private void validate(
            TrimmomaticSettings settings
    ) {

        if (settings.getInputRead1() == null
                || settings.getInputRead1().isBlank()) {

            throw new IllegalArgumentException(
                    "Input Read 1 is required"
            );
        }

        if (!Files.exists(
                Path.of(
                        settings.getInputRead1()
                )
        )) {

            throw new IllegalArgumentException(
                    "Read 1 does not exist: "
                            + settings.getInputRead1()
            );
        }

        if ("PE".equalsIgnoreCase(
                settings.getMode()
        )) {

            if (settings.getInputRead2() == null
                    || settings.getInputRead2().isBlank()) {

                throw new IllegalArgumentException(
                        "Input Read 2 is required "
                                + "for paired-end mode"
                );
            }

            if (!Files.exists(
                    Path.of(
                            settings.getInputRead2()
                    )
            )) {

                throw new IllegalArgumentException(
                        "Read 2 does not exist: "
                                + settings.getInputRead2()
                );
            }
        }

        if (settings.getTrimmingSteps()
                .isEmpty()) {

            throw new IllegalArgumentException(
                    "At least one trimming step "
                            + "is required"
            );
        }
    }

    private TrimmomaticResult createResult(
            TrimmomaticSettings settings,
            String log
    ) {

        TrimmomaticResult result =
                new TrimmomaticResult();

        if ("PE".equalsIgnoreCase(
                settings.getMode()
        )) {

            result.setPairedRead1(
                    settings.getOutputPairedRead1()
            );

            result.setPairedRead2(
                    settings.getOutputPairedRead2()
            );

            result.setUnpairedRead1(
                    settings.getOutputUnpairedRead1()
            );

            result.setUnpairedRead2(
                    settings.getOutputUnpairedRead2()
            );

        } else {

            result.setPairedRead1(
                    settings.getOutputPairedRead1()
            );
        }

        result.setLog(log);

        return result;
    }
}