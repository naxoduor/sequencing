package com.bio.sequencing.plugins.external_tool_support.stringtie;

public class StringTieSettings {

    private String inputBamFile;
    private String outputDirectory;

    private String stringTieExecutable = "stringtie";

    private int threads = 4;

    private String referenceGtf;
    private String referenceFasta;

    private boolean estimateAbundance = true;
    private boolean mergeMode = false;
    private boolean ballgown = false;

    private double minIsoformAbundance = 0.1;
    private double minTranscriptLength = 200.0;

    public String getInputBamFile() {
        return inputBamFile;
    }

    public void setInputBamFile(String inputBamFile) {
        this.inputBamFile = inputBamFile;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public String getStringTieExecutable() {
        return stringTieExecutable;
    }

    public void setStringTieExecutable(String stringTieExecutable) {
        this.stringTieExecutable = stringTieExecutable;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public String getReferenceGtf() {
        return referenceGtf;
    }

    public void setReferenceGtf(String referenceGtf) {
        this.referenceGtf = referenceGtf;
    }

    public String getReferenceFasta() {
        return referenceFasta;
    }

    public void setReferenceFasta(String referenceFasta) {
        this.referenceFasta = referenceFasta;
    }

    public boolean isEstimateAbundance() {
        return estimateAbundance;
    }

    public void setEstimateAbundance(boolean estimateAbundance) {
        this.estimateAbundance = estimateAbundance;
    }

    public boolean isMergeMode() {
        return mergeMode;
    }

    public void setMergeMode(boolean mergeMode) {
        this.mergeMode = mergeMode;
    }

    public boolean isBallgown() {
        return ballgown;
    }

    public void setBallgown(boolean ballgown) {
        this.ballgown = ballgown;
    }

    public double getMinIsoformAbundance() {
        return minIsoformAbundance;
    }

    public void setMinIsoformAbundance(double minIsoformAbundance) {
        this.minIsoformAbundance = minIsoformAbundance;
    }

    public double getMinTranscriptLength() {
        return minTranscriptLength;
    }

    public void setMinTranscriptLength(double minTranscriptLength) {
        this.minTranscriptLength = minTranscriptLength;
    }
}