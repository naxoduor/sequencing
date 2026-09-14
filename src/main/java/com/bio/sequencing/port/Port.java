package com.bio.sequencing.port;

import com.bio.sequencing.models.AnalysisRequest;

import java.util.Queue;

public class Port {

    private Queue<Object> queue = null;

    public Port(Queue<Object> queue) {
        this.queue = queue;
    }

    public Port(){

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