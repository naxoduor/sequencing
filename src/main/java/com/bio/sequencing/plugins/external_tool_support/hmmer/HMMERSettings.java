package com.bio.sequencing.plugins.external_tool_support.hmmer;

public class HMMERSettings {

    public enum Operation {
        HMMSEARCH,
        HMMSCAN
    }

    public enum OutputFormat {
        TBL_OUT,
        DOM_TBL_OUT,
        BOTH
    }

    private Operation operation = Operation.HMMSEARCH;

    /*
     * hmmsearch:
     *
     *   HMM file
     *   +
     *   sequence file
     *
     * hmmscan:
     *
     *   HMM database
     *   +
     *   sequence file
     */

    private String hmmFile;

    private String sequenceFile;

    private String hmmDatabase;

    private String outputDirectory;

    private String outputFile;

    private String tblOutputFile;

    private String domTblOutputFile;

    private String alignmentOutputFile;

    private String hmmerExecutable;

    private int threads = 1;

    private double sequenceEvalue = 10.0;

    private double domainEvalue = 10.0;

    private int maxSequences = 0;

    private int maxDomains = 0;

    private boolean noAlignment = false;

    private OutputFormat outputFormat =
            OutputFormat.BOTH;

    private String extraArguments;

    public HMMERSettings() {
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getHmmFile() {
        return hmmFile;
    }

    public void setHmmFile(String hmmFile) {
        this.hmmFile = hmmFile;
    }

    public String getSequenceFile() {
        return sequenceFile;
    }

    public void setSequenceFile(String sequenceFile) {
        this.sequenceFile = sequenceFile;
    }

    public String getHmmDatabase() {
        return hmmDatabase;
    }

    public void setHmmDatabase(String hmmDatabase) {
        this.hmmDatabase = hmmDatabase;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getTblOutputFile() {
        return tblOutputFile;
    }

    public void setTblOutputFile(String tblOutputFile) {
        this.tblOutputFile = tblOutputFile;
    }

    public String getDomTblOutputFile() {
        return domTblOutputFile;
    }

    public void setDomTblOutputFile(String domTblOutputFile) {
        this.domTblOutputFile = domTblOutputFile;
    }

    public String getAlignmentOutputFile() {
        return alignmentOutputFile;
    }

    public void setAlignmentOutputFile(
            String alignmentOutputFile) {

        this.alignmentOutputFile =
                alignmentOutputFile;
    }

    public String getHmmerExecutable() {
        return hmmerExecutable;
    }

    public void setHmmerExecutable(
            String hmmerExecutable) {

        this.hmmerExecutable =
                hmmerExecutable;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public double getSequenceEvalue() {
        return sequenceEvalue;
    }

    public void setSequenceEvalue(
            double sequenceEvalue) {

        this.sequenceEvalue =
                sequenceEvalue;
    }

    public double getDomainEvalue() {
        return domainEvalue;
    }

    public void setDomainEvalue(
            double domainEvalue) {

        this.domainEvalue =
                domainEvalue;
    }

    public int getMaxSequences() {
        return maxSequences;
    }

    public void setMaxSequences(int maxSequences) {
        this.maxSequences = maxSequences;
    }

    public int getMaxDomains() {
        return maxDomains;
    }

    public void setMaxDomains(int maxDomains) {
        this.maxDomains = maxDomains;
    }

    public boolean isNoAlignment() {
        return noAlignment;
    }

    public void setNoAlignment(boolean noAlignment) {
        this.noAlignment = noAlignment;
    }

    public OutputFormat getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(
            OutputFormat outputFormat) {

        this.outputFormat =
                outputFormat;
    }

    public String getExtraArguments() {
        return extraArguments;
    }

    public void setExtraArguments(
            String extraArguments) {

        this.extraArguments =
                extraArguments;
    }
}