package com.bio.sequencing.plugins.external_tool_support.mafft;

public class MAFFTExample {

    public static void main(String[] args) {

        MAFFTSettings settings =
                new MAFFTSettings();

        settings.setInputFilePath(
                "/data/input.fasta"
        );

        settings.setOutputFilePath(
                "/data/aligned.fasta"
        );

        settings.setMafftExecutable(
                "mafft"
        );

        settings.setAlgorithm(
                "--auto"
        );

        settings.setThreads(4);

        ProjectLoader projectLoader =
                new ProjectLoader();

        IOAdapterFactory ioAdapterFactory =
                new FastaIOAdapterFactory();

        MAFFTDocumentTask task =
                new MAFFTDocumentTask(
                        settings,
                        "/tmp/mafft-input.fasta",
                        projectLoader,
                        ioAdapterFactory
                );

        TaskScheduler scheduler =
                new TaskScheduler();

        scheduler.submit(task);
    }
}