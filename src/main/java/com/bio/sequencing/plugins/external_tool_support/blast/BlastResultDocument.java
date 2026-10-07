package com.bio.sequencing.plugins.external_tool_support.blast;

import java.util.List;

public class BlastResultDocument {

    private List<BlastHit> hits;

    public BlastResultDocument(
            List<BlastHit> hits) {

        this.hits = hits;
    }

    public List<BlastHit> getHits() {
        return hits;
    }
}