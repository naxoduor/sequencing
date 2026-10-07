//package com.bio.sequencing.plugins.external_tool_support.tophat;
//
//public class TopHatExample {
//
//    TopHatSettings settings =
//            new TopHatSettings();
//
//settings.setReferenceIndex(
//        "/data/genome/hg38"
//        );
//
//settings.setRead1File(
//        "/data/reads/sample_R1.fastq"
//        );
//
//settings.setRead2File(
//        "/data/reads/sample_R2.fastq"
//        );
//
//settings.setOutputDirectory(
//        "/data/tophat/sample1"
//        );
//
//settings.setTophatExecutable(
//        "tophat"
//        );
//
//settings.setThreads(8);
//
//settings.setAnnotationFile(
//        "/data/reference/genes.gtf"
//        );
//
//    ProjectLoader projectLoader =
//            new ProjectLoader();
//
//    TopHatDocumentTask task =
//            new TopHatDocumentTask(
//                    settings,
//                    projectLoader
//            );
//
//    TaskScheduler scheduler =
//            new TaskScheduler();
//
//scheduler.submit(task);
//}
