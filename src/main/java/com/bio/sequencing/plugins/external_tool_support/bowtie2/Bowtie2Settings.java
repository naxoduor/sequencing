package com.bio.sequencing.plugins.external_tool_support.bowtie2;

public class Bowtie2Settings {

    public enum InputMode {
        SINGLE_END,
        PAIRED_END
    }

    private String referenceIndex;

    private String read1File;
    private String read2File;

    private String outputSamFile;
    private String outputBamFile;

    private String bowtie2Executable = "bowtie2";

    private InputMode inputMode = InputMode.SINGLE_END;

    private int threads = 4;

    private boolean noUnal = false;
    private boolean noHead = false;
    private boolean noSQ = false;

    private boolean localAlignment = false;

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

    public String getBowtie2Executable() {
        return bowtie2Executable;
    }

    public void setBowtie2Executable(String bowtie2Executable) {
        this.bowtie2Executable = bowtie2Executable;
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

    public boolean isNoUnal() {
        return noUnal;
    }

    public void setNoUnal(boolean noUnal) {
        this.noUnal = noUnal;
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

    public boolean isLocalAlignment() {
        return localAlignment;
    }

    public void setLocalAlignment(boolean localAlignment) {
        this.localAlignment = localAlignment;
    }

    public String getExtraArguments() {
        return extraArguments;
    }

    public void setExtraArguments(String extraArguments) {
        this.extraArguments = extraArguments;
    }
}