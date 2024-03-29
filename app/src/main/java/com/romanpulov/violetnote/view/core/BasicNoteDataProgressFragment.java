package com.romanpulov.violetnote.view.core;

import android.content.Context;

import androidx.annotation.NonNull;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.view.action.BasicActionExecutor;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutor;

/**
 * ProgressFragment with BasicNoteDataActionExecutor execute support
 * Created by romanpulov on 31.07.2017.
 */

public class BasicNoteDataProgressFragment extends ProgressFragment {
    private OnBasicNoteDataFragmentInteractionListener mListener;

    public static BasicNoteDataProgressFragment newInstance() {
        return new BasicNoteDataProgressFragment();
    }

    public void execute(BasicNoteDataActionExecutor executor) {
        final BasicActionExecutor.OnExecutionCompletedListener<BasicNoteDataA> oldListener = executor.getOnExecutionCompletedListener();
        executor.setOnExecutionCompletedListener((BasicNoteDataActionExecutor.OnExecutionCompletedListener) (basicNoteData, result) -> {
            if (mListener != null)
                mListener.onBasicNoteDataLoaded(basicNoteData, result);
            if (oldListener != null)
                oldListener.onExecutionCompleted(basicNoteData, result);
        });
        executor.setOnExecutionProgressListener(this::setProgressText);
        executor.execute();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnBasicNoteDataFragmentInteractionListener) {
            mListener = (OnBasicNoteDataFragmentInteractionListener) context;
            mListener.onBasicNoteDataFragmentAttached(true);
        }
        else
            throw new RuntimeException(context.toString()
                    + " must implement OnBasicNoteDataFragmentInteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnBasicNoteDataFragmentInteractionListener {
        void onBasicNoteDataFragmentAttached(boolean isProgress);
        void onBasicNoteDataLoaded(BasicNoteDataA basicNoteData, boolean result);
    }
}
