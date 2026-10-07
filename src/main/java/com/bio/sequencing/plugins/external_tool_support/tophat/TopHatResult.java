package com.bio.sequencing.plugins.external_tool_support.tophat;

public class TopHatResult {

    private String outputDirectory;

    private String acceptedHitsBam;

    private String junctionsBed;

    private String insertionsBed;

    private String deletionsBed;

    private String unmappedBam;

    private String log;

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(
            String outputDirectory
    ) {
        this.outputDirectory = outputDirectory;
    }

    public String getAcceptedHitsBam() {
        return acceptedHitsBam;
    }

    public void setAcceptedHitsBam(
            String acceptedHitsBam
    ) {
        this.acceptedHitsBam = acceptedHitsBam;
    }

    public String getJunctionsBed() {
        return junctionsBed;
    }

    public void setJunctionsBed(
            String junctionsBed
    ) {
        this.junctionsBed = junctionsBed;
    }

    public String getInsertionsBed() {
        return insertionsBed;
    }

    public void setInsertionsBed(
            String insertionsBed
    ) {
        this.insertionsBed = insertionsBed;
    }

    public String getDeletionsBed() {
        return deletionsBed;
    }

    public void setDeletionsBed(
            String deletionsBed
    ) {
        this.deletionsBed = deletionsBed;
    }

    public String getUnmappedBam() {
        return unmappedBam;
    }

    public void setUnmappedBam(
            String unmappedBam
    ) {
        this.unmappedBam = unmappedBam;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}