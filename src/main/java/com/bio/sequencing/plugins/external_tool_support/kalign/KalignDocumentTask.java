package com.bio.sequencing.plugins.external_tool_support.kalign;

import com.bio.sequencing.plugins.external_tool_support.mafft.*;

import java.util.ArrayList;
import java.util.List;

public class KalignDocumentTask extends AbstractTask {

    private final KalignSettings settings;

    private final String temporaryInputFile;
    private final String temporaryOutputFile;

    private final ProjectLoader projectLoader;
    private final IOAdapterFactory ioAdapterFactory;

    private LoadDocumentTask loadDocumentTask;

    private KalignSupportTask kalignSupportTask;

    private SaveDocumentTask saveDocumentTask;

    private Document currentDocument;

    private MsaObject msaObject;

    public KalignDocumentTask(
            KalignSettings settings,
            String temporaryInputFile,
            String temporaryOutputFile,
            ProjectLoader projectLoader,
            IOAdapterFactory ioAdapterFactory
    ) {
        super("Kalign document alignment");

        this.settings = settings;
        this.temporaryInputFile = temporaryInputFile;
        this.temporaryOutputFile = temporaryOutputFile;

        this.projectLoader = projectLoader;
        this.ioAdapterFactory = ioAdapterFactory;
    }

    @Override
    protected void prepare() {

        loadDocumentTask =
                new LoadDocumentTask(
                        settings.getInputFilePath()
                );

        addSubTask(loadDocumentTask);
    }

    @Override
    protected List<AbstractTask> onSubTaskFinished(
            AbstractTask subTask
    ) {

        List<AbstractTask> result =
                new ArrayList<>();

        /*
         * --------------------------------------------------
         * 1. LOAD DOCUMENT
         * --------------------------------------------------
         */
        if (subTask == loadDocumentTask) {

            if (loadDocumentTask.hasError()) {

                setError(
                        "Failed loading document: "
                                + loadDocumentTask.getError()
                );

                return result;
            }

            currentDocument =
                    loadDocumentTask.takeDocument();

            if (currentDocument == null) {

                setError(
                        "Failed loading document: "
                                + settings.getInputFilePath()
                );

                return result;
            }

            /*
             * UGENE checks:
             *
             * currentDocument->getObjects().length() == 1
             */
            if (currentDocument.getObjects().size() != 1) {

                setError(
                        "Number of objects != 1: "
                                + settings.getInputFilePath()
                );

                return result;
            }

            Object object =
                    currentDocument.getObjects().get(0);

            if (!(object instanceof MsaObject)) {

                setError(
                        "MSA object not found: "
                                + settings.getInputFilePath()
                );

                return result;
            }

            msaObject =
                    (MsaObject) object;

            /*
             * Equivalent to:
             *
             * mAFFTSupportTask =
             *     new MAFFTSupportTask(
             *         mAObject->getAlignment(),
             *         GObjectReference(),
             *         settings);
             */
            kalignSupportTask =
                    new KalignSupportTask(
                            msaObject.getAlignment(),
                            new GObjectReference(),
                            settings,
                            temporaryInputFile,
                            temporaryOutputFile
                    );

            result.add(kalignSupportTask);
        }

        /*
         * --------------------------------------------------
         * 2. KALIGN SUPPORT TASK FINISHED
         * --------------------------------------------------
         */
        else if (subTask == kalignSupportTask) {

            if (kalignSupportTask.hasError()) {

                setError(
                        "Kalign support task failed: "
                                + kalignSupportTask.getError()
                );

                return result;
            }

            MultipleSequenceAlignment resultMA =
                    kalignSupportTask.getResultMA();

            if (resultMA == null) {

                setError(
                        "Kalign returned no alignment"
                );

                return result;
            }

            /*
             * Equivalent to:
             *
             * mAObject->updateGapModel(
             *     mAFFTSupportTask->resultMA->getRows()
             * );
             */
            msaObject.updateGapModel(
                    resultMA.getRows()
            );

            /*
             * Save the updated document.
             */
            saveDocumentTask =
                    new SaveDocumentTask(
                            currentDocument,
                            ioAdapterFactory,
                            settings.getOutputFilePath()
                    );

            result.add(saveDocumentTask);
        }

        /*
         * --------------------------------------------------
         * 3. SAVE DOCUMENT FINISHED
         * --------------------------------------------------
         */
        else if (subTask == saveDocumentTask) {

            if (saveDocumentTask.hasError()) {

                setError(
                        "Failed saving Kalign result: "
                                + saveDocumentTask.getError()
                );

                return result;
            }

            /*
             * Equivalent to:
             *
             * Task* openTask =
             *     AppContext::getProjectLoader()
             *         ->openWithProjectTask(
             *             settings.outputFilePath
             *         );
             */
            AbstractTask openTask =
                    projectLoader.openWithProjectTask(
                            settings.getOutputFilePath()
                    );

            if (openTask != null) {
                result.add(openTask);
            }
        }

        return result;
    }
}