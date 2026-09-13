package com.bio.sequencing.workflow.socket;

import java.time.Instant;

public class WorkflowEvent {

    public enum Type {
        WORKFLOW_STARTED,
        WORKER_STARTED,
        PORT_DATA,
        WORKER_COMPLETED,
        WORKER_FAILED,
        WORKFLOW_COMPLETED,
        WORKFLOW_FAILED
    }

    private Type type;

    private String workflowId;

    private String nodeId;

    private String portId;

    private Object data;

    private String message;

    private Instant timestamp;

    public WorkflowEvent() {
        this.timestamp = Instant.now();
    }

    public WorkflowEvent(
            Type type,
            String workflowId,
            String nodeId,
            String portId,
            Object data,
            String message) {

        this.type = type;
        this.workflowId = workflowId;
        this.nodeId = nodeId;
        this.portId = portId;
        this.data = data;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}