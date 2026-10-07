package com.bio.sequencing.plugins.external_tool_support.cap3;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;
import com.bio.sequencing.plugins.external_tool_support.mafft.SequenceRow;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ParseCap3ResultTask
        extends AbstractTask {

    private final Cap3Result cap3Result;

    private AssemblyDocument assemblyDocument;

    public ParseCap3ResultTask(
            Cap3Result cap3Result) {

        super("Parse CAP3 assembly");

        this.cap3Result = cap3Result;
    }

    @Override
    protected void execute()
            throws Exception {

        List<Contig> contigs =
                parseContigs(
                        cap3Result.getContigsFile());

        List<SequenceRow> singlets =
                parseSinglets(
                        cap3Result.getSingletsFile());

        assemblyDocument =
                new AssemblyDocument(
                        contigs,
                        singlets);
    }

    private List<Contig> parseContigs(
            String filename)
            throws IOException {

        List<Contig> result =
                new ArrayList<>();

        if (filename == null) {
            return result;
        }

        try (BufferedReader reader =
                     Files.newBufferedReader(
                             Paths.get(filename))) {

            String line;

            String currentId = null;

            StringBuilder sequence =
                    new StringBuilder();

            while ((line = reader.readLine())
                    != null) {

                if (line.startsWith(">")) {

                    if (currentId != null) {

                        Contig contig =
                                new Contig();

                        contig.setId(currentId);

                        contig.setSequence(
                                sequence.toString());

                        result.add(contig);
                    }

                    currentId =
                            line.substring(1)
                                    .trim();

                    sequence.setLength(0);

                } else {

                    sequence.append(
                            line.trim());
                }
            }

            if (currentId != null) {

                Contig contig =
                        new Contig();

                contig.setId(currentId);

                contig.setSequence(
                        sequence.toString());

                result.add(contig);
            }
        }

        return result;
    }

    private List<SequenceRow> parseSinglets(
            String filename)
            throws IOException {

        /*
         * Reuse your existing FASTA reader here.
         *
         * The implementation below intentionally
         * delegates to that component instead of
         * duplicating FASTA parsing.
         */

        if (filename == null) {
            return new ArrayList<>();
        }

        return FastaParser.parseRows(filename);
    }

    public AssemblyDocument getAssemblyDocument() {
        return assemblyDocument;
    }
}