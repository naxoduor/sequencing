package com.bio.sequencing.plugins.external_tool_support.stringtie;

public class BamObject {

    private final String filePath;

    public BamObject(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}