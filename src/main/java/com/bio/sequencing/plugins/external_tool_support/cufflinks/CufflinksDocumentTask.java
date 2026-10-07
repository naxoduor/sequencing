package com.bio.sequencing.plugins.external_tool_support.cufflinks;

import com.bio.sequencing.plugins.external_tool_support.mafft.*;

import java.util.ArrayList;
import java.util.List;

public class CufflinksDocumentTask
        extends AbstractTask {

    private final CufflinksSettings settings;

    private final ProjectLoader projectLoader;

    private LoadBamDocumentTask
            loadBamDocumentTask;

    private CufflinksSupportTask
            cufflinksSupportTask;

    private SaveDocumentTask
            saveDocumentTask;

    private Document currentDocument;

    private BamObject bamObject;

    public CufflinksDocumentTask(
            CufflinksSettings settings,
            ProjectLoader projectLoader
    ) {

        super("Cufflinks document task");

        this.settings =
                settings;

        this.projectLoader =
                projectLoader;
    }

    @Override
    protected void prepare() {

        loadBamDocumentTask =
                new LoadBamDocumentTask(
                        settings.getInputBamFile()
                );

        addSubTask(
                loadBamDocumentTask
        );
    }

    @Override
    protected List<AbstractTask>
    onSubTaskFinished(
            AbstractTask subTask
    ) {

        List<AbstractTask> result =
                new ArrayList<>();

        /*
         * -------------------------------------------
         * LOAD BAM
         * -------------------------------------------
         */
        if (subTask ==
                loadBamDocumentTask) {

            if (loadBamDocumentTask.hasError()) {

                setError(
                        "Failed loading BAM: "
                                + loadBamDocumentTask
                                .getError()
                );

                return result;
            }

            currentDocument =
                    loadBamDocumentTask
                            .takeDocument();

            if (currentDocument == null) {

                setError(
                        "Failed loading BAM document: "
                                + settings
                                .getInputBamFile()
                );

                return result;
            }

            /*
             * UGENE-style validation.
             */
            if (currentDocument
                    .getObjects()
                    .size() != 1) {

                setError(
                        "Expected exactly one "
                                + "BAM object"
                );

                return result;
            }

            Object object =
                    currentDocument
                            .getObjects()
                            .get(0);

            if (!(object instanceof BamObject)) {

                setError(
                        "BAM object not found"
                );

                return result;
            }

            bamObject =
                    (BamObject) object;

            /*
             * Launch Cufflinks.
             */
            cufflinksSupportTask =
                    new CufflinksSupportTask(
                            bamObject.getBamFile(),
                            new GObjectReference(),
                            settings
                    );

            result.add(
                    cufflinksSupportTask
            );
        }

        /*
         * -------------------------------------------
         * CUFFLINKS FINISHED
         * -------------------------------------------
         */
        else if (subTask ==
                cufflinksSupportTask) {

            if (cufflinksSupportTask
                    .hasError()) {

                setError(
                        "Cufflinks failed: "
                                + cufflinksSupportTask
                                .getError()
                );

                return result;
            }

            CufflinksResult
                    cufflinksResult =
                    cufflinksSupportTask
                            .getResult();

            if (cufflinksResult == null) {

                setError(
                        "No Cufflinks result"
                );

                return result;
            }

            /*
             * At this point the generated
             * transcript files are available.
             *
             * In a complete application you could
             * attach the result to the BAM document
             * or create a separate annotation object.
             */

            System.out.println(
                    "Cufflinks transcripts: "
                            + cufflinksResult
                            .getTranscriptsGtf()
            );

            /*
             * If the document model supports result
             * objects, attach one here.
             */
            currentDocument.addObject(
                    cufflinksResult
            );

            /*
             * Save the result/document.
             */
            saveDocumentTask =
                    new SaveDocumentTask(
                            currentDocument,
                            null,
                            settings
                                    .getOutputDirectory()
                                    + "/cufflinks-result"
                    );

            result.add(
                    saveDocumentTask
            );
        }

        /*
         * -------------------------------------------
         * SAVE FINISHED
         * -------------------------------------------
         */
        else if (subTask ==
                saveDocumentTask) {

            if (saveDocumentTask.hasError()) {

                setError(
                        "Failed saving Cufflinks result: "
                                + saveDocumentTask
                                .getError()
                );

                return result;
            }

            AbstractTask openTask =
                    projectLoader
                            .openWithProjectTask(
                                    settings
                                            .getOutputDirectory()
                            );

            if (openTask != null) {
                result.add(openTask);
            }
        }

        return result;
    }
}