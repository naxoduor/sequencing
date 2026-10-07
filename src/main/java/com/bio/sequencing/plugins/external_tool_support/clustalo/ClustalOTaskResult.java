package com.bio.sequencing.plugins.external_tool_support.clustalo;

public record ClustalOTaskResult(
        String taskId,
        String inputFasta,
        String alignedFasta,
        String outputFormat,
        String outputDirectory
) {}