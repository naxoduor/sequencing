package com.bio.sequencing.plugins.external_tool_support.clustalw;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;
import com.bio.sequencing.plugins.external_tool_support.mafft.MultipleSequenceAlignment;

import java.nio.file.Files;
import java.nio.file.Path;

public class
ClustalWExecutionTask
        extends AbstractTask {

    private final String inputFile;

    private final String outputFile;

    private final ClustalWSettings settings;

    private MultipleSequenceAlignment result;

    public ClustalWExecutionTask(
            String inputFile,
            String outputFile,
            ClustalWSettings settings) {

        super("Execute ClustalW");

        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.settings = settings;
    }

    @Override
    protected void execute() {

        try {

            /*
             * Execute ClustalW.
             */
            ClustalWExecutor.execute(
                    inputFile,
                    outputFile,
                    settings
            );

            /*
             * ClustalW has produced the
             * alignment file.
             */
            if (!Files.exists(
                    Path.of(outputFile))) {

                throw new IllegalStateException(
                        "ClustalW output file "
                                + "does not exist: "
                                + outputFile
                );
            }

            /*
             * Read aligned FASTA.
             */
            String alignedFasta =
                    Files.readString(
                            Path.of(outputFile)
                    );

            result =
                    MultipleSequenceAlignment
                            .fromFasta(
                                    alignedFasta
                            );

        } catch (Exception e) {

            setError(
                    "ClustalW execution failed: "
                            + e.getMessage()
            );
        }
    }

    public MultipleSequenceAlignment
    getResult() {

        return result;
    }
}