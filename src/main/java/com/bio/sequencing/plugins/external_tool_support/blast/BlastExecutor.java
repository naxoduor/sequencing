package com.bio.sequencing.plugins.external_tool_support.blast;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BlastExecutor {

    public BlastResult execute(BlastSettings settings)
            throws IOException, InterruptedException {

        long start = System.currentTimeMillis();

        List<String> command = buildCommand(settings);

        ProcessBuilder processBuilder = new ProcessBuilder(command);

        processBuilder.redirectErrorStream(false);

        Process process = processBuilder.start();

        StringBuilder stdout = new StringBuilder();
        StringBuilder stderr = new StringBuilder();

        Thread stdoutReader = new Thread(() -> {
            try (BufferedReader reader =
                         new BufferedReader(
                                 new InputStreamReader(process.getInputStream()))) {

                String line;

                while ((line = reader.readLine()) != null) {
                    stdout.append(line).append(System.lineSeparator());
                }

            } catch (IOException e) {
                stderr.append(e.getMessage());
            }
        });

        Thread stderrReader = new Thread(() -> {
            try (BufferedReader reader =
                         new BufferedReader(
                                 new InputStreamReader(process.getErrorStream()))) {

                String line;

                while ((line = reader.readLine()) != null) {
                    stderr.append(line).append(System.lineSeparator());
                }

            } catch (IOException e) {
                stderr.append(e.getMessage());
            }
        });

        stdoutReader.start();
        stderrReader.start();

        int exitCode = process.waitFor();

        stdoutReader.join();
        stderrReader.join();

        if (exitCode != 0) {
            throw new IllegalStateException(
                    "BLAST failed with exit code "
                            + exitCode
                            + "\n"
                            + stderr);
        }

        Path output = Paths.get(settings.getOutputFile());

        if (!Files.exists(output)) {
            throw new IllegalStateException(
                    "BLAST completed but output file was not created: "
                            + output);
        }

        long executionTime =
                System.currentTimeMillis() - start;

        return new BlastResult(
                settings.getOutputFile(),
                stderr.toString(),
                executionTime);
    }

    private List<String> buildCommand(BlastSettings settings) {

        List<String> command = new ArrayList<>();

        command.add(resolveExecutable(settings));

        command.add("-query");
        command.add(settings.getQueryFile());

        command.add("-db");
        command.add(settings.getDatabase());

        command.add("-out");
        command.add(settings.getOutputFile());

        command.add("-num_threads");
        command.add(String.valueOf(settings.getThreads()));

        command.add("-evalue");
        command.add(String.valueOf(settings.getEvalue()));

        command.add("-max_target_seqs");
        command.add(String.valueOf(
                settings.getMaxTargetSequences()));

        command.add("-outfmt");
        command.add(getOutputFormat(settings));

        if (settings.getWordSize() > 0) {
            command.add("-word_size");
            command.add(String.valueOf(settings.getWordSize()));
        }

        if (settings.getGapOpen() >= 0) {
            command.add("-gapopen");
            command.add(String.valueOf(settings.getGapOpen()));
        }

        if (settings.getGapExtend() >= 0) {
            command.add("-gapextend");
            command.add(String.valueOf(settings.getGapExtend()));
        }

        if (settings.getTask() != null
                && !settings.getTask().isBlank()) {

            command.add("-task");
            command.add(settings.getTask());
        }

        if (settings.getExtraArguments() != null
                && !settings.getExtraArguments().isBlank()) {

            command.addAll(
                    Arrays.asList(
                            settings.getExtraArguments().split("\\s+")));
        }

        return command;
    }

    private String resolveExecutable(
            BlastSettings settings) {

        if (settings.getBlastExecutable() != null
                && !settings.getBlastExecutable().isBlank()) {

            return settings.getBlastExecutable();
        }

        return switch (settings.getProgram()) {

            case BLASTN -> "blastn";
            case BLASTP -> "blastp";
            case BLASTX -> "blastx";
            case TBLASTN -> "tblastn";
            case TBLASTX -> "tblastx";
        };
    }

    private String getOutputFormat(
            BlastSettings settings) {

        return switch (settings.getOutputFormat()) {

            case XML -> "5";

            case TABULAR -> "6";

            case TEXT -> "0";
        };
    }
}