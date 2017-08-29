package com.romanpulov.violetnote.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicEntityNoteSelectionPosA;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutor;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutorHost;
import com.romanpulov.violetnote.view.action.BasicNoteDataItemAddAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataNoteItemAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataRefreshAction;
import com.romanpulov.violetnote.view.action.BasicNoteDeleteAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveAction;
import com.romanpulov.violetnote.view.core.AlertOkCancelSupportDialogFragment;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;
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

    private BasicNoteDataActionExecutorHost mExecutorHost;

    protected void setExecutorHost(BasicNoteDataActionExecutorHost value) {
        mExecutorHost = value;
    }

    protected OnBasicNoteItemFragmentInteractionListener mListener;

    public BasicNoteDataA getBasicNoteData() {
        return mBasicNoteData;
    }

    @Override
    public void refreshList(DBNoteManager noteManager) {
        noteManager.queryNoteDataItems(mBasicNoteData.getNote());
    }

    protected void afterExecutionCompleted() {
        //some action after execution
    }

    /**
     * Common logic to execute sync or async action
     * @param executor actions to execute
     */
    protected void executeActions(BasicNoteDataActionExecutor executor) {
        if (mBasicNoteData.getNote().isEncrypted())
            mExecutorHost.execute(executor);
        else
            executor.execute();
    }

    /**
     * Common code to update action menu
     * @param menu Menu to update
     */
    protected void updateActionMenu(Menu menu) {
        for (int menuIndex = 0; menuIndex < menu.size(); menuIndex ++) {
            MenuItem menuItem = menu.getItem(menuIndex);

            if (menuItem != null) {
                switch (menuItem.getItemId()) {
                    case R.id.select_all:
                        menuItem.setVisible(!((mRecyclerViewSelector != null) && (mRecyclerViewSelector.getSelectedItems().size() == mBasicNoteData.getNote().getItemCount())));
                        break;
                    case R.id.edit_value:
                    case R.id.edit:
                        menuItem.setVisible(mRecyclerViewSelector.getSelectedItems().size() == 1);
                        break;
                    case R.id.move_up:
                    case R.id.move_down:
                    case R.id.move_top:
                    case R.id.move_bottom:
                    case R.id.priority_up:
                    case R.id.priority_down:
                        menuItem.setVisible(mRecyclerViewSelector.getSelectedItems().size() != mBasicNoteData.getNote().getItemCount());
                        break;
                }
            }
        }
    }


    /**
     * Common logic to select all items
     */
    protected void performSelectAll() {
        Integer[] selectedItems = new Integer[mBasicNoteData.getNote().getItemCount()];

        for (int i = 0; i < mBasicNoteData.getNote().getItemCount(); i ++)
            selectedItems[i] = i;

        mRecyclerViewSelector.setSelectedItems(selectedItems);
    }


    /**
     * Common logic for Add action
     * @param item item to add
     */
    protected void performAddAction(final BasicNoteItemA item) {
        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity().getApplicationContext(), mBasicNoteData);
        executor.addAction(getString(R.string.caption_processing), new BasicNoteDataItemAddAction(mBasicNoteData, item));
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
        executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
            @Override
            public void onExecutionCompleted(BasicNoteDataA basicNoteData, boolean result) {
                if (result) {
                    afterExecutionCompleted();

                    mBasicNoteData = basicNoteData;

                    mRecyclerView.getAdapter().notifyDataSetChanged();
                    int position = mBasicNoteData.getNote().getLastNoteItemPriorityPosition(item.getPriority());

                    if (position > -1)
                        mRecyclerView.scrollToPosition(position);
                }
            }
        });

        executeActions(executor);
    }

    /**
     * Common logic for Delete action
     * @param mode ActionMode
     * @param items items to delete
     */
    protected void performDeleteAction(final ActionMode mode, final List<BasicNoteItemA> items) {
        AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(getString(R.string.ui_question_are_you_sure));
        dialog.setOkButtonClickListener(new AlertOkCancelSupportDialogFragment.OnClickListener() {
            @Override
            public void OnClick(DialogFragment dialog) {
                BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity(), mBasicNoteData);
                executor.addAction(getString(R.string.caption_processing), new BasicNoteDataNoteItemAction(mBasicNoteData, new BasicNoteDeleteAction<BasicNoteItemA>(), items));
                executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
                executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
                    @Override
                    public void onExecutionCompleted(BasicNoteDataA basicNoteData, boolean result) {
                        if (result) {
                            afterExecutionCompleted();
                            mode.finish();
                        }

                        mBasicNoteData = basicNoteData;

                        if (mDialogFragment != null) {
                            mDialogFragment.dismiss();
                            mDialogFragment = null;
                        }
                    }
                });
                executeActions(executor);
            }
        });

        dialog.show(getFragmentManager(), null);
        mDialogFragment = dialog;
    }

    /**
     * Common logic for Move action
     * @param action movement type action
     * @param items items to move
     */
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

                mBasicNoteData = basicNoteData;

                BasicEntityNoteSelectionPosA selectionPos = new BasicEntityNoteSelectionPosA(mBasicNoteData.getNote().getItems(), items);
                int selectionScrollPos = selectionPos.getDirectionPos(action.getDirection());

                if (selectionScrollPos != -1) {
                    mRecyclerViewSelector.setSelectedItems(selectionPos.getSelectedItemsPositions());
                    mRecyclerView.scrollToPosition(selectionScrollPos);
                }
            }
        });

        //execute
        executeActions(executor);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mBasicNoteData = getArguments().getParcelable(PasswordActivity.PASS_DATA);

            if (mBasicNoteData != null) {
                BasicNoteA note = mBasicNoteData.getNote();

                if ((note != null) && (!note.isEncrypted())) {
                    DBNoteManager noteManager = new DBNoteManager(getActivity());
                    noteManager.queryNoteDataItems(note);
                    noteManager.queryNoteDataValues(note);
                }
            }
        }
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
