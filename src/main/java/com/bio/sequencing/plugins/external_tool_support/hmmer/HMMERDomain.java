package com.bio.sequencing.plugins.external_tool_support.hmmer;

public class HMMERDomain {

    private String targetName;

    private String targetAccession;

    private String queryName;

    private String queryAccession;

    private int domainNumber;

    private int totalDomains;

    private double cEvalue;

    private double iEvalue;

    private double score;

    private double bias;

    private int hmmFrom;

    private int hmmTo;

    private int aliFrom;

    private int aliTo;

    private int envFrom;

    private int envTo;

    private double accuracy;

    private String description;

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

    public int getDomainNumber() {
        return domainNumber;
    }

    public void setDomainNumber(
            int domainNumber) {

        this.domainNumber =
                domainNumber;
    }

    public int getTotalDomains() {
        return totalDomains;
    }

    public void setTotalDomains(
            int totalDomains) {

        this.totalDomains =
                totalDomains;
    }

    public double getcEvalue() {
        return cEvalue;
    }

    public void setcEvalue(double cEvalue) {
        this.cEvalue = cEvalue;
    }

    public double getiEvalue() {
        return iEvalue;
    }

    public void setiEvalue(double iEvalue) {
        this.iEvalue = iEvalue;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public int getHmmFrom() {
        return hmmFrom;
    }

    public void setHmmFrom(int hmmFrom) {
        this.hmmFrom = hmmFrom;
    }

    public int getHmmTo() {
        return hmmTo;
    }

    public void setHmmTo(int hmmTo) {
        this.hmmTo = hmmTo;
    }

    public int getAliFrom() {
        return aliFrom;
    }

    public void setAliFrom(int aliFrom) {
        this.aliFrom = aliFrom;
    }

    public int getAliTo() {
        return aliTo;
    }

    public void setAliTo(int aliTo) {
        this.aliTo = aliTo;
    }

    public int getEnvFrom() {
        return envFrom;
    }

    public void setEnvFrom(int envFrom) {
        this.envFrom = envFrom;
    }

    public int getEnvTo() {
        return envTo;
    }

    public void setEnvTo(int envTo) {
        this.envTo = envTo;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(
            String description) {

        this.description = description;
    }
}
