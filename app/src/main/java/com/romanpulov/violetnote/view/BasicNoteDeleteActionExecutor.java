package com.romanpulov.violetnote.view;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.ActionMode;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.view.core.AlertOkCancelDialogFragment;

/**
 * Created by romanpulov on 07.09.2016.
 */
public class BasicNoteDeleteActionExecutor extends BasicNoteActionExecutor {

    public BasicNoteDeleteActionExecutor(Fragment fragment) {
        super(fragment);
    }

    @Override
    protected boolean execute(final ActionMode mode, final BasicCommonNoteA item) {
        AlertOkCancelDialogFragment dialog = AlertOkCancelDialogFragment.newAlertOkCancelDialog(mContext.getString(R.string.ui_question_are_you_sure));
        dialog.setOkButtonClickListener(new AlertOkCancelDialogFragment.OnClickListener() {
            @Override
            public void OnClick(DialogFragment dialog) {
                // delete item
                DBNoteManager mNoteManager = new DBNoteManager(mContext);
                mNoteManager.deleteCommonNote(mPersistenceProvider.getTableName(), item);

                // refresh list
                mPersistenceProvider.queryList();

                //finish action
                mode.finish();
            }
        });

        dialog.show(mFragment.getFragmentManager(), null);
        return true;

    }
}
