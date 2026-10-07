package com.bio.sequencing.plugins.external_tool_support.mafft;

import java.util.ArrayList;
import java.util.List;

public class MultipleSequenceAlignment {

    private List<SequenceRow> rows = new ArrayList<>();

    public MultipleSequenceAlignment() {
    }

    public MultipleSequenceAlignment(List<SequenceRow> rows) {
        this.rows = new ArrayList<>(rows);
    }

    public List<SequenceRow> getRows() {
        return rows;
    }

    public void setRows(List<SequenceRow> rows) {
        this.rows = rows;
    }

    public String toFasta() {

        StringBuilder fasta = new StringBuilder();

        for (SequenceRow row : rows) {

            fasta.append(">")
                    .append(row.getName())
                    .append("\n");

            fasta.append(row.getSequence())
                    .append("\n");
        }

        return fasta.toString();
    }

    public static MultipleSequenceAlignment fromFasta(
            String fasta) {

        List<SequenceRow> rows = new ArrayList<>();

        String currentName = null;
        StringBuilder sequence = new StringBuilder();

        for (String line : fasta.split("\\R")) {

            if (line.startsWith(">")) {

                if (currentName != null) {
                    rows.add(
                            new SequenceRow(
                                    currentName,
                                    sequence.toString()
                            )
                    );
                }

                currentName = line.substring(1).trim();
                sequence = new StringBuilder();

            } else {

                sequence.append(line.trim());
            }
        }

        if (currentName != null) {
            rows.add(
                    new SequenceRow(
                            currentName,
                            sequence.toString()
                    )
            );
        }

        return new MultipleSequenceAlignment(rows);
    }
}