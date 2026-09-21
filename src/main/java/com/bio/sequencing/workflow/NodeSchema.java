package com.bio.sequencing.workflow;

import java.util.ArrayList;
import java.util.List;

public class NodeSchema {

    private String id;

    private String type;

    private List<PortSchema> inputs = new ArrayList<>();

    private List<PortSchema> outputs = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<PortSchema> getInputs() {
        return inputs;
    }

    public void setInputs(List<PortSchema> inputs) {
        this.inputs = inputs;
    }

    public List<PortSchema> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<PortSchema> outputs) {
        this.outputs = outputs;
    }

    @Override
    public String toString() {
        return "NodeSchema{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", inputs=" + inputs +
                ", outputs=" + outputs +
                '}';
    }
}