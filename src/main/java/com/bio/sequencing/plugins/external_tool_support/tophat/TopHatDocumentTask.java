package com.bio.sequencing.plugins.external_tool_support.tophat;

import com.bio.sequencing.plugins.external_tool_support.cufflinks.BamObject;
import com.bio.sequencing.plugins.external_tool_support.mafft.*;

import java.util.ArrayList;
import java.util.List;

public class TopHatDocumentTask
        extends AbstractTask {

    private final TopHatSettings settings;

    private final ProjectLoader projectLoader;

    private LoadReferenceDocumentTask
            loadReferenceDocumentTask;

    private TopHatSupportTask
            topHatSupportTask;

    private Document currentDocument;

    private TopHatResult topHatResult;

    private SaveDocumentTask
            saveDocumentTask;

    public TopHatDocumentTask(
            TopHatSettings settings,
            ProjectLoader projectLoader
    ) {

        super("TopHat document task");

        this.settings =
                settings;

        this.projectLoader =
                projectLoader;
    }

    @Override
    protected void prepare() {

        /*
         * We don't necessarily need to load the
         * BAM because TopHat consumes FASTQ.
         *
         * We load/validate the reference document
         * first.
         */
        loadReferenceDocumentTask =
                new LoadReferenceDocumentTask(
                        settings.getReferenceIndex()
                );

        addSubTask(
                loadReferenceDocumentTask
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
         * -----------------------------------------
         * REFERENCE LOADED
         * -----------------------------------------
         */
        if (subTask ==
                loadReferenceDocumentTask) {

            if (loadReferenceDocumentTask
                    .hasError()) {

                setError(
                        "Failed loading reference: "
                                + loadReferenceDocumentTask
                                .getError()
                );

                return result;
            }

            currentDocument =
                    loadReferenceDocumentTask
                            .takeDocument();

            if (currentDocument == null) {

                setError(
                        "Reference document is null"
                );

                return result;
            }

            /*
             * Launch TopHat.
             */
            topHatSupportTask =
                    new TopHatSupportTask(
                            settings.getRead1File(),
                            settings.getRead2File(),
                            settings.getReferenceIndex(),
                            new GObjectReference(),
                            settings
                    );

            result.add(
                    topHatSupportTask
            );
        }

        /*
         * -----------------------------------------
         * TOPHAT FINISHED
         * -----------------------------------------
         */
        else if (subTask ==
                topHatSupportTask) {

            if (topHatSupportTask
                    .hasError()) {

                setError(
                        "TopHat failed: "
                                + topHatSupportTask
                                .getError()
                );

                return result;
            }

            topHatResult =
                    topHatSupportTask
                            .getResult();

            if (topHatResult == null) {

                setError(
                        "No TopHat result"
                );

                return result;
            }

            /*
             * At this point:
             *
             * accepted_hits.bam
             * junctions.bed
             * etc.
             *
             * are available.
             */

            System.out.println(
                    "TopHat BAM: "
                            + topHatResult
                            .getAcceptedHitsBam()
            );

            /*
             * Create a result document.
             */
            Document resultDocument =
                    new Document(
                            topHatResult
                                    .getOutputDirectory()
                    );

            BamObject bamObject =
                    new BamObject(
                            topHatResult
                                    .getAcceptedHitsBam()
                    );

            resultDocument.addObject(
                    bamObject
            );

            /*
             * SaveDocumentTask in this example
             * represents persistence/registration
             * of the result.
             */
            saveDocumentTask =
                    new SaveDocumentTask(
                            resultDocument,
                            null,
                            topHatResult
                                    .getOutputDirectory()
                    );

            result.add(
                    saveDocumentTask
            );
        }

        /*
         * -----------------------------------------
         * SAVE FINISHED
         * -----------------------------------------
         */
        else if (subTask ==
                saveDocumentTask) {

            if (saveDocumentTask.hasError()) {

                setError(
                        "Failed saving TopHat result: "
                                + saveDocumentTask
                                .getError()
                );

                return result;
            }

            AbstractTask openTask =
                    projectLoader
                            .openWithProjectTask(
                                    topHatResult
                                            .getOutputDirectory()
                            );

            if (openTask != null) {

                result.add(
                        openTask
                );
            }
        }

        return result;
    }

    public TopHatResult getTopHatResult() {
        return topHatResult;
    }
}