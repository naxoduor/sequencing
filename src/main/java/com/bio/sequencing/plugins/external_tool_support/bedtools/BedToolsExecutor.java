package com.bio.sequencing.plugins.external_tool_support.bedtools;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BedToolsExecutor {

    public BedToolsResult execute(
            BedToolsSettings settings)
            throws IOException, InterruptedException {

        validate(settings);

        List<String> command =
                buildCommand(settings);

        Path output =
                Paths.get(
                        settings.getOutputFile()
                );

        if (output.getParent() != null) {

            Files.createDirectories(
                    output.getParent()
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
                                     process.getInputStream()));

             BufferedWriter writer =
                     Files.newBufferedWriter(output)) {

            String line;

            while ((line =
                    reader.readLine()) != null) {

                /*
                 * BEDTools writes most results to stdout.
                 */
                writer.write(line);
                writer.newLine();
            }
        }

        int exitCode =
                process.waitFor();

        if (exitCode != 0) {

            throw new IOException(
                    "BEDTools failed with exit code "
                            + exitCode
                            + "\n"
                            + log
            );
        }

        if (!Files.exists(output)) {

            throw new IOException(
                    "BEDTools output was not created: "
                            + output
            );
        }

        BedToolsResult result =
                new BedToolsResult();

        result.setOutputFile(
                output.toString()
        );

        result.setLog(
                log.toString()
        );

        return result;
    }

    private List<String> buildCommand(
            BedToolsSettings settings) {

        List<String> command =
                new ArrayList<>();

        command.add(
                settings.getBedToolsExecutable()
        );

        switch (settings.getOperation()) {

            case INTERSECT:
                buildIntersectCommand(
                        command,
                        settings
                );
                break;

            case SORT:
                buildSortCommand(
                        command,
                        settings
                );
                break;

            case MERGE:
                buildMergeCommand(
                        command,
                        settings
                );
                break;

            case COVERAGE:
                buildCoverageCommand(
                        command,
                        settings
                );
                break;

            case GENOMECOV:
                buildGenomeCovCommand(
                        command,
                        settings
                );
                break;

            case WINDOW:
                buildWindowCommand(
                        command,
                        settings
                );
                break;

            case CLOSEST:
                buildClosestCommand(
                        command,
                        settings
                );
                break;

            case SLOP:
                buildSlopCommand(
                        command,
                        settings
                );
                break;

            case FLANK:
                buildFlankCommand(
                        command,
                        settings
                );
                break;

            case COMPLEMENT:
                buildComplementCommand(
                        command,
                        settings
                );
                break;

            default:
                throw new IllegalArgumentException(
                        "Unsupported BEDTools operation"
                );
        }

        command.addAll(
                settings.getExtraArguments()
        );

        return command;
    }

    private void buildIntersectCommand(
            List<String> command,
            BedToolsSettings settings) {

        command.add("intersect");

        command.add("-a");

        command.add(
                settings.getInputFile()
        );

        command.add("-b");

        command.add(
                settings.getSecondInputFile()
        );

        if (settings.isWriteOverlap()) {

            command.add("-wa");
            command.add("-wb");
        }

        if (settings.isCountOverlaps()) {

            command.add("-c");
        }

        if (settings.isStranded()) {

            command.add("-s");
        }

        if (settings.isInvert()) {

            command.add("-v");
        }
    }

    private void buildSortCommand(
            List<String> command,
            BedToolsSettings settings) {

        command.add("sort");

        command.add("-i");

        command.add(
                settings.getInputFile()
        );
    }

    private void buildMergeCommand(
            List<String> command,
            BedToolsSettings settings) {

        command.add("merge");

        command.add("-i");

        command.add(
                settings.getInputFile()
        );
    }

    private void buildCoverageCommand(
            List<String> command,
            BedToolsSettings settings) {

        command.add("coverage");

        command.add("-a");

        command.add(
                settings.getInputFile()
        );

        command.add("-b");

        command.add(
                settings.getSecondInputFile()
        );

        if (settings.isSorted()) {

            command.add("-sorted");
        }
    }

    private void buildGenomeCovCommand(
            List<String> command,
            BedToolsSettings settings) {

        command.add("genomecov");

        command.add("-ibam");

        command.add(
                settings.getInputFile()
        );

        if (settings.getGenomeFile() != null &&
                !settings.getGenomeFile().isBlank()) {

            command.add("-g");

            command.add(
                    settings.getGenomeFile()
            );
        }
    }

    private void buildWindowCommand(
            List<String> command,
            BedToolsSettings settings) {

        command.add("window");

        command.add("-a");

        command.add(
                settings.getInputFile()
        );

        command.add("-b");

        command.add(
                settings.getSecondInputFile()
        );

        command.add("-w");

        command.add(
                String.valueOf(
                        settings.getWindowSize()
                )
        );
    }

    private void buildClosestCommand(
            List<String> command,
            BedToolsSettings settings) {

        command.add("closest");

        command.add("-a");

        command.add(
                settings.getInputFile()
        );

        command.add("-b");

        command.add(
                settings.getSecondInputFile()
        );
    }

    private void buildSlopCommand(
            List<String> command,
            BedToolsSettings settings) {

        command.add("slop");

        command.add("-i");

        command.add(
                settings.getInputFile()
        );

        command.add("-g");

        command.add(
                settings.getGenomeFile()
        );

        command.add("-l");

        command.add(
                String.valueOf(
                        settings.getLeft()
                )
        );

        command.add("-r");

        command.add(
                String.valueOf(
                        settings.getRight()
                )
        );
    }

    private void buildFlankCommand(
            List<String> command,
            BedToolsSettings settings) {

        command.add("flank");

        command.add("-i");

        command.add(
                settings.getInputFile()
        );

        command.add("-g");

        command.add(
                settings.getGenomeFile()
        );

        command.add("-l");

        command.add(
                String.valueOf(
                        settings.getLeft()
                )
        );

        command.add("-r");

        command.add(
                String.valueOf(
                        settings.getRight()
                )
        );
    }

    private void buildComplementCommand(
            List<String> command,
            BedToolsSettings settings) {

        command.add("complement");

        command.add("-i");

        command.add(
                settings.getInputFile()
        );

        command.add("-g");

        command.add(
                settings.getGenomeFile()
        );
    }

    private void validate(
            BedToolsSettings settings)
            throws IOException {

        if (settings == null) {

            throw new IllegalArgumentException(
                    "BEDTools settings cannot be null"
            );
        }

        if (settings.getOperation() == null) {

            throw new IllegalArgumentException(
                    "BEDTools operation is required"
            );
        }

        if (settings.getInputFile() == null ||
                settings.getInputFile().isBlank()) {

            throw new IllegalArgumentException(
                    "Input file is required"
            );
        }

        if (!Files.exists(
                Paths.get(
                        settings.getInputFile()
                ))) {

            throw new FileNotFoundException(
                    "Input file does not exist: "
                            + settings.getInputFile()
            );
        }

        if (settings.getOutputFile() == null ||
                settings.getOutputFile().isBlank()) {

            throw new IllegalArgumentException(
                    "Output file is required"
            );
        }

        if (requiresSecondInput(
                settings.getOperation())) {

            if (settings.getSecondInputFile() == null ||
                    settings.getSecondInputFile().isBlank()) {

                throw new IllegalArgumentException(
                        "Second input file is required "
                                + "for "
                                + settings.getOperation()
                );
            }
        }

        if (requiresGenome(
                settings.getOperation())) {

            if (settings.getGenomeFile() == null ||
                    settings.getGenomeFile().isBlank()) {

                throw new IllegalArgumentException(
                        "Genome file is required "
                                + "for "
                                + settings.getOperation()
                );
            }
        }
    }

    private boolean requiresSecondInput(
            BedToolsSettings.Operation operation) {

        return operation ==
                BedToolsSettings.Operation.INTERSECT

                || operation ==
                BedToolsSettings.Operation.COVERAGE

                || operation ==
                BedToolsSettings.Operation.WINDOW

                || operation ==
                BedToolsSettings.Operation.CLOSEST;
    }

    private boolean requiresGenome(
            BedToolsSettings.Operation operation) {

        return operation ==
                BedToolsSettings.Operation.SLOP

                || operation ==
                BedToolsSettings.Operation.FLANK

                || operation ==
                BedToolsSettings.Operation.COMPLEMENT;
    }
}