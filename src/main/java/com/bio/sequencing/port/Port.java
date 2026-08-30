package com.bio.sequencing.port;

import java.util.Queue;

public class Port {

    private final Queue<Object> queue;

    public Port(Queue<Object> queue) {
        this.queue = queue;
    }

    public void put(Object data) {
        queue.offer(data);
    }

    public Object get() {
        return queue.poll();
    }

    public boolean hasData() {
        return !queue.isEmpty();
    }
}