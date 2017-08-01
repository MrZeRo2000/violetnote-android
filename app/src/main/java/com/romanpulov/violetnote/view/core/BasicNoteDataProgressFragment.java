package com.romanpulov.violetnote.view.core;

import android.app.Activity;

import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutor;

/**
 * Created by romanpulov on 31.07.2017.
 */

public class BasicNoteDataProgressFragment extends ProgressFragment {
    private OnBasicNoteDataFragmentInteractionListener mListener;

    public static BasicNoteDataProgressFragment newInstance() {
        return new BasicNoteDataProgressFragment();
    }

    public void execute(BasicNoteDataActionExecutor executor) {
        final BasicNoteDataActionExecutor.OnExecutionCompletedListener oldListener = executor.getOnExecutionCompletedListener();
        executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
            @Override
            public void onExecutionCompleted(BasicNoteDataA basicNoteData, boolean result) {
                if (mListener != null)
                    mListener.onBasicNoteDataLoaded(basicNoteData, result);
                if (oldListener != null)
                    oldListener.onExecutionCompleted(basicNoteData, result);
            }
        });
        executor.execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnBasicNoteDataFragmentInteractionListener) {
            mListener = (OnBasicNoteDataFragmentInteractionListener) activity;
            mListener.onBasicNoteDataFragmentAttached();
        }
        else
            throw new RuntimeException(activity.toString()
                    + " must implement OnBasicNoteDataFragmentInteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnBasicNoteDataFragmentInteractionListener {
        void onBasicNoteDataFragmentAttached();
        void onBasicNoteDataLoaded(BasicNoteDataA basicNoteData, boolean result);
    }
}
