package com.bio.sequencing.plugins.external_tool_support.bwa;

import com.bio.sequencing.plugins.external_tool_support.bowtie2.SamToBamTask;
import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

import java.util.*;

public class BwaMemSupportTask
        extends AbstractTask {

    private final BwaMemSettings settings;

    private BwaMemAlignTask
            alignTask;

    private SamToBamTask
            samToBamTask;

    private BwaMemResult result;

    public BwaMemSupportTask(
            BwaMemSettings settings) {

        super("BWA-MEM");

        this.settings = settings;
    }

    @Override
    protected void prepare() {

        validateSettings();

        alignTask =
                new BwaMemAlignTask(
                        settings
                );

        alignTask
                .setSubtaskProgressWeight(80);

        addSubTask(
                alignTask
        );
    }

    @Override
    protected List<AbstractTask>
    onSubTaskFinished(
            AbstractTask subTask) {

        List<AbstractTask> resultTasks =
                new ArrayList<>();

        /*
         * -------------------------------------------
         * BWA-MEM alignment finished.
         * -------------------------------------------
         */
        if (subTask == alignTask) {

            List<BwaMemResult>
                    alignmentResults =
                    alignTask.getResults();

            if (alignmentResults.isEmpty()) {

                setError(
                        "BWA-MEM produced no result"
                );

                return resultTasks;
            }

            /*
             * For the normal single-input case
             * we have one result.
             */
            result =
                    alignmentResults.get(0);

            /*
             * Convert SAM -> BAM.
             */
            samToBamTask =
                    new SamToBamTask(
                            result.getSamFile(),
                            settings.getOutputBamFile()
                    );

            samToBamTask
                    .setSubtaskProgressWeight(20);

            /*
             * Dynamically return the next
             * task to the scheduler.
             */
            resultTasks.add(
                    samToBamTask
            );
        }

        /*
         * -------------------------------------------
         * SAM -> BAM finished.
         * -------------------------------------------
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
                    "BWA settings cannot be null"
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

    public BwaMemResult getResult() {
        return result;
    }
}