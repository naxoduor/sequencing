package com.bio.sequencing.plugins.external_tool_support;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskScheduler {

    private final ExecutorService executor;

    public TaskScheduler(int threads) {
        this.executor =
                Executors.newFixedThreadPool(threads);
    }

    public Future<?> submit(Task task) {

        return executor.submit(task);
    }

    public void shutdown() {

        executor.shutdown();
    }
}