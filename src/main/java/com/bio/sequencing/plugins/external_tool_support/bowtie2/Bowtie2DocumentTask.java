package com.bio.sequencing.plugins.external_tool_support.bowtie2;

import com.bio.sequencing.plugins.external_tool_support.bowtie.LoadReferenceDocumentTask;
import com.bio.sequencing.plugins.external_tool_support.bowtie.ReferenceObject;
import com.bio.sequencing.plugins.external_tool_support.cufflinks.BamObject;
import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;
import com.bio.sequencing.plugins.external_tool_support.mafft.Document;
import com.bio.sequencing.plugins.external_tool_support.mafft.ProjectLoader;
import com.bio.sequencing.plugins.external_tool_support.mafft.SaveDocumentTask;

import java.util.*;

public class Bowtie2DocumentTask
        extends AbstractTask {

    private final Bowtie2Settings settings;

    private LoadReferenceDocumentTask
            loadReferenceDocumentTask;

    private Bowtie2SupportTask
            bowtie2SupportTask;

    private SaveDocumentTask
            saveDocumentTask;

    private Document currentDocument;

    private ReferenceObject referenceObject;

    public Bowtie2DocumentTask(
            Bowtie2Settings settings) {

        super("Bowtie2 Alignment");

        this.settings = settings;
    }

    @Override
    protected void prepare() {

        /*
         * First load the reference/index.
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
                        "Failed to load reference"
                );

                return result;
            }

            if (currentDocument
                    .getObjects()
                    .size() != 1) {

                setError(
                        "Expected one reference object"
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
                                + "a ReferenceObject"
                );

                return result;
            }

            referenceObject =
                    (ReferenceObject) object;

            /*
             * -----------------------------------------
             * Start Bowtie2
             * -----------------------------------------
             */
            bowtie2SupportTask =
                    new Bowtie2SupportTask(
                            settings
                    );

            bowtie2SupportTask
                    .setSubtaskProgressWeight(90);

            result.add(
                    bowtie2SupportTask
            );
        }

        /*
         * ---------------------------------------------
         * Bowtie2 + SAM -> BAM completed
         * ---------------------------------------------
         */
        else if (subTask ==
                bowtie2SupportTask) {

            Bowtie2Result bowtie2Result =
                    bowtie2SupportTask.getResult();

            if (bowtie2Result == null) {

                setError(
                        "Bowtie2 did not produce "
                                + "a result"
                );

                return result;
            }

            /*
             * Create result document.
             */
            Document resultDocument =
                    new Document(
                            "Bowtie2 Alignment"
                    );

            /*
             * Add BAM object.
             */
            BamObject bamObject =
                    new BamObject(
                            bowtie2Result
                                    .getBamFile()
                    );

            resultDocument.addObject(
                    bamObject
            );

            /*
             * Save BAM.
             */
            saveDocumentTask =
                    new SaveDocumentTask(
                            resultDocument,
                            bowtie2Result
                                    .getBamFile()
                    );

            saveDocumentTask
                    .setSubtaskProgressWeight(5);

            result.add(
                    saveDocumentTask
            );
        }

        /*
         * ---------------------------------------------
         * BAM saved
         * ---------------------------------------------
         */
        else if (subTask ==
                saveDocumentTask) {

            AbstractTask openTask =
                    ProjectLoader
                            .openWithProjectTask(
                                    settings
                                            .getOutputBamFile()
                            );

            if (openTask != null) {

                result.add(
                        openTask
                );
            }
        }

        return result;
    }
}