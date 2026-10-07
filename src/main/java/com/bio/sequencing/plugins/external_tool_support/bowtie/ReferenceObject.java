package com.bio.sequencing.plugins.external_tool_support.bowtie;

public class ReferenceObject {

    private final String referenceIndex;

    public ReferenceObject(
            String referenceIndex) {

        this.referenceIndex =
                referenceIndex;
    }

    public String getReferenceIndex() {
        return referenceIndex;
    }
}