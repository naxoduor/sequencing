package com.bio.sequencing.plugins.external_tool_support.bowtie;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

import java.util.*;

public class BowtieSupportTask
        extends AbstractTask {

    private final BowtieSettings settings;

    private BowtieExecutionTask executionTask;

    private BowtieResult result;

    public BowtieSupportTask(
            BowtieSettings settings) {

        super("Bowtie Alignment");

        this.settings = settings;
    }

    @Override
    protected void prepare() {

        validateSettings();

        executionTask =
                new BowtieExecutionTask(settings);

        executionTask
                .setSubtaskProgressWeight(100);

        addSubTask(executionTask);
    }

    @Override
    protected List<AbstractTask> onSubTaskFinished(
            AbstractTask subTask) {

        List<AbstractTask> resultTasks =
                new ArrayList<>();

        if (subTask == executionTask) {

            if (executionTask.hasError()) {

                setError(
                        executionTask.getError()
                );

                return resultTasks;
            }

            result =
                    executionTask.getResult();
        }

        return resultTasks;
    }

    private void validateSettings() {

        if (settings == null) {

            throw new IllegalArgumentException(
                    "Bowtie settings cannot be null"
            );
        }

        if (settings.getReferenceIndex() == null ||
                settings.getReferenceIndex().isBlank()) {

            throw new IllegalArgumentException(
                    "Reference index is required"
            );
        }

        if (settings.getRead1File() == null ||
                settings.getRead1File().isBlank()) {

            throw new IllegalArgumentException(
                    "Read file is required"
            );
        }
    }

    public BowtieResult getResult() {
        return result;
    }
}