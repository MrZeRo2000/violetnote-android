package com.romanpulov.violetnote.view.action;

import android.content.Context;
import androidx.annotation.NonNull;

import com.romanpulov.violetnote.db.manager.DBNoteManager;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BasicActionExecutor<T> {
    protected final Context mContext;
    protected final T mData;
    protected final List<Map.Entry<String, BasicAction<T>>> mActionList = new ArrayList<>();

    protected OnExecutionCompletedListener<T> mListener;

    public OnExecutionCompletedListener<T> getOnExecutionCompletedListener() {
        return mListener;
    }

    public void setOnExecutionCompletedListener(OnExecutionCompletedListener<T> listener) {
        mListener = listener;
    }

    protected OnExecutionProgressListener mProgressListener;

    public void setOnExecutionProgressListener(OnExecutionProgressListener progressListener) {
        mProgressListener = progressListener;
    }

    public BasicActionExecutor (Context context, T data) {
        this.mContext = context;
        this.mData = data;
    }

    public interface OnExecutionCompletedListener<T> {
        void onExecutionCompleted(T data, boolean result);
    }

    public interface OnExecutionProgressListener {
        void onExecutionProgress(String progressText);
    }

    public void addAction(String description, BasicAction<T> action) {
        mActionList.add(new AbstractMap.SimpleEntry<>(description, action));
    }

    @NonNull
    protected DBNoteManager createNoteManager() {
        return new DBNoteManager(mContext);
    }

    protected boolean internalExecute() {
        DBNoteManager noteManager = createNoteManager();

        for (Map.Entry<String, BasicAction<T>> entry : mActionList) {
            if (!entry.getValue().execute(noteManager)) {
                return false;
            }
        }
        return true;
    }

    public void execute() {
        boolean executionResult = internalExecute();

        if (mListener != null)
            mListener.onExecutionCompleted(mData, executionResult);
    }
}
