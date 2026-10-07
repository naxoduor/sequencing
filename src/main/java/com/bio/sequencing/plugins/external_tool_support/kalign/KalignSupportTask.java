package com.bio.sequencing.plugins.external_tool_support.kalign;

import com.bio.sequencing.plugins.external_tool_support.mafft.*;

import java.util.ArrayList;
import java.util.List;

public class KalignSupportTask extends AbstractTask {

    private final MultipleSequenceAlignment inputMsa;

    private final GObjectReference objectReference;

    private final KalignSettings settings;

    private final String temporaryInputUrl;
    private final String temporaryOutputUrl;

    private SaveMSA2SequencesTask saveTemporaryDocumentTask;

    private KalignExecutionTask kalignExecutionTask;

    private MultipleSequenceAlignment resultMA;

    public KalignSupportTask(
            MultipleSequenceAlignment inputMsa,
            GObjectReference objectReference,
            KalignSettings settings,
            String temporaryInputUrl,
            String temporaryOutputUrl
    ) {
        super("Kalign support task");

        this.inputMsa = inputMsa;
        this.objectReference = objectReference;
        this.settings = settings;
        this.temporaryInputUrl = temporaryInputUrl;
        this.temporaryOutputUrl = temporaryOutputUrl;
    }

    @Override
    protected void prepare() {

        if (inputMsa == null) {
            setError("Input MSA is null");
            return;
        }

        /*
         * Equivalent to:
         *
         * MsaUtils::createCopyWithIndexedRowNames(inputMsa)
         *
         * This allows us to reliably map the Kalign result
         * back to the original sequences.
         */
        MultipleSequenceAlignment indexedMsa =
                MsaUtils.createCopyWithIndexedRowNames(
                        inputMsa
                );

        saveTemporaryDocumentTask =
                new SaveMSA2SequencesTask(
                        indexedMsa,
                        temporaryInputUrl,
                        false,
                        BaseDocumentFormats.FASTA
                );

        /*
         * Same weighting idea as UGENE:
         *
         * Save input = 5%
         * Kalign execution = 95%
         */
        saveTemporaryDocumentTask.setSubtaskProgressWeight(5);

        addSubTask(saveTemporaryDocumentTask);
    }

    @Override
    protected List<AbstractTask> onSubTaskFinished(
            AbstractTask subTask
    ) {

        List<AbstractTask> result =
                new ArrayList<>();

        if (subTask == saveTemporaryDocumentTask) {

            if (subTask.hasError()) {
                setError(
                        "Failed to save temporary Kalign input: "
                                + subTask.getError()
                );

                return result;
            }

            kalignExecutionTask =
                    new KalignExecutionTask(
                            temporaryInputUrl,
                            temporaryOutputUrl,
                            settings
                    );

            kalignExecutionTask.setSubtaskProgressWeight(95);

            result.add(kalignExecutionTask);

        } else if (subTask == kalignExecutionTask) {

            if (subTask.hasError()) {
                setError(
                        "Kalign execution failed: "
                                + subTask.getError()
                );

                return result;
            }

            resultMA =
                    kalignExecutionTask.getResult();

            if (resultMA == null) {
                setError(
                        "Kalign completed but returned no alignment"
                );

                return result;
            }
        }

        return result;
    }

    public MultipleSequenceAlignment getResultMA() {
        return resultMA;
    }

    public GObjectReference getObjectReference() {
        return objectReference;
    }
}