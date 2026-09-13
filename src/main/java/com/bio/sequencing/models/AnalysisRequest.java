package com.bio.sequencing.models;

import java.util.HashMap;
import java.util.Map;

public class AnalysisRequest {

    private String jobId;


    private String packageName;


    private String operation;

    /**
     * Input files or data.
     *
     * Example:
     * query -> /data/query.bed
     * subject -> /data/genes.bed
     */
    private Map<String, String> input;

    /**
     * Parameters passed to the Bioconductor operation.
     */
    private Map<String, Object> parameters;

    public AnalysisRequest() {
        this.input = new HashMap<>();
        this.parameters = new HashMap<>();
    }

    public AnalysisRequest(
            String jobId,
            String packageName,
            String operation) {

        this.jobId = jobId;
        this.packageName = packageName;
        this.operation = operation;
        this.input = new HashMap<>();
        this.parameters = new HashMap<>();
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Map<String, String> getInput() {
        return input;
    }

    public void setInput(Map<String, String> input) {
        this.input = input;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}