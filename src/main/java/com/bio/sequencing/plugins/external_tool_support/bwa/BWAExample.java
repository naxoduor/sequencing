package com.bio.sequencing.plugins.external_tool_support.bwa;

import com.bio.sequencing.plugins.external_tool_support.mafft.TaskScheduler;

public class BWAExample {
        public static void main(String[] args) {
    BwaMemSettings settings =
            new BwaMemSettings();

settings.setReferenceIndex(
        "/data/genome/hg38"
        );

settings.setRead1File(
        "/data/reads/sample_R1.fastq"
        );

settings.setRead2File(
        "/data/reads/sample_R2.fastq"
        );

settings.setInputMode(
    BwaMemSettings.InputMode.PAIRED_END
);

settings.setThreads(8);

settings.setOutputSamFile(
        "/data/results/sample.sam"
        );

settings.setOutputBamFile(
        "/data/results/sample.bam"
        );


    BwaMemDocumentTask task =
            new BwaMemDocumentTask(settings);

        new TaskScheduler().submit(task);
    }
}
