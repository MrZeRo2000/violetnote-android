package com.romanpulov.violetnote.view;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.*;
import com.romanpulov.violetnote.view.action.BasicItemsMoveDownAction;
import com.romanpulov.violetnote.view.action.BasicItemsMovePriorityDownAction;
import com.romanpulov.violetnote.view.action.BasicItemsMovePriorityUpAction;
import com.romanpulov.violetnote.view.action.BasicItemsMoveTopAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutor;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutorHost;
import com.romanpulov.violetnote.view.action.BasicNoteDataItemAddAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataRefreshAction;
import com.romanpulov.violetnote.view.action.BasicNoteItemDeleteAction;
import com.romanpulov.violetnote.view.action.BasicItemsMoveAction;
import com.romanpulov.violetnote.view.action.BasicItemsMoveBottomAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveToOtherNoteAction;
import com.romanpulov.violetnote.view.action.BasicItemsMoveUpAction;
import com.romanpulov.violetnote.view.core.AlertOkCancelSupportDialogFragment;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;
import com.romanpulov.violetnote.view.core.PasswordActivity;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.helper.ActionHelper;

import java.util.List;

/**
 * Base class for BasicNoteXXXItemFragment classes
 * Shall not be instantiated
 * Created by romanpulov on 09.09.2016.
 */
public abstract class BasicNoteItemFragment extends BasicCommonNoteFragment {
    protected final static int MENU_GROUP_OTHER_ITEMS = Menu.FIRST + 1;

    protected BasicNoteDataA mBasicNoteData;
    protected List<BasicNoteA> mRelatedNotes;

    protected DialogInterface mEditorDialog;

    private BasicNoteDataActionExecutorHost mExecutorHost;

    protected void setExecutorHost(BasicNoteDataActionExecutorHost value) {
        mExecutorHost = value;
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
        ActionHelper.updateActionMenu(menu, mRecyclerViewSelector.getSelectedItems().size(), mBasicNoteData.getNote().getSummary().getItemCount());
    }

    /**
     * Returns selected items
     * @return selected items
     */
    protected List<BasicNoteItemA> getSelectedNoteItems() {
        return BasicEntityNoteSelectionPosA.getItemsByPositions(mBasicNoteData.getNote().getItems(), mRecyclerViewSelector.getSelectedItems());
    }

    protected boolean processMoveMenuItemClick(MenuItem menuItem) {
        List<BasicNoteItemA> selectedNoteItems = getSelectedNoteItems();

        if (!selectedNoteItems.isEmpty()) {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.move_up) {
                performMoveAction(new BasicItemsMoveUpAction<>(mBasicNoteData, selectedNoteItems), selectedNoteItems);
                return true;
            } else if (itemId == R.id.move_top) {
                performMoveAction(new BasicItemsMoveTopAction<>(mBasicNoteData, selectedNoteItems), selectedNoteItems);
                return true;
            } else if (itemId == R.id.move_down) {
                performMoveAction(new BasicItemsMoveDownAction<>(mBasicNoteData, selectedNoteItems), selectedNoteItems);
                return true;
            } else if (itemId == R.id.move_bottom) {
                performMoveAction(new BasicItemsMoveBottomAction<>(mBasicNoteData, selectedNoteItems), selectedNoteItems);
                return true;
            } else if (itemId == R.id.priority_up) {
                performMoveAction(new BasicItemsMovePriorityUpAction<>(mBasicNoteData, selectedNoteItems), selectedNoteItems);
                return true;
            } else if (itemId == R.id.priority_down) {
                performMoveAction(new BasicItemsMovePriorityDownAction<>(mBasicNoteData, selectedNoteItems), selectedNoteItems);
                return true;
            }
            return false;
        } else
            return false;
    }

    /**
     * Common logic to select all items
     */
    protected void performSelectAll() {
        if (mBasicNoteData.getNote().getSummary().getItemCount() > 0)
            mRecyclerViewSelector.setSelectedItems(ActionHelper.createSelectAllItems(mBasicNoteData.getNote().getSummary().getItemCount()));
    }

    /**
     * Common logic for creation of related menu for movement to other note
     * @param menu Menu to add sub-menu
     */
    protected void buildMoveToOtherNotesSubMenu(Menu menu) {
        SubMenu subMenu = null;
        mRelatedNotes = mBasicNoteData.getRelatedNoteList();
        int order = 1;
        int relatedNoteIndex = 0;
        for (BasicNoteA relatedNote : mRelatedNotes) {
            if (subMenu == null) {
                subMenu = menu.addSubMenu(Menu.NONE, Menu.NONE, order++, getString(R.string.action_move_other));
                subMenu.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                subMenu.clearHeader();
            }
            subMenu.add(MENU_GROUP_OTHER_ITEMS, relatedNoteIndex ++, Menu.NONE, relatedNote.getTitle());
        }
    }

    private void notifyNoteGroupsChanged() {
        BasicNoteGroupViewModel.setAppNoteGroupsChanged(requireActivity().getApplication());
    }

    /**
     * Common logic for Add action
     * @param item item to add
     */
    protected void performAddAction(final BasicNoteItemA item) {
        FragmentActivity activity = getActivity();
        if (activity == null)
            throw new RuntimeException("Null activity for " + this);

        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(activity.getApplicationContext(), mBasicNoteData);
        executor.addAction(getString(R.string.caption_processing), new BasicNoteDataItemAddAction(mBasicNoteData, item));
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
        executor.setOnExecutionCompletedListener((BasicNoteDataActionExecutor.OnExecutionCompletedListener) (basicNoteData, result) -> {
            if (result) {
                afterExecutionCompleted();

                //mBasicNoteData = basicNoteData;

                RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);
                int position = mBasicNoteData.getNote().getLastNoteItemPriorityPosition(item.getPriority());

                if (position > -1)
                    mRecyclerView.scrollToPosition(position);

                notifyNoteGroupsChanged();
            }
        });

        executeActions(executor);
    }

    protected void performMoveToOtherNoteAction(final ActionMode mode, final List<BasicNoteItemA> items, final BasicNoteA otherNote) {
        String confirmationQuestion = getResources()
                .getQuantityString(R.plurals.ui_question_selected_note_items_move_to_other_note, items.size(), items.size(), otherNote.getTitle());
        AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(confirmationQuestion);
        dialog.setOkButtonClickListener(dialog1 -> {
            BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity(), mBasicNoteData);
            executor.addAction(getString(R.string.caption_processing), new BasicNoteMoveToOtherNoteAction<>(mBasicNoteData, items, otherNote));
            executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
            executor.setOnExecutionCompletedListener((BasicNoteDataActionExecutor.OnExecutionCompletedListener) (basicNoteData, result) -> {
                if (result) {
                    afterExecutionCompleted();
                    mode.finish();
                }

                mBasicNoteData = basicNoteData;

                if (mDialogFragment != null) {
                    mDialogFragment.dismiss();
                    mDialogFragment = null;
                }
                notifyNoteGroupsChanged();
            });
            executeActions(executor);
        });

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null)
            dialog.show(fragmentManager, null);

        mDialogFragment = dialog;
    }

    /**
     * Common logic for Delete action
     * @param mode ActionMode
     * @param items items to delete
     */
    protected void performDeleteAction(final ActionMode mode, final List<BasicNoteItemA> items) {
        AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment
                .newAlertOkCancelDialog(getResources().getQuantityString(R.plurals.ui_question_delete_items_are_you_sure, items.size(), items.size()));
        dialog.setOkButtonClickListener(dialog1 -> {
            BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity(), mBasicNoteData);
            executor.addAction(getString(R.string.caption_processing), new BasicNoteItemDeleteAction(mBasicNoteData, items));
            executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
            executor.setOnExecutionCompletedListener((BasicNoteDataActionExecutor.OnExecutionCompletedListener) (basicNoteData, result) -> {
                if (result) {
                    afterExecutionCompleted();
                    mode.finish();
                }

                mBasicNoteData = basicNoteData;

                if (mDialogFragment != null) {
                    mDialogFragment.dismiss();
                    mDialogFragment = null;
                }
                notifyNoteGroupsChanged();
            });
            executeActions(executor);
        });

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null)
            dialog.show(fragmentManager, null);

        mDialogFragment = dialog;
    }

    /**
     * Common logic for Move action
     * @param action movement type action
     * @param items items to move
     */
    protected void performMoveAction(final BasicItemsMoveAction<BasicNoteDataA, BasicNoteItemA> action, final List<BasicNoteItemA> items) {
        //executor
        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity(), mBasicNoteData);

        //actions
        executor.addAction(getString(R.string.caption_processing), action);
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));

        //on complete
        executor.setOnExecutionCompletedListener((BasicNoteDataActionExecutor.OnExecutionCompletedListener) (basicNoteData, result) -> {
            if (result)
                afterExecutionCompleted();

            mBasicNoteData = basicNoteData;

            BasicEntityNoteSelectionPosA selectionPos = new BasicEntityNoteSelectionPosA(mBasicNoteData.getNote().getItems(), items);
            int selectionScrollPos = selectionPos.getDirectionPos(action.getDirection());

            if (selectionScrollPos != -1) {
                mRecyclerViewSelector.setSelectedItems(selectionPos.getSelectedItemsPositions());
                mRecyclerView.scrollToPosition(selectionScrollPos);
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
                    noteManager.mBasicNoteItemDAO.fillNoteDataItemsWithSummary(note);
                    noteManager.mBasicNoteDAO.fillNoteValues(note);
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
        void onBasicNoteItemFragmentInteraction(BasicNoteItemA item);
    }
}
