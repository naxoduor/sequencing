package com.bio.sequencing.plugins.external_tool_support.trimmomatic;

public record TrimmomaticTaskSettings(
        String inputFile,
        String outputFile,
        String trimmomaticJar,
        String adaptersFile,
        int threads,
        String workingDirectory
) {}
