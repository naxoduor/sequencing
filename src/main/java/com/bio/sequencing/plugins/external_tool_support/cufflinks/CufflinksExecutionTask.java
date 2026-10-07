package com.bio.sequencing.plugins.external_tool_support.cufflinks;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

public class CufflinksExecutionTask
        extends AbstractTask {

    private final CufflinksSettings settings;

    private CufflinksResult result;

    private final CufflinksExecutor executor;

    public CufflinksExecutionTask(
            CufflinksSettings settings
    ) {

        super("Cufflinks execution");

        this.settings = settings;

        this.executor =
                new CufflinksExecutor();
    }

    @Override
    protected void execute() throws Exception {

        if (settings.getInputBamFile() == null
                || settings.getInputBamFile().isBlank()) {

            throw new IllegalArgumentException(
                    "Input BAM file is required"
            );
        }

        if (!java.nio.file.Files.exists(
                java.nio.file.Path.of(
                        settings.getInputBamFile()
                ))) {

            throw new IllegalArgumentException(
                    "Input BAM does not exist: "
                            + settings.getInputBamFile()
            );
        }

        result =
                executor.execute(settings);

        if (result == null) {

            throw new IllegalStateException(
                    "Cufflinks returned no result"
            );
        }

        if (result.getTranscriptsGtf() == null) {

            throw new IllegalStateException(
                    "Cufflinks did not produce "
                            + "transcripts.gtf"
            );
        }
    }

    public CufflinksResult getResult() {
        return result;
    }
}