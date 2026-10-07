package com.bio.sequencing.plugins.external_tool_support.cufflinks;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;
import com.bio.sequencing.plugins.external_tool_support.mafft.Document;

import java.nio.file.Files;
import java.nio.file.Path;

public class LoadBamDocumentTask
        extends AbstractTask {

    private final String bamFile;

    private Document document;

    public LoadBamDocumentTask(
            String bamFile
    ) {

        super("Load BAM document");

        this.bamFile = bamFile;
    }

    @Override
    protected void execute()
            throws Exception {

        if (!Files.exists(
                Path.of(bamFile)
        )) {

            throw new IllegalArgumentException(
                    "BAM file does not exist: "
                            + bamFile
            );
        }

        BamObject bamObject =
                new BamObject(
                        bamFile
                );

        /*
         * Automatically detect accompanying BAI.
         */
        Path bai =
                Path.of(
                        bamFile + ".bai"
                );

        if (Files.exists(bai)) {

            bamObject.setBaiFile(
                    bai.toString()
            );
        }

        document =
                new Document(bamFile);

        document.addObject(
                bamObject
        );
    }

    public Document takeDocument() {

        Document result =
                document;

        document = null;

        return result;
    }
}