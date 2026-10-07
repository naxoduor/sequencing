package com.bio.sequencing.plugins.external_tool_support.blast;

public class BlastSettings {

    public enum Program {
        BLASTN,
        BLASTP,
        BLASTX,
        TBLASTN,
        TBLASTX
    }

    public enum OutputFormat {
        XML,
        TABULAR,
        TEXT
    }

    private Program program = Program.BLASTN;

    private String queryFile;
    private String database;

    private String outputFile;

    private String blastExecutable = "blastn";

    private OutputFormat outputFormat = OutputFormat.XML;

    private int threads = 4;

    private int maxTargetSequences = 50;

    private double evalue = 10.0;

    private int wordSize;

    private int gapOpen = -1;
    private int gapExtend = -1;

    private String task;

    private String extraArguments;

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public String getQueryFile() {
        return queryFile;
    }

    public void setQueryFile(String queryFile) {
        this.queryFile = queryFile;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getBlastExecutable() {
        return blastExecutable;
    }

    public void setBlastExecutable(String blastExecutable) {
        this.blastExecutable = blastExecutable;
    }

    public OutputFormat getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getMaxTargetSequences() {
        return maxTargetSequences;
    }

    public void setMaxTargetSequences(int maxTargetSequences) {
        this.maxTargetSequences = maxTargetSequences;
    }

    public double getEvalue() {
        return evalue;
    }

    public void setEvalue(double evalue) {
        this.evalue = evalue;
    }

    public int getWordSize() {
        return wordSize;
    }

    public void setWordSize(int wordSize) {
        this.wordSize = wordSize;
    }

    public int getGapOpen() {
        return gapOpen;
    }

    public void setGapOpen(int gapOpen) {
        this.gapOpen = gapOpen;
    }

    public int getGapExtend() {
        return gapExtend;
    }

    public void setGapExtend(int gapExtend) {
        this.gapExtend = gapExtend;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getExtraArguments() {
        return extraArguments;
    }

    public void setExtraArguments(String extraArguments) {
        this.extraArguments = extraArguments;
    }
}