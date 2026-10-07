package com.bio.sequencing.plugins.external_tool_support.mafft;

public interface IOAdapterFactory {

    void save(
            Document document,
            String path
    ) throws Exception;
}