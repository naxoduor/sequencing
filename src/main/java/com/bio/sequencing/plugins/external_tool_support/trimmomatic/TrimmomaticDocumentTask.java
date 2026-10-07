package com.bio.sequencing.plugins.external_tool_support.trimmomatic;

import com.bio.sequencing.plugins.external_tool_support.mafft.*;

import java.util.ArrayList;
import java.util.List;

public class TrimmomaticDocumentTask
        extends AbstractTask {

    private final TrimmomaticSettings settings;

    private final ProjectLoader projectLoader;

    private TrimmomaticSupportTask
            trimmomaticSupportTask;

    private SaveDocumentTask
            saveDocumentTask;

    private TrimmomaticResult
            trimmomaticResult;

    private Document resultDocument;

    public TrimmomaticDocumentTask(
            TrimmomaticSettings settings,
            ProjectLoader projectLoader
    ) {

        super("Trimmomatic document task");

        this.settings =
                settings;

        this.projectLoader =
                projectLoader;
    }

    @Override
    protected void prepare() {

        /*
         * Unlike an MSA task, there isn't necessarily
         * a document object that needs to be loaded.
         *
         * FASTQ files can be passed directly to
         * Trimmomatic.
         */
        if (settings.getInputRead1() == null
                || settings.getInputRead1().isBlank()) {

            setError(
                    "Input FASTQ is required"
            );

            return;
        }

        trimmomaticSupportTask =
                new TrimmomaticSupportTask(
                        settings,
                        new GObjectReference()
                );

        addSubTask(
                trimmomaticSupportTask
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
         * TRIMMOMATIC FINISHED
         * -----------------------------------------
         */
        if (subTask ==
                trimmomaticSupportTask) {

            if (trimmomaticSupportTask
                    .hasError()) {

                setError(
                        "Trimmomatic failed: "
                                + trimmomaticSupportTask
                                .getError()
                );

                return result;
            }

            trimmomaticResult =
                    trimmomaticSupportTask
                            .getResult();

            if (trimmomaticResult == null) {

                setError(
                        "No Trimmomatic result"
                );

                return result;
            }

            /*
             * Create a result document.
             */
            resultDocument =
                    new Document(
                            settings
                                    .getOutputPairedRead1()
                    );

            /*
             * Add processed FASTQ files.
             */
            if (trimmomaticResult
                    .getPairedRead1() != null) {

                resultDocument.addObject(
                        trimmomaticResult
                                .getPairedRead1()
                );
            }

            if (trimmomaticResult
                    .getPairedRead2() != null) {

                resultDocument.addObject(
                        trimmomaticResult
                                .getPairedRead2()
                );
            }

            if (trimmomaticResult
                    .getUnpairedRead1() != null) {

                resultDocument.addObject(
                        trimmomaticResult
                                .getUnpairedRead1()
                );
            }

            if (trimmomaticResult
                    .getUnpairedRead2() != null) {

                resultDocument.addObject(
                        trimmomaticResult
                                .getUnpairedRead2()
                );
            }

            /*
             * Persist/collect the result.
             */
            saveDocumentTask =
                    new SaveDocumentTask(
                            resultDocument,
                            null,
                            settings
                                    .getOutputPairedRead1()
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
                        "Failed saving Trimmomatic result: "
                                + saveDocumentTask
                                .getError()
                );

                return result;
            }

            AbstractTask openTask =
                    projectLoader
                            .openWithProjectTask(
                                    settings
                                            .getOutputPairedRead1()
                            );

            if (openTask != null) {

                result.add(
                        openTask
                );
            }
        }

        return result;
    }

    public TrimmomaticResult
    getTrimmomaticResult() {

        return trimmomaticResult;
    }
}