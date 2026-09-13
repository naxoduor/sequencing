package com.bio.sequencing.uploads;

public class CreateUploadRequest {

    private String fileName;
    private long fileSize;
    private int chunkSize;

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public int getChunkSize() {
        return chunkSize;
    }
}