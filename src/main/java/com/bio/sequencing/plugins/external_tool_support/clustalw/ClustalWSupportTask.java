package com.bio.sequencing.plugins.external_tool_support.clustalw;

import com.bio.sequencing.plugins.external_tool_support.mafft.*;

import java.util.ArrayList;
import java.util.List;

public class ClustalWSupportTask
        extends AbstractTask {

    private final MultipleSequenceAlignment inputMsa;

    private final GObjectReference objectReference;

    private final ClustalWSettings settings;

    private final String temporaryInputUrl;

    private final String temporaryOutputUrl;

    private SaveMSA2SequencesTask
            saveTemporaryDocumentTask;

    private ClustalWExecutionTask
            clustalWExecutionTask;

    private MultipleSequenceAlignment resultMA;

    public ClustalWSupportTask(
            MultipleSequenceAlignment inputMsa,
            GObjectReference objectReference,
            ClustalWSettings settings,
            String temporaryInputUrl,
            String temporaryOutputUrl) {

        super("ClustalW Support Task");

        this.inputMsa = inputMsa;

        this.objectReference =
                objectReference;

        this.settings = settings;

        this.temporaryInputUrl =
                temporaryInputUrl;

        this.temporaryOutputUrl =
                temporaryOutputUrl;
    }

    @Override
    protected void prepare() {

        /*
         * Validate input.
         */
        if (inputMsa == null) {

            setError(
                    "Input MSA cannot be null"
            );

            return;
        }

        /*
         * Equivalent to:
         *
         * MsaUtils::
         * createCopyWithIndexedRowNames(inputMsa)
         */
        MultipleSequenceAlignment indexedMsa =
                MsaUtils
                        .createCopyWithIndexedRowNames(
                                inputMsa
                        );

        /*
         * Save temporary FASTA.
         */
        saveTemporaryDocumentTask =
                new SaveMSA2SequencesTask(
                        indexedMsa,
                        temporaryInputUrl,
                        false,
                        BaseDocumentFormats.FASTA
                );

        /*
         * Same UGENE progress weight.
         */
        saveTemporaryDocumentTask
                .setSubtaskProgressWeight(5);

        /*
         * Add dependency.
         */
        addSubTask(
                saveTemporaryDocumentTask
        );
    }

    @Override
    public List<AbstractTask>
    onSubTaskFinished(
            AbstractTask subTask) {

        List<AbstractTask> result =
                new ArrayList<>();

        /*
         * ==================================================
         * 1. SAVE TEMPORARY FASTA FINISHED
         * ==================================================
         */
        if (subTask ==
                saveTemporaryDocumentTask) {

            if (subTask.hasError()) {

                setError(
                        subTask.getError()
                );

                return result;
            }

            if (subTask.isCanceled()) {

                cancel();

                return result;
            }

            /*
             * Start ClustalW.
             */
            clustalWExecutionTask =
                    new ClustalWExecutionTask(
                            temporaryInputUrl,
                            temporaryOutputUrl,
                            settings
                    );

            /*
             * Remaining 95%.
             */
            clustalWExecutionTask
                    .setSubtaskProgressWeight(95);

            /*
             * Dynamically schedule
             * ClustalW execution.
             */
            result.add(
                    clustalWExecutionTask
            );

            return result;
        }

        /*
         * ==================================================
         * 2. CLUSTALW FINISHED
         * ==================================================
         */
        if (subTask ==
                clustalWExecutionTask) {

            if (subTask.hasError()) {

                setError(
                        subTask.getError()
                );

                return result;
            }

            if (subTask.isCanceled()) {

                cancel();

                return result;
            }

            /*
             * Get alignment.
             */
            resultMA =
                    clustalWExecutionTask
                            .getResult();

            if (resultMA == null) {

                setError(
                        "ClustalW returned "
                                + "no alignment result"
                );

                return result;
            }
        }

        return result;
    }

    public MultipleSequenceAlignment
    getResultMA() {

        return resultMA;
    }
}