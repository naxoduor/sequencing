package com.bio.sequencing.plugins.external_tool_support.tophat;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

public class TopHatExecutionTask
        extends AbstractTask {

    private final TopHatSettings settings;

    private final TopHatExecutor executor;

    private TopHatResult result;

    public TopHatExecutionTask(
            TopHatSettings settings
    ) {

        super("TopHat execution");

        this.settings =
                settings;

        this.executor =
                new TopHatExecutor();
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
                    "TopHat returned no result"
            );
        }

        if (result.getAcceptedHitsBam()
                == null) {

            throw new IllegalStateException(
                    "TopHat did not produce "
                            + "accepted_hits.bam"
            );
        }
    }

    public TopHatResult getResult() {
        return result;
    }
}