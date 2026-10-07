package com.bio.sequencing.plugins.external_tool_support.clustalo;

public class ClustalOSettings {

    private String inputFilePath;

    private String outputFilePath;

    private String clustalOExecutable = "clustalo";

    private int threads = 1;

    private boolean force = true;

    public String getInputFilePath() {
        return inputFilePath;
    }

    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public String getClustalOExecutable() {
        return clustalOExecutable;
    }

    public void setClustalOExecutable(
            String clustalOExecutable) {

        this.clustalOExecutable =
                clustalOExecutable;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }
}