package com.bio.sequencing.plugins.external_tool_support.cap3;

public class Cap3Settings {

    private String inputFasta;

    private String outputDirectory;

    private String cap3Executable = "cap3";

    /*
     * Number of matching bases required for overlap.
     * CAP3 default is commonly 40.
     */
    private int overlapLength = 40;

    /*
     * Percent identity required for overlap.
     */
    private double overlapIdentity = 90.0;

    /*
     * Maximum number of base mismatches allowed in an overlap.
     */
    private int maxMismatches = 12;

    /*
     * Maximum number of unmatched bases.
     */
    private int maxUnmatchedBases = 20;

    /*
     * Maximum gap length.
     */
    private int maxGapLength = 20;

    /*
     * Minimum number of reads required to form a contig.
     */
    private int minimumReads = 2;

    /*
     * CAP3 generally works as a single process.
     */
    private boolean usePairedReads = false;

    private String extraArguments;

    public String getInputFasta() {
        return inputFasta;
    }

    public void setInputFasta(String inputFasta) {
        this.inputFasta = inputFasta;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public String getCap3Executable() {
        return cap3Executable;
    }

    public void setCap3Executable(String cap3Executable) {
        this.cap3Executable = cap3Executable;
    }

    public int getOverlapLength() {
        return overlapLength;
    }

    public void setOverlapLength(int overlapLength) {
        this.overlapLength = overlapLength;
    }

    public double getOverlapIdentity() {
        return overlapIdentity;
    }

    public void setOverlapIdentity(double overlapIdentity) {
        this.overlapIdentity = overlapIdentity;
    }

    public int getMaxMismatches() {
        return maxMismatches;
    }

    public void setMaxMismatches(int maxMismatches) {
        this.maxMismatches = maxMismatches;
    }

    public int getMaxUnmatchedBases() {
        return maxUnmatchedBases;
    }

    public void setMaxUnmatchedBases(int maxUnmatchedBases) {
        this.maxUnmatchedBases = maxUnmatchedBases;
    }

    public int getMaxGapLength() {
        return maxGapLength;
    }

    public void setMaxGapLength(int maxGapLength) {
        this.maxGapLength = maxGapLength;
    }

    public int getMinimumReads() {
        return minimumReads;
    }

    public void setMinimumReads(int minimumReads) {
        this.minimumReads = minimumReads;
    }

    public boolean isUsePairedReads() {
        return usePairedReads;
    }

    public void setUsePairedReads(boolean usePairedReads) {
        this.usePairedReads = usePairedReads;
    }

    public String getExtraArguments() {
        return extraArguments;
    }

    public void setExtraArguments(String extraArguments) {
        this.extraArguments = extraArguments;
    }
}