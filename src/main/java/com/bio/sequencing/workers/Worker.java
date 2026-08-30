package com.bio.sequencing.workers;

public interface Worker {

    void init();

    void tick();

    boolean isDone();
}