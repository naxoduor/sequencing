package com.bio.sequencing.plugins.external_tool_support.mafft;

import java.util.ArrayList;
import java.util.List;

public class Document {

    private final String url;

    private final List<Object> objects =
            new ArrayList<>();

    public Document(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public List<Object> getObjects() {
        return objects;
    }

    public void addObject(Object object) {
        objects.add(object);
    }
}