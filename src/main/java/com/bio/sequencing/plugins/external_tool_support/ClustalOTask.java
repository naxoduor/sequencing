package com.bio.sequencing.plugins.external_tool_support;

import java.util.List;

public class ClustalOTask extends Task {

    private final String inputFile;
    private final String outputFile;
    private final String executable;

    private ExternalToolRunTask externalTask;

    public ClustalOTask(
            String executable,
            String inputFile,
            String outputFile) {

        super("Clustal Omega Alignment");

        this.executable = executable;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    @Override
    protected void prepare() throws Exception {

        externalTask = new ExternalToolRunTask(
                executable,
                List.of(
                        "-i", inputFile,
                        "-o", outputFile,
                        "--force"
                )
        );
    }

    @Override
    protected void execute() throws Exception {

        externalTask.run();

        if (externalTask.getStatus() == TaskStatus.FAILED) {
            throw externalTask.getError();
        }
    }
}