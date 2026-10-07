package com.bio.sequencing.plugins.external_tool_support.mafft;

public class SaveDocumentTask
        extends AbstractTask {

    private final Document document;

    private final IOAdapterFactory adapterFactory;

    private final String outputPath;

    public SaveDocumentTask(
            Document document,
            IOAdapterFactory adapterFactory,
            String outputPath) {

        super("Save document");

        this.document = document;
        this.adapterFactory = adapterFactory;
        this.outputPath = outputPath;
    }

    @Override
    protected void execute() {

        try {

            adapterFactory.save(
                    document,
                    outputPath
            );

        } catch (Exception e) {

            setError(
                    "Failed to save document: "
                            + e.getMessage()
            );
        }
    }
}