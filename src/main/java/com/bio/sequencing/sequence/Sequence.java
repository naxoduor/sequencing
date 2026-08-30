package com.bio.sequencing.sequence;

public class Sequence {

    private final String id;
    private final String data;

    public Sequence(String id, String data) {
        this.id = id;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public String getData() {
        return data;
    }
}