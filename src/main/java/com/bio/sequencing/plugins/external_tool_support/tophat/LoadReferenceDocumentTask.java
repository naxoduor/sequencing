package com.bio.sequencing.plugins.external_tool_support.tophat;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;
import com.bio.sequencing.plugins.external_tool_support.mafft.Document;

import java.nio.file.Files;
import java.nio.file.Path;

public class LoadReferenceDocumentTask
        extends AbstractTask {

    private final String referenceIndex;

    private Document document;

    public LoadReferenceDocumentTask(
            String referenceIndex
    ) {

        super("Load reference");

        this.referenceIndex =
                referenceIndex;
    }

    @Override
    protected void execute()
            throws Exception {

        /*
         * A Bowtie/TopHat index is normally represented
         * by several files rather than a single file.
         *
         * This example checks that the supplied
         * reference/index location exists.
         */
        if (!Files.exists(
                Path.of(referenceIndex)
        )) {

            throw new IllegalArgumentException(
                    "Reference index does not exist: "
                            + referenceIndex
            );
        }

        document =
                new Document(
                        referenceIndex
                );

        document.addObject(
                referenceIndex
        );
    }

    public Document takeDocument() {

        Document result =
                document;

        document = null;

        return result;
    }
}