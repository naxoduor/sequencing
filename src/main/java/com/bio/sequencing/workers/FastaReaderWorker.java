package com.bio.sequencing.workers;

import com.bio.sequencing.port.Port;
import com.bio.sequencing.sequence.Sequence;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Reads sequences from a FASTA file and sends them
 * to the output port one at a time.
 *
 * FASTA format:
 *
 * >seq1
 * ATCGATCG
 *
 * >seq2
 * GGGCTTA
 */
public class FastaReaderWorker implements Worker {

    private final Port output;
    private final Path fastaFile;

    private BufferedReader reader;

    private String currentId;
    private StringBuilder currentSequence;

    private boolean initialized = false;
    private boolean done = false;

    public FastaReaderWorker(Path fastaFile, Port output) {
        this.fastaFile = fastaFile;
        this.output = output;
    }

    @Override
    public void init() {
        if (reader != null) {
            return;
        }


        try {
            reader = Files.newBufferedReader(fastaFile);

            initialized = true;

            System.out.println(
                    "FASTA reader initialized: " + fastaFile
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to open FASTA file: " + fastaFile,
                    e
            );
        }
    }

    @Override
    public void tick() {

        if (done) {
            return;
        }

        if (!initialized) {
            throw new IllegalStateException(
                    "FastaReaderWorker has not been initialized"
            );
        }

        try {

            /*
             * Read until we have one complete FASTA record.
             */
            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                // Ignore empty lines
                if (line.isEmpty()) {
                    continue;
                }

                /*
                 * FASTA header
                 *
                 * Example:
                 * >seq1
                 */
                if (line.startsWith(">")) {

                    /*
                     * If we already have a sequence,
                     * the previous record is complete.
                     */
                        if (currentId != null && currentSequence != null
                            && currentSequence.length() > 0) {

                        Sequence sequence = new Sequence(
                                currentId,
                                currentSequence.toString()
                        );

                        output.put(sequence);

                        /*
                         * Save the new header so that the
                         * next tick() continues from here.
                         */
                        currentId = line.substring(1).trim();
                        currentSequence = new StringBuilder();
                        currentSequence.append(line).append(System.lineSeparator());

                        return;
                    }

                    // First FASTA record
                    currentId = line.substring(1).trim();
                    currentSequence = new StringBuilder();
                    currentSequence.append(line).append(System.lineSeparator());

                } else {

                    /*
                     * Sequence line.
                     *
                     * FASTA sequences can span multiple lines.
                     */
                    if (currentSequence == null) {
                        throw new IllegalStateException(
                                "FASTA sequence found before header"
                        );
                    }

                    currentSequence.append(line);
                }
            }

            /*
             * EOF reached.
             *
             * There may still be one final sequence
             * that has not yet been emitted.
             */
//                if (currentId != null && currentSequence != null
//                    && currentSequence.length() > 0) {
//
//                Sequence sequence = new Sequence(
//                        currentId,
//                        currentSequence.toString()
//                );
//
//                output.put(sequence);
//
//                currentId = null;
//                currentSequence = null;
//            }

            output.close();
            done = true;
            close();

        } catch (IOException e) {

            close();

            throw new RuntimeException(
                    "Error reading FASTA file: " + fastaFile,
                    e
            );
        }
    }

    @Override
    public boolean isDone() {
        return done;
    }

    private void close() {

        if (reader != null) {

            try {
                reader.close();
            } catch (IOException ignored) {
            }

            reader = null;
        }
    }
}