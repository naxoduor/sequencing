package com.bio.sequencing.plugins.external_tool_support.cufflinks;

public interface TaskListener {

    default void onStarted(String taskId) {}

    default void onLog(String taskId, String message) {}

    default void onCompleted(String taskId) {}

    default void onFailed(String taskId, Throwable error) {}
}