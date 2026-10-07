package com.bio.sequencing.plugins.external_tool_support.kalign;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;
import com.bio.sequencing.plugins.external_tool_support.mafft.MultipleSequenceAlignment;

import java.nio.file.Files;
import java.nio.file.Path;

public class


KalignExecutionTask extends AbstractTask {

    private final String inputFile;
    private final String outputFile;
    private final KalignSettings settings;

    private MultipleSequenceAlignment result;

    private final KalignExecutor executor;

    public KalignExecutionTask(
            String inputFile,
            String outputFile,
            KalignSettings settings
    ) {
        super("Kalign alignment");

        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.settings = settings;

        this.executor = new KalignExecutor();
    }

    @Override
    protected void execute() throws Exception {

        if (!Files.exists(Path.of(inputFile))) {
            throw new IllegalArgumentException(
                    "Kalign input file does not exist: "
                            + inputFile
            );
        }

        executor.execute(
                inputFile,
                outputFile,
                settings
        );

        if (!Files.exists(Path.of(outputFile))) {
            throw new IllegalStateException(
                    "Kalign did not produce output file: "
                            + outputFile
            );
        }

        /*
         * Read Kalign's output FASTA.
         */
        result = MultipleSequenceAlignment
                .fromFasta(outputFile);

        if (result == null || result.getRows().isEmpty()) {
            throw new IllegalStateException(
                    "Kalign produced an empty alignment"
            );
        }
    }

    public MultipleSequenceAlignment getResult() {
        return result;
    }
}