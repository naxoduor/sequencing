package com.bio.sequencing.plugins.external_tool_support.bedtools;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

public class BedToolsExecutionTask
        extends AbstractTask {

    private final BedToolsSettings settings;

    private BedToolsResult result;

    private Exception error;

    public BedToolsExecutionTask(
            BedToolsSettings settings) {

        super("Execute BEDTools");

        this.settings = settings;
    }

    @Override
    protected void execute() {

        try {

            BedToolsExecutor executor =
                    new BedToolsExecutor();

            result =
                    executor.execute(settings);

        } catch (Exception e) {

            error = e;

            setError(
                    "BEDTools execution failed: "
                            + e.getMessage()
            );
        }
    }

    public BedToolsResult getResult() {
        return result;
    }

    public String getError() {
        return super.getError();
    }
}