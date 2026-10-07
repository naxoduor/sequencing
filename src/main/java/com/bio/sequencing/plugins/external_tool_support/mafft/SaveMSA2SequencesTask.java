package com.bio.sequencing.plugins.external_tool_support.mafft;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SaveMSA2SequencesTask extends AbstractTask {

    private final MultipleSequenceAlignment msa;

    private final String url;

    private final boolean something;

    private final String format;

    public SaveMSA2SequencesTask(
            MultipleSequenceAlignment msa,
            String url,
            boolean something,
            String format) {

        super("Save MSA to FASTA");

        this.msa = msa;
        this.url = url;
        this.something = something;
        this.format = format;
    }

    @Override
    protected void execute() {

        try {

            String fasta = msa.toFasta();

            Files.writeString(
                    Path.of(url),
                    fasta
            );

        } catch (IOException e) {

            setError(
                    "Failed to save MSA: "
                            + e.getMessage()
            );
        }
    }

    public String getUrlString() {
        return url;
    }
}