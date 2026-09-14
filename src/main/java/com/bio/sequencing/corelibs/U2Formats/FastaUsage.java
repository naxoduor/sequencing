package com.bio.sequencing.corelibs.U2Formats;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FastaUsage {
    Path path = Paths.get("genome.fasta");

    public  void process() throws IOException {
        try (InputStream input = Files.newInputStream(path);
             StreamSequenceReader reader =
                     new StreamSequenceReader(input)) {

            SequenceRecord record;

            while ((record = reader.readNext()) != null) {

                System.out.println("ID: " + record.getId());
                System.out.println("Length: " + record.length());

                // Process immediately.
                processSequence(record);
            }
        }
    }



    private void processSequence(
            SequenceRecord record) {

        System.out.println(
                record.getId()
                        + " -> "
                        + record.getSequence().substring(
                        0,
                        Math.min(20, record.length())
                )
        );
    }

    public void doProcess(){
        try (InputStream input =
                     Files.newInputStream(Paths.get("reads.fastq"));
             StreamSequenceReader reader =
                     new StreamSequenceReader(input)) {

            reader.forEach(record -> {

                if (record.getFormat()
                        == SequenceFormat.FASTQ) {

                    System.out.println(
                            record.getId()
                                    + " "
                                    + record.length()
                    );
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
