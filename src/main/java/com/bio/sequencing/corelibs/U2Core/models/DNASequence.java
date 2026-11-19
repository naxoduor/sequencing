package com.bio.sequencing.corelibs.U2Core.models;


import java.util.UUID;

public class DNASequence implements DocumentObject {

    private final UUID id;

    private String name;

    private String sequence;

    private boolean modified;

    public DNASequence(String name, String sequence) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.sequence = sequence;
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
        return ObjectType.DNA_SEQUENCE;
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    public String getSequence() {
        return sequence;
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