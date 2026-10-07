package com.bio.sequencing.plugins.external_tool_support.trimmomatic;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

public class TrimmomaticExecutionTask
        extends AbstractTask {

    private final TrimmomaticSettings settings;

    private final TrimmomaticExecutor executor;

    private TrimmomaticResult result;

    public TrimmomaticExecutionTask(
            TrimmomaticSettings settings
    ) {

        super("Trimmomatic execution");

        this.settings =
                settings;

        this.executor =
                new TrimmomaticExecutor();
    }

    @Override
    protected void execute()
            throws Exception {

        result =
                executor.execute(
                        settings
                );

        if (result == null) {

            throw new IllegalStateException(
                    "Trimmomatic returned no result"
            );
        }

        validateOutputs();
    }

    private void validateOutputs() {

        if ("PE".equalsIgnoreCase(
                settings.getMode()
        )) {

            checkFile(
                    result.getPairedRead1()
            );

            checkFile(
                    result.getPairedRead2()
            );

            checkFile(
                    result.getUnpairedRead1()
            );

            checkFile(
                    result.getUnpairedRead2()
            );

        } else {

            checkFile(
                    result.getPairedRead1()
            );
        }
    }

    private void checkFile(
            String file
    ) {

        if (file == null) {
            return;
        }

        if (!java.nio.file.Files.exists(
                java.nio.file.Path.of(file)
        )) {

            throw new IllegalStateException(
                    "Expected Trimmomatic output "
                            + "does not exist: "
                            + file
            );
        }
    }

    public TrimmomaticResult getResult() {
        return result;
    }
}