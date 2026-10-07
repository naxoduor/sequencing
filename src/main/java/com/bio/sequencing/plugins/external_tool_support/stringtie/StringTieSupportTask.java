package com.bio.sequencing.plugins.external_tool_support.stringtie;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

import java.util.*;

public class StringTieSupportTask
        extends AbstractTask {

    private final StringTieSettings settings;

    private StringTieExecutionTask executionTask;

    private StringTieResult result;

    public StringTieSupportTask(
            StringTieSettings settings) {

        super("StringTie");

        this.settings = settings;
    }

    @Override
    protected void prepare() {

        validateSettings();

        executionTask =
                new StringTieExecutionTask(settings);

        executionTask.setSubtaskProgressWeight(100);

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
                    "StringTie settings cannot be null"
            );
        }

        if (settings.getInputBamFile() == null ||
                settings.getInputBamFile().isBlank()) {

            throw new IllegalArgumentException(
                    "Input BAM is required"
            );
        }

        if (settings.getOutputDirectory() == null ||
                settings.getOutputDirectory().isBlank()) {

            throw new IllegalArgumentException(
                    "Output directory is required"
            );
        }
    }

    public StringTieResult getResult() {
        return result;
    }
}