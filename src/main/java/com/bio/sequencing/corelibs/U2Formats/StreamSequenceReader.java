package com.bio.sequencing.corelibs.U2Formats;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Streaming FASTA/FASTQ sequence reader.
 *
 * Reads one sequence record at a time from an InputStream.
 *
 * Supported:
 *   FASTA:
 *      >sequence1
 *      ATGCATGC
 *
 *   FASTQ:
 *      @sequence1
 *      ATGCATGC
 *      +
 *      IIIIIIII
 *
 * The underlying InputStream is NOT closed by this class.
 */
public class StreamSequenceReader implements Closeable {

    private final BufferedReader reader;

    private String pendingHeader;
    private boolean closed;

    public StreamSequenceReader(InputStream inputStream) {
        this(inputStream, 64 * 1024);
    }

    public StreamSequenceReader(InputStream inputStream, int bufferSize) {
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream cannot be null");
        }

        if (bufferSize <= 0) {
            throw new IllegalArgumentException("bufferSize must be > 0");
        }

        this.reader = new BufferedReader(
                new InputStreamReader(
                        inputStream,
                        StandardCharsets.UTF_8
                ),
                bufferSize
        );
    }

    /**
     * Read the next sequence record.
     *
     * @return SequenceRecord or null when EOF is reached.
     */
    public SequenceRecord readNext() throws IOException {

        ensureOpen();

        String header = nextHeader();

        if (header == null) {
            return null;
        }

        if (header.startsWith(">")) {
            return readFasta(header);
        }

        if (header.startsWith("@")) {
            return readFastq(header);
        }

        throw new IOException(
                "Unsupported sequence format. Expected FASTA '>' or FASTQ '@': "
                        + header
        );
    }

    /**
     * Find the next FASTA/FASTQ header.
     */
    private String nextHeader() throws IOException {

        if (pendingHeader != null) {
            String result = pendingHeader;
            pendingHeader = null;
            return result;
        }

        String line;

        while ((line = reader.readLine()) != null) {

            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            if (line.startsWith(">") || line.startsWith("@")) {
                return line;
            }

            throw new IOException(
                    "Unexpected line outside sequence record: " + line
            );
        }

        return null;
    }

    /**
     * Read one FASTA record.
     */
    private SequenceRecord readFasta(String header)
            throws IOException {

        String id = header.substring(1).trim();

        StringBuilder sequence = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {

            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            if (line.startsWith(">") || line.startsWith("@")) {
                pendingHeader = line;
                break;
            }

            sequence.append(line);
        }

        return new SequenceRecord(
                id,
                sequence.toString(),
                null,
                SequenceFormat.FASTA
        );
    }

    /**
     * Read one FASTQ record.
     */
    private SequenceRecord readFastq(String header)
            throws IOException {

        String id = header.substring(1).trim();

        StringBuilder sequence = new StringBuilder();

        String line;

        /*
         * FASTQ normally has:
         *
         * @header
         * sequence
         * +
         * quality
         *
         * We read until '+'.
         */
        while ((line = reader.readLine()) != null) {

            line = line.trim();

            if (line.equals("+")) {
                break;
            }

            if (line.isEmpty()) {
                continue;
            }

            sequence.append(line);
        }

        if (line == null) {
            throw new IOException(
                    "Unexpected EOF: FASTQ record has no '+' separator: "
                            + id
            );
        }

        StringBuilder quality = new StringBuilder();

        while (quality.length() < sequence.length()) {

            line = reader.readLine();

            if (line == null) {
                throw new IOException(
                        "Unexpected EOF: FASTQ quality shorter than sequence: "
                                + id
                );
            }

            quality.append(line.trim());
        }

        if (quality.length() != sequence.length()) {
            throw new IOException(
                    "FASTQ sequence/quality length mismatch for "
                            + id
                            + ": sequence="
                            + sequence.length()
                            + ", quality="
                            + quality.length()
            );
        }

        return new SequenceRecord(
                id,
                sequence.toString(),
                quality.toString(),
                SequenceFormat.FASTQ
        );
    }

    /**
     * Convenience method for checking whether another record exists.
     * <p>
     * Note: this performs a small read-ahead.
     */
    public boolean hasNext() throws IOException {

        ensureOpen();

        if (pendingHeader != null) {
            return true;
        }

        String line;

        while ((line = reader.readLine()) != null) {

            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            if (line.startsWith(">") || line.startsWith("@")) {
                pendingHeader = line;
                return true;
            }

            throw new IOException(
                    "Unexpected line outside sequence record: " + line
            );
        }

        return false;
    }

    /**
     * Read all records through a callback.
     * <p>
     * This avoids creating a large List in memory.
     */
    public void forEach(SequenceConsumer consumer)
            throws IOException {

        ensureOpen();

        SequenceRecord record;

        while ((record = readNext()) != null) {
            consumer.accept(record);
        }
    }

    private void ensureOpen() throws IOException {

        if (closed) {
            throw new IOException(
                    "StreamSequenceReader is already closed"
            );
        }
    }

    @Override
    public void close() throws IOException {

        if (!closed) {
            closed = true;
            reader.close();
        }
    }
}

