package com.bio.sequencing.plugins.external_tool_support.clustalo;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;
import com.bio.sequencing.plugins.external_tool_support.mafft.MultipleSequenceAlignment;

public class ClustalOExecutionTask
        extends AbstractTask {

    private final String inputFile;

    private final String outputFile;

    private final ClustalOSettings settings;

    private MultipleSequenceAlignment result;

    public ClustalOExecutionTask(
            String inputFile,
            String outputFile,
            ClustalOSettings settings) {

        super("Execute Clustal Omega");

        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.settings = settings;
    }

    @Override
    protected void execute() {

        try {

            /*
             * Execute:
             *
             * clustalo -i input
             *          -o output
             *          --force
             *          --threads N
             */
            ClustalOExecutor.execute(
                    inputFile,
                    outputFile,
                    settings
            );

            /*
             * Read the resulting alignment.
             */
            String alignedFasta =
                    java.nio.file.Files.readString(
                            java.nio.file.Path.of(
                                    outputFile
                            )
                    );

            result =
                    MultipleSequenceAlignment
                            .fromFasta(
                                    alignedFasta
                            );

        } catch (Exception e) {

            setError(
                    "Clustal Omega execution failed: "
                            + e.getMessage()
            );
        }
    }

    public MultipleSequenceAlignment
    getResult() {

        return result;
    }
}