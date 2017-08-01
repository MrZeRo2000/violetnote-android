package com.romanpulov.violetnote.view.action;

/**
 * Created by rpulov on 31.07.2017.
 */

public interface BasicNoteDataActionExecutorHost {
    void execute(BasicNoteDataActionExecutor executor);
    void onExecutionStarted();
    void onExecutionCompleted();
}
