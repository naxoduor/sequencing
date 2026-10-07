package com.bio.sequencing.plugins.external_tool_support.cap3;

import com.bio.sequencing.plugins.external_tool_support.mafft.TaskScheduler;

public class CAP3Example {

        public static void main(String[] args) {
    Cap3Settings settings =
            new Cap3Settings();

settings.setInputFasta(
        "/data/reads.fasta");

settings.setOutputDirectory(
        "/data/results");

settings.setCap3Executable(
        "cap3");

    Cap3DocumentTask task =
            new Cap3DocumentTask(settings);

        new TaskScheduler().submit(task);
    }
}
