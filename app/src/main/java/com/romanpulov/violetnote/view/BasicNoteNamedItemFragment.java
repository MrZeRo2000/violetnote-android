package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicEntityNoteSelectionPosA;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutorHost;
import com.romanpulov.violetnote.view.action.BasicNoteMovePriorityDownAction;
import com.romanpulov.violetnote.view.action.BasicNoteMovePriorityUpAction;
import com.romanpulov.violetnote.view.helper.BottomToolbarHelper;
import com.romanpulov.violetnote.view.helper.DisplayTitleBuilder;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutor;
import com.romanpulov.violetnote.view.action.BasicNoteDataItemEditNameValueAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataRefreshAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveBottomAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveDownAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveTopAction;
import com.romanpulov.violetnote.view.action.BasicNoteMoveUpAction;
import com.romanpulov.violetnote.view.core.NameValueInputDialog;
import com.romanpulov.violetnote.view.core.PasswordActivity;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.core.TextEditDialogBuilder;
import com.romanpulov.violetnote.view.core.TextInputDialog;
import com.romanpulov.violetnote.view.helper.InputManagerHelper;

import java.util.List;

public class BasicNoteNamedItemFragment extends BasicNoteItemFragment {

    private BottomToolbarHelper mBottomToolbarHelper;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BasicNoteNamedItemFragment() {
    }

    public static BasicNoteNamedItemFragment newInstance(BasicNoteDataA basicNoteDataA, BasicNoteDataActionExecutorHost host) {
        BasicNoteNamedItemFragment fragment = new BasicNoteNamedItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(PasswordActivity.PASS_DATA, basicNoteDataA);
        fragment.setExecutorHost(host);
        fragment.setArguments(args);
        return fragment;
    }

    private void setupBottomToolbarHelper() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            mBottomToolbarHelper = BottomToolbarHelper.fromContext(activity, new ActionMenuView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    return processMoveMenuItemClick(menuItem);
                }
            });
        }
    }

    public void performAddAction() {
        NameValueInputDialog dialog = new NameValueInputDialog(getActivity(), getString(R.string.ui_name_value_title));
        dialog.setOnNameValueInputListener(new NameValueInputDialog.OnNameValueInputListener() {
            @Override
            public void onNameValueInput(String name, String value) {
                if ((name != null) && (value != null)) {
                    performAddAction(BasicNoteItemA.newNamedEditInstance(name, value));
                }
            }
        });
        dialog.show();
        mEditorDialog = dialog.getAlertDialog();
    }

    private void performEditValueAction(final ActionMode mode, final BasicNoteItemA item) {
        TextEditDialogBuilder textEditDialogBuilder = (new TextEditDialogBuilder(getActivity(), getString(R.string.ui_note_value), item.getValue()))
                .setNonEmptyErrorMessage(getString(R.string.error_field_not_empty));

        final AlertDialog alertDialog = textEditDialogBuilder.execute();

        textEditDialogBuilder.setOnTextInputListener(new TextInputDialog.OnTextInputListener() {
            @Override
            public void onTextInput(String text) {
                if (!text.equals(item.getValue())) {
                    //hide editor
                    View focusedView = alertDialog.getCurrentFocus();
                    InputManagerHelper.hideInput(focusedView);

                    //change
                    item.setValue(text);

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


    private void performEditAction(final ActionMode mode, final BasicNoteItemA item) {
        NameValueInputDialog dialog = new NameValueInputDialog(getActivity(), getString(R.string.ui_name_value_title));
        dialog.setNameValue(item.getName(), item.getValue());
        dialog.setOnNameValueInputListener(new NameValueInputDialog.OnNameValueInputListener() {
            @Override
            public void onNameValueInput(String name, String value) {
                if (!name.equals(item.getName()) || !value.equals(item.getValue())) {
                    //change
                    item.setName(name);
                    item.setValue(value);

                    BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity(), mBasicNoteData);
                    executor.addAction(getString(R.string.caption_processing), new BasicNoteDataItemEditNameValueAction(mBasicNoteData, item));
                    executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
                    executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
                        @Override
                        public void onExecutionCompleted(BasicNoteDataA basicNoteData, boolean result) {
                            mBasicNoteData = basicNoteData;

                            // finish anyway
                            mode.finish();

                            //clear editor reference
                            if (mEditorDialog != null) {
                                mEditorDialog.dismiss();
                                mEditorDialog = null;
                            }
                        }
                    });
                    executeActions(executor);
                }
                // finish anyway
                mode.finish();
                //
                mEditorDialog = null;
            }
        });
        dialog.show();
        mEditorDialog = dialog.getAlertDialog();
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
                    case R.id.edit:
                        performEditAction(mode, selectedNoteItems.get(0));
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
            mode.getMenuInflater().inflate(R.menu.menu_listitem_namevalue_actions, menu);

            buildMoveToOtherNotesSubMenu(menu);

            if (mRecyclerViewSelector.isSelected())
                mode.setTitle(DisplayTitleBuilder.buildItemsDisplayTitle(getActivity(), mBasicNoteData.getNote().getItems(), mRecyclerViewSelector.getSelectedItems()));
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (mBottomToolbarHelper != null) {
                mBottomToolbarHelper.hideLayout();
            }
            if (mRecyclerViewSelector != null)
                mRecyclerViewSelector.destroyActionMode();
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            if (mBottomToolbarHelper == null) {
                setupBottomToolbarHelper();
            }

            if (mBottomToolbarHelper != null) {
                mBottomToolbarHelper.showLayout(mRecyclerViewSelector.getSelectedItems().size(), mBasicNoteData.getNote().getSummary().getItemCount());
            }
            updateActionMenu(menu);
            return true;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_note_named_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;

            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

            BasicNoteNamedItemRecyclerViewAdapter recyclerViewAdapter = new BasicNoteNamedItemRecyclerViewAdapter(mBasicNoteData, new ActionBarCallBack(),
                    new OnBasicNoteItemFragmentInteractionListener() {
                        @Override
                        public void onBasicNoteItemFragmentInteraction(BasicNoteItemA item, int position) {
                            // no action currently required
                            //placeholder for future
                        }
                    }
            );
            mRecyclerViewSelector = recyclerViewAdapter.getRecyclerViewSelector();
            mRecyclerView.setAdapter(recyclerViewAdapter);

            //restore selected items
            restoreSelectedItems(savedInstanceState, view);

            // add decoration
            mRecyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));
        }
        return view;
    }
}
