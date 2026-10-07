package com.bio.sequencing.plugins.external_tool_support.hmmer;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

import java.util.*;

public class HMMERDocumentTask
        extends AbstractTask {

    private final HMMERSettings settings;

    private HMMERSupportTask supportTask;

    private SaveHMMERResultTask saveTask;

    private HMMERResultDocument resultDocument;

    private HMMERExecutionResult executionResult;

    public HMMERDocumentTask(
            HMMERSettings settings) {

        super("HMMER document task");

        this.settings = settings;
    }

    @Override
    protected void prepare() {

        validateSettings();

        supportTask =
                new HMMERSupportTask(settings);

        supportTask.setSubtaskProgressWeight(95);

        addSubTask(supportTask);
    }

    @Override
    protected List<AbstractTask> onSubTaskFinished(
            AbstractTask subTask) {

        List<AbstractTask> result =
                new ArrayList<>();

        if (subTask == supportTask) {

            executionResult =
                    supportTask.getExecutionResult();

            resultDocument =
                    supportTask.getResultDocument();

            /*
             * Result has now been parsed.
             *
             * Create the persistence task dynamically.
             */

            if (settings.getOutputFile() != null
                    && !settings.getOutputFile()
                    .isBlank()) {

                saveTask =
                        new SaveHMMERResultTask(
                                resultDocument,
                                settings.getOutputFile());

                saveTask.setSubtaskProgressWeight(5);

                result.add(saveTask);
            }
        }

        else if (subTask == saveTask) {

            /*
             * At this point the result has been
             * persisted.
             *
             * Equivalent UGENE code:
             *
             * Task* openTask =
             *     projectLoader.openWithProjectTask(...);
             *
             * result.add(openTask);
             */

            /*
             * If you have OpenWithProjectTask:
             *
             * AbstractTask openTask =
             *     projectLoader.openWithProjectTask(
             *         settings.getOutputFile());
             *
             * if (openTask != null) {
             *     result.add(openTask);
             * }
             */
        }

        return result;
    }

    private void validateSettings() {

        if (settings.getSequenceFile() == null
                || settings.getSequenceFile()
                .isBlank()) {

            throw new IllegalArgumentException(
                    "Sequence file is required");
        }

        if (settings.getOutputDirectory() == null
                || settings.getOutputDirectory()
                .isBlank()) {

            throw new IllegalArgumentException(
                    "Output directory is required");
        }

        if (settings.getOperation()
                == HMMERSettings.Operation.HMMSEARCH) {

            if (settings.getHmmFile() == null
                    || settings.getHmmFile()
                    .isBlank()) {

                throw new IllegalArgumentException(
                        "HMM file is required for hmmsearch");
            }
        }

        if (settings.getOperation()
                == HMMERSettings.Operation.HMMSCAN) {

            if (settings.getHmmDatabase() == null
                    || settings.getHmmDatabase()
                    .isBlank()) {

                throw new IllegalArgumentException(
                        "HMM database is required for hmmscan");
            }
        }
    }

    public HMMERResultDocument getResultDocument() {
        return resultDocument;
    }

    public HMMERExecutionResult getExecutionResult() {
        return executionResult;
    }
}