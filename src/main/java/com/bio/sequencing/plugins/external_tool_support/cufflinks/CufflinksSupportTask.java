package com.bio.sequencing.plugins.external_tool_support.cufflinks;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;
import com.bio.sequencing.plugins.external_tool_support.mafft.GObjectReference;

import java.util.ArrayList;
import java.util.List;

public class CufflinksSupportTask
        extends AbstractTask {

    private final String inputBamFile;

    private final GObjectReference objectReference;

    private final CufflinksSettings settings;

    private CufflinksExecutionTask
            cufflinksExecutionTask;

    private CufflinksResult result;

    public CufflinksSupportTask(
            String inputBamFile,
            GObjectReference objectReference,
            CufflinksSettings settings
    ) {

        super("Cufflinks support task");

        this.inputBamFile =
                inputBamFile;

        this.objectReference =
                objectReference;

        this.settings =
                settings;
    }

    @Override
    protected void prepare() {

        if (inputBamFile == null
                || inputBamFile.isBlank()) {

            setError(
                    "Input BAM file is empty"
            );

            return;
        }

        if (!java.nio.file.Files.exists(
                java.nio.file.Path.of(
                        inputBamFile
                ))) {

            setError(
                    "Input BAM does not exist: "
                            + inputBamFile
            );

            return;
        }

        /*
         * The settings object owns the actual
         * Cufflinks input.
         */
        settings.setInputBamFile(
                inputBamFile
        );

        /*
         * Cufflinks itself is the next task.
         */
        cufflinksExecutionTask =
                new CufflinksExecutionTask(
                        settings
                );

        cufflinksExecutionTask
                .setSubtaskProgressWeight(100);

        addSubTask(
                cufflinksExecutionTask
        );
    }

    @Override
    protected List<AbstractTask>
    onSubTaskFinished(
            AbstractTask subTask
    ) {

        List<AbstractTask> result =
                new ArrayList<>();

        if (subTask ==
                cufflinksExecutionTask) {

            if (subTask.hasError()) {

                setError(
                        "Cufflinks execution failed: "
                                + subTask.getError()
                );

                return result;
            }

            result =
                    cufflinksExecutionTask
                            .getResult();

            if (result == null) {

                setError(
                        "Cufflinks returned no result"
                );
            }
        }

        return new ArrayList<>();
    }

    public CufflinksResult getResult() {
        return result;
    }

    public GObjectReference
    getObjectReference() {

        return objectReference;
    }
}