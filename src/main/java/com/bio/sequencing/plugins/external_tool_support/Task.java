package com.bio.sequencing.plugins.external_tool_support;

public abstract class Task implements Runnable {

    private final String name;

    private volatile TaskStatus status = TaskStatus.CREATED;
    private volatile Exception error;

    protected Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TaskStatus getStatus() {
        return status;
    }

    protected void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Exception getError() {
        return error;
    }

    protected void setError(Exception error) {
        this.error = error;
        this.status = TaskStatus.FAILED;
    }

    @Override
    public final void run() {
        try {
            setStatus(TaskStatus.RUNNING);

            prepare();

            execute();

            setStatus(TaskStatus.COMPLETED);

        } catch (Exception e) {
            setError(e);
        }
    }

    protected void prepare() throws Exception {
    }

    protected abstract void execute() throws Exception;
}