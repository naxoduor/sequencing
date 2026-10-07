package com.bio.sequencing.plugins.external_tool_support.bedtools;

import com.bio.sequencing.plugins.external_tool_support.mafft.TaskScheduler;

public class BEDToolsExample {

        public static void main(String[] args) {
    BedToolsSettings settings =
            new BedToolsSettings();

settings.setOperation(
    BedToolsSettings.Operation.INTERSECT
);

settings.setInputFile(
        "/data/genes.bed"
        );

settings.setSecondInputFile(
        "/data/exons.bed"
        );

settings.setOutputFile(
        "/data/results/intersection.bed"
        );

settings.setWriteOverlap(true);

    BedToolsDocumentTask task =
            new BedToolsDocumentTask(settings);

        new TaskScheduler().submit(task);
    }
}
