package com.bio.sequencing.plugins.dbi_bam;

import htsjdk.samtools.*;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BAMReader implements Closeable {

    private final Path bamPath;

    private SamReader samReader;

    public BAMReader(Path bamPath) {
        this.bamPath = bamPath;
    }

    /**
     * Open BAM file.
     */
    public void open() throws IOException {

        SamReaderFactory factory =
                SamReaderFactory.makeDefault();

        samReader = factory.open(bamPath.toFile());
    }

    /**
     * Return BAM header.
     */
    public SAMFileHeader getHeader() {

        ensureOpen();

        return samReader.getFileHeader();
    }

    /**
     * Return reference/chromosome names.
     */
    public List<String> getReferences() {

        ensureOpen();

        List<String> references = new ArrayList<>();

        SAMSequenceDictionary dictionary =
                samReader.getFileHeader()
                        .getSequenceDictionary();

        for (SAMSequenceRecord sequence : dictionary.getSequences()) {
            references.add(sequence.getSequenceName());
        }

        return references;
    }

    /**
     * Return reference lengths.
     */
    public List<ReferenceInfo> getReferenceInfo() {

        ensureOpen();

        List<ReferenceInfo> result = new ArrayList<>();

        SAMSequenceDictionary dictionary =
                samReader.getFileHeader()
                        .getSequenceDictionary();

        for (SAMSequenceRecord sequence : dictionary.getSequences()) {

            result.add(
                    new ReferenceInfo(
                            sequence.getSequenceName(),
                            sequence.getSequenceLength()
                    )
            );
        }

        return result;
    }

    /**
     * Read every alignment in the BAM.
     */
    public List<BAMRecord> readAll() {

        ensureOpen();

        List<BAMRecord> records = new ArrayList<>();

        try (SAMRecordIterator iterator =
                     samReader.iterator()) {

            while (iterator.hasNext()) {

                SAMRecord record = iterator.next();

                records.add(convert(record));
            }
        }

        return records;
    }

    /**
     * Query BAM using genomic coordinates.
     *
     * Coordinates are 1-based inclusive.
     */
    public List<BAMRecord> query(
            String chromosome,
            int start,
            int end) {

        ensureOpen();

        List<BAMRecord> records = new ArrayList<>();

        try (SAMRecordIterator iterator =
                     samReader.query(
                             chromosome,
                             start,
                             end,
                             false)) {

            while (iterator.hasNext()) {

                SAMRecord record = iterator.next();

                records.add(convert(record));
            }
        }

        return records;
    }

    /**
     * Stream records instead of loading everything
     * into memory.
     */
    public void query(
            String chromosome,
            int start,
            int end,
            BAMRecordConsumer consumer) {

        ensureOpen();

        try (SAMRecordIterator iterator =
                     samReader.query(
                             chromosome,
                             start,
                             end,
                             false)) {

            while (iterator.hasNext()) {

                SAMRecord record = iterator.next();

                consumer.accept(convert(record));
            }
        }
    }

    private BAMRecord convert(SAMRecord record) {

        return new BAMRecord(
                record.getReadName(),
                record.getReferenceName(),
                record.getAlignmentStart(),
                record.getAlignmentEnd(),
                record.getCigarString(),
                record.getReadString(),
                record.getBaseQualityString(),
                record.getMappingQuality(),
                record.getFlags()
        );
    }

    private void ensureOpen() {

        if (samReader == null) {
            throw new IllegalStateException(
                    "BAM reader is not open"
            );
        }
    }

    @Override
    public void close() throws IOException {

        if (samReader != null) {
            samReader.close();
            samReader = null;
        }
    }
}