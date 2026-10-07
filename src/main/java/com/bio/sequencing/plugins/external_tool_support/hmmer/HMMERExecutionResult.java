package com.bio.sequencing.plugins.external_tool_support.hmmer;

public class HMMERExecutionResult {

    private final String rawOutputFile;

    private final String tableOutputFile;

    private final String domainTableOutputFile;

    private final String alignmentOutputFile;

    private final String log;

    private final long executionTimeMs;

    public HMMERExecutionResult(
            String rawOutputFile,
            String tableOutputFile,
            String domainTableOutputFile,
            String alignmentOutputFile,
            String log,
            long executionTimeMs) {

        this.rawOutputFile =
                rawOutputFile;

        this.tableOutputFile =
                tableOutputFile;

        this.domainTableOutputFile =
                domainTableOutputFile;

        this.alignmentOutputFile =
                alignmentOutputFile;

        this.log = log;

        this.executionTimeMs =
                executionTimeMs;
    }

    public String getRawOutputFile() {
        return rawOutputFile;
    }

    public String getTableOutputFile() {
        return tableOutputFile;
    }

    public String getDomainTableOutputFile() {
        return domainTableOutputFile;
    }

    public String getAlignmentOutputFile() {
        return alignmentOutputFile;
    }

    public String getLog() {
        return log;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }
}