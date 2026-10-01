package com.bio.sequencing.plugins.external_tool_support.cufflinks;

public record CufflinksTaskSettings(
        String inputBam,
        String outputDirectory,
        String referenceGtf,
        String referenceSequence,
        int threads,
        String cufflinksExecutable,
        String workingDirectory
) {}