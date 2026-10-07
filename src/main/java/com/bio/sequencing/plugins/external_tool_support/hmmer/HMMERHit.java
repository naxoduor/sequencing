package com.bio.sequencing.plugins.external_tool_support.hmmer;

public class HMMERHit {

    private String targetName;

    private String targetAccession;

    private String queryName;

    private String queryAccession;

    private String description;

    private int targetLength;

    private int queryLength;

    private int domainCount;

    private double sequenceEvalue;

    private double sequenceScore;

    private double sequenceBias;

    private double domainEvalue;

    private double domainScore;

    private double domainBias;

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(
            String targetName) {

        this.targetName = targetName;
    }

    public String getTargetAccession() {
        return targetAccession;
    }

    public void setTargetAccession(
            String targetAccession) {

        this.targetAccession =
                targetAccession;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(
            String queryName) {

        this.queryName = queryName;
    }

    public String getQueryAccession() {
        return queryAccession;
    }

    public void setQueryAccession(
            String queryAccession) {

        this.queryAccession =
                queryAccession;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(
            String description) {

        this.description = description;
    }

    public int getTargetLength() {
        return targetLength;
    }

    public void setTargetLength(int targetLength) {
        this.targetLength = targetLength;
    }

    public int getQueryLength() {
        return queryLength;
    }

    public void setQueryLength(int queryLength) {
        this.queryLength = queryLength;
    }

    public int getDomainCount() {
        return domainCount;
    }

    public void setDomainCount(int domainCount) {
        this.domainCount = domainCount;
    }

    public double getSequenceEvalue() {
        return sequenceEvalue;
    }

    public void setSequenceEvalue(
            double sequenceEvalue) {

        this.sequenceEvalue =
                sequenceEvalue;
    }

    public double getSequenceScore() {
        return sequenceScore;
    }

    public void setSequenceScore(
            double sequenceScore) {

        this.sequenceScore =
                sequenceScore;
    }

    public double getSequenceBias() {
        return sequenceBias;
    }

    public void setSequenceBias(
            double sequenceBias) {

        this.sequenceBias =
                sequenceBias;
    }

    public double getDomainEvalue() {
        return domainEvalue;
    }

    public void setDomainEvalue(
            double domainEvalue) {

        this.domainEvalue =
                domainEvalue;
    }

    public double getDomainScore() {
        return domainScore;
    }

    public void setDomainScore(
            double domainScore) {

        this.domainScore =
                domainScore;
    }

    public double getDomainBias() {
        return domainBias;
    }

    public void setDomainBias(
            double domainBias) {

        this.domainBias =
                domainBias;
    }
}
