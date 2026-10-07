package com.bio.sequencing.plugins.external_tool_support.cufflinks;

public class BamObject {

    private final String bamFile;

    private String baiFile;

    public BamObject(
            String bamFile
    ) {
        this.bamFile = bamFile;
    }

    public String getBamFile() {
        return bamFile;
    }

    public String getBaiFile() {
        return baiFile;
    }

    public void setBaiFile(
            String baiFile
    ) {
        this.baiFile = baiFile;
    }
}