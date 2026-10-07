package com.bio.sequencing.plugins.external_tool_support.clustalw;

public record ClustalWTaskSettings(
        String inputFasta,
        String outputFasta,
        String clustalwExecutable,
        String workingDirectory,
        int threads,
        String outputFormat,
        boolean outputOrder,
        boolean convertToProtein
) {}