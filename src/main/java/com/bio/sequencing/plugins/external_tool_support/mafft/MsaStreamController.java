package com.bio.sequencing.plugins.external_tool_support.mafft;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.nio.file.Path;

@RestController
@RequestMapping("/api/msa")
public class MsaStreamController {

    private final MsaStreamService msaStreamService;

    public MsaStreamController(MsaStreamService msaStreamService) {
        this.msaStreamService = msaStreamService;
    }

    @GetMapping(
            value = "/stream",
            produces = "application/x-ndjson"
    )
    public StreamingResponseBody streamAlignment(
            @RequestParam String file
    ) {
        Path fastaFile = Path.of(file);

        return outputStream ->
                msaStreamService.streamFasta(fastaFile, outputStream);
    }
}