package com.bio.sequencing.workflow;

import java.util.ArrayList;
import java.util.List;

public class Schema {
    private String id;
    private String type;
    private Position position;
    private List<PortSchema> inputs;
    private List<PortSchema> outputs;
    private File file;
    private String status;
    private Data data;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
