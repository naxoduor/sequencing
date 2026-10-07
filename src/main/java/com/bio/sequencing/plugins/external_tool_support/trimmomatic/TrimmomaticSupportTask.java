package com.bio.sequencing.plugins.external_tool_support.trimmomatic;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;
import com.bio.sequencing.plugins.external_tool_support.mafft.GObjectReference;

import java.util.ArrayList;
import java.util.List;

public class TrimmomaticSupportTask
        extends AbstractTask {

    private final TrimmomaticSettings settings;

    private final GObjectReference
            objectReference;

    private TrimmomaticExecutionTask
            trimmomaticExecutionTask;

    private TrimmomaticResult result;

    public TrimmomaticSupportTask(
            TrimmomaticSettings settings,
            GObjectReference objectReference
    ) {

        super("Trimmomatic support task");

        this.settings =
                settings;

        this.objectReference =
                objectReference;
    }

    @Override
    protected void prepare() {

        if (settings == null) {

            setError(
                    "Trimmomatic settings are null"
            );

            return;
        }

        if (settings.getInputRead1() == null
                || settings.getInputRead1().isBlank()) {

            setError(
                    "Input Read 1 is required"
            );

            return;
        }

        /*
         * Create actual Trimmomatic task.
         */
        trimmomaticExecutionTask =
                new TrimmomaticExecutionTask(
                        settings
                );

        trimmomaticExecutionTask
                .setSubtaskProgressWeight(
                        100
                );

        addSubTask(
                trimmomaticExecutionTask
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
                trimmomaticExecutionTask) {

            if (subTask.hasError()) {

                setError(
                        "Trimmomatic execution failed: "
                                + subTask.getError()
                );

                return resultTasks;
            }

            result =
                    trimmomaticExecutionTask
                            .getResult();

            if (result == null) {

                setError(
                        "Trimmomatic returned "
                                + "no result"
                );
            }
        }

        return resultTasks;
    }

    public TrimmomaticResult getResult() {
        return result;
    }

    public GObjectReference
    getObjectReference() {

        return objectReference;
    }
}