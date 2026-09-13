package com.bio.sequencing.workflow;

import java.util.ArrayList;
import java.util.List;

public class WorkflowSchema {

    private List<NodeSchema> nodes = new ArrayList<>();

    private List<ConnectionSchema> connections =
            new ArrayList<>();

    public List<NodeSchema> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeSchema> nodes) {
        this.nodes = nodes;
    }

    public List<ConnectionSchema> getConnections() {
        return connections;
    }

    public void setConnections(
            List<ConnectionSchema> connections) {

        this.connections = connections;
    }
}