package com.bio.sequencing.plugins.external_tool_support.bwa;

import com.bio.sequencing.plugins.external_tool_support.bowtie.LoadReferenceDocumentTask;
import com.bio.sequencing.plugins.external_tool_support.bowtie.ReferenceObject;
import com.bio.sequencing.plugins.external_tool_support.cufflinks.BamObject;
import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;
import com.bio.sequencing.plugins.external_tool_support.mafft.Document;
import com.bio.sequencing.plugins.external_tool_support.mafft.ProjectLoader;
import com.bio.sequencing.plugins.external_tool_support.mafft.SaveDocumentTask;

import java.util.*;

public class BwaMemDocumentTask
        extends AbstractTask {

    private final BwaMemSettings settings;

    private LoadReferenceDocumentTask
            loadReferenceDocumentTask;

    private BwaMemSupportTask
            bwaMemSupportTask;

    private SaveDocumentTask
            saveDocumentTask;

    private Document currentDocument;

    private ReferenceObject referenceObject;

    public BwaMemDocumentTask(
            BwaMemSettings settings) {

        super("BWA-MEM Alignment");

        this.settings = settings;
    }

    @Override
    protected void prepare() {

        /*
         * Load reference.
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
    protected List<AbstractTask>
    onSubTaskFinished(
            AbstractTask subTask) {

        List<AbstractTask> result =
                new ArrayList<>();

        /*
         * -------------------------------------------
         * Reference loaded
         * -------------------------------------------
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
                                + "a ReferenceObject"
                );

                return result;
            }

            referenceObject =
                    (ReferenceObject) object;

            /*
             * Start BWA-MEM.
             */
            bwaMemSupportTask =
                    new BwaMemSupportTask(
                            settings
                    );

            bwaMemSupportTask
                    .setSubtaskProgressWeight(90);

            result.add(
                    bwaMemSupportTask
            );
        }

        /*
         * -------------------------------------------
         * BWA-MEM finished
         * -------------------------------------------
         */
        else if (subTask ==
                bwaMemSupportTask) {

            BwaMemResult bwaResult =
                    bwaMemSupportTask.getResult();

            if (bwaResult == null) {

                setError(
                        "BWA-MEM did not produce "
                                + "a result"
                );

                return result;
            }

            /*
             * Create BAM document.
             */
            Document resultDocument =
                    new Document(
                            "BWA-MEM Alignment"
                    );

            BamObject bamObject =
                    new BamObject(
                            bwaResult.getBamFile()
                    );

            resultDocument.addObject(
                    bamObject
            );

            /*
             * Save BAM document.
             */
            saveDocumentTask =
                    new SaveDocumentTask(
                            resultDocument,
                            bwaResult.getBamFile()
                    );

            saveDocumentTask
                    .setSubtaskProgressWeight(5);

            result.add(
                    saveDocumentTask
            );
        }

        /*
         * -------------------------------------------
         * BAM saved
         * -------------------------------------------
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