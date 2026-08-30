package com.bio.sequencing.workers;

public class Actor {

    private final String id;
    private final Worker worker;

    public Actor(String id, Worker worker) {
        this.id = id;
        this.worker = worker;
    }

    public String getId() {
        return id;
    }

    public Worker getWorker() {
        return worker;
    }
}