package com.bio.sequencing.uploads;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    private final FileUploadService uploadService;

    private final Map<String, UploadMetadata> uploads =
            new ConcurrentHashMap<>();

    public UploadController(
            FileUploadService uploadService) {

        this.uploadService = uploadService;
    }

    @PostMapping
    public Map<String, Object> createUpload(
            @RequestBody CreateUploadRequest request)
            throws IOException {

        String uploadId =
                UUID.randomUUID().toString();

        int totalChunks =
                (int) Math.ceil(
                        (double) request.getFileSize()
                                / request.getChunkSize()
                );

        UploadMetadata metadata =
                new UploadMetadata(
                        uploadId,
                        request.getFileName(),
                        request.getFileSize(),
                        totalChunks,
                        request.getChunkSize()
                );

        uploads.put(uploadId, metadata);

        uploadService.initializeUpload(metadata);

        return Map.of(
                "uploadId", uploadId,
                "totalChunks", totalChunks
        );
    }

    @PostMapping("/{uploadId}/chunks/{chunkNumber}")
    public ResponseEntity<?> uploadChunk(
            @PathVariable String uploadId,
            @PathVariable int chunkNumber,
            @RequestBody byte[] data)
            throws IOException {

        UploadMetadata metadata =
                uploads.get(uploadId);

        if (metadata == null) {
            return ResponseEntity.notFound().build();
        }

        uploadService.saveChunk(
                uploadId,
                chunkNumber,
                data
        );

        boolean complete =
                uploadService.isComplete(
                        uploadId,
                        metadata.getTotalChunks()
                );

        if (complete) {

            Path file =
                    uploadService.assemble(metadata);

            return ResponseEntity.ok(
                    Map.of(
                            "status", "complete",
                            "file", file.toString()
                    )
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "status", "chunk_received",
                        "chunk", chunkNumber
                )
        );
    }
}