package com.bio.sequencing.workflow;

import com.bio.sequencing.port.Port;

public interface Worker {

    void init();

    void tick();

    Port getInputPort(String id);

    Port getOutputPort(String id);
}