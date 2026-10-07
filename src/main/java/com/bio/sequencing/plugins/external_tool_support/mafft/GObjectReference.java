package com.bio.sequencing.plugins.external_tool_support.mafft;

public class GObjectReference {

    private String objectId;

    public GObjectReference() {
    }

    public GObjectReference(String objectId) {
        this.objectId = objectId;
    }

    public boolean isEmpty() {
        return objectId == null ||
                objectId.isBlank();
    }

    public String getObjectId() {
        return objectId;
    }
}