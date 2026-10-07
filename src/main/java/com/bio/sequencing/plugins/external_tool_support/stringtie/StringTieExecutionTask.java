package com.bio.sequencing.plugins.external_tool_support.stringtie;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

public class StringTieExecutionTask
        extends AbstractTask {

    private final StringTieSettings settings;

    private StringTieResult result;

    private String error;

    public StringTieExecutionTask(
            StringTieSettings settings) {

        super("Execute StringTie");

        this.settings = settings;
    }

    @Override
    protected void execute() {

        try {

            StringTieExecutor executor =
                    new StringTieExecutor();

            result = executor.execute(settings);

        } catch (Exception e) {

            error = e.getMessage();

            setError(
                    "StringTie execution failed: "
                            + e.getMessage()
            );
        }
    }

    public StringTieResult getResult() {
        return result;
    }

    public String getError() {
        return error;
    }
}