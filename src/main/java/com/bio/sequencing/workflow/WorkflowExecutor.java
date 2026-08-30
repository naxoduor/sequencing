package com.bio.sequencing.workflow;

import com.bio.sequencing.workers.Actor;
import com.bio.sequencing.workers.Worker;

public class WorkflowExecutor {

    private final WorkflowGraph graph;

    public WorkflowExecutor(WorkflowGraph graph) {
        this.graph = graph;
    }

    public void execute() {

        for (Actor actor : graph.getActors()) {

            Worker worker = actor.getWorker();

            worker.init();

            while (!worker.isDone()) {
                worker.tick();
            }
        }
    }
}