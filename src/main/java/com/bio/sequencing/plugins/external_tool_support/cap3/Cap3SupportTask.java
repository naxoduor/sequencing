package com.bio.sequencing.plugins.external_tool_support.cap3;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

import java.util.*;

public class Cap3SupportTask
        extends AbstractTask {

    private final Cap3Settings settings;

    private Cap3ExecutionTask executionTask;

    private ParseCap3ResultTask parseTask;

    private Cap3Result result;

    private AssemblyDocument assemblyDocument;

    public Cap3SupportTask(
            Cap3Settings settings) {

        super("CAP3 support");

        this.settings = settings;
    }

    @Override
    protected void prepare() {

        executionTask =
                new Cap3ExecutionTask(settings);

        executionTask.setSubtaskProgressWeight(80);

        addSubTask(executionTask);
    }

    @Override
    protected List<AbstractTask> onSubTaskFinished(
            AbstractTask subTask) {

        List<AbstractTask> resultTasks =
                new ArrayList<>();

        if (subTask == executionTask) {

            result =
                    executionTask.getResult();

            parseTask =
                    new ParseCap3ResultTask(result);

            parseTask.setSubtaskProgressWeight(20);

            resultTasks.add(parseTask);
        }

        else if (subTask == parseTask) {

            assemblyDocument =
                    parseTask.getAssemblyDocument();
        }

        return resultTasks;
    }

    public Cap3Result getResult() {
        return result;
    }

    public AssemblyDocument getAssemblyDocument() {
        return assemblyDocument;
    }
}