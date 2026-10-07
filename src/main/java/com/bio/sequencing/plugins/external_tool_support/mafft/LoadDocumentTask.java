package com.bio.sequencing.plugins.external_tool_support.mafft;

import java.nio.file.Files;
import java.nio.file.Path;

public class LoadDocumentTask
        extends AbstractTask {

    private final String url;

    private Document document;

    public LoadDocumentTask(String url) {

        super("Load document");

        this.url = url;
    }

    @Override
    protected void execute() {

        try {

            String fasta =
                    Files.readString(
                            Path.of(url)
                    );

            MultipleSequenceAlignment msa =
                    MultipleSequenceAlignment
                            .fromFasta(fasta);

            MsaObject msaObject =
                    new MsaObject(msa);

            document =
                    new Document(url);

            document.addObject(msaObject);

        } catch (Exception e) {

            setError(
                    "Failed loading document: "
                            + e.getMessage()
            );
        }
    }

    public Document takeDocument() {

        Document result = document;

        document = null;

        return result;
    }

    public String getUrlString() {
        return url;
    }
}