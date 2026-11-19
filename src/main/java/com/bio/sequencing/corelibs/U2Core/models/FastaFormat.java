package com.bio.sequencing.corelibs.U2Core.models;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;

public class FastaFormat implements DocumentFormat {

    private static final int LINE_WIDTH = 60;

    @Override
    public String getFormatId() {
        return "fasta";
    }

    @Override
    public String getFormatName() {
        return "FASTA";
    }

    @Override
    public boolean supportsWriting() {
        return true;
    }

    @Override
    public List<DocumentObject> load(Path path)
            throws IOException {

        List<DocumentObject> sequences =
                new ArrayList<>();

        try (BufferedReader reader =
                     Files.newBufferedReader(
                             path,
                             StandardCharsets.UTF_8)) {

            String line;

            String header = null;

            StringBuilder sequence =
                    new StringBuilder();

            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                line = line.trim();

                // Ignore empty lines
                if (line.isEmpty()) {
                    continue;
                }

                // New FASTA sequence
                if (line.startsWith(">")) {

                    // Save previous sequence
                    if (header != null) {

                        sequences.add(
                                createSequence(
                                        header,
                                        sequence.toString()
                                )
                        );
                    }

                    header = line.substring(1).trim();

                    if (header.isEmpty()) {
                        throw new IOException(
                                "Empty FASTA header at line "
                                        + lineNumber
                        );
                    }

                    sequence.setLength(0);

                } else {

                    // Sequence before header is invalid
                    if (header == null) {
                        throw new IOException(
                                "Sequence encountered before "
                                        + "FASTA header at line "
                                        + lineNumber
                        );
                    }

                    // Remove internal whitespace
                    for (int i = 0; i < line.length(); i++) {

                        char c = line.charAt(i);

                        if (!Character.isWhitespace(c)) {
                            sequence.append(c);
                        }
                    }
                }
            }

            // Save final sequence
            if (header != null) {

                sequences.add(
                        createSequence(
                                header,
                                sequence.toString()
                        )
                );
            }
        }

        return sequences;
    }

    private SequenceObject createSequence(
            String header,
            String sequence) {

        String name = header;

        String description = "";

        int separator = header.indexOf(' ');

        if (separator >= 0) {

            name = header.substring(0, separator);

            description =
                    header.substring(separator + 1);
        }

        return new SequenceObject(
                name,
                sequence,
                description
        );
    }

    @Override
    public void save(
            Path path,
            List<DocumentObject> objects)
            throws IOException {

        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                             path,
                             StandardCharsets.UTF_8)) {

            for (DocumentObject object : objects) {

                if (!(object instanceof SequenceObject)) {
                    continue;
                }

                SequenceObject sequence =
                        (SequenceObject) object;

                writeSequence(writer, sequence);
            }
        }
    }

    private void writeSequence(
            BufferedWriter writer,
            SequenceObject sequence)
            throws IOException {

        String header = sequence.getName();

        if (sequence.getDescription() != null
                && !sequence.getDescription().isBlank()) {

            header += " " + sequence.getDescription();
        }

        writer.write(">" + header);
        writer.newLine();

        String data = sequence.getSequence();

        for (int i = 0; i < data.length(); i += LINE_WIDTH) {

            int end = Math.min(
                    i + LINE_WIDTH,
                    data.length()
            );

            writer.write(data, i, end - i);
            writer.newLine();
        }
    }
}