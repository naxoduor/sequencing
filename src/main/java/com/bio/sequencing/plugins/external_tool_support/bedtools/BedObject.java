package com.bio.sequencing.plugins.external_tool_support.bedtools;

public class BedObject {

    private final String filePath;

    public BedObject(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}