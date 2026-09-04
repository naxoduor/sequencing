package com.bio.sequencing.plugins.external_tool_support;

public class Main {

    public static void main(String[] args)
            throws Exception {

        ClustalOSupport clustalO =
                new ClustalOSupport("/usr/bin/clustalo");

        TaskScheduler scheduler =
                new TaskScheduler(4);

        ClustalOTask task =
                clustalO.createTask(
                        "input.fasta",
                        "output.aln"
                );

        scheduler.submit(task);

        scheduler.shutdown();
    }
}