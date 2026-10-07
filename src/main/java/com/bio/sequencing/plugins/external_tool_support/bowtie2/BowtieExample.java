package com.bio.sequencing.plugins.external_tool_support.bowtie2;

import com.bio.sequencing.plugins.external_tool_support.mafft.TaskScheduler;

public class BowtieExample {
        public static void main(String[] args) {
    Bowtie2Settings settings =
            new Bowtie2Settings();

settings.setReferenceIndex(
        "/data/reference/hg38"
        );

settings.setRead1File(
        "/data/reads/sample.fastq"
        );

settings.setOutputSamFile(
        "/data/results/sample.sam"
        );

settings.setOutputBamFile(
        "/data/results/sample.bam"
        );

settings.setInputMode(
    Bowtie2Settings.InputMode.SINGLE_END
);

settings.setThreads(8);

    Bowtie2DocumentTask task =
            new Bowtie2DocumentTask(settings);

    Bowtie2Settings pairedSettings = new Bowtie2Settings();

pairedSettings.setReferenceIndex("/data/reference/hg38");
pairedSettings.setRead1File(
        "/data/reads/sample_R1.fastq"
        );

pairedSettings.setRead2File(
        "/data/reads/sample_R2.fastq"
        );

pairedSettings.setOutputSamFile("/data/results/paired.sam");
pairedSettings.setOutputBamFile("/data/results/paired.bam");
pairedSettings.setInputMode(
    Bowtie2Settings.InputMode.PAIRED_END
);

    Bowtie2DocumentTask pairedTask =
            new Bowtie2DocumentTask(pairedSettings);
    TaskScheduler scheduler = new TaskScheduler();
    scheduler.submit(task);
    scheduler.submit(pairedTask);
    }
}
