package com.bio.sequencing.plugins.external_tool_support.blast;

public class BlastResult {

    private final String outputFile;
    private final String log;
    private final long executionTimeMs;

    public BlastResult(
            String outputFile,
            String log,
            long executionTimeMs) {

        this.outputFile = outputFile;
        this.log = log;
        this.executionTimeMs = executionTimeMs;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public String getLog() {
        return log;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }
}