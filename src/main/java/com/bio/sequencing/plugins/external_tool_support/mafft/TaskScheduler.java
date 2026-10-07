package com.bio.sequencing.plugins.external_tool_support.mafft;

import java.util.List;

public class TaskScheduler {

    public void submit(AbstractTask rootTask) {

        /*
         * Execute prepare().
         */
        rootTask.run();

        /*
         * Process dynamically-created subtasks.
         */
        processSubTasks(rootTask);
    }

    private void processSubTasks(AbstractTask parent) {

        for (AbstractTask subTask : parent.getSubTasks()) {

            /*
             * Execute child.
             */
            subTask.run();

            /*
             * Tell parent that child finished.
             */
            List<AbstractTask> nextTasks =
                    parent.onSubTaskFinished(subTask);

            /*
             * Dynamically-created tasks are the equivalent
             * of UGENE's:
             *
             * res.append(nextTask)
             */
            for (AbstractTask nextTask : nextTasks) {

                /*
                 * Execute the next task.
                 */
                nextTask.run();

                /*
                 * Continue the chain.
                 */
                processSubTasks(nextTask);

                /*
                 * Notify the parent.
                 */
                parent.onSubTaskFinished(nextTask);
            }
        }
    }
}

