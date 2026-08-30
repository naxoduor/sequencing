package com.bio.sequencing.workers;

import com.bio.sequencing.port.Port;
import com.bio.sequencing.sequence.Sequence;

public class FastaReaderWorker implements Worker {

    private final Port output;
    private boolean done = false;

    public FastaReaderWorker(Port output) {
        this.output = output;
    }

    @Override
    public void init() {
        System.out.println("Reader initialized");
    }

    @Override
    public void tick() {

        if (done) {
            return;
        }

        Sequence sequence =
                new Sequence("seq1", "ATCGATCGATCG");

        output.put(sequence);

        done = true;
    }

    @Override
    public boolean isDone() {
        return done;
    }
}