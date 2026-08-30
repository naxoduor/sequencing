package com.bio.sequencing.workers;

import com.bio.sequencing.port.Port;
import com.bio.sequencing.sequence.Sequence;

public class AlignerWorker implements Worker {

    private final Port input;
    private final Port output;

    private boolean done = false;

    public AlignerWorker(
            Port input,
            Port output) {

        this.input = input;
        this.output = output;
    }

    @Override
    public void init() {
        System.out.println("Aligner initialized");
    }

    @Override
    public void tick() {

        if (!input.hasData()) {
            return;
        }

        Sequence sequence =
                (Sequence) input.get();

        System.out.println(
                "Aligning: " + sequence.getData()
        );

        output.put(sequence);

        done = true;
    }

    @Override
    public boolean isDone() {
        return done;
    }
}