package com.bio.sequencing.plugins.external_tool_support.bwa;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

import java.util.*;

public class BwaMemMultiTask
        extends AbstractTask {

    private final List<
            BwaMemExecutionTask
            > tasks;

    private final Map<
            BwaMemExecutionTask,
            Boolean
            > completed =
            new HashMap<>();

    public BwaMemMultiTask(
            String name,
            List<BwaMemExecutionTask> tasks) {

        super(name);

        this.tasks =
                new ArrayList<>(tasks);
    }

    @Override
    protected void prepare() {

        int weight =
                tasks.isEmpty()
                        ? 100
                        : 100 / tasks.size();

        for (BwaMemExecutionTask task :
                tasks) {

            task.setSubtaskProgressWeight(
                    weight
            );

            addSubTask(task);

            completed.put(
                    task,
                    false
            );
        }
    }

    @Override
    protected List<AbstractTask>
    onSubTaskFinished(
            AbstractTask subTask) {

        List<AbstractTask> result =
                new ArrayList<>();

        if (subTask instanceof
                BwaMemExecutionTask) {

            BwaMemExecutionTask task =
                    (BwaMemExecutionTask) subTask;

            completed.put(
                    task,
                    true
            );
        }

        return result;
    }
}