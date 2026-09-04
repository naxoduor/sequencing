package com.bio.sequencing.plugins.external_tool_support;

public class ClustalOSupport {

    private final String executable;

    public ClustalOSupport(String executable) {
        this.executable = executable;
    }

    public ClustalOTask createTask(
            String inputFile,
            String outputFile) {

        return new ClustalOTask(
                executable,
                inputFile,
                outputFile
        );
    }
}