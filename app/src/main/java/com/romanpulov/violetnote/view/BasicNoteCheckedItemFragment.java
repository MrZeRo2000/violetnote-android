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
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutor;
import com.romanpulov.violetnote.view.action.BasicNoteDataItemAddAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataItemCheckOutAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataItemEditNameValueAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataItemUpdateCheckedAction;
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
                            executor.addAction(getString(R.string.caption_processing), new BasicNoteDataItemEditNameValueAction(mBasicNoteData, item));
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
                            executor.execute(mBasicNoteData.getNote().isEncrypted());
                        }
                    }
                })
                .execute();
    }

    @Override
    public void onPause() {
        //hide editors
        mAddActionHelper.hideLayout();

        super.onPause();
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

        mAddActionHelper = new AddActionHelper(view.findViewById(R.id.add_panel_include));
        if (!mBasicNoteData.getNote().isEncrypted())
            mAddActionHelper.setAutoCompleteList(mBasicNoteData.getNote().getValues());
        mAddActionHelper.setOnAddInteractionListener(new AddActionHelper.OnAddInteractionListener() {
            @Override
            public void onAddFragmentInteraction(final String text) {
                performAddAction(BasicNoteItemA.newCheckedEditInstance(text));
            }
        });

        return view;
    }

    public void showAddLayout() {
        if (mAddActionHelper != null) {
            mAddActionHelper.showLayout();
        }
    }

    public void performUpdateChecked(boolean checked) {
        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity());

        executor.addAction(getString(R.string.caption_processing), new BasicNoteDataItemUpdateCheckedAction(mBasicNoteData, checked));
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));

        executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
            @Override
            public void onExecutionCompleted(boolean result) {
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        executor.execute(mBasicNoteData.getNote().isEncrypted());
    }

    public void performCheckOutAction() {
        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity());

        executor.addAction(getString(R.string.caption_processing), new BasicNoteDataItemCheckOutAction(mBasicNoteData));
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData).requireValues());

        executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
            @Override
            public void onExecutionCompleted(boolean result) {
                mRecyclerView.getAdapter().notifyDataSetChanged();

                //update autocomplete
                if ((mAddActionHelper != null) && (!mBasicNoteData.getNote().isEncrypted()))
                    mAddActionHelper.setAutoCompleteList(mBasicNoteData.getNote().getValues());
            }
        });

        executor.execute(mBasicNoteData.getNote().isEncrypted());
    }

    public void checkOut() {
        int checkedCount = mBasicNoteData.getCheckedCount();
        if (checkedCount > 0) {
            String queryString = String.format(getString(R.string.ui_question_are_you_sure_checkout_items), new Integer[] {checkedCount});
            AlertOkCancelDialogFragment dialog = AlertOkCancelDialogFragment.newAlertOkCancelDialog(queryString);
            dialog.setOkButtonClickListener(new AlertOkCancelDialogFragment.OnClickListener() {
                @Override
                public void OnClick(DialogFragment dialog) {
                    performCheckOutAction();
                }
            });
            dialog.show(getFragmentManager(), null);
        }
    }
}
