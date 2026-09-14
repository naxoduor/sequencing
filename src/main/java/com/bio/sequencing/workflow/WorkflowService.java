package com.bio.sequencing.workflow;

import com.bio.sequencing.port.Port;
import com.bio.sequencing.workers.BioconductorWorker;
import com.bio.sequencing.workers.FastaReaderWorker;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

@Service
public class WorkflowService {

    public WorkflowResponse create(
            WorkflowSchema schema) {

        Map<String, Worker> workers =
                new HashMap<>();

        // Create workers
        for (NodeSchema node : schema.getNodes()) {

            Worker worker =
                    createWorker(node);

            workers.put(
                    node.getId(),
                    worker
            );
        }

        // Connect workers
        for (ConnectionSchema connection :
                schema.getConnections()) {

            Worker source =
                    workers.get(
                            connection.getSourceNode()
                    );

            Worker target =
                    workers.get(
                            connection.getTargetNode()
                    );

            connect(
                    source,
                    connection.getSourcePort(),
                    target,
                    connection.getTargetPort()
            );
        }

        return new WorkflowResponse(
                "created"
        );
    }

    private Worker createWorker(
            NodeSchema node) {

        return (Worker) switch (node.getType()) {

            case "SequenceReader" ->
                    new FastaReaderWorker(new Port());

            case "BioconductorWorker" ->
                    new BioconductorWorker("id", null, null);

            default ->
                    throw new IllegalArgumentException(
                            "Unknown worker: "
                                    + node.getType()
                    );
        };
    }

    private void connect(
            Worker source,
            String sourcePort,
            Worker target,
            String targetPort) {

        Port output =
                source.getOutputPort(sourcePort);

        Port input =
                target.getInputPort(targetPort);

//        CommunicationChannel channel =
//                new CommunicationChannel();
//
//        output.setChannel(channel);
//        input.setChannel(channel);
    }
}