package com.bio.sequencing.plugins.external_tool_support.bedtools;

import java.util.ArrayList;
import java.util.List;

public class BedToolsSettings {

    public enum Operation {
        INTERSECT,
        SORT,
        MERGE,
        COVERAGE,
        GENOMECOV,
        WINDOW,
        CLOSEST,
        SLOP,
        FLANK,
        COMPLEMENT
    }

    private Operation operation;

    private String inputFile;

    private String secondInputFile;

    private String genomeFile;

    private String outputFile;

    private String bedToolsExecutable = "bedtools";

    private boolean sorted;

    private boolean writeOverlap;

    private boolean countOverlaps;

    private boolean stranded;

    private boolean invert;

    private int windowSize = 1000;

    private int left = 0;

    private int right = 0;

    private List<String> extraArguments =
            new ArrayList<>();

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getSecondInputFile() {
        return secondInputFile;
    }

    public void setSecondInputFile(String secondInputFile) {
        this.secondInputFile = secondInputFile;
    }

    public String getGenomeFile() {
        return genomeFile;
    }

    public void setGenomeFile(String genomeFile) {
        this.genomeFile = genomeFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getBedToolsExecutable() {
        return bedToolsExecutable;
    }

    public void setBedToolsExecutable(
            String bedToolsExecutable) {

        this.bedToolsExecutable =
                bedToolsExecutable;
    }

    public boolean isSorted() {
        return sorted;
    }

    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    public boolean isWriteOverlap() {
        return writeOverlap;
    }

    public void setWriteOverlap(
            boolean writeOverlap) {

        this.writeOverlap =
                writeOverlap;
    }

    public boolean isCountOverlaps() {
        return countOverlaps;
    }

    public void setCountOverlaps(
            boolean countOverlaps) {

        this.countOverlaps =
                countOverlaps;
    }

    public boolean isStranded() {
        return stranded;
    }

    public void setStranded(boolean stranded) {
        this.stranded = stranded;
    }

    public boolean isInvert() {
        return invert;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public List<String> getExtraArguments() {
        return extraArguments;
    }

    public void setExtraArguments(
            List<String> extraArguments) {

        this.extraArguments =
                extraArguments;
    }
}