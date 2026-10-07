package com.bio.sequencing.plugins.external_tool_support.kalign;

public record KalignTaskResult(
        String taskId,
        String inputFasta,
        String alignedFasta,
        String outputFormat,
        String outputDirectory
) {
}