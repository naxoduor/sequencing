package com.bio.sequencing.plugins.external_tool_support.stringtie;

public record StringTieTaskSettings(
        String inputBam,
        String outputGtf,
        String outputDirectory,
        String referenceGtf,
        String stringTieExecutable,
        int threads,
        double minimumTranscriptLength,
        double minimumAbundance,
        boolean estimateAbundance,
        boolean mergeMode,
        String workingDirectory
) {}