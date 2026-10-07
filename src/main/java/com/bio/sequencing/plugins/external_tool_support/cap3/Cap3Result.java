package com.bio.sequencing.plugins.external_tool_support.cap3;

public class Cap3Result {

    private final String contigsFile;
    private final String singletsFile;
    private final String assemblyFile;
    private final String qualityFile;
    private final String aceFile;
    private final String infoFile;
    private final String log;

    public Cap3Result(
            String contigsFile,
            String singletsFile,
            String assemblyFile,
            String qualityFile,
            String aceFile,
            String infoFile,
            String log) {

        this.contigsFile = contigsFile;
        this.singletsFile = singletsFile;
        this.assemblyFile = assemblyFile;
        this.qualityFile = qualityFile;
        this.aceFile = aceFile;
        this.infoFile = infoFile;
        this.log = log;
    }

    public String getContigsFile() {
        return contigsFile;
    }

    public String getSingletsFile() {
        return singletsFile;
    }

    public String getAssemblyFile() {
        return assemblyFile;
    }

    public String getQualityFile() {
        return qualityFile;
    }

    public String getAceFile() {
        return aceFile;
    }

    public String getInfoFile() {
        return infoFile;
    }

    public String getLog() {
        return log;
    }
}