package com.bio.sequencing.corelibs.U2Formats;

@FunctionalInterface
public interface SequenceConsumer {

    void accept(SequenceRecord record) throws IOException;
}
