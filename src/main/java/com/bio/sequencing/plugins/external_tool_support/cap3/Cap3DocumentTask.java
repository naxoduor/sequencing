package com.bio.sequencing.plugins.external_tool_support.cap3;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

import java.util.*;

public class Cap3DocumentTask
        extends AbstractTask {

    private final Cap3Settings settings;

    private Cap3SupportTask supportTask;

    private AssemblyDocument assemblyDocument;

    private Cap3Result result;

    public Cap3DocumentTask(
            Cap3Settings settings) {

        super("CAP3 document task");

        this.settings = settings;
    }

    @Override
    protected void prepare() {

        validateSettings();

        supportTask =
                new Cap3SupportTask(settings);

        supportTask.setSubtaskProgressWeight(100);

        addSubTask(supportTask);
    }

    @Override
    protected List<AbstractTask> onSubTaskFinished(
            AbstractTask subTask) {

        List<AbstractTask> tasks =
                new ArrayList<>();

        if (subTask == supportTask) {

            result =
                    supportTask.getResult();

            assemblyDocument =
                    supportTask.getAssemblyDocument();

            /*
             * Next UGENE-style tasks can be
             * scheduled here:
             *
             * SaveAssemblyDocumentTask
             * OpenWithProjectTask
             */

            /*
             * Example:
             *
             * SaveAssemblyDocumentTask saveTask =
             *     new SaveAssemblyDocumentTask(
             *         assemblyDocument,
             *         settings.getOutputDirectory());
             *
             * tasks.add(saveTask);
             */
        }

        return tasks;
    }

    private void validateSettings() {

        if (settings.getInputFasta() == null
                || settings.getInputFasta().isBlank()) {

            throw new IllegalArgumentException(
                    "CAP3 input FASTA is required");
        }

        if (settings.getOutputDirectory() == null
                || settings.getOutputDirectory().isBlank()) {

            throw new IllegalArgumentException(
                    "CAP3 output directory is required");
        }
    }

    public AssemblyDocument getAssemblyDocument() {
        return assemblyDocument;
    }

    public Cap3Result getResult() {
        return result;
    }
}