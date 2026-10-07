package com.bio.sequencing.plugins.external_tool_support.bowtie;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;
import com.bio.sequencing.plugins.external_tool_support.mafft.Document;
import com.bio.sequencing.plugins.external_tool_support.mafft.ProjectLoader;
import com.bio.sequencing.plugins.external_tool_support.mafft.SaveDocumentTask;
import com.bio.sequencing.plugins.external_tool_support.tophat.LoadReferenceDocumentTask;

import java.util.*;

public class BowtieDocumentTask
        extends AbstractTask {

    private final BowtieSettings settings;

    private LoadReferenceDocumentTask
            loadReferenceDocumentTask;

    private BowtieSupportTask
            bowtieSupportTask;

    private SaveDocumentTask
            saveDocumentTask;

    private Document currentDocument;

    private ReferenceObject referenceObject;

    public BowtieDocumentTask(
            BowtieSettings settings) {

        super("Bowtie Alignment");

        this.settings = settings;
    }

    @Override
    protected void prepare() {

        /*
         * Load reference/index.
         */
        loadReferenceDocumentTask =
                new LoadReferenceDocumentTask(
                        settings.getReferenceIndex()
                );

        loadReferenceDocumentTask
                .setSubtaskProgressWeight(10);

        addSubTask(
                loadReferenceDocumentTask
        );
    }

    @Override
    protected List<AbstractTask> onSubTaskFinished(
            AbstractTask subTask) {

        List<AbstractTask> result =
                new ArrayList<>();

        /*
         * ---------------------------------------------
         * Reference loaded
         * ---------------------------------------------
         */
        if (subTask ==
                loadReferenceDocumentTask) {

            currentDocument =
                    loadReferenceDocumentTask
                            .takeDocument();

            if (currentDocument == null) {

                setError(
                        "Failed to load reference document"
                );

                return result;
            }

            if (currentDocument
                    .getObjects()
                    .size() != 1) {

                setError(
                        "Expected exactly one "
                                + "reference object"
                );

                return result;
            }

            Object object =
                    currentDocument
                            .getObjects()
                            .get(0);

            if (!(object instanceof ReferenceObject)) {

                setError(
                        "Loaded object is not "
                                + "a reference object"
                );

                return result;
            }

            referenceObject =
                    (ReferenceObject) object;

            /*
             * -----------------------------------------
             * Create Bowtie support task.
             * -----------------------------------------
             */
            bowtieSupportTask =
                    new BowtieSupportTask(
                            settings
                    );

            bowtieSupportTask
                    .setSubtaskProgressWeight(90);

            result.add(
                    bowtieSupportTask
            );
        }

        /*
         * ---------------------------------------------
         * Bowtie finished
         * ---------------------------------------------
         */
        else if (subTask ==
                bowtieSupportTask) {

            BowtieResult bowtieResult =
                    bowtieSupportTask.getResult();

            if (bowtieResult == null) {

                setError(
                        "Bowtie did not produce "
                                + "a result"
                );

                return result;
            }

            /*
             * Create alignment document.
             */
            Document resultDocument =
                    new Document(
                            "Bowtie Alignment"
                    );

            SamObject samObject =
                    new SamObject(
                            bowtieResult.getSamFile()
                    );

            resultDocument.addObject(
                    samObject
            );

            /*
             * Save.
             */
            saveDocumentTask =
                    new SaveDocumentTask(
                            resultDocument,
                            bowtieResult.getSamFile()
                    );

            saveDocumentTask
                    .setSubtaskProgressWeight(5);

            result.add(
                    saveDocumentTask
            );
        }

        /*
         * ---------------------------------------------
         * Save finished
         * ---------------------------------------------
         */
        else if (subTask ==
                saveDocumentTask) {

            AbstractTask openTask =
                    ProjectLoader
                            .openWithProjectTask(
                                    settings
                                            .getOutputSamFile()
                            );

            if (openTask != null) {

                result.add(openTask);
            }
        }

        return result;
    }
}