package com.bio.sequencing.plugins.external_tool_support.mafft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractTask {

    private final String name;

    private volatile boolean finished;
    private volatile boolean canceled;
    private volatile String error;

    private final List<AbstractTask> subTasks = new ArrayList<>();

    protected AbstractTask(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void run() {
        try {
            prepare();

            if (hasError() || isCanceled()) {
                return;
            }

            execute();

            if (!hasError() && !isCanceled()) {
                finished = true;
            }

        } catch (Exception e) {
            setError(e.getMessage());
        }
    }

    protected void prepare() {
    }

    protected void execute() throws Exception {
    }

    protected void addSubTask(AbstractTask task) {
        if (task != null) {
            subTasks.add(task);
        }
    }

    public List<AbstractTask> getSubTasks() {
        return Collections.unmodifiableList(subTasks);
    }

    /**
     * Called by the scheduler when one of the subtasks completes.
     */
    protected List<AbstractTask> onSubTaskFinished(AbstractTask subTask) {
        return new ArrayList<>();
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public boolean hasError() {
        return error != null;
    }

    public String getError() {
        return error;
    }

    protected void setError(String error) {
        this.error = error;
    }

    public void cancel() {
        this.canceled = true;
    }

    public void setSubtaskProgressWeight(int weight) {
        // Sample implementation.
        // Real implementation would store this on the task.
    }
}