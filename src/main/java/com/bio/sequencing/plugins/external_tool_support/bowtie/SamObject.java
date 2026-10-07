package com.bio.sequencing.plugins.external_tool_support.bowtie;

public class SamObject {

    private final String filePath;

    public SamObject(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}