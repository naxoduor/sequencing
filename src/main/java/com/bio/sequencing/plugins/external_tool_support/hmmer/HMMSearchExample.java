package com.bio.sequencing.plugins.external_tool_support.hmmer;

import com.bio.sequencing.plugins.external_tool_support.mafft.TaskScheduler;

public class HMMSearchExample {

        public static void main(String[] args) {
    HMMERSettings settings =
            new HMMERSettings();

settings.setOperation(
    HMMERSettings.Operation.HMMSEARCH);

settings.setHmmFile(
        "/data/profiles/kinase.hmm");

settings.setSequenceFile(
        "/data/proteins.fasta");

settings.setOutputDirectory(
        "/data/results");

settings.setTblOutputFile(
        "/data/results/kinase.tbl");

settings.setDomTblOutputFile(
        "/data/results/kinase.domtbl");

settings.setOutputFile(
        "/data/results/kinase-result.txt");

settings.setThreads(8);

settings.setSequenceEvalue(1e-5);

settings.setDomainEvalue(1e-5);

settings.setNoAlignment(true);

settings.setHmmerExecutable(
        "hmmsearch");

    HMMERDocumentTask task =
            new HMMERDocumentTask(settings);

        new TaskScheduler().submit(task);
    }
}
