package com.bio.sequencing.corelibs.U2Core.models;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DocumentInfo {

    private String name;
    private String description;
    private Instant createdAt;
    private Instant modifiedAt;

    private final Map<String, Object> metadata =
            new ConcurrentHashMap<>();

    public DocumentInfo(String name) {
        this.name = name;
        this.createdAt = Instant.now();
        this.modifiedAt = Instant.now();
    }

    public void setMetadata(String key, Object value) {
        metadata.put(key, value);
        modifiedAt = Instant.now();
    }

    public Object getMetadata(String key) {
        return metadata.get(key);
    }

    public Map<String, Object> getMetadata() {
        return Map.copyOf(metadata);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        modifiedAt = Instant.now();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        modifiedAt = Instant.now();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getModifiedAt() {
        return modifiedAt;
    }
}