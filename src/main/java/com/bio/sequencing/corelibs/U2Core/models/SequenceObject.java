package com.bio.sequencing.corelibs.U2Core.models;

import java.util.UUID;

public class SequenceObject implements DocumentObject {

    private final UUID id = UUID.randomUUID();

    private String name;
    private String sequence;
    private String description;

    private boolean modified;

    public SequenceObject(
            String name,
            String sequence,
            String description) {

        this.name = name;
        this.sequence = sequence;
        this.description = description;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ObjectType getType() {
        return ObjectType.SEQUENCE;
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    public String getSequence() {
        return sequence;
    }

    public String getDescription() {
        return description;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
        this.modified = true;
    }

    public void setName(String name) {
        this.name = name;
        this.modified = true;
    }
}