package com.bio.sequencing.plugins.external_tool_support.blast;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

public class BlastExecutionTask extends AbstractTask {

    private final BlastSettings settings;

    private final BlastExecutor executor;

    private BlastResult result;

    public BlastExecutionTask(
            BlastSettings settings) {

        super("Execute BLAST");

        this.settings = settings;
        this.executor = new BlastExecutor();
    }

    @Override
    protected void execute() throws Exception {

        result = executor.execute(settings);
    }

    public BlastResult getResult() {
        return result;
    }
}