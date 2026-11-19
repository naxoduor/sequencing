package com.bio.sequencing.corelibs.U2Core.models;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class DocumentLoader {

    public Document load(
            Path path,
            DocumentFormat format) throws IOException {

        DocumentInfo info = new DocumentInfo(
                path.getFileName().toString()
        );

        Document document = new Document(
                path,
                format,
                info
        );

        try {
            List<DocumentObject> objects =
                    format.load(path);

            for (DocumentObject object : objects) {
                document.addObject(object);
            }

            return document;

        } catch (IOException | RuntimeException e) {
            document.close();
            throw e;
        }
    }
}