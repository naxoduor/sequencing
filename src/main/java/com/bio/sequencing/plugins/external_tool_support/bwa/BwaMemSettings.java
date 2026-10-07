package com.bio.sequencing.plugins.external_tool_support.bwa;

public class BwaMemSettings {

    public enum InputMode {
        SINGLE_END,
        PAIRED_END
    }

    private String referenceIndex;

    private String read1File;
    private String read2File;

    private String outputSamFile;
    private String outputBamFile;

    private String bwaExecutable = "bwa";

    private InputMode inputMode = InputMode.SINGLE_END;

    private int threads = 4;

    private int minSeedLength = 19;

    private int bandWidth = 100;

    private int gapOpenPenalty = 6;

    private int gapExtensionPenalty = 1;

    private int mismatchPenalty = 4;

    private int clippingPenalty = 5;

    private String readGroup;

    private String extraArguments;

    public String getReferenceIndex() {
        return referenceIndex;
    }

    public void setReferenceIndex(String referenceIndex) {
        this.referenceIndex = referenceIndex;
    }

    public String getRead1File() {
        return read1File;
    }

    public void setRead1File(String read1File) {
        this.read1File = read1File;
    }

    public String getRead2File() {
        return read2File;
    }

    public void setRead2File(String read2File) {
        this.read2File = read2File;
    }

    public String getOutputSamFile() {
        return outputSamFile;
    }

    public void setOutputSamFile(String outputSamFile) {
        this.outputSamFile = outputSamFile;
    }

    public String getOutputBamFile() {
        return outputBamFile;
    }

    public void setOutputBamFile(String outputBamFile) {
        this.outputBamFile = outputBamFile;
    }

    public String getBwaExecutable() {
        return bwaExecutable;
    }

    public void setBwaExecutable(String bwaExecutable) {
        this.bwaExecutable = bwaExecutable;
    }

    public InputMode getInputMode() {
        return inputMode;
    }

    public void setInputMode(InputMode inputMode) {
        this.inputMode = inputMode;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getMinSeedLength() {
        return minSeedLength;
    }

    public void setMinSeedLength(int minSeedLength) {
        this.minSeedLength = minSeedLength;
    }

    public int getBandWidth() {
        return bandWidth;
    }

    public void setBandWidth(int bandWidth) {
        this.bandWidth = bandWidth;
    }

    public int getGapOpenPenalty() {
        return gapOpenPenalty;
    }

    public void setGapOpenPenalty(int gapOpenPenalty) {
        this.gapOpenPenalty = gapOpenPenalty;
    }

    public int getGapExtensionPenalty() {
        return gapExtensionPenalty;
    }

    public void setGapExtensionPenalty(int gapExtensionPenalty) {
        this.gapExtensionPenalty = gapExtensionPenalty;
    }

    public int getMismatchPenalty() {
        return mismatchPenalty;
    }

    public void setMismatchPenalty(int mismatchPenalty) {
        this.mismatchPenalty = mismatchPenalty;
    }

    public int getClippingPenalty() {
        return clippingPenalty;
    }

    public void setClippingPenalty(int clippingPenalty) {
        this.clippingPenalty = clippingPenalty;
    }

    public String getReadGroup() {
        return readGroup;
    }

    public void setReadGroup(String readGroup) {
        this.readGroup = readGroup;
    }

    public String getExtraArguments() {
        return extraArguments;
    }

    public void setExtraArguments(String extraArguments) {
        this.extraArguments = extraArguments;
    }
}