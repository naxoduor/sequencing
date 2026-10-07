package com.bio.sequencing.plugins.external_tool_support.bowtie;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;
import com.bio.sequencing.plugins.external_tool_support.mafft.Document;

public class LoadReferenceDocumentTask
        extends AbstractTask {

    private final String referenceIndex;

    private Document document;

    public LoadReferenceDocumentTask(
            String referenceIndex) {

        super("Load Reference");

        this.referenceIndex =
                referenceIndex;
    }

    @Override
    protected void execute() {

        ReferenceObject referenceObject =
                new ReferenceObject(
                        referenceIndex
                );

        document =
                new Document(
                        "Reference"
                );

        document.addObject(
                referenceObject
        );
    }

    public Document takeDocument() {

        Document result = document;

        document = null;

        return result;
    }
}