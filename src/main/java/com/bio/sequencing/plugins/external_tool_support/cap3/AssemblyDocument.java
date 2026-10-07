package com.bio.sequencing.plugins.external_tool_support.cap3;

import com.bio.sequencing.plugins.external_tool_support.mafft.SequenceRow;

import java.util.*;

public class AssemblyDocument {

    private final List<Contig> contigs;

    private final List<SequenceRow> singlets;

    public AssemblyDocument(
            List<Contig> contigs,
            List<SequenceRow> singlets) {

        this.contigs = contigs;
        this.singlets = singlets;
    }

    public List<Contig> getContigs() {
        return contigs;
    }

    public List<SequenceRow> getSinglets() {
        return singlets;
    }
}