package com.bio.sequencing.plugins.external_tool_support.hmmer;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

public class HMMERExecutionTask
        extends AbstractTask {

    private final HMMERSettings settings;

    private final HMMERExecutor executor;

    private HMMERExecutionResult result;

    public HMMERExecutionTask(
            HMMERSettings settings) {

        super("Execute HMMER");

        this.settings = settings;

        this.executor =
                new HMMERExecutor();
    }

    @Override
    protected void execute()
            throws Exception {

        result =
                executor.execute(settings);
    }

    public HMMERExecutionResult getResult() {
        return result;
    }
}