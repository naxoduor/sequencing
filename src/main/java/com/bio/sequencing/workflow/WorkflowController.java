package com.bio.sequencing.workflow;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {

    private static final Path OUTPUT_DIRECTORY =
            Paths.get("/home/maradona/Downloads").toAbsolutePath().normalize();

    private final WorkflowService workflowService;

    public WorkflowController(
            WorkflowService workflowService) {

        this.workflowService = workflowService;
    }

    @PostMapping
    public ResponseEntity<WorkflowResponse> createWorkflow(
            @RequestBody WorkflowSchema schema) {
        System.out.println("Print the schema");
        System.out.println(schema.getConnections());
        System.out.println(schema.getNodes());
        return ResponseEntity.accepted().body(workflowService.create(schema));
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String filename) {
                System.out.println("Print the filename");
                System.out.println(filename);

        Path file = OUTPUT_DIRECTORY.resolve(filename).normalize();
        if (!file.startsWith(OUTPUT_DIRECTORY) || !java.nio.file.Files.isRegularFile(file)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }
}