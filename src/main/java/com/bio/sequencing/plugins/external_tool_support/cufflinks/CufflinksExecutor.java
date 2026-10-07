package com.bio.sequencing.plugins.external_tool_support.cufflinks;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CufflinksExecutor {

    public CufflinksResult execute(
            CufflinksSettings settings
    ) throws Exception {

        Path outputDirectory =
                Path.of(settings.getOutputDirectory());

        Files.createDirectories(outputDirectory);

        List<String> command =
                new ArrayList<>();

        command.add(
                settings.getCufflinksExecutable()
        );

        /*
         * Number of threads.
         */
        command.add("-p");
        command.add(
                String.valueOf(settings.getThreads())
        );

        /*
         * Output directory.
         */
        command.add("-o");
        command.add(
                settings.getOutputDirectory()
        );

        /*
         * Reference annotation.
         */
        if (settings.getReferenceGtf() != null
                && !settings.getReferenceGtf().isBlank()) {

            command.add("-G");
            command.add(
                    settings.getReferenceGtf()
            );
        }

        /*
         * Reference transcriptome.
         */
        if (settings.getReferenceFasta() != null
                && !settings.getReferenceFasta().isBlank()) {

            command.add("-b");
            command.add(
                    settings.getReferenceFasta()
            );
        }

        /*
         * Compatible hits only.
         */
        if (settings.isCompatibleHitsOnly()) {
            command.add("-u");
        }

        /*
         * Multi-read correction.
         */
        if (settings.isMultiReadCorrect()) {
            command.add("-u");
        }

        /*
         * Frag-bias correction.
         */
        if (settings.isFragBiasCorrect()) {
            command.add("-b");
        }

        /*
         * Input BAM.
         */
        command.add(
                settings.getInputBamFile()
        );

        System.out.println(
                "Starting Cufflinks:"
        );

        System.out.println(
                String.join(" ", command)
        );

        ProcessBuilder processBuilder =
                new ProcessBuilder(command);

        processBuilder.redirectErrorStream(true);

        Process process =
                processBuilder.start();

        String log;

        try (InputStream inputStream =
                     process.getInputStream()) {

            log = new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );
        }

        int exitCode =
                process.waitFor();

        System.out.println(
                "Cufflinks output:"
        );

        System.out.println(log);

        if (exitCode != 0) {

            throw new RuntimeException(
                    "Cufflinks failed with exit code "
                            + exitCode
                            + "\n"
                            + log
            );
        }

        return buildResult(
                outputDirectory,
                log
        );
    }

    private CufflinksResult buildResult(
            Path outputDirectory,
            String log
    ) {

        CufflinksResult result =
                new CufflinksResult();

        result.setOutputDirectory(
                outputDirectory.toString()
        );

        Path transcripts =
                outputDirectory.resolve(
                        "transcripts.gtf"
                );

        Path genes =
                outputDirectory.resolve(
                        "genes.fpkm_tracking"
                );

        Path isoforms =
                outputDirectory.resolve(
                        "isoforms.fpkm_tracking"
                );

        Path skipped =
                outputDirectory.resolve(
                        "skipped.gtf"
                );

        if (Files.exists(transcripts)) {
            result.setTranscriptsGtf(
                    transcripts.toString()
            );
        }

        if (Files.exists(genes)) {
            result.setGenesFpkm(
                    genes.toString()
            );
        }

        if (Files.exists(isoforms)) {
            result.setIsoformsFpkm(
                    isoforms.toString()
            );
        }

        if (Files.exists(skipped)) {
            result.setSkippedGtf(
                    skipped.toString()
            );
        }

        result.setLog(log);

        return result;
    }
}