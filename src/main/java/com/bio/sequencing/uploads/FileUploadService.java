package com.bio.sequencing.uploads;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class FileUploadService {

    private final Path uploadRoot;

    public FileUploadService(
            @Value("${app.storage.upload-dir}")
            String uploadDir) {

        this.uploadRoot =
                Paths.get(uploadDir)
                        .toAbsolutePath()
                        .normalize();

        try {
            Files.createDirectories(uploadRoot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initializeUpload(
            UploadMetadata metadata) throws IOException {

        Path uploadDir =
                uploadRoot.resolve(metadata.getUploadId());

        Path chunksDir =
                uploadDir.resolve("chunks");

        Files.createDirectories(chunksDir);
    }

    public void saveChunk(
            String uploadId,
            int chunkNumber,
            byte[] data) throws IOException {

        Path chunk =
                uploadRoot
                        .resolve(uploadId)
                        .resolve("chunks")
                        .resolve(String.valueOf(chunkNumber));

        Files.write(
                chunk,
                data,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    public boolean isComplete(
            String uploadId,
            int totalChunks) {

        Path chunksDir =
                uploadRoot
                        .resolve(uploadId)
                        .resolve("chunks");

        for (int i = 0; i < totalChunks; i++) {

            if (!Files.exists(
                    chunksDir.resolve(String.valueOf(i)))) {

                return false;
            }
        }

        return true;
    }

    public Path assemble(
            UploadMetadata metadata)
            throws IOException {

        Path uploadDir =
                uploadRoot.resolve(metadata.getUploadId());

        Path output =
                uploadDir.resolve(metadata.getFileName());

        try (OutputStream out =
                     Files.newOutputStream(
                             output,
                             StandardOpenOption.CREATE,
                             StandardOpenOption.TRUNCATE_EXISTING)) {

            for (int i = 0;
                 i < metadata.getTotalChunks();
                 i++) {

                Path chunk =
                        uploadDir
                                .resolve("chunks")
                                .resolve(String.valueOf(i));

                Files.copy(chunk, out);
            }
        }

        return output;
    }
}