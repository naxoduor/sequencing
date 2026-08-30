package com.bio.sequencing.port;

public class Connection {

    private final Port source;
    private final Port destination;

    public Connection(
            Port source,
            Port destination) {

        this.source = source;
        this.destination = destination;
    }
}