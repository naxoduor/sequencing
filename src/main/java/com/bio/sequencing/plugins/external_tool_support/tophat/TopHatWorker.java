package com.bio.sequencing.plugins.external_tool_support.tophat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TopHatWorker {

    private final List<TopHatTask> tophatTasks =
            new ArrayList<>();

    private final List<Future<Void>> futures =
            new ArrayList<>();

    private final ExecutorService executor =
            Executors.newFixedThreadPool(4);

    public void processMessages(
            List<Message> messages,
            String workingDirectory) {

        for (Message message : messages) {

            TopHatTaskSettings settings =
                    getSettings(message, workingDirectory);

            TopHatTask task =
                    new TopHatTask(settings);

            task.addListeners(createLogListeners());

            tophatTasks.add(task);

            Future<Void> future = executor.submit(task);
            futures.add(future);
        }
    }

    private TopHatTaskSettings getSettings(
            Message message,
            String workingDirectory) {

        String sampleId = message.getId();

        return new TopHatTaskSettings(
                message.getInputFile(),
                message.getMateInputFile(),
                "/data/reference/bowtie/genome",
                workingDirectory + "/tophat/" + sampleId,
                "/data/reference/genes.gtf",
                4,
                2,
                2,
                2,
                "/usr/bin/tophat",
                workingDirectory
        );
    }

    private List<TaskListener> createLogListeners() {
        return List.of(new TaskListener() {

            @Override
            public void onStarted(String taskId) {
                System.out.println(
                        "TopHat started: " + taskId
                );
            }

            @Override
            public void onLog(
                    String taskId, String message) {
                System.out.println(
                        "[TopHat " + taskId + "] " + message
                );
            }

            @Override
            public void onCompleted(String taskId) {
                System.out.println(
                        "TopHat completed: " + taskId
                );
            }

            @Override
            public void onFailed(
                    String taskId, Throwable error) {
                System.err.println(
                        "TopHat failed: " + taskId
                                + " - " + error.getMessage()
                );
            }
        });
    }

    public List<TopHatTask> getTasks() {
        return List.copyOf(tophatTasks);
    }

    public List<Future<Void>> getFutures() {
        return List.copyOf(futures);
    }

    public void shutdown() {
        executor.shutdown();
    }
}