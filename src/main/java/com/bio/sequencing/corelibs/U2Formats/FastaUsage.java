package com.bio.sequencing.corelibs.U2Formats;

import java.io.InputStream;
import java.nio.file.Path;

public class FastaUsage {
    Path path = Paths.get("genome.fasta");

try (InputStream input = Files.newInputStream(path);
    StreamSequenceReader reader =
            new StreamSequenceReader(input)) {

        StreamSequenceReader.SequenceRecord record;

        while ((record = reader.readNext()) != null) {

            System.out.println("ID: " + record.getId());
            System.out.println("Length: " + record.length());

            // Process immediately.
            processSequence(record);
        }
    }

    private static void processSequence(
            StreamSequenceReader.SequenceRecord record) {

        System.out.println(
                record.getId()
                        + " -> "
                        + record.getSequence().substring(
                        0,
                        Math.min(20, record.length())
                )
        );
    }

try (InputStream input =
            Files.newInputStream(Paths.get("reads.fastq"));
    StreamSequenceReader reader =
            new StreamSequenceReader(input)) {

        reader.forEach(record -> {

            if (record.getFormat()
                    == StreamSequenceReader.SequenceFormat.FASTQ) {

                System.out.println(
                        record.getId()
                                + " "
                                + record.length()
                );
            }
        });
    }
}
