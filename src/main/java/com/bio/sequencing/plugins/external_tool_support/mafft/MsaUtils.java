package com.bio.sequencing.plugins.external_tool_support.mafft;

import java.util.ArrayList;
import java.util.List;

public final class MsaUtils {

    private MsaUtils() {
    }

    public static MultipleSequenceAlignment
    createCopyWithIndexedRowNames(
            MultipleSequenceAlignment inputMsa) {

        List<SequenceRow> rows = new ArrayList<>();

        int index = 1;

        for (SequenceRow row : inputMsa.getRows()) {

            rows.add(
                    new SequenceRow(
                            "sequence_" + index,
                            row.getSequence()
                    )
            );

            index++;
        }

        return new MultipleSequenceAlignment(rows);
    }
}