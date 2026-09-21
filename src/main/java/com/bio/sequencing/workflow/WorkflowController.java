package com.bio.sequencing.workflow;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(
            WorkflowService workflowService) {

        this.workflowService = workflowService;
    }

    @PostMapping
    public CompletableFuture<WorkflowResponse> createWorkflow(
            @RequestBody WorkflowSchema schema) {
        System.out.println("Print the schema");
        System.out.println(schema.getConnections());
        System.out.println(schema.getNodes());
        return workflowService.create(schema);
    }
}