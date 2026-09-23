package com.bio.sequencing.port;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Port {

    private static final Object END_OF_STREAM = new Object();

    private Queue<Object> queue = null;
    private boolean closed = false;

    public Port(Queue<Object> queue) {
        this.queue = queue;
    }

    public Port(){
        this(new ConcurrentLinkedQueue<>());
    }

    public void put(Object data) {
        queue.offer(data);
    }

    public Object get() {
        Object data = queue.poll();
        if (data == END_OF_STREAM) {
            closed = true;
            return null;
        }
        return data;
    }

    public void close() {
        queue.offer(END_OF_STREAM);
    }

    public boolean isClosed() {
        return closed;
    }


    public Queue<Object> getQueue(){
        return queue;
    }

    public void setQueue(Queue<Object> queue) {
        this.queue = queue;
    }

    public boolean hasData() {
        return !queue.isEmpty();
    }

}