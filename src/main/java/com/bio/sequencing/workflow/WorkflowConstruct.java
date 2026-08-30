package com.bio.sequencing.workflow;

import com.bio.sequencing.port.Port;
import com.bio.sequencing.workers.Actor;
import com.bio.sequencing.workers.AlignerWorker;
import com.bio.sequencing.workers.FastaReaderWorker;
import com.bio.sequencing.workers.ParserWorker;

import java.util.LinkedList;
import java.util.Queue;

public class WorkflowConstruct {
    public void construct(){
        Queue<Object> channel1 = new LinkedList<>();
        Queue<Object> channel2 = new LinkedList<>();

        Port readerOutput = new Port(channel1);
        Port parserInput = new Port(channel1);

        Port parserOutput = new Port(channel2);
        Port alignerInput = new Port(channel2);

        FastaReaderWorker reader =
                new FastaReaderWorker(readerOutput);

        ParserWorker parser =
                new ParserWorker(
                        parserInput,
                        parserOutput);

        AlignerWorker aligner =
                new AlignerWorker(
                        alignerInput,
                        new Port(new LinkedList<>()));

        Actor readerActor =
                new Actor("reader", reader);

        Actor parserActor =
                new Actor("parser", parser);

        Actor alignerActor =
                new Actor("aligner", aligner);

        WorkflowGraph graph = new WorkflowGraph();

        graph.addActor(readerActor);
        graph.addActor(parserActor);
        graph.addActor(alignerActor);

        WorkflowExecutor executor =
                new WorkflowExecutor(graph);

        executor.execute();
    }

}
