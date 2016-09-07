package com.romanpulov.violetnote.view.action;

import android.support.v4.app.DialogFragment;
import android.support.v7.view.ActionMode;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.model.BasicEntityNoteA;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;
import com.romanpulov.violetnote.view.core.AlertOkCancelDialogFragment;

/**
 * Created by romanpulov on 07.09.2016.
 */
public class BasicNoteDeleteAction extends BasicNoteAction {

    public BasicNoteDeleteAction(BasicCommonNoteFragment fragment) {
        super(fragment);
    }

    @Override
    public boolean execute(final ActionMode mode, final BasicEntityNoteA item) {
        AlertOkCancelDialogFragment dialog = AlertOkCancelDialogFragment.newAlertOkCancelDialog(mContext.getString(R.string.ui_question_are_you_sure));
        dialog.setOkButtonClickListener(new AlertOkCancelDialogFragment.OnClickListener() {
            @Override
            public void OnClick(DialogFragment dialog) {
                // delete item
                DBNoteManager mNoteManager = new DBNoteManager(mContext);
                mNoteManager.deleteEntityNote(mFragment.getDBTableName(), item);

                // refresh list
                mFragment.refreshList(mNoteManager);

                //finish action
                mode.finish();
            }
        });

        dialog.show(mFragment.getFragmentManager(), null);
        return true;
    }
}
