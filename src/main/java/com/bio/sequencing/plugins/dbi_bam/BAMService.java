package com.bio.sequencing.plugins.dbi_bam;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Service
public class BAMService {

    public List<ReferenceInfo> getReferences(
            String bamFile) throws IOException {

        try (BAMReader reader =
                     new BAMReader(Path.of(bamFile))) {

            reader.open();

            return reader.getReferenceInfo();
        }
    }

    public List<BAMRecord> query(
            String bamFile,
            String chromosome,
            int start,
            int end) throws IOException {

        try (BAMReader reader =
                     new BAMReader(Path.of(bamFile))) {

            reader.open();

            return reader.query(
                    chromosome,
                    start,
                    end
            );
        }
    }
}