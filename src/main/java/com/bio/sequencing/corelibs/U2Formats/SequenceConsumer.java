package com.bio.sequencing.corelibs.U2Formats;

import java.io.IOException;

@FunctionalInterface
public interface SequenceConsumer {

    void accept(SequenceRecord record) throws IOException;
}
