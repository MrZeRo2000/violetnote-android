package com.romanpulov.violetnote.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicEntityNoteSelectionPosA;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteValueA;
import com.romanpulov.violetnote.model.BasicNoteValueDataA;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutorHost;
import com.romanpulov.violetnote.view.action.BasicNoteMovePriorityDownAction;
import com.romanpulov.violetnote.view.action.BasicNoteMovePriorityUpAction;
import com.romanpulov.violetnote.view.helper.DisplayTitleBuilder;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutor;
import com.romanpulov.violetnote.view.action.BasicNoteDataItemCheckOutAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataItemEditNameValueAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataItemUpdateCheckedAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataRefreshAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveBottomAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveDownAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveTopAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveUpAction;
import com.romanpulov.violetnote.view.core.AlertOkCancelSupportDialogFragment;
import com.romanpulov.violetnote.view.core.PasswordActivity;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.core.TextInputDialog;
import com.romanpulov.violetnote.view.helper.AddActionHelper;
import com.romanpulov.violetnote.view.core.TextEditDialogBuilder;
import com.romanpulov.violetnote.view.helper.CheckoutProgressHelper;
import com.romanpulov.violetnote.view.helper.InputManagerHelper;

import java.util.ArrayList;
import java.util.List;

public class BasicNoteCheckedItemFragment extends BasicNoteItemFragment {

    private AddActionHelper mAddActionHelper;
    private CheckoutProgressHelper mCheckoutProgressHelper;

    @Override
    public void refreshList(DBNoteManager noteManager) {
        super.refreshList(noteManager);
        updateCheckoutProgress();
    }

    @Override
    protected void afterExecutionCompleted() {
        updateCheckoutProgress();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BasicNoteCheckedItemFragment() {
    }

    public static BasicNoteCheckedItemFragment newInstance(BasicNoteDataA basicNoteDataA, BasicNoteDataActionExecutorHost host) {
        BasicNoteCheckedItemFragment fragment = new BasicNoteCheckedItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(PasswordActivity.PASS_DATA, basicNoteDataA);
        fragment.setExecutorHost(host);
        fragment.setArguments(args);
        return fragment;
    }

    private void updateCheckoutProgress() {
        if (mCheckoutProgressHelper != null)
            mCheckoutProgressHelper.setProgressData(mBasicNoteData.getNote().getCheckedItemCount(), mBasicNoteData.getNote().getItemCount());
    }

    private void performEditValueAction(final ActionMode mode, final BasicNoteItemA item) {
        TextEditDialogBuilder textEditDialogBuilder = (new TextEditDialogBuilder(getActivity(), getString(R.string.ui_note_value), item.getValueWithParams()))
                .setNonEmptyErrorMessage(getString(R.string.error_field_not_empty))
                .setSelectEnd(true);

        final AlertDialog alertDialog = textEditDialogBuilder.execute();

        textEditDialogBuilder.setOnTextInputListener(new TextInputDialog.OnTextInputListener() {
            @Override
            public void onTextInput(String text) {
                if (!text.equals(item.getValue())) {
                    //hide editor
                    View focusedView = alertDialog.getCurrentFocus();
                    InputManagerHelper.hideInput(focusedView);

                    //change
                    item.setValueWithParams(text);

                    // finish anyway
                    mode.finish();

                    BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity(), mBasicNoteData);
                    executor.addAction(getString(R.string.caption_processing), new BasicNoteDataItemEditNameValueAction(mBasicNoteData, item));
                    executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
                    executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
                        @Override
                        public void onExecutionCompleted(BasicNoteDataA basicNoteData, boolean result) {
                            mBasicNoteData = basicNoteData;

                            //clear editor reference
                            if (mEditorDialog != null) {
                                mEditorDialog.dismiss();
                                mEditorDialog = null;
                            }
                        }
                    });
                    executeActions(executor);
                }
            }
        });

        mEditorDialog = alertDialog;
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            List<BasicNoteItemA> selectedNoteItems = BasicEntityNoteSelectionPosA.getItemsByPositions(mBasicNoteData.getNote().getItems(), mRecyclerViewSelector.getSelectedItems());

            if (selectedNoteItems.size() > 0) {
                if ((item.getGroupId() == MENU_GROUP_OTHER_ITEMS) && (mRelatedNotes != null)) {
                    // move to other items
                    BasicNoteA otherNote = mRelatedNotes.get(item.getItemId());
                    performMoveToOtherNoteAction(mode, selectedNoteItems, otherNote);
                } else
                    // regular menu
                    switch (item.getItemId()) {
                        case R.id.delete:
                            performDeleteAction(mode, selectedNoteItems);
                            break;
                        case R.id.edit_value:
                            performEditValueAction(mode, selectedNoteItems.get(0));
                            break;
                        case R.id.move_up:
                            performMoveAction(new BasicNoteMoveUpAction<BasicNoteItemA>(), selectedNoteItems);
                            break;
                        case R.id.move_top:
                            performMoveAction(new BasicNoteMoveTopAction<BasicNoteItemA>(), selectedNoteItems);
                            break;
                        case R.id.move_down:
                            performMoveAction(new BasicNoteMoveDownAction<BasicNoteItemA>(), selectedNoteItems);
                            break;
                        case R.id.move_bottom:
                            performMoveAction(new BasicNoteMoveBottomAction<BasicNoteItemA>(), selectedNoteItems);
                            break;
                        case R.id.priority_up:
                            performMoveAction(new BasicNoteMovePriorityUpAction<BasicNoteItemA>(), selectedNoteItems);
                            break;
                        case R.id.priority_down:
                            performMoveAction(new BasicNoteMovePriorityDownAction<BasicNoteItemA>(), selectedNoteItems);
                            break;
                        case R.id.select_all:
                            performSelectAll();
                            break;
                    }
            }
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_listitem_checked_actions, menu);

            buildMoveToOtherNotesSubMenu(menu);

            if (mRecyclerViewSelector.isSelectedSingle())
                mode.setTitle(DisplayTitleBuilder.buildItemsDisplayTitle(getActivity(), mBasicNoteData.getNote().getItems(), mRecyclerViewSelector.getSelectedItems()));
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (mRecyclerViewSelector != null)
                mRecyclerViewSelector.destroyActionMode();
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            updateActionMenu(menu);
            mode.setTitle(DisplayTitleBuilder.buildItemsDisplayTitle(getActivity(), mBasicNoteData.getNote().getItems(), mRecyclerViewSelector.getSelectedItems()));
            return true;
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_basic_note_checked_item_list, container, false);

        Context context = view.getContext();
        mRecyclerView = view.findViewById(R.id.list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        BasicNoteCheckedItemRecyclerViewAdapter recyclerViewAdapter = new BasicNoteCheckedItemRecyclerViewAdapter(mBasicNoteData.getNote().getItems(), new ActionBarCallBack(),
                new OnBasicNoteItemFragmentInteractionListener() {
                    @Override
                    public void onBasicNoteItemFragmentInteraction(BasicNoteItemA item, int position) {
                        DBNoteManager manager = new DBNoteManager(getActivity());
                        //update item
                        manager.checkNoteItem(item);
                        //ensure item is updated and reload
                        BasicNoteItemA updatedItem = manager.getNoteItem(item.getId());
                        item.updateChecked(updatedItem);

                        //update checked
                        mBasicNoteData.getNote().addCheckedItemCount(item.isChecked() ? 1 : - 1);
                        updateCheckoutProgress();

                        RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);
                    }
                }
        );
        mRecyclerViewSelector = recyclerViewAdapter.getRecyclerViewSelector();
        mRecyclerView.setAdapter(recyclerViewAdapter);

        //add action panel
        mAddActionHelper = new AddActionHelper(view.findViewById(R.id.add_panel_include));
        mAddActionHelper.setOnAddInteractionListener(new AddActionHelper.OnAddInteractionListener() {
            @Override
            public void onAddFragmentInteraction(final String text) {
                performAddAction(BasicNoteItemA.newCheckedEditInstance(text));
            }
        });

        //add checkout progress
        mCheckoutProgressHelper = new CheckoutProgressHelper(view.findViewById(R.id.checkout_progress_panel_include));
        updateCheckoutProgress();

        // for not encrypted set up AutoComplete and list button
        if (!mBasicNoteData.getNote().isEncrypted()) {
            mAddActionHelper.setAutoCompleteList(mBasicNoteData.getNote().getValues());

            mAddActionHelper.setOnListClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // new intent for activity
                    Intent intent = new Intent(getActivity(), BasicNoteValueActivity.class);

                    //retrieve data
                    List<BasicNoteValueA> values = new ArrayList<>();
                    DBNoteManager manager = new DBNoteManager(getActivity());
                    manager.queryNoteDataValuesOrdered(mBasicNoteData.getNote(), values);
                    BasicNoteValueDataA noteValueDataA = BasicNoteValueDataA.newInstance(mBasicNoteData.getNote(), values);

                    //pass and start activity
                    intent.putExtra(BasicNoteValueDataA.class.getName(), noteValueDataA);
                    startActivityForResult(intent, 0);
                }
            });
        }

        //restore selected items
        restoreSelectedItems(savedInstanceState, view);

        // add decoration
        mRecyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //update values
        DBNoteManager noteManager = new DBNoteManager(getActivity());
        noteManager.queryNoteDataValues(mBasicNoteData.getNote());

        //update autocomplete
        if (mAddActionHelper != null)
            mAddActionHelper.setAutoCompleteList(mBasicNoteData.getNote().getValues());
    }

    public void showAddLayout() {
        if (mAddActionHelper != null)
            mAddActionHelper.showLayout();
    }

    public void hideAddLayout() {
        if (mAddActionHelper != null)
            mAddActionHelper.hideLayout();
    }

    public void performUpdateChecked(boolean checked) {
        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity(), mBasicNoteData);

        executor.addAction(getString(R.string.caption_processing), new BasicNoteDataItemUpdateCheckedAction(mBasicNoteData, checked));
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));

        executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
            @Override
            public void onExecutionCompleted(BasicNoteDataA basicNoteData, boolean result) {
                afterExecutionCompleted();
                RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);
            }
        });

        executeActions(executor);
    }

    public void performCheckOutAction() {
        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity(), mBasicNoteData);

        executor.addAction(getString(R.string.caption_processing), new BasicNoteDataItemCheckOutAction(mBasicNoteData));
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData).requireValues());

        executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
            @Override
            public void onExecutionCompleted(BasicNoteDataA basicNoteData, boolean result) {
                afterExecutionCompleted();
                RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);

                //update autocomplete
                if ((mAddActionHelper != null) && (!mBasicNoteData.getNote().isEncrypted()))
                    mAddActionHelper.setAutoCompleteList(mBasicNoteData.getNote().getValues());
            }
        });

        executeActions(executor);
    }

    public void checkOut() {
        int checkedCount = mBasicNoteData.getCheckedCount();
        if (checkedCount > 0) {
            String queryString = getString(R.string.ui_question_are_you_sure_checkout_items, checkedCount);
            AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(queryString);
            dialog.setOkButtonClickListener(new AlertOkCancelSupportDialogFragment.OnClickListener() {
                @Override
                public void OnClick(DialogFragment dialog) {
                    performCheckOutAction();
                }
            });

            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null)
                dialog.show(fragmentManager, null);
        }
    }
}
