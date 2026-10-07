package com.bio.sequencing.plugins.external_tool_support.clustalw;

public record ClustalWTaskResult(
        String taskId,
        String inputFasta,
        String alignedFasta,
        String outputFormat,
        String outputDirectory
) {}