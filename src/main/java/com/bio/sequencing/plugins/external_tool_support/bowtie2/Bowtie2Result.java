package com.bio.sequencing.plugins.external_tool_support.bowtie2;

public class Bowtie2Result {

    private String samFile;

    private String bamFile;

    private String log;

    public String getSamFile() {
        return samFile;
    }

    public void setSamFile(String samFile) {
        this.samFile = samFile;
    }

    public String getBamFile() {
        return bamFile;
    }

    public void setBamFile(String bamFile) {
        this.bamFile = bamFile;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}