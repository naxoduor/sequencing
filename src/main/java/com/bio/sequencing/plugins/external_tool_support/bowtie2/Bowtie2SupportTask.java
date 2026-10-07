package com.bio.sequencing.plugins.external_tool_support.bowtie2;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

import java.util.*;

public class Bowtie2SupportTask
        extends AbstractTask {

    private final Bowtie2Settings settings;

    private Bowtie2ExecutionTask
            executionTask;

    private SamToBamTask
            samToBamTask;

    private Bowtie2Result result;

    public Bowtie2SupportTask(
            Bowtie2Settings settings) {

        super("Bowtie2 Alignment");

        this.settings = settings;
    }

    @Override
    protected void prepare() {

        validateSettings();

        executionTask =
                new Bowtie2ExecutionTask(
                        settings
                );

        executionTask
                .setSubtaskProgressWeight(80);

        addSubTask(
                executionTask
        );
    }

    @Override
    protected List<AbstractTask> onSubTaskFinished(
            AbstractTask subTask) {

        List<AbstractTask> resultTasks =
                new ArrayList<>();

        /*
         * ---------------------------------------------
         * Bowtie2 finished
         * ---------------------------------------------
         */
        if (subTask == executionTask) {

            if (executionTask.hasError()) {

                setError(
                        executionTask.getError()
                );

                return resultTasks;
            }

            result =
                    executionTask.getResult();

            /*
             * Create SAM -> BAM task.
             */
            samToBamTask =
                    new SamToBamTask(
                            result.getSamFile(),
                            settings.getOutputBamFile()
                    );

            samToBamTask
                    .setSubtaskProgressWeight(20);

            /*
             * IMPORTANT:
             *
             * Return it to the scheduler.
             */
            resultTasks.add(
                    samToBamTask
            );
        }

        /*
         * ---------------------------------------------
         * SAM -> BAM finished
         * ---------------------------------------------
         */
        else if (subTask == samToBamTask) {

            if (samToBamTask.hasError()) {

                setError(
                        samToBamTask.getError()
                );

                return resultTasks;
            }

            result.setBamFile(
                    samToBamTask.getBamFile()
            );
        }

        return resultTasks;
    }

    private void validateSettings() {

        if (settings == null) {

            throw new IllegalArgumentException(
                    "Bowtie2 settings cannot be null"
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
                    "Read 1 file is required"
            );
        }
    }

    public Bowtie2Result getResult() {
        return result;
    }
}