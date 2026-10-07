package com.bio.sequencing.plugins.external_tool_support.hmmer;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

import java.io.*;
import java.nio.file.*;

public class SaveHMMERResultTask
        extends AbstractTask {

    private final HMMERResultDocument result;

    private final String outputFile;

    public SaveHMMERResultTask(

            HMMERResultDocument result,
            String outputFile) {

        super("Save HMMER result");

        this.result = result;

        this.outputFile = outputFile;
    }

    @Override
    protected void execute()
            throws Exception {

        Path path =
                Paths.get(outputFile);

        if (path.getParent() != null) {

            Files.createDirectories(
                    path.getParent());
        }

        try (BufferedWriter writer =
                     Files.newBufferedWriter(path)) {

            writer.write(
                    "HMMER RESULT");

            writer.newLine();

            writer.write(
                    "Query: "
                            + result.getQueryFile());

            writer.newLine();

            writer.write(
                    "HMM: "
                            + result.getHmmFile());

            writer.newLine();

            writer.newLine();

            writer.write(
                    "TARGET\tE-VALUE\tSCORE\tDESCRIPTION");

            writer.newLine();

            for (HMMERHit hit :
                    result.getHits()) {

                writer.write(
                        safe(hit.getTargetName()));

                writer.write("\t");

                writer.write(
                        String.valueOf(
                                hit.getSequenceEvalue()));

                writer.write("\t");

                writer.write(
                        String.valueOf(
                                hit.getSequenceScore()));

                writer.write("\t");

                writer.write(
                        safe(hit.getDescription()));

                writer.newLine();
            }

            writer.newLine();

            writer.write(
                    "DOMAINS");

            writer.newLine();

            writer.write(
                    "TARGET\tDOMAIN\tC-EVALUE\tI-EVALUE\tSCORE\tHMM_FROM\tHMM_TO\tALI_FROM\tALI_TO");

            writer.newLine();

            for (HMMERDomain domain :
                    result.getDomains()) {

                writer.write(
                        safe(domain.getTargetName()));

                writer.write("\t");

                writer.write(
                        domain.getDomainNumber()
                                + "/"
                                + domain.getTotalDomains());

                writer.write("\t");

                writer.write(
                        String.valueOf(
                                domain.getcEvalue()));

                writer.write("\t");

                writer.write(
                        String.valueOf(
                                domain.getiEvalue()));

                writer.write("\t");

                writer.write(
                        String.valueOf(
                                domain.getScore()));

                writer.write("\t");

                writer.write(
                        String.valueOf(
                                domain.getHmmFrom()));

                writer.write("\t");

                writer.write(
                        String.valueOf(
                                domain.getHmmTo()));

                writer.write("\t");

                writer.write(
                        String.valueOf(
                                domain.getAliFrom()));

                writer.write("\t");

                writer.write(
                        String.valueOf(
                                domain.getAliTo()));

                writer.newLine();
            }
        }
    }

    private String safe(String value) {

        return value == null
                ? ""
                : value;
    }
}
