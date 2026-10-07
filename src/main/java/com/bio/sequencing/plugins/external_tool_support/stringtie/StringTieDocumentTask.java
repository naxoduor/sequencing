package com.bio.sequencing.plugins.external_tool_support.stringtie;

import com.bio.sequencing.plugins.external_tool_support.cufflinks.BamObject;
import com.bio.sequencing.plugins.external_tool_support.cufflinks.LoadBamDocumentTask;
import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;
import com.bio.sequencing.plugins.external_tool_support.mafft.Document;
import com.bio.sequencing.plugins.external_tool_support.mafft.SaveDocumentTask;

import java.util.*;

public class StringTieDocumentTask
        extends AbstractTask {

    private final StringTieSettings settings;

    private LoadBamDocumentTask loadDocumentTask;

    private StringTieSupportTask stringTieSupportTask;

    private SaveDocumentTask saveDocumentTask;

    private Document currentDocument;

    private BamObject bamObject;

    public StringTieDocumentTask(
            StringTieSettings settings) {

        super("StringTie Transcript Assembly");

        this.settings = settings;
    }

    @Override
    protected void prepare() {

        /*
         * First load the BAM document.
         */
        loadDocumentTask =
                new LoadBamDocumentTask(
                        settings.getInputBamFile()
                );

        loadDocumentTask
                .setSubtaskProgressWeight(10);

        addSubTask(loadDocumentTask);
    }

    @Override
    protected List<AbstractTask> onSubTaskFinished(
            AbstractTask subTask) {

        List<AbstractTask> result =
                new ArrayList<>();

        /*
         * ------------------------------------------------
         * BAM loaded
         * ------------------------------------------------
         */
        if (subTask == loadDocumentTask) {

            currentDocument =
                    loadDocumentTask.takeDocument();

            if (currentDocument == null) {

                setError(
                        "Failed to load BAM document"
                );

                return result;
            }

            if (currentDocument.getObjects().size() != 1) {

                setError(
                        "Expected exactly one BAM object"
                );

                return result;
            }

            Object object =
                    currentDocument
                            .getObjects()
                            .get(0);

            if (!(object instanceof BamObject)) {

                setError(
                        "Loaded object is not a BAM object"
                );

                return result;
            }

            bamObject =
                    (BamObject) object;

            /*
             * ------------------------------------------------
             * Create StringTie support task
             * ------------------------------------------------
             */

            stringTieSupportTask =
                    new StringTieSupportTask(settings);

            stringTieSupportTask
                    .setSubtaskProgressWeight(90);

            result.add(stringTieSupportTask);

        }

        /*
         * ------------------------------------------------
         * StringTie completed
         * ------------------------------------------------
         */
        else if (subTask == stringTieSupportTask) {

            StringTieResult stringTieResult =
                    stringTieSupportTask.getResult();

            if (stringTieResult == null) {

                setError(
                        "StringTie did not produce a result"
                );

                return result;
            }

            /*
             * Create a result document.
             */
            Document resultDocument =
                    new Document(
                            "StringTie Results"
                    );

            resultDocument.addObject(
                    new GtfObject(
                            stringTieResult
                                    .getTranscriptGtf()
                    )
            );

            /*
             * Save result.
             */
            saveDocumentTask =
                    new SaveDocumentTask(
                            resultDocument,
                            settings.getOutputDirectory()
                                    + "/stringtie-results.gtf"
                    );

            saveDocumentTask
                    .setSubtaskProgressWeight(5);

            result.add(saveDocumentTask);
        }

        /*
         * ------------------------------------------------
         * Result saved
         * ------------------------------------------------
         */
        else if (subTask == saveDocumentTask) {

            AbstractTask openTask =
                    ProjectLoader
                            .openWithProjectTask(
                                    settings
                                            .getOutputDirectory()
                                            + "/stringtie-results.gtf"
                            );

            if (openTask != null) {
                result.add(openTask);
            }
        }

        return result;
    }
}