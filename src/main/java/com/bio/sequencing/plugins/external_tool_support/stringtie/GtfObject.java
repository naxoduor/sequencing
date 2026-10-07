package com.bio.sequencing.plugins.external_tool_support.stringtie;

public class GtfObject {

    private final String filePath;

    public GtfObject(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}