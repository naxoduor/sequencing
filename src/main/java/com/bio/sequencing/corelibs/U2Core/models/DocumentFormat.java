package com.bio.sequencing.corelibs.U2Core.models;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface DocumentFormat {

    String getFormatId();

    String getFormatName();

    List<DocumentObject> load(Path path) throws IOException;

    void save(
            Path path,
            List<DocumentObject> objects
    ) throws IOException;

    boolean supportsWriting();
}