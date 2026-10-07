package com.bio.sequencing.plugins.external_tool_support.clustalo;

import com.bio.sequencing.plugins.external_tool_support.mafft.*;

import java.util.ArrayList;
import java.util.List;

public class ClustalOSupportTask
        extends AbstractTask {

    private final MultipleSequenceAlignment inputMsa;

    private final GObjectReference objectReference;

    private final ClustalOSettings settings;

    private final String url;

    private SaveMSA2SequencesTask
            saveTemporaryDocumentTask;

    private ClustalOExecutionTask
            clustalOExecutionTask;

    private MultipleSequenceAlignment resultMA;

    public ClustalOSupportTask(
            MultipleSequenceAlignment inputMsa,
            GObjectReference objectReference,
            ClustalOSettings settings,
            String temporaryUrl) {

        super("Clustal Omega Support Task");

        this.inputMsa = inputMsa;
        this.objectReference =
                objectReference;
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

        /*
         * UGENE equivalent:
         *
         * saveTemporaryDocumentTask =
         *     new SaveMSA2SequencesTask(
         *         MsaUtils::createCopyWithIndexedRowNames(
         *             inputMsa
         *         ),
         *         url,
         *         false,
         *         BaseDocumentFormats::FASTA
         *     );
         */

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
         * Same 5% progress weight.
         */
        saveTemporaryDocumentTask
                .setSubtaskProgressWeight(5);

        /*
         * Equivalent to:
         *
         * addSubTask(
         *     saveTemporaryDocumentTask
         * );
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
         * ================================================
         * SAVE TEMPORARY FASTA FINISHED
         * ================================================
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
             * Now launch Clustal Omega.
             */
            clustalOExecutionTask =
                    new ClustalOExecutionTask(
                            url,
                            url + ".clustalo.fasta",
                            settings
                    );

            /*
             * Remaining 95%.
             */
            clustalOExecutionTask
                    .setSubtaskProgressWeight(95);

            /*
             * Equivalent to:
             *
             * res.append(clustalOTask);
             */
            result.add(
                    clustalOExecutionTask
            );

            return result;
        }

        /*
         * ================================================
         * CLUSTAL O FINISHED
         * ================================================
         */
        if (subTask == clustalOExecutionTask) {

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
                    clustalOExecutionTask
                            .getResult();

            if (resultMA == null) {

                setError(
                        "Clustal Omega returned "
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