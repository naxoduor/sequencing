package com.bio.sequencing.plugins.external_tool_support.kalign;

public record KalignTaskSettings(
        String inputFasta,
        String outputFasta,
        String kalignExecutable,
        String workingDirectory,
        String outputFormat,
        boolean overwrite,
        boolean verbose
) {
}