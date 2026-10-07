package com.bio.sequencing.plugins.external_tool_support.blast;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

import java.util.List;

public class BlastSupportTask extends AbstractTask {

    private final BlastSettings settings;

    private BlastExecutionTask executionTask;

    private BlastResult result;

    public BlastSupportTask(
            BlastSettings settings) {

        super("BLAST support");

        this.settings = settings;
    }

    @Override
    protected void prepare() {

        executionTask =
                new BlastExecutionTask(settings);

        executionTask.setSubtaskProgressWeight(95);

        addSubTask(executionTask);
    }

    @Override
    protected List<AbstractTask> onSubTaskFinished(
            AbstractTask subTask) {

        if (subTask == executionTask) {

            result = executionTask.getResult();
        }

        return List.of();
    }

    public BlastResult getResult() {
        return result;
    }
}