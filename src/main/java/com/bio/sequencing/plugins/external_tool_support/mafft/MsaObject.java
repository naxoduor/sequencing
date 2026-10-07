package com.bio.sequencing.plugins.external_tool_support.mafft;

import java.util.List;

public class MsaObject {

    private MultipleSequenceAlignment alignment;

    public MsaObject(
            MultipleSequenceAlignment alignment) {

        this.alignment = alignment;
    }

    public MultipleSequenceAlignment getAlignment() {
        return alignment;
    }

    public void setAlignment(
            MultipleSequenceAlignment alignment) {

        this.alignment = alignment;
    }

    /**
     * Equivalent conceptually to:
     *
     * mAObject->updateGapModel(...)
     */
    public void updateGapModel(
            List<SequenceRow> rows) {

        this.alignment.setRows(rows);
    }
}