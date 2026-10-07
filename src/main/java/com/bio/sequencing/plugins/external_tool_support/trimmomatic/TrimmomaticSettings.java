package com.bio.sequencing.plugins.external_tool_support.trimmomatic;

import java.util.ArrayList;
import java.util.List;

public class TrimmomaticSettings {

    private String inputRead1;

    private String inputRead2;

    private String outputPairedRead1;

    private String outputPairedRead2;

    private String outputUnpairedRead1;

    private String outputUnpairedRead2;

    private String trimmomaticExecutable =
            "trimmomatic";

    /*
     * PE or SE
     */
    private String mode = "PE";

    private int threads = 4;

    private String adaptersFile;

    private boolean phred33 = true;

    /*
     * Trimming steps.
     *
     * Examples:
     *
     * ILLUMINACLIP:TruSeq3-PE.fa:2:30:10
     * LEADING:3
     * TRAILING:3
     * SLIDINGWINDOW:4:20
     * MINLEN:36
     */
    private List<String> trimmingSteps =
            new ArrayList<>();

    public String getInputRead1() {
        return inputRead1;
    }

    public void setInputRead1(
            String inputRead1
    ) {
        this.inputRead1 = inputRead1;
    }

    public String getInputRead2() {
        return inputRead2;
    }

    public void setInputRead2(
            String inputRead2
    ) {
        this.inputRead2 = inputRead2;
    }

    public String getOutputPairedRead1() {
        return outputPairedRead1;
    }

    public void setOutputPairedRead1(
            String outputPairedRead1
    ) {
        this.outputPairedRead1 =
                outputPairedRead1;
    }

    public String getOutputPairedRead2() {
        return outputPairedRead2;
    }

    public void setOutputPairedRead2(
            String outputPairedRead2
    ) {
        this.outputPairedRead2 =
                outputPairedRead2;
    }

    public String getOutputUnpairedRead1() {
        return outputUnpairedRead1;
    }

    public void setOutputUnpairedRead1(
            String outputUnpairedRead1
    ) {
        this.outputUnpairedRead1 =
                outputUnpairedRead1;
    }

    public String getOutputUnpairedRead2() {
        return outputUnpairedRead2;
    }

    public void setOutputUnpairedRead2(
            String outputUnpairedRead2
    ) {
        this.outputUnpairedRead2 =
                outputUnpairedRead2;
    }

    public String getTrimmomaticExecutable() {
        return trimmomaticExecutable;
    }

    public void setTrimmomaticExecutable(
            String trimmomaticExecutable
    ) {
        this.trimmomaticExecutable =
                trimmomaticExecutable;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public String getAdaptersFile() {
        return adaptersFile;
    }

    public void setAdaptersFile(
            String adaptersFile
    ) {
        this.adaptersFile = adaptersFile;
    }

    public boolean isPhred33() {
        return phred33;
    }

    public void setPhred33(boolean phred33) {
        this.phred33 = phred33;
    }

    public List<String> getTrimmingSteps() {
        return trimmingSteps;
    }

    public void setTrimmingSteps(
            List<String> trimmingSteps
    ) {
        this.trimmingSteps =
                trimmingSteps;
    }

    public void addTrimmingStep(
            String step
    ) {
        trimmingSteps.add(step);
    }
}