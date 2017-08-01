package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.View;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicEntityNoteA;
import com.romanpulov.violetnote.model.BasicEntityNoteSelectionPosA;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutor;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutorHost;
import com.romanpulov.violetnote.view.action.BasicNoteDataDeleteEntityAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataItemAddAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataNoteItemAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataRefreshAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveAction;
import com.romanpulov.violetnote.view.core.AlertOkCancelSupportDialogFragment;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;
import com.romanpulov.violetnote.view.core.BasicNoteDataProgressFragment;
import com.romanpulov.violetnote.view.core.PasswordActivity;

import java.util.List;

/**
 * Base class for BasicNoteXXXItemFragment classes
 * Shall not be instantiated
 * Created by romanpulov on 09.09.2016.
 */
public abstract class BasicNoteItemFragment extends BasicCommonNoteFragment {
    protected BasicNoteDataA mBasicNoteData;
    protected DialogInterface mEditorDialog;
    protected BasicNoteDataActionExecutorHost mExecutorHost;

    protected OnBasicNoteItemFragmentInteractionListener mListener;
    private BasicNoteDataProgressFragment.OnBasicNoteDataFragmentInteractionListener mProgressListener;

    private boolean mIsProgress = false;

    public BasicNoteDataA getBasicNoteData() {
        return mBasicNoteData;
    }

    @Override
    public String getDBTableName() {
        return DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME;
    }

    @Override
    public void refreshList(DBNoteManager noteManager) {
        noteManager.queryNoteDataItems(mBasicNoteData.getNote());
    }

    public BasicNoteItemFragment() {
        super();
        setRetainInstance(true);
    }

    protected void afterExecutionCompleted() {

    };

    @Override
    public void onAttach(Activity activity) {
        Log.d("BasicNoteItemFragment", "onAttach");
        super.onAttach(activity);
        if (activity instanceof BasicNoteDataProgressFragment.OnBasicNoteDataFragmentInteractionListener) {
            mProgressListener = (BasicNoteDataProgressFragment.OnBasicNoteDataFragmentInteractionListener) activity;
            mProgressListener.onBasicNoteDataFragmentAttached(mIsProgress);
        }
        else
            throw new RuntimeException(activity.toString()
                    + " must implement OnBasicNoteDataFragmentInteractionListener");

    }


    protected void performAddAction(final BasicNoteItemA item) {
        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity().getApplicationContext(), mBasicNoteData);
        executor.addAction(getString(R.string.caption_processing), new BasicNoteDataItemAddAction(mBasicNoteData, item));
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
        executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
            @Override
            public void onExecutionCompleted(BasicNoteDataA basicNoteData, boolean result) {
                mBasicNoteData = basicNoteData;
                mExecutorHost.onExecutionCompleted();

                mIsProgress = false;

                /*
                View v = getView();
                if (v != null) {
                    View contentView = v.findViewById(R.id.content_layout);
                    View progressView = v.findViewById(R.id.progress_layout);

                    if ((contentView != null) && (progressView != null)) {
                        contentView.setVisibility(View.VISIBLE);
                        progressView.setVisibility(View.GONE);
                    }
                }
                */

                if (result) {
                    mRecyclerView.scrollToPosition(mBasicNoteData.getNote().getItems().size() - 1);
                }
            }
        });
        /*
        View v = getView();
        View contentView = v.findViewById(R.id.content_layout);
        View progressView = v.findViewById(R.id.progress_layout);
        contentView.setVisibility(View.GONE);
        progressView.setVisibility(View.VISIBLE);
        */
        if (mBasicNoteData.getNote().isEncrypted())
            mExecutorHost.execute(executor);
        else
            executor.execute();

        //mExecutorHost.execute(executor);

        //mExecutorHost.onExecutionStarted();

        mIsProgress = true;
        //executor.execute();
    }

    protected void performDeleteAction(final ActionMode mode, final List<? extends BasicEntityNoteA> items) {
        AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(getString(R.string.ui_question_are_you_sure));
        dialog.setOkButtonClickListener(new AlertOkCancelSupportDialogFragment.OnClickListener() {
            @Override
            public void OnClick(DialogFragment dialog) {
                BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity(), mBasicNoteData);
                executor.addAction(getString(R.string.caption_processing), new BasicNoteDataDeleteEntityAction(mBasicNoteData, getDBTableName(), items));
                executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
                executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
                    @Override
                    public void onExecutionCompleted(BasicNoteDataA basicNoteData, boolean result) {
                        if (result) {
                            afterExecutionCompleted();
                            mode.finish();
                        }
                        mDialogFragment.dismiss();
                        mDialogFragment = null;
                    }
                });
                executor.execute();
            }
        });

        dialog.show(getFragmentManager(), null);
        mDialogFragment = dialog;
    }

    protected void performMoveAction(final BasicNoteMoveAction<BasicNoteItemA> action, final List<BasicNoteItemA> items) {
        //executor
        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity(), mBasicNoteData);
        executor.setNoteId(mBasicNoteData.getNote().getId());

        //actions
        executor.addAction(getString(R.string.caption_processing), new BasicNoteDataNoteItemAction(mBasicNoteData, action, items));
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));

        //on complete
        executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
            @Override
            public void onExecutionCompleted(BasicNoteDataA basicNoteData, boolean result) {
                if (result)
                    afterExecutionCompleted();

                BasicEntityNoteSelectionPosA selectionPos = new BasicEntityNoteSelectionPosA(mBasicNoteData.getNote().getItems(), items);
                int selectionScrollPos = selectionPos.getDirectionPos(action.getDirection());

                if (selectionScrollPos != -1) {
                    mRecyclerViewSelector.setSelectedItems(selectionPos.getSelectedItemsPositions());
                    mRecyclerView.scrollToPosition(selectionScrollPos);
                }
            }
        });

        //execute
        executor.execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("BasicNoteItemFragment", "onCreate");
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mBasicNoteData = getArguments().getParcelable(PasswordActivity.PASS_DATA);
            if ((mBasicNoteData.getNote() != null) && (!mBasicNoteData.getNote().isEncrypted())) {
                DBNoteManager noteManager = new DBNoteManager(getActivity());
                noteManager.queryNoteDataItems(mBasicNoteData.getNote());
                noteManager.queryNoteDataValues(mBasicNoteData.getNote());
            }
        }
    }

    @Override
    public void onDetach() {
        Log.d("BasicNoteItemFragment", "onDetach");
        super.onDetach();
        mListener = null;
        mProgressListener = null;
    }

    @Override
    public void onPause() {
        // for password protected fragment close editors and action mode
        if (mBasicNoteData.getNote().isEncrypted()) {
            //close editor
            if (mEditorDialog != null) {
                mEditorDialog.dismiss();
                mEditorDialog = null;
            }
            //close dialog
            if (mDialogFragment != null) {
                mDialogFragment.dismiss();
                mDialogFragment = null;
            }

            //finish action
            mRecyclerViewSelector.finishActionMode();
        }

        super.onPause();
    }

    public interface OnBasicNoteItemFragmentInteractionListener {
        void onBasicNoteItemFragmentInteraction(BasicNoteItemA item, int position);
    }
}
