package com.bio.sequencing.plugins.external_tool_support.trimmomatic;

public class TrimmomaticResult {

    private String pairedRead1;

    private String pairedRead2;

    private String unpairedRead1;

    private String unpairedRead2;

    private String log;

    public String getPairedRead1() {
        return pairedRead1;
    }

    public void setPairedRead1(
            String pairedRead1
    ) {
        this.pairedRead1 = pairedRead1;
    }

    public String getPairedRead2() {
        return pairedRead2;
    }

    public void setPairedRead2(
            String pairedRead2
    ) {
        this.pairedRead2 = pairedRead2;
    }

    public String getUnpairedRead1() {
        return unpairedRead1;
    }

    public void setUnpairedRead1(
            String unpairedRead1
    ) {
        this.unpairedRead1 = unpairedRead1;
    }

    public String getUnpairedRead2() {
        return unpairedRead2;
    }

    public void setUnpairedRead2(
            String unpairedRead2
    ) {
        this.unpairedRead2 = unpairedRead2;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}