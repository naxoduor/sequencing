package com.bio.sequencing.uploads;

public class UploadMetadata {

    private String uploadId;
    private String fileName;
    private long fileSize;
    private int totalChunks;
    private int chunkSize;

    public UploadMetadata() {
    }

    public UploadMetadata(
            String uploadId,
            String fileName,
            long fileSize,
            int totalChunks,
            int chunkSize) {

        this.uploadId = uploadId;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.totalChunks = totalChunks;
        this.chunkSize = chunkSize;
    }

    public String getUploadId() {
        return uploadId;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public int getTotalChunks() {
        return totalChunks;
    }

    public int getChunkSize() {
        return chunkSize;
    }
}