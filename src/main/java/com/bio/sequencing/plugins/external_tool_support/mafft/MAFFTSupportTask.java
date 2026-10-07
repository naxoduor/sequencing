package com.bio.sequencing.plugins.external_tool_support.mafft;

import java.util.ArrayList;
import java.util.List;

public class MAFFTSupportTask
        extends AbstractTask {

    private final MultipleSequenceAlignment inputMsa;

    private final GObjectReference objectReference;

    private final MAFFTSettings settings;

    private final String url;

    private SaveMSA2SequencesTask
            saveTemporaryDocumentTask;

    private MAFFTExecutionTask
            mafftExecutionTask;

    private MultipleSequenceAlignment resultMA;

    public MAFFTSupportTask(
            MultipleSequenceAlignment inputMsa,
            GObjectReference objectReference,
            MAFFTSettings settings,
            String temporaryUrl) {

        super("MAFFT Support Task");

        this.inputMsa = inputMsa;
        this.objectReference = objectReference;
        this.settings = settings;
        this.url = temporaryUrl;
    }

    @Override
    protected void prepare() {

        if (inputMsa == null) {

            setError(
                    "Input MSA cannot be null"
            );

            return;
        }


        MultipleSequenceAlignment indexedMsa =
                MsaUtils
                        .createCopyWithIndexedRowNames(
                                inputMsa
                        );

        saveTemporaryDocumentTask =
                new SaveMSA2SequencesTask(
                        indexedMsa,
                        url,
                        false,
                        BaseDocumentFormats.FASTA
                );

        /*
         * C++:
         *
         * saveTemporaryDocumentTask
         *      ->setSubtaskProgressWeight(5);
         */
        saveTemporaryDocumentTask
                .setSubtaskProgressWeight(5);

        /*
         * C++:
         *
         * addSubTask(saveTemporaryDocumentTask);
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
         * -------------------------------------------------
         * Temporary FASTA has been created.
         * -------------------------------------------------
         */
        if (subTask == saveTemporaryDocumentTask) {

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
             * Now start MAFFT.
             */
            mafftExecutionTask =
                    new MAFFTExecutionTask(
                            url,
                            settings
                    );

            /*
             * MAFFT represents the remaining
             * 95% of the operation.
             */
            mafftExecutionTask
                    .setSubtaskProgressWeight(95);

            /*
             * Dynamically add the next task.
             *
             * This is equivalent to:
             *
             * res.append(mafftTask);
             */
            result.add(
                    mafftExecutionTask
            );

            return result;
        }

        /*
         * -------------------------------------------------
         * MAFFT has finished.
         * -------------------------------------------------
         */
        if (subTask == mafftExecutionTask) {

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

            resultMA =
                    mafftExecutionTask.getResult();

            if (resultMA == null) {

                setError(
                        "MAFFT returned no alignment"
                );

                return result;
            }

            /*
             * MAFFTSupportTask is now complete.
             */
            return result;
        }

        return result;
    }

    public MultipleSequenceAlignment
    getResultMA() {

        return resultMA;
    }
}