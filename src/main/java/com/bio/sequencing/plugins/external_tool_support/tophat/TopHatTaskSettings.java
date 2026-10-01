package com.bio.sequencing.plugins.external_tool_support.tophat;

public record TopHatTaskSettings(
        String inputFastq1,
        String inputFastq2,
        String referenceIndex,
        String outputDirectory,
        String annotationGtf,
        int threads,
        int readMismatches,
        int readGapLength,
        int readEditDistance,
        String tophatExecutable,
        String workingDirectory
) {
    public boolean isPairedEnd() {
        return inputFastq2 != null
                && !inputFastq2.isBlank();
    }
}