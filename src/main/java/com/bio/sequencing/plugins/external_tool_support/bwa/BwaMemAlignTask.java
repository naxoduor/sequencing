package com.bio.sequencing.plugins.external_tool_support.bwa;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

import java.util.*;

public class BwaMemAlignTask
        extends AbstractTask {

    private final BwaMemSettings settings;

    private List<BwaMemExecutionTask>
            alignTasks =
            new ArrayList<>();

    private BwaMemMultiTask
            alignMultiTask;

    private List<BwaMemResult>
            results =
            new ArrayList<>();

    public BwaMemAlignTask(
            BwaMemSettings settings) {

        super("Align reads with BWA-MEM");

        this.settings = settings;
    }

    @Override
    protected void prepare() {

        /*
         * Create individual alignment tasks.
         */
        BwaMemExecutionTask
                alignTask =
                new BwaMemExecutionTask(
                        settings
                );

        alignTask
                .setSubtaskProgressWeight(80);

        alignTasks.add(
                alignTask
        );

        /*
         * Equivalent of UGENE MultiTask.
         */
        alignMultiTask =
                new BwaMemMultiTask(
                        "Align reads with BWA-MEM Multitask",
                        alignTasks
                );

        addSubTask(
                alignMultiTask
        );
    }

    @Override
    protected List<AbstractTask> onSubTaskFinished(
            AbstractTask subTask) {

        List<AbstractTask> resultTasks =
                new ArrayList<>();

        /*
         * MultiTask has completed all
         * underlying BWA tasks.
         */
        if (subTask == alignMultiTask) {

            for (BwaMemExecutionTask task :
                    alignTasks) {

                if (task.hasError()) {

                    setError(
                            task.getError()
                    );

                    return resultTasks;
                }

                BwaMemResult result =
                        task.getResult();

                if (result != null) {

                    results.add(result);
                }
            }
        }

        return resultTasks;
    }

    public List<BwaMemResult> getResults() {
        return results;
    }
}
