package com.bio.sequencing.plugins.external_tool_support.mafft;

import java.util.ArrayList;
import java.util.List;

public class MAFFTDocumentTask
        extends AbstractTask {

    private final MAFFTSettings settings;

    private final String temporaryFilePath;

    private final ProjectLoader projectLoader;

    private final IOAdapterFactory ioAdapterFactory;

    private LoadDocumentTask loadDocumentTask;

    private MAFFTSupportTask mafftSupportTask;

    private SaveDocumentTask saveDocumentTask;

    private Document currentDocument;

    private MsaObject msaObject;

    public MAFFTDocumentTask(
            MAFFTSettings settings,
            String temporaryFilePath,
            ProjectLoader projectLoader,
            IOAdapterFactory ioAdapterFactory) {

        super("MAFFT Document Task");

        this.settings = settings;
        this.temporaryFilePath =
                temporaryFilePath;

        this.projectLoader =
                projectLoader;

        this.ioAdapterFactory =
                ioAdapterFactory;
    }

    @Override
    protected void prepare() {

        /*
         * C++:
         *
         * loadDocumentTask =
         *     new LoadDocumentTask(...);
         *
         * res.append(loadDocumentTask);
         */
        loadDocumentTask =
                new LoadDocumentTask(
                        settings.getInputFilePath()
                );

        addSubTask(
                loadDocumentTask
        );
    }

    @Override
    public List<AbstractTask>
    onSubTaskFinished(
            AbstractTask subTask) {

        List<AbstractTask> result =
                new ArrayList<>();

        /*
         * =================================================
         * 1. LOAD DOCUMENT FINISHED
         * =================================================
         */
        if (subTask == loadDocumentTask) {

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
             * C++:
             *
             * currentDocument =
             *     loadDocumentTask->takeDocument();
             */
            currentDocument =
                    loadDocumentTask.takeDocument();

            if (currentDocument == null) {

                setError(
                        "Failed loading document: "
                                + loadDocumentTask
                                .getUrlString()
                );

                return result;
            }

            /*
             * C++:
             *
             * currentDocument->getObjects()
             *      .length() == 1
             */
            if (currentDocument
                    .getObjects()
                    .size() != 1) {

                setError(
                        "Number of objects != 1: "
                                + loadDocumentTask
                                .getUrlString()
                );

                return result;
            }

            /*
             * C++:
             *
             * mAObject =
             * qobject_cast<MsaObject*>(
             *     currentDocument
             *       ->getObjects()
             *       .first()
             * );
             */
            Object object =
                    currentDocument
                            .getObjects()
                            .get(0);

            if (!(object instanceof MsaObject)) {

                setError(
                        "MA object not found: "
                                + loadDocumentTask
                                .getUrlString()
                );

                return result;
            }

            msaObject =
                    (MsaObject) object;

            /*
             * C++:
             *
             * mAFFTSupportTask =
             *     new MAFFTSupportTask(
             *         mAObject->getAlignment(),
             *         GObjectReference(),
             *         settings
             *     );
             */
            mafftSupportTask =
                    new MAFFTSupportTask(
                            msaObject.getAlignment(),
                            new GObjectReference(),
                            settings,
                            temporaryFilePath
                    );

            /*
             * res.append(mAFFTSupportTask);
             */
            result.add(
                    mafftSupportTask
            );

            return result;
        }

        /*
         * =================================================
         * 2. MAFFT FINISHED
         * =================================================
         */
        if (subTask == mafftSupportTask) {

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
             * C++:
             *
             * mAObject =
             * qobject_cast<MsaObject*>(
             *     currentDocument
             *       ->getObjects()
             *       .first()
             * );
             */
            Object object =
                    currentDocument
                            .getObjects()
                            .get(0);

            if (!(object instanceof MsaObject)) {

                setError(
                        "MA object not found: "
                                + loadDocumentTask
                                .getUrlString()
                );

                return result;
            }

            msaObject =
                    (MsaObject) object;

            /*
             * Get MAFFT result.
             */
            MultipleSequenceAlignment
                    resultMA =
                    mafftSupportTask
                            .getResultMA();

            if (resultMA == null) {

                setError(
                        "MAFFT result is null"
                );

                return result;
            }

            /*
             * C++:
             *
             * mAObject->updateGapModel(
             *     mAFFTSupportTask
             *       ->resultMA
             *       ->getRows()
             *       .toList()
             * );
             */
            msaObject.updateGapModel(
                    resultMA.getRows()
            );

            /*
             * C++:
             *
             * saveDocumentTask =
             *     new SaveDocumentTask(
             *         currentDocument,
             *         ...,
             *         settings.outputFilePath
             *     );
             */
            saveDocumentTask =
                    new SaveDocumentTask(
                            currentDocument,
                            ioAdapterFactory,
                            settings
                                    .getOutputFilePath()
                    );

            /*
             * res.append(saveDocumentTask);
             */
            result.add(
                    saveDocumentTask
            );

            return result;
        }

        /*
         * =================================================
         * 3. SAVE DOCUMENT FINISHED
         * =================================================
         */
        if (subTask == saveDocumentTask) {

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
             * C++:
             *
             * Task* openTask =
             *     AppContext::getProjectLoader()
             *       ->openWithProjectTask(
             *           settings.outputFilePath
             *       );
             */
            AbstractTask openTask =
                    projectLoader
                            .openWithProjectTask(
                                    settings
                                            .getOutputFilePath()
                            );

            /*
             * C++:
             *
             * if (openTask != nullptr) {
             *     res << openTask;
             * }
             */
            if (openTask != null) {

                result.add(openTask);
            }

            return result;
        }

        return result;
    }

    public Document getCurrentDocument() {
        return currentDocument;
    }

    public MsaObject getMsaObject() {
        return msaObject;
    }
}