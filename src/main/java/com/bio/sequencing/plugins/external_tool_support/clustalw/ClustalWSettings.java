package com.bio.sequencing.plugins.external_tool_support.clustalw;

public class ClustalWSettings {

    private String inputFilePath;

    private String outputFilePath;

    private String clustalWExecutable = "clustalw2";

    private String outputFormat = "FASTA";

    private boolean outputOrderAligned = true;

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

    public String getClustalWExecutable() {
        return clustalWExecutable;
    }

    public void setClustalWExecutable(
            String clustalWExecutable) {

        this.clustalWExecutable =
                clustalWExecutable;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(
            String outputFormat) {

        this.outputFormat = outputFormat;
    }

    public boolean isOutputOrderAligned() {
        return outputOrderAligned;
    }

    public void setOutputOrderAligned(
            boolean outputOrderAligned) {

        this.outputOrderAligned =
                outputOrderAligned;
    }
}