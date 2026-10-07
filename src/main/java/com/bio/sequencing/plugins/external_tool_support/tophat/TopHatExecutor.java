package com.bio.sequencing.plugins.external_tool_support.tophat;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TopHatExecutor {

    public TopHatResult execute(
            TopHatSettings settings
    ) throws Exception {

        validate(settings);

        Path outputDirectory =
                Path.of(
                        settings.getOutputDirectory()
                );

        Files.createDirectories(
                outputDirectory
        );

        List<String> command =
                new ArrayList<>();

        command.add(
                settings.getTophatExecutable()
        );

        /*
         * Number of threads.
         */
        command.add("-p");

        command.add(
                String.valueOf(
                        settings.getThreads()
                )
        );

        /*
         * Output directory.
         */
        command.add("-o");

        command.add(
                settings.getOutputDirectory()
        );

        /*
         * Annotation/GTF.
         */
        if (settings.getAnnotationFile() != null
                && !settings.getAnnotationFile().isBlank()) {

            command.add("-G");

            command.add(
                    settings.getAnnotationFile()
            );
        }

        /*
         * Mate inner distance.
         */
        if (settings.getMateInnerDist() > 0) {

            command.add(
                    "--mate-inner-dist"
            );

            command.add(
                    String.valueOf(
                            settings.getMateInnerDist()
                    )
            );
        }

        /*
         * Mate standard deviation.
         */
        if (settings.getMateStdDev() > 0) {

            command.add(
                    "--mate-std-dev"
            );

            command.add(
                    String.valueOf(
                            settings.getMateStdDev()
                    )
            );
        }

        /*
         * Disable novel junction discovery.
         */
        if (settings.isNoNovelJunctions()) {

            command.add(
                    "--no-novel-juncs"
            );
        }

        /*
         * Disable coverage search.
         */
        if (settings.isNoCoverageSearch()) {

            command.add(
                    "--no-coverage-search"
            );
        }

        /*
         * Microexon search.
         */
        if (settings.isMicroexonSearch()) {

            command.add(
                    "--microexon-search"
            );
        }

        /*
         * Use Bowtie 1 instead of Bowtie 2.
         */
        if (settings.isBowtie1()) {

            command.add(
                    "--bowtie1"
            );
        }

        /*
         * Reference index.
         */
        command.add(
                settings.getReferenceIndex()
        );

        /*
         * Read 1.
         */
        command.add(
                settings.getRead1File()
        );

        /*
         * Paired-end Read 2.
         */
        if (settings.getRead2File() != null
                && !settings.getRead2File().isBlank()) {

            command.add(
                    settings.getRead2File()
            );
        }

        System.out.println(
                "Starting TopHat:"
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
                "TopHat output:"
        );

        System.out.println(log);

        if (exitCode != 0) {

            throw new RuntimeException(
                    "TopHat failed with exit code "
                            + exitCode
                            + "\n"
                            + log
            );
        }

        return createResult(
                outputDirectory,
                log
        );
    }

    private void validate(
            TopHatSettings settings
    ) {

        if (settings.getReferenceIndex() == null
                || settings.getReferenceIndex().isBlank()) {

            throw new IllegalArgumentException(
                    "Reference index is required"
            );
        }

        if (settings.getRead1File() == null
                || settings.getRead1File().isBlank()) {

            throw new IllegalArgumentException(
                    "Read 1 file is required"
            );
        }

        if (!Files.exists(
                Path.of(
                        settings.getRead1File()
                )
        )) {

            throw new IllegalArgumentException(
                    "Read 1 does not exist: "
                            + settings.getRead1File()
            );
        }

        if (settings.getRead2File() != null
                && !settings.getRead2File().isBlank()
                && !Files.exists(
                Path.of(
                        settings.getRead2File()
                )
        )) {

            throw new IllegalArgumentException(
                    "Read 2 does not exist: "
                            + settings.getRead2File()
            );
        }
    }

    private TopHatResult createResult(
            Path outputDirectory,
            String log
    ) {

        TopHatResult result =
                new TopHatResult();

        result.setOutputDirectory(
                outputDirectory.toString()
        );

        result.setLog(log);

        Path acceptedHits =
                outputDirectory.resolve(
                        "accepted_hits.bam"
                );

        Path junctions =
                outputDirectory.resolve(
                        "junctions.bed"
                );

        Path insertions =
                outputDirectory.resolve(
                        "insertions.bed"
                );

        Path deletions =
                outputDirectory.resolve(
                        "deletions.bed"
                );

        Path unmapped =
                outputDirectory.resolve(
                        "unmapped.bam"
                );

        if (Files.exists(acceptedHits)) {

            result.setAcceptedHitsBam(
                    acceptedHits.toString()
            );
        }

        if (Files.exists(junctions)) {

            result.setJunctionsBed(
                    junctions.toString()
            );
        }

        if (Files.exists(insertions)) {

            result.setInsertionsBed(
                    insertions.toString()
            );
        }

        if (Files.exists(deletions)) {

            result.setDeletionsBed(
                    deletions.toString()
            );
        }

        if (Files.exists(unmapped)) {

            result.setUnmappedBam(
                    unmapped.toString()
            );
        }

        return result;
    }
}