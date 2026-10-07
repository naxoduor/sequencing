package com.bio.sequencing.plugins.external_tool_support.bowtie;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

public class BowtieExecutionTask
        extends AbstractTask {

    private final BowtieSettings settings;

    private BowtieResult result;

    private String error;

    public BowtieExecutionTask(
            BowtieSettings settings) {

        super("Execute Bowtie");

        this.settings = settings;
    }

    @Override
    protected void execute() {

        try {

            BowtieExecutor executor =
                    new BowtieExecutor();

            result =
                    executor.execute(settings);

        } catch (Exception e) {

            error = e.getMessage();

            setError(
                    "Bowtie execution failed: "
                            + e.getMessage()
            );
        }
    }

    public BowtieResult getResult() {
        return result;
    }

    public String getError() {
        return error;
    }
}