package com.bio.sequencing.plugins.external_tool_support.mafft;

public class OpenWithProjectTask
        extends AbstractTask {

    private final String path;

    public OpenWithProjectTask(
            String path) {

        super("Open document in project");

        this.path = path;
    }

    @Override
    protected void execute() {

        System.out.println(
                "Opening document in project: "
                        + path
        );

        /*
         * In the real application:
         *
         * project.addDocument(...)
         * project.addObject(...)
         */
    }
}