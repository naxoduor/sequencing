package com.bio.sequencing.plugins.external_tool_support.mafft;

public class ProjectLoader {

    public static AbstractTask openWithProjectTask(
            String path) {

        if (path == null ||
                path.isBlank()) {

            return null;
        }

        return new OpenWithProjectTask(path);
    }
}