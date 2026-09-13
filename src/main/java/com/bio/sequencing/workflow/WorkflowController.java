package com.bio.sequencing.workflow;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(
            WorkflowService workflowService) {

        this.workflowService = workflowService;
    }

    @PostMapping
    public WorkflowResponse createWorkflow(
            @RequestBody WorkflowSchema schema) {

        return workflowService.create(schema);
    }
}