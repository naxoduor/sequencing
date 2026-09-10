package com.bio.sequencing.plugins.dbi_bam;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/bam")
public class BAMController {

    private final BAMService bamService;

    public BAMController(BAMService bamService) {
        this.bamService = bamService;
    }

    @GetMapping("/references")
    public List<ReferenceInfo> references(
            @RequestParam String file)
            throws IOException {

        return bamService.getReferences(file);
    }

    @GetMapping("/query")
    public List<BAMRecord> query(
            @RequestParam String file,
            @RequestParam String chromosome,
            @RequestParam int start,
            @RequestParam int end)
            throws IOException {

        return bamService.query(
                file,
                chromosome,
                start,
                end
        );
    }
}