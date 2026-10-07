package com.bio.sequencing.plugins.external_tool_support.cap3;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

public class Cap3ExecutionTask
        extends AbstractTask {

    private final Cap3Settings settings;

    private final Cap3Executor executor;

    private Cap3Result result;

    public Cap3ExecutionTask(
            Cap3Settings settings) {

        super("Execute CAP3 assembly");

        this.settings = settings;

        this.executor =
                new Cap3Executor();
    }

    @Override
    protected void execute()
            throws Exception {

        result =
                executor.execute(settings);
    }

    public Cap3Result getResult() {
        return result;
    }
}