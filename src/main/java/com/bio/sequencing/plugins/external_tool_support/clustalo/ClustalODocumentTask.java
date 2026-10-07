package com.bio.sequencing.plugins.external_tool_support.clustalo;

import com.bio.sequencing.plugins.external_tool_support.mafft.*;

import java.util.ArrayList;
import java.util.List;

public class ClustalODocumentTask
        extends AbstractTask {

    private final ClustalOSettings settings;

    private final String temporaryFilePath;

    private final ProjectLoader projectLoader;

    private final IOAdapterFactory ioAdapterFactory;

    private LoadDocumentTask loadDocumentTask;

    private ClustalOSupportTask
            clustalOSupportTask;

    private SaveDocumentTask saveDocumentTask;

    private Document currentDocument;

    private MsaObject msaObject;

    public ClustalODocumentTask(
            ClustalOSettings settings,
            String temporaryFilePath,
            ProjectLoader projectLoader,
            IOAdapterFactory ioAdapterFactory) {

        super("Clustal Omega Document Task");

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
         * C++ equivalent:
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
         * 1. LOAD DOCUMENT
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
                    loadDocumentTask
                            .takeDocument();

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
             *     .length() == 1
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
             * Get MSA object.
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
             * Launch Clustal Omega support task.
             */
            clustalOSupportTask =
                    new ClustalOSupportTask(
                            msaObject.getAlignment(),
                            new GObjectReference(),
                            settings,
                            temporaryFilePath
                    );

            /*
             * C++:
             *
             * res.append(clustalOSupportTask);
             */
            result.add(
                    clustalOSupportTask
            );

            return result;
        }

        /*
         * =================================================
         * 2. CLUSTAL O FINISHED
         * =================================================
         */
        if (subTask == clustalOSupportTask) {

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
             * Retrieve MSA object from document.
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
             * Get Clustal Omega result.
             */
            MultipleSequenceAlignment
                    resultMA =
                    clustalOSupportTask
                            .getResultMA();

            if (resultMA == null) {

                setError(
                        "Clustal Omega result "
                                + "is null"
                );

                return result;
            }

            /*
             * UGENE equivalent:
             *
             * mAObject->updateGapModel(
             *     mAFFTSupportTask
             *         ->resultMA
             *         ->getRows()
             *         .toList()
             * );
             */
            msaObject.updateGapModel(
                    resultMA.getRows()
            );

            /*
             * Save modified document.
             */
            saveDocumentTask =
                    new SaveDocumentTask(
                            currentDocument,
                            ioAdapterFactory,
                            settings
                                    .getOutputFilePath()
                    );

            /*
             * C++:
             *
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
             *         ->openWithProjectTask(
             *              settings.outputFilePath
             *         );
             */
            AbstractTask openTask =
                    projectLoader
                            .openWithProjectTask(
                                    settings
                                            .getOutputFilePath()
                            );

            if (openTask != null) {

                result.add(openTask);
            }
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