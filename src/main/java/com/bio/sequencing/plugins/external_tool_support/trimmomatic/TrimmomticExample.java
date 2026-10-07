package com.bio.sequencing.plugins.external_tool_support.trimmomatic;

import com.bio.sequencing.plugins.external_tool_support.mafft.ProjectLoader;
import com.bio.sequencing.plugins.external_tool_support.mafft.TaskScheduler;

public class TrimmomticExample {
        public static void main(String[] args) {
    TrimmomaticSettings settings =
            new TrimmomaticSettings();

settings.setMode("PE");

settings.setInputRead1(
        "/data/reads/sample_R1.fastq"
        );

settings.setInputRead2(
        "/data/reads/sample_R2.fastq"
        );

settings.setOutputPairedRead1(
        "/data/trimmed/sample_R1_paired.fastq"
        );

settings.setOutputPairedRead2(
        "/data/trimmed/sample_R2_paired.fastq"
        );

settings.setOutputUnpairedRead1(
        "/data/trimmed/sample_R1_unpaired.fastq"
        );

settings.setOutputUnpairedRead2(
        "/data/trimmed/sample_R2_unpaired.fastq"
        );

settings.setThreads(8);

settings.setTrimmomaticExecutable(
        "trimmomatic"
        );

settings.addTrimmingStep(
        "ILLUMINACLIP:/data/adapters/TruSeq3-PE.fa:2:30:10"
        );

settings.addTrimmingStep(
        "LEADING:3"
        );

settings.addTrimmingStep(
        "TRAILING:3"
        );

settings.addTrimmingStep(
        "SLIDINGWINDOW:4:20"
        );

settings.addTrimmingStep(
        "MINLEN:36"
        );

    ProjectLoader projectLoader =
            new ProjectLoader();

    TrimmomaticDocumentTask task =
            new TrimmomaticDocumentTask(
                    settings,
                    projectLoader
            );

    TaskScheduler scheduler =
            new TaskScheduler();

                scheduler.submit(task);
        }
}
