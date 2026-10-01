package com.bio.sequencing.plugins.external_tool_support.stringtie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StringTieWorker {

    private final List<StringTieTask> stringTieTasks =
            new ArrayList<>();

    private final List<Future<Void>> futures =
            new ArrayList<>();

    private final ExecutorService executor =
            Executors.newFixedThreadPool(4);

    public void processMessages(
            List<Message> messages,
            String workingDirectory) {

        for (Message message : messages) {

            StringTieTaskSettings settings =
                    getSettings(message, workingDirectory);

            StringTieTask task =
                    new StringTieTask(settings);

            task.addListeners(createLogListeners());

            stringTieTasks.add(task);

            Future<Void> future = executor.submit(task);
            futures.add(future);
        }
    }

    private StringTieTaskSettings getSettings(
            Message message,
            String workingDirectory) {

        String sampleId = message.getId();

        String outputDirectory =
                workingDirectory + "/stringtie/" + sampleId;

        return new StringTieTaskSettings(
                message.getInputFile(),
                outputDirectory + "/transcripts.gtf",
                outputDirectory,
                "/data/reference/genes.gtf",
                "/usr/bin/stringtie",
                4,
                200.0,
                1.0,
                false,
                false,
                workingDirectory
        );
    }

    private List<TaskListener> createLogListeners() {
        return List.of(new TaskListener() {

            @Override
            public void onStarted(String taskId) {
                System.out.println(
                        "StringTie started: " + taskId
                );
            }

            @Override
            public void onLog(
                    String taskId, String message) {
                System.out.println(
                        "[StringTie " + taskId + "] " + message
                );
            }

            @Override
            public void onCompleted(String taskId) {
                System.out.println(
                        "StringTie completed: " + taskId
                );
            }

            @Override
            public void onFailed(
                    String taskId, Throwable error) {
                System.err.println(
                        "StringTie failed: " + taskId
                                + " - " + error.getMessage()
                );
            }
        });
    }

    public List<StringTieTask> getTasks() {
        return List.copyOf(stringTieTasks);
    }

    public List<Future<Void>> getFutures() {
        return List.copyOf(futures);
    }

    public void shutdown() {
        executor.shutdown();
    }
}