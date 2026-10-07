package com.bio.sequencing.plugins.external_tool_support.clustalw;

import com.bio.sequencing.plugins.external_tool_support.mafft.*;

import java.util.ArrayList;
import java.util.List;

public class ClustalWDocumentTask
        extends AbstractTask {

    private final ClustalWSettings settings;

    private final String temporaryInputFile;

    private final String temporaryOutputFile;

    private final ProjectLoader projectLoader;

    private final IOAdapterFactory ioAdapterFactory;

    private LoadDocumentTask loadDocumentTask;

    private ClustalWSupportTask
            clustalWSupportTask;

    private SaveDocumentTask
            saveDocumentTask;

    private Document currentDocument;

    private MsaObject msaObject;

    public ClustalWDocumentTask(
            ClustalWSettings settings,
            String temporaryInputFile,
            String temporaryOutputFile,
            ProjectLoader projectLoader,
            IOAdapterFactory ioAdapterFactory) {

        super("ClustalW Document Task");

        this.settings = settings;

        this.temporaryInputFile =
                temporaryInputFile;

        this.temporaryOutputFile =
                temporaryOutputFile;

        this.projectLoader =
                projectLoader;

        this.ioAdapterFactory =
                ioAdapterFactory;
    }

    @Override
    protected void prepare() {

        /*
         * Load input document.
         */
        loadDocumentTask =
                new LoadDocumentTask(
                        settings.getInputFilePath()
                );

        /*
         * Equivalent:
         *
         * res.append(loadDocumentTask);
         */
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
         * ==================================================
         * 1. LOAD DOCUMENT FINISHED
         * ==================================================
         */
        if (subTask ==
                loadDocumentTask) {

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
             * Equivalent:
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
             * UGENE:
             *
             * currentDocument
             *     ->getObjects()
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
             * Get first object.
             */
            Object object =
                    currentDocument
                            .getObjects()
                            .get(0);

            /*
             * Equivalent to:
             *
             * qobject_cast<MsaObject*>(...)
             */
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
             * Create ClustalW support task.
             */
            clustalWSupportTask =
                    new ClustalWSupportTask(
                            msaObject.getAlignment(),
                            new GObjectReference(),
                            settings,
                            temporaryInputFile,
                            temporaryOutputFile
                    );

            /*
             * Equivalent:
             *
             * res.append(
             *     clustalWSupportTask
             * );
             */
            result.add(
                    clustalWSupportTask
            );

            return result;
        }

        /*
         * ==================================================
         * 2. CLUSTALW FINISHED
         * ==================================================
         */
        if (subTask ==
                clustalWSupportTask) {

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
             * Retrieve MSA object.
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
             * Get ClustalW result.
             */
            MultipleSequenceAlignment
                    resultMA =
                    clustalWSupportTask
                            .getResultMA();

            if (resultMA == null) {

                setError(
                        "ClustalW result is null"
                );

                return result;
            }

            /*
             * Equivalent:
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
             * Save current document.
             */
            saveDocumentTask =
                    new SaveDocumentTask(
                            currentDocument,
                            ioAdapterFactory,
                            settings
                                    .getOutputFilePath()
                    );

            /*
             * Equivalent:
             *
             * res.append(saveDocumentTask);
             */
            result.add(
                    saveDocumentTask
            );

            return result;
        }

        /*
         * ==================================================
         * 3. SAVE DOCUMENT FINISHED
         * ==================================================
         */
        if (subTask ==
                saveDocumentTask) {

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
             * Equivalent:
             *
             * Task* openTask =
             *     AppContext::
             *         getProjectLoader()
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

            /*
             * Equivalent:
             *
             * if (openTask != nullptr) {
             *     res << openTask;
             * }
             */
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