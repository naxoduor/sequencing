package com.bio.sequencing.plugins.external_tool_support.mafft;

public class MAFFTSettings {

    private String inputFilePath;

    private String outputFilePath;

    private String mafftExecutable = "mafft";

    private String algorithm = "--auto";

    private int threads = 1;

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

    public String getMafftExecutable() {
        return mafftExecutable;
    }

    public void setMafftExecutable(String mafftExecutable) {
        this.mafftExecutable = mafftExecutable;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }
}