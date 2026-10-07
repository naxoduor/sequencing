package com.bio.sequencing.plugins.external_tool_support.stringtie;

public class StringTieResult {

    private String transcriptGtf;
    private String geneAbundance;
    private String log;

    public String getTranscriptGtf() {
        return transcriptGtf;
    }

    public void setTranscriptGtf(String transcriptGtf) {
        this.transcriptGtf = transcriptGtf;
    }

    public String getGeneAbundance() {
        return geneAbundance;
    }

    public void setGeneAbundance(String geneAbundance) {
        this.geneAbundance = geneAbundance;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}