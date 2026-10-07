package com.bio.sequencing.plugins.external_tool_support.blast;

import com.bio.sequencing.plugins.external_tool_support.mafft.TaskScheduler;

public class BlastExample {

    public static void main(String[] args) {
    BlastSettings settings = new BlastSettings();

settings.setProgram(
    BlastSettings.Program.BLASTN);

settings.setQueryFile(
        "/data/query.fasta");

settings.setDatabase(
        "/data/blastdb/nt");

settings.setOutputFile(
        "/data/results/blast.xml");

settings.setOutputFormat(
    BlastSettings.OutputFormat.XML);

settings.setThreads(8);

settings.setEvalue(1e-5);

settings.setMaxTargetSequences(100);

settings.setTask("megablast");

    BlastDocumentTask task =
            new BlastDocumentTask(settings);

        BlastSettings blastpSettings = new BlastSettings();

    blastpSettings.setProgram(
        BlastSettings.Program.BLASTP);

    blastpSettings.setQueryFile(
        "/data/proteins.fasta");

    blastpSettings.setDatabase(
        "/data/blastdb/protein_db");

    blastpSettings.setOutputFile(
        "/data/results/blastp.xml");

    blastpSettings.setOutputFormat(
        BlastSettings.OutputFormat.XML);

    blastpSettings.setThreads(8);

    blastpSettings.setEvalue(1e-5);

        BlastDocumentTask blastpTask =
            new BlastDocumentTask(blastpSettings);

        TaskScheduler scheduler = new TaskScheduler();
        scheduler.submit(task);
        scheduler.submit(blastpTask);
        }
}
