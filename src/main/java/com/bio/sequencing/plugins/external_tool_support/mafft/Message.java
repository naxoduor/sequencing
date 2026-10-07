package com.bio.sequencing.plugins.external_tool_support.mafft;

import java.util.Map;
import java.util.UUID;

public class Message {

    private final String id;
    private final String source;
    private final String destination;
    private final Map<String, Object> data;

    public Message(
            String source,
            String destination,
            Map<String, Object> data) {
        this.id = UUID.randomUUID().toString();
        this.source = source;
        this.destination = destination;
        this.data = Map.copyOf(data);
    }

    public String getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Object get(String key) {
        return data.get(key);
    }

    public String getString(String key) {
        Object value = data.get(key);
        return value == null ? null : value.toString();
    }

    public String getInputFile() {
        return getString("inputFile");
    }

    public String getOutputFile() {
        return getString("outputFile");
    }

    public String getMateInputFile() {
        return getString("mateInputFile");
    }

    public boolean contains(String key) {
        return data.containsKey(key);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", data=" + data +
                '}';
    }
}