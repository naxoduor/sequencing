package com.bio.sequencing.scheduling;

import com.bio.sequencing.port.Port;
import com.bio.sequencing.workers.Actor;
import com.bio.sequencing.workers.AlignerWorker;
import com.bio.sequencing.workers.FastaReaderWorker;
import com.bio.sequencing.workers.ParserWorker;
import com.bio.sequencing.workflow.WorkflowExecutor;
import com.bio.sequencing.workflow.WorkflowGraph;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class MyScheduledTask {

    @Scheduled(fixedRate = 10_000)
    public void executeEveryMinute() {
        // business logic
        Queue<Object> channel1 = new ConcurrentLinkedQueue<>();
        Queue<Object> channel2 = new ConcurrentLinkedQueue<>();

        Port readerOutput = new Port(channel1);
        Port parserInput = new Port(channel1);

        Port parserOutput = new Port(channel2);
        Port alignerInput = new Port(channel2);

        FastaReaderWorker reader =
                new FastaReaderWorker(Path.of("/home/maradona/Downloads/check.fasta"), readerOutput);

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