package com.bio.sequencing.plugins.external_tool_support.mafft;

public class MAFFTExecutionTask extends AbstractTask {

    private final String inputFile;

    private final MAFFTSettings settings;

    private MultipleSequenceAlignment result;

    public MAFFTExecutionTask(
            String inputFile,
            MAFFTSettings settings) {

        super("Execute MAFFT");

        this.inputFile = inputFile;
        this.settings = settings;
    }

    @Override
    protected void execute() {

        try {

            String fasta =
                    java.nio.file.Files.readString(
                            java.nio.file.Path.of(inputFile)
                    );

            String output =
                    MafftExecutor.execute(
                            fasta,
                            settings
                    );

            result =
                    MultipleSequenceAlignment
                            .fromFasta(output);

        } catch (Exception e) {

            setError(
                    "MAFFT execution failed: "
                            + e.getMessage()
            );
        }
    }

    public MultipleSequenceAlignment getResult() {
        return result;
    }
}