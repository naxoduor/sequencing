package com.bio.sequencing.workflow;

import com.bio.sequencing.workers.Actor;

import java.util.ArrayList;
import java.util.List;

public class WorkflowGraph {

    private final List<Actor> actors = new ArrayList<>();

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    public List<Actor> getActors() {
        return actors;
    }
}