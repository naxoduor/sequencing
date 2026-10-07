package com.bio.sequencing.plugins.external_tool_support.bwa;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

public class BwaMemExecutionTask
        extends AbstractTask {

    private final BwaMemSettings settings;

    private BwaMemResult result;

    private String error;

    public BwaMemExecutionTask(
            BwaMemSettings settings) {

        super("Execute BWA-MEM");

        this.settings = settings;
    }

    @Override
    protected void execute() {

        try {

            BwaMemExecutor executor =
                    new BwaMemExecutor();

            result =
                    executor.execute(settings);

        } catch (Exception e) {

            error = e.getMessage();

            setError(
                    "BWA-MEM execution failed: "
                            + e.getMessage()
            );
        }
    }

    public BwaMemResult getResult() {
        return result;
    }

    public String getError() {
        return error;
    }
}