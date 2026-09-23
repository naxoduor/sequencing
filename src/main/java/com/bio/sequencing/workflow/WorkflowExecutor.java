package com.bio.sequencing.workflow;

import com.bio.sequencing.workers.Actor;
import com.bio.sequencing.workers.Worker;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;

public class WorkflowExecutor {

    private final WorkflowGraph graph;

    public WorkflowExecutor(WorkflowGraph graph) {
        this.graph = graph;
    }

    public void execute() {
        int workerCount = Math.max(1, graph.getActors().size());
        ExecutorService executor = new ThreadPoolExecutor(
                workerCount,
                workerCount,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>()
        );

        try {
            for (Actor actor : graph.getActors()) {
                actor.getWorker().init();
            }

            boolean hasPendingWork;
            do {
                List<Future<?>> futures = new ArrayList<>();
                hasPendingWork = false;

                for (Actor actor : graph.getActors()) {
                    if (!actor.getWorker().isDone()) {
                        hasPendingWork = true;
                        futures.add(executor.submit(() -> {
                            try {
                                actor.getWorker().tick();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }));
                    }
                }

                for (Future<?> future : futures) {
                    future.get();
                }
            } while (hasPendingWork);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Workflow execution was interrupted", exception);
        } catch (ExecutionException exception) {
            throw new IllegalStateException("Worker execution failed", exception.getCause());
        } finally {
            executor.shutdownNow();
//            executor.close();
        }
    }
}
