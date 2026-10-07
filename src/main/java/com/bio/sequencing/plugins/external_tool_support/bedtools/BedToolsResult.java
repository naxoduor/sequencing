package com.bio.sequencing.plugins.external_tool_support.bedtools;

public class BedToolsResult {

    private String outputFile;

    private String log;

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}