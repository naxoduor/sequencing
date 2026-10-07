package com.bio.sequencing.plugins.external_tool_support.cufflinks;

public class CufflinksSettings {

    private String inputBamFile;
    private String outputDirectory;

    private String cufflinksExecutable = "cufflinks";

    private int threads = 1;

    private String referenceGtf;

    private String referenceFasta;

    private boolean compatibleHitsOnly = false;

    private boolean multiReadCorrect = false;

    private boolean fragBiasCorrect = false;

    private boolean upperQuartileNorm = false;

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

    public String getCufflinksExecutable() {
        return cufflinksExecutable;
    }

    public void setCufflinksExecutable(String cufflinksExecutable) {
        this.cufflinksExecutable = cufflinksExecutable;
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

    public boolean isCompatibleHitsOnly() {
        return compatibleHitsOnly;
    }

    public void setCompatibleHitsOnly(
            boolean compatibleHitsOnly
    ) {
        this.compatibleHitsOnly = compatibleHitsOnly;
    }

    public boolean isMultiReadCorrect() {
        return multiReadCorrect;
    }

    public void setMultiReadCorrect(
            boolean multiReadCorrect
    ) {
        this.multiReadCorrect = multiReadCorrect;
    }

    public boolean isFragBiasCorrect() {
        return fragBiasCorrect;
    }

    public void setFragBiasCorrect(
            boolean fragBiasCorrect
    ) {
        this.fragBiasCorrect = fragBiasCorrect;
    }

    public boolean isUpperQuartileNorm() {
        return upperQuartileNorm;
    }

    public void setUpperQuartileNorm(
            boolean upperQuartileNorm
    ) {
        this.upperQuartileNorm = upperQuartileNorm;
    }
}