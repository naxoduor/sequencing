package com.bio.sequencing.plugins.external_tool_support.cap3;

import com.bio.sequencing.plugins.external_tool_support.mafft.SequenceRow;

import java.util.ArrayList;
import java.util.List;

public class Contig {

    private String id;

    private String sequence;

    private int length;

    private List<SequenceRow> reads =
            new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
        this.length =
                sequence != null
                        ? sequence.length()
                        : 0;
    }

    public int getLength() {
        return length;
    }

    public List<SequenceRow> getReads() {
        return reads;
    }
}