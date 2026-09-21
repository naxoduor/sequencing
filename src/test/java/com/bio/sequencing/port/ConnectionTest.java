package com.bio.sequencing.port;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ConnectionTest {

    @Test
    void connectedPortsShareQueue() {
        Port output = new Port();
        Port input = new Port();

        new Connection(output, input);

        assertSame(output.getQueue(), input.getQueue());

        output.put("data");

        assertEquals("data", input.get());
    }
}