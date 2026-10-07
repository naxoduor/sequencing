package com.bio.sequencing.plugins.external_tool_support.mafft;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class MsaStreamService {

    private final ObjectMapper objectMapper;

    public MsaStreamService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void streamFasta(Path fastaFile, OutputStream output)
            throws IOException {

        try (
                BufferedReader reader = Files.newBufferedReader(
                        fastaFile, StandardCharsets.UTF_8
                );
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(output, StandardCharsets.UTF_8)
                )
        ) {
            int sequenceCount = 0;
            int alignmentLength = -1;

            String name = null;
            StringBuilder residues = new StringBuilder();

            // Emit an initial event so the client knows the stream began.
            writeEvent(writer, Map.of(
                    "type", "started",
                    "file", fastaFile.getFileName().toString()
            ));

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                if (line.startsWith(">")) {
                    if (name != null) {
                        alignmentLength = emitSequence(
                                writer, name, residues, sequenceCount,
                                alignmentLength
                        );
                        sequenceCount++;
                    }

                    name = line.substring(1).trim();
                    residues.setLength(0);
                } else {
                    if (name == null) {
                        throw new IOException(
                                "FASTA data must begin with a header"
                        );
                    }

                    residues.append(line);
                }
            }

            if (name != null) {
                alignmentLength = emitSequence(
                        writer, name, residues, sequenceCount,
                        alignmentLength
                );
                sequenceCount++;
            }

            if (sequenceCount == 0) {
                throw new IOException("No FASTA sequences found");
            }

            writeEvent(writer, Map.of(
                    "type", "complete",
                    "sequenceCount", sequenceCount,
                    "alignmentLength", alignmentLength
            ));
        }
    }

    private int emitSequence(
            BufferedWriter writer,
            String name,
            StringBuilder residues,
            int sequenceIndex,
            int expectedLength
    ) throws IOException {

        String sequence = residues.toString()
                .toUpperCase(java.util.Locale.ROOT);

        if (expectedLength >= 0 && sequence.length() != expectedLength) {
            throw new IOException(
                    "Alignment length mismatch for sequence: " + name
            );
        }

        Map<String, Object> event = new LinkedHashMap<>();
        event.put("type", "sequence");
        event.put("id", Integer.toString(sequenceIndex + 1));
        event.put("name", name);
        event.put("residues", sequence);

        writeEvent(writer, event);

        return expectedLength < 0
                ? sequence.length()
                : expectedLength;
    }

    private void writeEvent(
            BufferedWriter writer,
            Map<String, ?> event
    ) throws IOException {
        writer.write(objectMapper.writeValueAsString(event));
        writer.newLine();
        writer.flush();
    }
}