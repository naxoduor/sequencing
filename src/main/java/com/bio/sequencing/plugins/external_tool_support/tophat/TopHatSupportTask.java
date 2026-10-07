package com.bio.sequencing.plugins.external_tool_support.tophat;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;
import com.bio.sequencing.plugins.external_tool_support.mafft.GObjectReference;

import java.util.ArrayList;
import java.util.List;

public class TopHatSupportTask
        extends AbstractTask {

    private final String read1File;

    private final String read2File;

    private final String referenceIndex;

    private final GObjectReference
            objectReference;

    private final TopHatSettings settings;

    private TopHatExecutionTask
            topHatExecutionTask;

    private TopHatResult result;

    public TopHatSupportTask(
            String read1File,
            String read2File,
            String referenceIndex,
            GObjectReference objectReference,
            TopHatSettings settings
    ) {

        super("TopHat support task");

        this.read1File =
                read1File;

        this.read2File =
                read2File;

        this.referenceIndex =
                referenceIndex;

        this.objectReference =
                objectReference;

        this.settings =
                settings;
    }

    @Override
    protected void prepare() {

        if (read1File == null
                || read1File.isBlank()) {

            setError(
                    "Read 1 is required"
            );

            return;
        }

        if (referenceIndex == null
                || referenceIndex.isBlank()) {

            setError(
                    "Reference index is required"
            );

            return;
        }

        settings.setRead1File(
                read1File
        );

        settings.setRead2File(
                read2File
        );

        settings.setReferenceIndex(
                referenceIndex
        );

        /*
         * TopHat itself becomes the child task.
         */
        topHatExecutionTask =
                new TopHatExecutionTask(
                        settings
                );

        topHatExecutionTask
                .setSubtaskProgressWeight(
                        100
                );

        addSubTask(
                topHatExecutionTask
        );
    }

    @Override
    protected List<AbstractTask>
    onSubTaskFinished(
            AbstractTask subTask
    ) {

        List<AbstractTask> resultTasks =
                new ArrayList<>();

        if (subTask ==
                topHatExecutionTask) {

            if (subTask.hasError()) {

                setError(
                        "TopHat execution failed: "
                                + subTask.getError()
                );

                return resultTasks;
            }

            this.result =
                    topHatExecutionTask
                            .getResult();

            if (this.result == null) {

                setError(
                        "TopHat returned no result"
                );
            }
        }

        return resultTasks;
    }

    public TopHatResult getResult() {
        return result;
    }

    public GObjectReference
    getObjectReference() {

        return objectReference;
    }
}