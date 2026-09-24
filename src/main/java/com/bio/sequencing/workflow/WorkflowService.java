package com.bio.sequencing.workflow;

import com.bio.sequencing.port.Port;
import com.bio.sequencing.workers.*;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class WorkflowService {

        @Async
        public CompletableFuture<WorkflowResponse> create(
            WorkflowSchema schema) {

        WorkflowGraph graph = createGraph(schema);

        WorkflowExecutor executor =
                new WorkflowExecutor(graph);

        executor.execute();

        return CompletableFuture.completedFuture(
                new WorkflowResponse("created"));
    }

    public WorkflowGraph createGraph(
            WorkflowSchema schema) {

        Map<String, NodePorts> portsByNode =
                new HashMap<>();

        for (NodeSchema node : schema.getNodes()) {
            portsByNode.put(node.getId(), createPorts(node));
        }
        portsByNode.keySet().forEach(System.out::println);
        portsByNode.values().forEach(System.out::println);

        for (ConnectionSchema connection : schema.getConnections()) {
            System.out.println("inside connections");
            System.out.println(connection.getSourceNode());
            NodePorts source = requireNodePorts(
                    portsByNode, connection.getSourceNode());
            NodePorts target = requireNodePorts(
                    portsByNode, connection.getTargetNode());

            Queue<Object> channel = new ConcurrentLinkedQueue<>();
            source.output(connection.getSourcePort()).setQueue(channel);
            target.input(connection.getTargetPort()).setQueue(channel);
        }
        portsByNode.keySet().forEach(System.out::println);
        portsByNode.values().forEach(System.out::println);

        Map<String, com.bio.sequencing.workers.Worker> workers =
                new HashMap<>();
        for (NodeSchema node : schema.getNodes()) {
            workers.put(node.getId(), createWorker(
                    node,
                    requireNodePorts(portsByNode, node.getId())));
        }

        WorkflowGraph graph = new WorkflowGraph();
        for (NodeSchema node : schema.getNodes()) {
            graph.addActor(new Actor(node.getId(), workers.get(node.getId())));
        }
        return graph;
    }

    private com.bio.sequencing.workers.Worker createWorker(
            NodeSchema node,
            NodePorts ports) {

        return switch (node.getType()) {

            case "SequenceReader", "FastaReaderWorker" ->
                    new FastaReaderWorker(
                            Path.of("/home/maradona/Downloads/check.fasta"),
                            ports.firstOutput());

            case "MAFFTWorker" ->
                    new MAFFTWorker(ports.firstInput(), ports.firstOutput());

            case "ClustalOWorker" ->
                    new ClustalOWorker(ports.firstInput(), ports.firstOutput());

            case "ClustalWWorker" ->
                new ClustalWWorker(ports.firstInput(), ports.firstOutput());

            case "KalignWorker" ->
                new KalignWorker(ports.firstInput(), ports.firstOutput());

        
            case "FileWriterWorker" ->
                         new FileWriterWorker(Path.of("/home/maradona/Downloads/checktwo.fasta"), ports.firstInput());

            default ->
                    throw new IllegalArgumentException(
                            "Unknown worker: "
                                    + node.getType()
                    );
        };
    }

        private NodePorts createPorts(NodeSchema node) {
                return new NodePorts(
                                createPorts(node.getInputs()),
                                createPorts(node.getOutputs()));
        }

        private Map<String, Port> createPorts(List<PortSchema> schemas) {
                Map<String, Port> ports = new HashMap<>();
            System.out.println(schemas);
                for (int index = 0; index < schemas.size(); index++) {
                        PortSchema schema = schemas.get(index);
                        Port port = new Port(new ConcurrentLinkedQueue<>());
                        ports.put(schema.getId(), port);
                        ports.putIfAbsent(String.valueOf(index), port);
                }
                return ports;
        }

        private NodePorts requireNodePorts(
                        Map<String, NodePorts> portsByNode,
                        String nodeId) {
                NodePorts ports = portsByNode.get(nodeId);
                if (ports == null) {
                        throw new IllegalArgumentException("Unknown node: " + nodeId);
                }
                return ports;
        }

        private static class NodePorts {
                private final Map<String, Port> inputs;
                private final Map<String, Port> outputs;

                private NodePorts(Map<String, Port> inputs, Map<String, Port> outputs) {
                        this.inputs = inputs;
                        this.outputs = outputs;
                }

                private Port input(String portId) {
                        return port(inputs, portId);
                }

                private Port output(String portId) {
                        return port(outputs, portId);
                }

                private Port firstInput() {
                        return first(inputs);
                }

                private Port firstOutput() {
                        return first(outputs);
                }

                private Port port(Map<String, Port> ports, String portId) {
                        Port port = ports.get(portId);
                        if (port == null) {
                                throw new IllegalArgumentException(
                                                "Port " + portId + " is not declared");
                        }
                        return port;
                }

                private Port first(Map<String, Port> ports) {
                        if (ports.isEmpty()) {
                                return new Port(new ConcurrentLinkedQueue<>());
                        }
                        return ports.values().iterator().next();
                }
    }
}