package com.bio.sequencing.workers;

import com.bio.sequencing.port.Port;
import com.bio.sequencing.sequence.Sequence;

public class ParserWorker implements Worker {

    private final Port input;
    private final Port output;

    private boolean done = false;

    public ParserWorker(Port input, Port output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public void init() {
        System.out.println("Parser initialized");
    }

    @Override
    public void tick() {

        if (!input.hasData()) {
            return;
        }

        Sequence sequence =
                (Sequence) input.get();

        System.out.println(
                "Parsing: " + sequence.getId()
        );

        output.put(sequence);

        done = true;
    }

    @Override
    public boolean isDone() {
        return done;
    }
}