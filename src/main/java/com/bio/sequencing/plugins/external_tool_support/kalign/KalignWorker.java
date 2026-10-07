package com.bio.sequencing.plugins.external_tool_support.kalign;

import com.bio.sequencing.plugins.external_tool_support.clustalw.TaskListener;
import com.bio.sequencing.plugins.external_tool_support.mafft.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class KalignWorker {

    private final ExecutorService executor =
            Executors.newFixedThreadPool(2);

    private final List<Future<KalignTaskResult>>
            kalignTaskResults =
            new CopyOnWriteArrayList<>();

    private final List<TaskListener> listeners =
            new CopyOnWriteArrayList<>();

    public void addListener(TaskListener listener) {

        listeners.add(listener);
    }

    public void processMessages(
            Iterable<Message> messages) {

        for (Message message : messages) {

            KalignTaskSettings settings =
                    getSettings(message);

            KalignTask task =
                    new KalignTask(
                            message.getId(),
                            settings
                    );

            task.addListener(
                    createLogListener()
            );

            Future<KalignTaskResult> future =
                    executor.submit(task);

            kalignTaskResults.add(future);
        }
    }

    private KalignTaskSettings getSettings(
            Message message) {

        String inputFile =
                message.getInputFile();

        if (inputFile == null
                || inputFile.isBlank()) {

            throw new IllegalArgumentException(
                    "Missing input FASTA file for message: "
                            + message.getId()
            );
        }

        String outputFile =
                message.getOutputFile();

        if (outputFile == null
                || outputFile.isBlank()) {

            outputFile =
                    inputFile + ".kalign.fasta";
        }

        String executable =
                message.getString(
                        "kalignExecutable"
                );

        if (executable == null
                || executable.isBlank()) {

            executable = "kalign";
        }

        String workingDirectory =
                message.getString(
                        "workingDirectory"
                );

        String outputFormat =
                message.getString(
                        "outputFormat"
                );

        if (outputFormat == null
                || outputFormat.isBlank()) {

            outputFormat = "fasta";
        }

        String overwriteValue =
                message.getString(
                        "overwrite"
                );

        boolean overwrite =
                overwriteValue == null
                        || Boolean.parseBoolean(
                        overwriteValue
                );

        String verboseValue =
                message.getString(
                        "verbose"
                );

        boolean verbose =
                Boolean.parseBoolean(
                        verboseValue
                );

        return new KalignTaskSettings(
                inputFile,
                outputFile,
                executable,
                workingDirectory,
                outputFormat,
                overwrite,
                verbose
        );
    }

    private TaskListener createLogListener() {

        return new TaskListener() {

            @Override
            public void onStarted(
                    String taskId) {

                listeners.forEach(
                        listener ->
                                listener.onStarted(
                                        taskId
                                )
                );
            }

            @Override
            public void onLog(
                    String taskId,
                    String message) {

                listeners.forEach(
                        listener ->
                                listener.onLog(
                                        taskId,
                                        message
                                )
                );
            }

            @Override
            public void onCompleted(
                    String taskId) {

                listeners.forEach(
                        listener ->
                                listener.onCompleted(
                                        taskId
                                )
                );
            }

            @Override
            public void onFailed(
                    String taskId,
                    Throwable error) {

                listeners.forEach(
                        listener ->
                                listener.onFailed(
                                        taskId,
                                        error
                                )
                );
            }
        };
    }

    public List<Future<KalignTaskResult>>
    getTaskResults() {

        return Collections.unmodifiableList(
                kalignTaskResults
        );
    }

    public void shutdown() {

        executor.shutdown();
    }

    public List<KalignTaskResult> collectCompletedResults()
            throws Exception {

        List<KalignTaskResult> completed =
                new ArrayList<>();

        for (Future<KalignTaskResult> future
                : kalignTaskResults) {

            if (future.isDone()
                    && !future.isCancelled()) {

                completed.add(
                        future.get()
                );
            }
        }

        return completed;
    }
}