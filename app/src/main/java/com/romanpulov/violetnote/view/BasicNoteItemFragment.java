package com.romanpulov.violetnote.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.view.ActionMode;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicEntityNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.action.BasicNoteAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutor;
import com.romanpulov.violetnote.view.action.BasicNoteDataDeleteEntityAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataItemAddAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataNoteItemAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataRefreshAction;
import com.romanpulov.violetnote.view.core.AlertOkCancelDialogFragment;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;
import com.romanpulov.violetnote.view.core.PasswordActivity;

/**
 * Created by romanpulov on 09.09.2016.
 */
public class BasicNoteItemFragment extends BasicCommonNoteFragment {
    protected BasicNoteDataA mBasicNoteData;
    protected DialogInterface mEditorDialog;

    protected OnBasicNoteItemFragmentInteractionListener mListener;

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

    protected void performAddAction(final BasicNoteItemA item) {
        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity());
        executor.addAction(getString(R.string.caption_processing), new BasicNoteDataItemAddAction(mBasicNoteData, item));
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
        executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
            @Override
            public void onExecutionCompleted(boolean result) {
                if (result)
                    mRecyclerView.scrollToPosition(mBasicNoteData.getNote().getItems().size() - 1);
            }
        });
        executor.execute(mBasicNoteData.getNote().isEncrypted());
    }

    protected void performDeleteAction(final ActionMode mode, final BasicNoteItemA item) {
        AlertOkCancelDialogFragment dialog = AlertOkCancelDialogFragment.newAlertOkCancelDialog(getString(R.string.ui_question_are_you_sure));
        dialog.setOkButtonClickListener(new AlertOkCancelDialogFragment.OnClickListener() {
            @Override
            public void OnClick(DialogFragment dialog) {
                BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity());
                executor.addAction(getString(R.string.caption_processing), new BasicNoteDataDeleteEntityAction(mBasicNoteData, getDBTableName(), item));
                executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
                executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
                    @Override
                    public void onExecutionCompleted(boolean result) {
                        if (result)
                            mode.finish();
                        mDialogFragment.dismiss();
                        mDialogFragment = null;
                    }
                });
                executor.execute(mBasicNoteData.getNote().isEncrypted());
            }
        });

        dialog.show(getFragmentManager(), null);
        mDialogFragment = dialog;
    }

    protected void performMoveAction(final BasicNoteAction<BasicNoteItemA> action, final BasicNoteItemA item) {
        //executor
        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity());
        executor.setNoteId(mBasicNoteData.getNote().getId());

        //actions
        executor.addAction(getString(R.string.caption_processing), new BasicNoteDataNoteItemAction(mBasicNoteData, action, item));
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));

        //on complete
        executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
            @Override
            public void onExecutionCompleted(boolean result) {
                int notePos = BasicEntityNoteA.getNotePosWithId(mBasicNoteData.getNote().getItems(), item.getId());
                if (notePos != -1) {
                    mRecyclerViewSelector.setSelectedView(null, notePos);
                    mRecyclerView.scrollToPosition(notePos);
                }
            }
        });

        //execute
        executor.execute(mBasicNoteData.getNote().isEncrypted());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mBasicNoteData = getArguments().getParcelable(PasswordActivity.PASS_DATA);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
