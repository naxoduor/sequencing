package com.bio.sequencing.plugins.external_tool_support.bowtie2;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

public class Bowtie2ExecutionTask
        extends AbstractTask {

    private final Bowtie2Settings settings;

    private Bowtie2Result result;

    private Exception error;

    public Bowtie2ExecutionTask(
            Bowtie2Settings settings) {

        super("Execute Bowtie2");

        this.settings = settings;
    }

    @Override
    protected void execute() {

        try {

            Bowtie2Executor executor =
                    new Bowtie2Executor();

            result =
                    executor.execute(settings);

        } catch (Exception e) {

            error = e;

            setError(
                    "Bowtie2 execution failed: "
                            + e.getMessage()
            );
        }
    }

    public Bowtie2Result getResult() {
        return result;
    }

    public Exception getError() {
        return error;
    }
}