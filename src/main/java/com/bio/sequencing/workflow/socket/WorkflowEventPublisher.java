package com.bio.sequencing.workflow.socket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WorkflowEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public WorkflowEventPublisher(
            SimpMessagingTemplate messagingTemplate) {

        this.messagingTemplate = messagingTemplate;
    }

    public void publish(
            WorkflowEvent event) {

        String destination =
                "/topic/workflows/"
                        + event.getWorkflowId();

        messagingTemplate.convertAndSend(
                destination,
                event
        );
    }

    public void workerStarted(
            String workflowId,
            String nodeId) {

        publish(
                new WorkflowEvent(
                        WorkflowEvent.Type.WORKER_STARTED,
                        workflowId,
                        nodeId,
                        null,
                        null,
                        "Worker started"
                )
        );
    }

    public void portData(
            String workflowId,
            String nodeId,
            String portId,
            Object data) {

        publish(
                new WorkflowEvent(
                        WorkflowEvent.Type.PORT_DATA,
                        workflowId,
                        nodeId,
                        portId,
                        data,
                        "Data received"
                )
        );
    }

    public void workerCompleted(
            String workflowId,
            String nodeId) {

        publish(
                new WorkflowEvent(
                        WorkflowEvent.Type.WORKER_COMPLETED,
                        workflowId,
                        nodeId,
                        null,
                        null,
                        "Worker completed"
                )
        );
    }

    public void workerFailed(
            String workflowId,
            String nodeId,
            String error) {

        publish(
                new WorkflowEvent(
                        WorkflowEvent.Type.WORKER_FAILED,
                        workflowId,
                        nodeId,
                        null,
                        null,
                        error
                )
        );
    }
}