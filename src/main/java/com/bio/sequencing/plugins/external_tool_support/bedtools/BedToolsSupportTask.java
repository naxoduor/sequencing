package com.bio.sequencing.plugins.external_tool_support.bedtools;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

import java.util.ArrayList;
import java.util.List;

public class BedToolsSupportTask
        extends AbstractTask {

    private final BedToolsSettings settings;

    private BedToolsExecutionTask
            executionTask;

    private BedToolsResult result;

    public BedToolsSupportTask(
            BedToolsSettings settings) {

        super("BEDTools");

        this.settings = settings;
    }

    @Override
    protected void prepare() {

        validateSettings();

        executionTask =
                new BedToolsExecutionTask(
                        settings
                );

        executionTask
                .setSubtaskProgressWeight(100);

        addSubTask(
                executionTask
        );
    }

    @Override
    protected List<AbstractTask>
    onSubTaskFinished(
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
                    "BEDTools settings cannot be null"
            );
        }

        if (settings.getOperation() == null) {

            throw new IllegalArgumentException(
                    "BEDTools operation is required"
            );
        }

        if (settings.getInputFile() == null ||
                settings.getInputFile().isBlank()) {

            throw new IllegalArgumentException(
                    "Input file is required"
            );
        }
    }

    public BedToolsResult getResult() {
        return result;
    }
}