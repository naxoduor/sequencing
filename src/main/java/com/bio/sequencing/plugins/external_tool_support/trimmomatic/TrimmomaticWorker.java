package com.bio.sequencing.plugins.external_tool_support.trimmomatic;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrimmomaticWorker {

    private final List<TrimmomaticTask> trimmomaticTasks =
            new ArrayList<>();

    private final ExecutorService executor =
            Executors.newFixedThreadPool(4);

    public void processMessages(
            List<Message> messages,
            String workingDirectory) {

        for (Message message : messages) {

            TrimmomaticTaskSettings settings =
                    getSettings(message, workingDirectory);

            TrimmomaticTask task =
                    new TrimmomaticTask(settings);

            task.addListeners(createLogListeners());

            trimmomaticTasks.add(task);

            executor.submit(task);
        }
    }

    private TrimmomaticTaskSettings getSettings(
            Message message,
            String workingDirectory) {

        return new TrimmomaticTaskSettings(
                message.getInputFile(),
                message.getOutputFile(),
                "/opt/trimmomatic/trimmomatic.jar",
                "/opt/trimmomatic/adapters.fa",
                2,
                workingDirectory
        );
    }

    // Include createLogListeners() from the previous example.

    public void shutdown() {
        executor.shutdown();
    }


    private List<TaskListener> createLogListeners() {
        return List.of(
                new TaskListener() {
                    @Override
                    public void onStarted(String taskId) {
                        System.out.println("Started: " + taskId);
                    }

                    @Override
                    public void onLog(String taskId, String message) {
                        System.out.println(
                                "[" + taskId + "] " + message
                        );
                    }

                    @Override
                    public void onCompleted(String taskId) {
                        System.out.println("Completed: " + taskId);
                    }

                    @Override
                    public void onFailed(String taskId, Throwable error) {
                        System.err.println(
                                "Failed: " + taskId + " - " + error.getMessage()
                        );
                    }
                }
        );
    }
}