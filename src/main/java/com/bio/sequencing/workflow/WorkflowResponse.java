package com.bio.sequencing.workflow;

public class WorkflowResponse {

    private String name;
    private String workflowId;

    public WorkflowResponse(String name, String workflowId){
        this.name= name;
        this.workflowId = workflowId;
    }

    public String getName() {
        return name;
    }

    public String getWorkflowId() {
        return workflowId;
    }
}
