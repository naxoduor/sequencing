package com.bio.sequencing.plugins.external_tool_support.kalign;

public class KalignSettings {

    private String inputFilePath;
    private String outputFilePath;

    private String kalignExecutable = "kalign";

    private int threads = 1;

    /**
     * Kalign output format.
     *
     * Commonly:
     * fasta
     * clustal
     * msf
     */
    private String outputFormat = "fasta";

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

    public String getKalignExecutable() {
        return kalignExecutable;
    }

    public void setKalignExecutable(String kalignExecutable) {
        this.kalignExecutable = kalignExecutable;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }
}