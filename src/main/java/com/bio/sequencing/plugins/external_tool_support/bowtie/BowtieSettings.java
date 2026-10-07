package com.bio.sequencing.plugins.external_tool_support.bowtie;

public class BowtieSettings {

    public enum Version {
        BOWTIE1,
        BOWTIE2
    }

    public enum InputMode {
        SINGLE_END,
        PAIRED_END
    }

    private String referenceIndex;

    private String read1File;
    private String read2File;

    private String outputSamFile;

    private String bowtieExecutable = "bowtie2";

    private Version version = Version.BOWTIE2;

    private InputMode inputMode = InputMode.SINGLE_END;

    private int threads = 4;

    private boolean noUnaligned = false;

    private boolean noHead = false;

    private boolean noSQ = false;

    private int seedLength = 20;

    private int maxMismatches = 0;

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

    public String getBowtieExecutable() {
        return bowtieExecutable;
    }

    public void setBowtieExecutable(String bowtieExecutable) {
        this.bowtieExecutable = bowtieExecutable;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
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

    public boolean isNoUnaligned() {
        return noUnaligned;
    }

    public void setNoUnaligned(boolean noUnaligned) {
        this.noUnaligned = noUnaligned;
    }

    public boolean isNoHead() {
        return noHead;
    }

    public void setNoHead(boolean noHead) {
        this.noHead = noHead;
    }

    public boolean isNoSQ() {
        return noSQ;
    }

    public void setNoSQ(boolean noSQ) {
        this.noSQ = noSQ;
    }

    public int getSeedLength() {
        return seedLength;
    }

    public void setSeedLength(int seedLength) {
        this.seedLength = seedLength;
    }

    public int getMaxMismatches() {
        return maxMismatches;
    }

    public void setMaxMismatches(int maxMismatches) {
        this.maxMismatches = maxMismatches;
    }

    public String getExtraArguments() {
        return extraArguments;
    }

    public void setExtraArguments(String extraArguments) {
        this.extraArguments = extraArguments;
    }
}