package com.bio.sequencing.plugins.external_tool_support.tophat;

public class TopHatSettings {

    private String referenceIndex;

    private String read1File;

    private String read2File;

    private String outputDirectory;

    private String tophatExecutable = "tophat";

    private int threads = 1;

    private int mateInnerDist = 0;

    private int mateStdDev = 0;

    private String annotationFile;

    private boolean noNovelJunctions = false;

    private boolean noCoverageSearch = false;

    private boolean microexonSearch = false;

    private boolean bowtie1 = false;

    public String getReferenceIndex() {
        return referenceIndex;
    }

    public void setReferenceIndex(
            String referenceIndex
    ) {
        this.referenceIndex = referenceIndex;
    }

    public String getRead1File() {
        return read1File;
    }

    public void setRead1File(
            String read1File
    ) {
        this.read1File = read1File;
    }

    public String getRead2File() {
        return read2File;
    }

    public void setRead2File(
            String read2File
    ) {
        this.read2File = read2File;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(
            String outputDirectory
    ) {
        this.outputDirectory = outputDirectory;
    }

    public String getTophatExecutable() {
        return tophatExecutable;
    }

    public void setTophatExecutable(
            String tophatExecutable
    ) {
        this.tophatExecutable = tophatExecutable;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getMateInnerDist() {
        return mateInnerDist;
    }

    public void setMateInnerDist(
            int mateInnerDist
    ) {
        this.mateInnerDist = mateInnerDist;
    }

    public int getMateStdDev() {
        return mateStdDev;
    }

    public void setMateStdDev(
            int mateStdDev
    ) {
        this.mateStdDev = mateStdDev;
    }

    public String getAnnotationFile() {
        return annotationFile;
    }

    public void setAnnotationFile(
            String annotationFile
    ) {
        this.annotationFile = annotationFile;
    }

    public boolean isNoNovelJunctions() {
        return noNovelJunctions;
    }

    public void setNoNovelJunctions(
            boolean noNovelJunctions
    ) {
        this.noNovelJunctions = noNovelJunctions;
    }

    public boolean isNoCoverageSearch() {
        return noCoverageSearch;
    }

    public void setNoCoverageSearch(
            boolean noCoverageSearch
    ) {
        this.noCoverageSearch = noCoverageSearch;
    }

    public boolean isMicroexonSearch() {
        return microexonSearch;
    }

    public void setMicroexonSearch(
            boolean microexonSearch
    ) {
        this.microexonSearch = microexonSearch;
    }

    public boolean isBowtie1() {
        return bowtie1;
    }

    public void setBowtie1(
            boolean bowtie1
    ) {
        this.bowtie1 = bowtie1;
    }
}