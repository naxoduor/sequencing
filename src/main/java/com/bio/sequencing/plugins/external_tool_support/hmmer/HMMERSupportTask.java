package com.bio.sequencing.plugins.external_tool_support.hmmer;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

import java.util.*;

public class HMMERSupportTask
        extends AbstractTask {

    private final HMMERSettings settings;

    private HMMERExecutionTask executionTask;

    private ParseHMMERResultTask parseTask;

    private HMMERExecutionResult executionResult;

    private HMMERResultDocument resultDocument;

    public HMMERSupportTask(
            HMMERSettings settings) {

        super("HMMER support");

        this.settings = settings;
    }

    @Override
    protected void prepare() {

        executionTask =
                new HMMERExecutionTask(settings);

        executionTask.setSubtaskProgressWeight(80);

        addSubTask(executionTask);
    }

    @Override
    protected List<AbstractTask> onSubTaskFinished(
            AbstractTask subTask) {

        List<AbstractTask> result =
                new ArrayList<>();

        if (subTask == executionTask) {

            executionResult =
                    executionTask.getResult();

            parseTask =
                    new ParseHMMERResultTask(
                            settings,
                            executionResult);

            parseTask.setSubtaskProgressWeight(20);

            /*
             * This is equivalent to UGENE:
             *
             * res.append(parseTask);
             *
             * The scheduler will execute it after
             * executionTask completes.
             */

            result.add(parseTask);
        }

        else if (subTask == parseTask) {

            resultDocument =
                    parseTask.getResultDocument();
        }

        return result;
    }

    public HMMERExecutionResult getExecutionResult() {
        return executionResult;
    }

    public HMMERResultDocument getResultDocument() {
        return resultDocument;
    }
}