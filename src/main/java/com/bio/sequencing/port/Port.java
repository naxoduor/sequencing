package com.bio.sequencing.port;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Port {

    private Queue<Object> queue = null;

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
        return queue.poll();
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