package com.bio.sequencing.plugins.external_tool_support.hmmer;

import java.util.*;

public class HMMERResultDocument {

    private String queryFile;

    private String hmmFile;

    private String database;

    private List<HMMERHit> hits =
            new ArrayList<>();

    private List<HMMERDomain> domains =
            new ArrayList<>();

    private String rawOutputFile;

    private String tableOutputFile;

    private String domainTableOutputFile;

    public String getQueryFile() {
        return queryFile;
    }

    public void setQueryFile(String queryFile) {
        this.queryFile = queryFile;
    }

    public String getHmmFile() {
        return hmmFile;
    }

    public void setHmmFile(String hmmFile) {
        this.hmmFile = hmmFile;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public List<HMMERHit> getHits() {
        return hits;
    }

    public List<HMMERDomain> getDomains() {
        return domains;
    }

    public String getRawOutputFile() {
        return rawOutputFile;
    }

    public void setRawOutputFile(
            String rawOutputFile) {

        this.rawOutputFile =
                rawOutputFile;
    }

    public String getTableOutputFile() {
        return tableOutputFile;
    }

    public void setTableOutputFile(
            String tableOutputFile) {

        this.tableOutputFile =
                tableOutputFile;
    }

    public String getDomainTableOutputFile() {
        return domainTableOutputFile;
    }

    public void setDomainTableOutputFile(
            String domainTableOutputFile) {

        this.domainTableOutputFile =
                domainTableOutputFile;
    }
}