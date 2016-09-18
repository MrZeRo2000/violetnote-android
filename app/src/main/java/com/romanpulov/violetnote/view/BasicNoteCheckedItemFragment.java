package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.model.BasicEntityNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.action.BasicNoteAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutor;
import com.romanpulov.violetnote.view.action.BasicNoteDataAddItemAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataDeleteItemAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataEditNameValueAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataNoteItemAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataRefreshAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveBottomAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveDownAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveTopAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveUpAction;
import com.romanpulov.violetnote.view.core.AlertOkCancelDialogFragment;
import com.romanpulov.violetnote.view.core.PasswordActivity;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.core.TextInputDialog;
import com.romanpulov.violetnote.view.helper.AddActionHelper;
import com.romanpulov.violetnote.view.core.TextEditDialogBuilder;

public class BasicNoteCheckedItemFragment extends BasicNoteItemFragment {

    private AddActionHelper mAddActionHelper;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BasicNoteCheckedItemFragment() {
    }

    public static BasicNoteCheckedItemFragment newInstance(BasicNoteDataA basicNoteDataA) {
        BasicNoteCheckedItemFragment fragment = new BasicNoteCheckedItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(PasswordActivity.PASS_DATA, basicNoteDataA);
        fragment.setArguments(args);
        return fragment;
    }

    private void performAddAction(final BasicNoteItemA item) {
        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity());
        executor.addAction(getString(R.string.caption_processing), new BasicNoteDataAddItemAction(mBasicNoteData, item));
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
        executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
            @Override
            public void onExecutionCompleted(boolean result) {
                if (result)
                    mRecyclerView.scrollToPosition(mBasicNoteData.getNote().getItems().size() - 1);
            }
        });
        if (mBasicNoteData.getNote().isEncrypted())
            executor.executeAsync();
        else
            executor.execute();
    }

    private void performDeleteAction(final ActionMode mode, final BasicNoteItemA item) {
        AlertOkCancelDialogFragment dialog = AlertOkCancelDialogFragment.newAlertOkCancelDialog(getString(R.string.ui_question_are_you_sure));
        dialog.setOkButtonClickListener(new AlertOkCancelDialogFragment.OnClickListener() {
            @Override
            public void OnClick(DialogFragment dialog) {
                BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity());
                executor.addAction(getString(R.string.caption_processing), new BasicNoteDataDeleteItemAction(mBasicNoteData, item));
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
                if (mBasicNoteData.getNote().isEncrypted())
                    executor.executeAsync();
                else
                    executor.execute();

            }
        });

        dialog.show(getFragmentManager(), null);
        mDialogFragment = dialog;
    }


    /*
    private void performDeleteAction(final ActionMode mode, final BasicNoteItemA item) {
        (new BasicNoteDeleteAction(BasicNoteCheckedItemFragment.this)).execute(mode, item);
    }
       */

    private void performEditAction(final ActionMode mode, final BasicNoteItemA item) {
        mEditorDialog = (new TextEditDialogBuilder(getActivity(), getString(R.string.ui_note_title), item.getValue()))
                .setNonEmptyErrorMessage(getString(R.string.error_field_not_empty))
                .setOnTextInputListener(new TextInputDialog.OnTextInputListener() {
                    @Override
                    public void onTextInput(String text) {
                        if (!text.equals(item.getValue())) {
                            //change
                            item.setValue(text);

                            BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity());
                            executor.addAction(getString(R.string.caption_processing), new BasicNoteDataEditNameValueAction(mBasicNoteData, item));
                            executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
                            executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
                                @Override
                                public void onExecutionCompleted(boolean result) {
                                    // finish anyway
                                    mode.finish();
                                    //clear editor reference
                                    mEditorDialog.dismiss();
                                    mEditorDialog = null;
                                }
                            });
                            if (mBasicNoteData.getNote().isEncrypted())
                                executor.executeAsync();
                            else
                                executor.execute();
                        }
                    }
                })
                .execute();
    }

    @Override
    public void onPause() {
        //hide editors
        mAddActionHelper.hideLayout();

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

        super.onPause();
    }

    private void performMoveAction(final BasicNoteAction<BasicNoteItemA> action, final BasicNoteItemA item) {
        /*
        int notePos = action.executeAndReturnNewPos(mBasicNoteData.getNote().getItems(), item);
        if (notePos != -1) {
            mRecyclerViewSelector.setSelectedView(null, notePos);
            mRecyclerView.scrollToPosition(notePos);
        }
        */
        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity());
        executor.addAction(getString(R.string.caption_processing), new BasicNoteDataNoteItemAction(mBasicNoteData, action, item));
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
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
        if (mBasicNoteData.getNote().isEncrypted())
            executor.executeAsync();
        else
            executor.execute();
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int selectedItemPos = mRecyclerViewSelector.getSelectedItemPos();
            if (selectedItemPos != -1) {
                BasicNoteItemA selectedItem = mBasicNoteData.getNote().getItems().get(selectedItemPos);
                switch (item.getItemId()) {
                    case R.id.delete:
                        performDeleteAction(mode, selectedItem);
                        break;
                    case R.id.edit:
                        performEditAction(mode, selectedItem);
                        break;
                    case R.id.move_up:
                        performMoveAction(new BasicNoteMoveUpAction<BasicNoteItemA>(BasicNoteCheckedItemFragment.this), selectedItem);
                        break;
                    case R.id.move_top:
                        performMoveAction(new BasicNoteMoveTopAction<BasicNoteItemA>(BasicNoteCheckedItemFragment.this), selectedItem);
                        break;
                    case R.id.move_down:
                        performMoveAction(new BasicNoteMoveDownAction<BasicNoteItemA>(BasicNoteCheckedItemFragment.this), selectedItem);
                        break;
                    case R.id.move_bottom:
                        performMoveAction(new BasicNoteMoveBottomAction<BasicNoteItemA>(BasicNoteCheckedItemFragment.this), selectedItem);
                        break;
                }
            }
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_listitem_generic_actions, menu);
            if (mRecyclerViewSelector.getSelectedItemPos() != -1)
                mode.setTitle( mBasicNoteData.getNote().getItems().get(mRecyclerViewSelector.getSelectedItemPos()).getValue());
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (mRecyclerViewSelector != null)
                mRecyclerViewSelector.destroyActionMode();
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_note_checked_item_list, container, false);

        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);

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

                        mRecyclerView.getAdapter().notifyItemChanged(position);
                    }
                }
        );
        mRecyclerViewSelector = recyclerViewAdapter.getRecyclerViewSelector();
        mRecyclerView.setAdapter(recyclerViewAdapter);

        // add decoration
        mRecyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

        mAddActionHelper = new AddActionHelper(view.findViewById(R.id.add_panel_include), mBasicNoteData.getNote().getValues());
        mAddActionHelper.setOnAddInteractionListener(new AddActionHelper.OnAddInteractionListener() {
            @Override
            public void onAddFragmentInteraction(final String text) {
                // create new note

                performAddAction(BasicNoteItemA.newCheckedEditInstance(text));

                /*
                final DBNoteManager manager = new DBNoteManager(getActivity());

                if ((mBasicNoteData.getNote().isEncrypted()) && (mBasicNoteData.getPassword() == null)) {
                    PasswordInputDialog passwordInputDialog = new PasswordInputDialog(getActivity());
                    passwordInputDialog.setOnTextInputListener(new TextInputDialog.OnTextInputListener() {
                        @Override
                        public void onTextInput(String password) {
                            if (!password.isEmpty()) {
                                //set new password
                                mBasicNoteData.setPassword(password);

                                //encrypt data
                                BasicNoteItemA newItem = BasicNoteItemA.newCheckedEditInstance(text);

                                //save encrypted item
                                if (PassNoteItemCryptService.encryptBasicNoteItem(newItem, password)) {
                                    manager.insertNoteItem(mBasicNoteData.getNote(), newItem);
                                }

                                //refresh list
                                refreshList(manager);
                            }
                        }
                    });
                    passwordInputDialog.show();
                    mEditorDialog = passwordInputDialog.getAlertDialog();
                } else {
                    manager.insertNoteItem(mBasicNoteData.getNote(), BasicNoteItemA.newCheckedEditInstance(text));

                    refreshList(manager);

                    mRecyclerView.scrollToPosition(mBasicNoteData.getNote().getItems().size() - 1);
                }
                */
            }
        });

        return view;
    }



    public void showAddLayout() {
        if (mAddActionHelper != null) {
            mAddActionHelper.showLayout();
        }
    }

    public void updateNoteDataChecked(boolean checked) {
        DBNoteManager noteManager = new DBNoteManager(getActivity());

        noteManager.updateNoteDataChecked(mBasicNoteData, checked);
        noteManager.queryNoteDataItems(mBasicNoteData.getNote());

        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public void checkOut() {
        int checkedCount = mBasicNoteData.getCheckedCount();
        if (checkedCount > 0) {
            String queryString = String.format(getString(R.string.ui_question_are_you_sure_checkout_items), new Integer[] {checkedCount});
            AlertOkCancelDialogFragment dialog = AlertOkCancelDialogFragment.newAlertOkCancelDialog(queryString);
            dialog.setOkButtonClickListener(new AlertOkCancelDialogFragment.OnClickListener() {
                @Override
                public void OnClick(DialogFragment dialog) {
                    DBNoteManager noteManager = new DBNoteManager(getActivity());

                    noteManager.checkOut(mBasicNoteData.getNote());

                    noteManager.queryNoteDataItems(mBasicNoteData.getNote());
                    noteManager.queryNoteDataValues(mBasicNoteData.getNote());

                    //update main list
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                    //update autocomplete
                    if (mAddActionHelper != null)
                        mAddActionHelper.setAutoCompleteList(mBasicNoteData.getNote().getValues());
                }
            });
            dialog.show(getFragmentManager(), null);
        }
    }
}
