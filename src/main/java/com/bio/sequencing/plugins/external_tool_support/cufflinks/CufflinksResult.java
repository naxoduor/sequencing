package com.bio.sequencing.plugins.external_tool_support.cufflinks;

public class CufflinksResult {

    private String outputDirectory;

    private String transcriptsGtf;

    private String genesFpkm;

    private String isoformsFpkm;

    private String skippedGtf;

    private String log;

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(
            String outputDirectory
    ) {
        this.outputDirectory = outputDirectory;
    }

    public String getTranscriptsGtf() {
        return transcriptsGtf;
    }

    public void setTranscriptsGtf(
            String transcriptsGtf
    ) {
        this.transcriptsGtf = transcriptsGtf;
    }

    public String getGenesFpkm() {
        return genesFpkm;
    }

    public void setGenesFpkm(
            String genesFpkm
    ) {
        this.genesFpkm = genesFpkm;
    }

    public String getIsoformsFpkm() {
        return isoformsFpkm;
    }

    public void setIsoformsFpkm(
            String isoformsFpkm
    ) {
        this.isoformsFpkm = isoformsFpkm;
    }

    public String getSkippedGtf() {
        return skippedGtf;
    }

    public void setSkippedGtf(
            String skippedGtf
    ) {
        this.skippedGtf = skippedGtf;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}