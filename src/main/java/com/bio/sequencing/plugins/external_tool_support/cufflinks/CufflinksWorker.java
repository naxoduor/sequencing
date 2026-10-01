package com.bio.sequencing.plugins.external_tool_support.cufflinks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CufflinksWorker {

    private final List<CufflinksTask> cufflinksTasks =
            new ArrayList<>();

    private final List<Future<Void>> futures =
            new ArrayList<>();

    private final ExecutorService executor =
            Executors.newFixedThreadPool(4);

    public void processMessages(
            List<Message> messages,
            String workingDirectory) {

        for (Message message : messages) {

            CufflinksTaskSettings settings =
                    getSettings(message, workingDirectory);

            CufflinksTask task =
                    new CufflinksTask(settings);

            task.addListeners(createLogListeners());

            cufflinksTasks.add(task);

            Future<Void> future = executor.submit(task);
            futures.add(future);
        }
    }

    private CufflinksTaskSettings getSettings(
            Message message,
            String workingDirectory) {

        return new CufflinksTaskSettings(
                message.getInputFile(),
                workingDirectory + "/cufflinks/" + message.getId(),
                "/data/reference/genes.gtf",
                "/data/reference/genome.fa",
                4,
                "/usr/bin/cufflinks",
                workingDirectory
        );
    }

    private List<TaskListener> createLogListeners() {
        return List.of(new TaskListener() {

            @Override
            public void onStarted(String taskId) {
                System.out.println(
                        "Cufflinks started: " + taskId
                );
            }

            @Override
            public void onLog(String taskId, String message) {
                System.out.println(
                        "[Cufflinks " + taskId + "] " + message
                );
            }

            @Override
            public void onCompleted(String taskId) {
                System.out.println(
                        "Cufflinks completed: " + taskId
                );
            }

            @Override
            public void onFailed(
                    String taskId, Throwable error) {
                System.err.println(
                        "Cufflinks failed: " + taskId
                                + " - " + error.getMessage()
                );
            }
        });
    }

    public List<CufflinksTask> getTasks() {
        return List.copyOf(cufflinksTasks);
    }

    public void shutdown() {
        executor.shutdown();
    }
}