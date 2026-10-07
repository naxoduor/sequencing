package com.bio.sequencing.plugins.external_tool_support.mafft;

import java.nio.file.Files;
import java.nio.file.Path;

public class FastaIOAdapterFactory
        implements IOAdapterFactory {

    @Override
    public void save(
            Document document,
            String path)
            throws Exception {

        if (document.getObjects().size() != 1) {

            throw new IllegalStateException(
                    "Expected exactly one object"
            );
        }

        MsaObject msaObject =
                (MsaObject)
                        document
                                .getObjects()
                                .get(0);

        String fasta =
                msaObject
                        .getAlignment()
                        .toFasta();

        Files.writeString(
                Path.of(path),
                fasta
        );
    }
}