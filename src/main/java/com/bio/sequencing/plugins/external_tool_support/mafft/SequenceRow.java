package com.bio.sequencing.plugins.external_tool_support.mafft;

public class SequenceRow {

    private String name;
    private String sequence;

    public SequenceRow(
            String name,
            String sequence) {

        this.name = name;
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}