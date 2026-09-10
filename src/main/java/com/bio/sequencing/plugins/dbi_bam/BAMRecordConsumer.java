package com.bio.sequencing.plugins.dbi_bam;

@FunctionalInterface
public interface BAMRecordConsumer {

    void accept(BAMRecord record);
}