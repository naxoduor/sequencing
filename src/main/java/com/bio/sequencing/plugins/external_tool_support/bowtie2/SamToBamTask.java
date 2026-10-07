package com.bio.sequencing.plugins.external_tool_support.bowtie2;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

import java.io.*;
import java.nio.file.*;

public class SamToBamTask
        extends AbstractTask {

    private final String samFile;
    private final String bamFile;

    public SamToBamTask(
            String samFile,
            String bamFile) {

        super("Convert SAM to BAM");

        this.samFile = samFile;
        this.bamFile = bamFile;
    }

    @Override
    protected void execute() {

        try {

            ProcessBuilder processBuilder =
                    new ProcessBuilder(
                            "samtools",
                            "view",
                            "-b",
                            "-o",
                            bamFile,
                            samFile
                    );

            processBuilder
                    .redirectErrorStream(true);

            Process process =
                    processBuilder.start();

            StringBuilder output =
                    new StringBuilder();

            try (BufferedReader reader =
                         new BufferedReader(
                                 new InputStreamReader(
                                         process.getInputStream()))) {

                String line;

                while ((line = reader.readLine()) != null) {

                    output.append(line)
                            .append(
                                    System.lineSeparator()
                            );
                }
            }

            int exitCode =
                    process.waitFor();

            if (exitCode != 0) {

                throw new IOException(
                        "samtools failed: "
                                + output
                );
            }

            if (!Files.exists(
                    Paths.get(bamFile))) {

                throw new IOException(
                        "BAM file was not created: "
                                + bamFile
                );
            }

        } catch (Exception e) {

            setError(
                    "SAM to BAM conversion failed: "
                            + e.getMessage()
            );
        }
    }

    public String getBamFile() {
        return bamFile;
    }
}