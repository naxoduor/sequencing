package com.bio.sequencing.plugins.external_tool_support.mafft;

public class SaveDocumentTask
        extends AbstractTask {

    private final Document document;

    private IOAdapterFactory adapterFactory = null;

    private final String outputPath;

    public SaveDocumentTask(
            Document document,
            String outputPath) {

        super("Save document");

        this.document = document;
        this.adapterFactory = new FastaIOAdapterFactory();
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