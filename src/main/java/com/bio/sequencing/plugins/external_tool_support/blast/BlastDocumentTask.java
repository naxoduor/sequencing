package com.bio.sequencing.plugins.external_tool_support.blast;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

import java.util.List;

public class BlastDocumentTask extends AbstractTask {

    private final BlastSettings settings;

    private BlastSupportTask blastSupportTask;

    private BlastResult result;

    public BlastDocumentTask(
            BlastSettings settings) {

        super("BLAST document task");

        this.settings = settings;
    }

    @Override
    protected void prepare() {

        validateSettings();

        blastSupportTask =
                new BlastSupportTask(settings);

        blastSupportTask.setSubtaskProgressWeight(95);

        addSubTask(blastSupportTask);
    }

    @Override
    protected List<AbstractTask> onSubTaskFinished(
            AbstractTask subTask) {

        if (subTask == blastSupportTask) {

            result =
                    blastSupportTask.getResult();

            /*
             * In the full UGENE-style implementation,
             * this is where you can add:
             *
             * SaveDocumentTask
             * OpenWithProjectTask
             * ParseBlastResultTask
             */

            return List.of();
        }

        return List.of();
    }

    private void validateSettings() {

        if (settings.getQueryFile() == null
                || settings.getQueryFile().isBlank()) {

            throw new IllegalArgumentException(
                    "BLAST query file is required");
        }

        if (settings.getDatabase() == null
                || settings.getDatabase().isBlank()) {

            throw new IllegalArgumentException(
                    "BLAST database is required");
        }

        if (settings.getOutputFile() == null
                || settings.getOutputFile().isBlank()) {

            throw new IllegalArgumentException(
                    "BLAST output file is required");
        }
    }

    public BlastResult getResult() {
        return result;
    }
}