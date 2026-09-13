package com.bio.sequencing.workflow;

public interface Worker {

    void init();

    void tick();

    InputPort getInputPort(String id);

    OutputPort getOutputPort(String id);
}