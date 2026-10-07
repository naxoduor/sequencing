package com.bio.sequencing.plugins.external_tool_support.bedtools;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;
import com.bio.sequencing.plugins.external_tool_support.mafft.Document;
import com.bio.sequencing.plugins.external_tool_support.mafft.ProjectLoader;
import com.bio.sequencing.plugins.external_tool_support.mafft.SaveDocumentTask;

import java.util.ArrayList;
import java.util.List;

public class BedToolsDocumentTask
        extends AbstractTask {

    private final BedToolsSettings settings;

    private BedToolsSupportTask
            bedToolsSupportTask;

    private SaveDocumentTask
            saveDocumentTask;

    public BedToolsDocumentTask(
            BedToolsSettings settings) {

        super("BEDTools Analysis");

        this.settings = settings;
    }

    @Override
    protected void prepare() {

        /*
         * BEDTools does not necessarily require
         * loading a biological document first.
         *
         * The input file can be passed directly
         * to BEDTools.
         */

        bedToolsSupportTask =
                new BedToolsSupportTask(
                        settings
                );

        bedToolsSupportTask
                .setSubtaskProgressWeight(95);

        addSubTask(
                bedToolsSupportTask
        );
    }

    @Override
    protected List<AbstractTask>
    onSubTaskFinished(
            AbstractTask subTask) {

        List<AbstractTask> result =
                new ArrayList<>();

        /*
         * -----------------------------------------
         * BEDTools finished.
         * -----------------------------------------
         */
        if (subTask ==
                bedToolsSupportTask) {

            BedToolsResult
                    bedToolsResult =
                    bedToolsSupportTask
                            .getResult();

            if (bedToolsResult == null) {

                setError(
                        "BEDTools did not "
                                + "produce a result"
                );

                return result;
            }

            /*
             * Create result document.
             */
            Document resultDocument =
                    new Document(
                            "BEDTools Result"
                    );

            BedObject bedObject =
                    new BedObject(
                            bedToolsResult
                                    .getOutputFile()
                    );

            resultDocument.addObject(
                    bedObject
            );

            /*
             * Save document.
             */
            saveDocumentTask =
                    new SaveDocumentTask(
                            resultDocument,
                            bedToolsResult
                                    .getOutputFile()
                    );

            saveDocumentTask
                    .setSubtaskProgressWeight(5);

            result.add(
                    saveDocumentTask
            );
        }

        /*
         * -----------------------------------------
         * Save finished.
         * -----------------------------------------
         */
        else if (subTask ==
                saveDocumentTask) {

            AbstractTask openTask =
                    ProjectLoader
                            .openWithProjectTask(
                                    settings
                                            .getOutputFile()
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