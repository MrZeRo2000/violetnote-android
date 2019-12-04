package com.romanpulov.violetnote.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.ActionMenuView;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.romanpulov.violetnote.view.action.BasicItemsMoveBottomAction;
import com.romanpulov.violetnote.view.action.BasicItemsMoveDownAction;
import com.romanpulov.violetnote.view.action.BasicItemsMovePriorityDownAction;
import com.romanpulov.violetnote.view.action.BasicItemsMoveTopAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutorHost;
import com.romanpulov.violetnote.view.action.BasicItemsMovePriorityUpAction;
import com.romanpulov.violetnote.view.helper.BottomToolbarHelper;
import com.romanpulov.violetnote.view.helper.DisplayTitleBuilder;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutor;
import com.romanpulov.violetnote.view.action.BasicNoteDataItemEditNameValueAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataRefreshAction;
import com.romanpulov.violetnote.view.action.BasicItemsMoveUpAction;
import com.romanpulov.violetnote.view.core.NameValueInputDialog;
import com.romanpulov.violetnote.view.core.PasswordActivity;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.core.TextEditDialogBuilder;
import com.romanpulov.violetnote.view.core.TextInputDialog;
import com.romanpulov.violetnote.view.helper.InputActionHelper;
import com.romanpulov.violetnote.view.helper.InputManagerHelper;

import java.util.List;

public class BasicNoteNamedItemFragment extends BasicNoteItemFragment {

    private InputActionHelper mInputActionHelper;

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

    @Override
    public void onPause() {
        super.onPause();
        hideInputActionLayout();
    }

    public void hideInputActionLayout() {
        if (mInputActionHelper != null)
            mInputActionHelper.hideLayout();
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

    private void performEditValueAction(String text) {
        List<BasicNoteItemA> selectedNoteItems = getSelectedNoteItems();
        if (selectedNoteItems.size() == 1) {
            BasicNoteItemA item = selectedNoteItems.get(0);

            if (!(text.equals(item.getValue()))) {

                item.setValue(text);

                if (mRecyclerViewSelector != null)
                    mRecyclerViewSelector.finishActionMode();

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

    public void startHEventHistoryActivity(BasicNoteItemA item) {
        Intent intent = new Intent(getActivity(), BasicHEventNamedItemActivity.class);
        intent.putExtra(BasicHEventNamedItemActivity.class.getName(), item);
        startActivity(intent);
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
                        //performEditValueAction(mode, selectedNoteItems.get(0));
                        mInputActionHelper.showLayout(selectedNoteItems.get(0).getValue(),
                                InputActionHelper.INPUT_ACTION_TYPE_EDIT);
                        break;
                    case R.id.edit:
                        performEditAction(mode, selectedNoteItems.get(0));
                        break;
                    case R.id.history:
                        if (selectedNoteItems.size() == 1) {
                            startHEventHistoryActivity(selectedNoteItems.get(0));
                        }
                        //Toast.makeText(getContext(), "History", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.move_up:
                        performMoveAction(new BasicItemsMoveUpAction<>(mBasicNoteData, selectedNoteItems), selectedNoteItems);
                        break;
                    case R.id.move_top:
                        performMoveAction(new BasicItemsMoveTopAction<>(mBasicNoteData, selectedNoteItems), selectedNoteItems);
                        break;
                    case R.id.move_down:
                        performMoveAction(new BasicItemsMoveDownAction<>(mBasicNoteData, selectedNoteItems), selectedNoteItems);
                        break;
                    case R.id.move_bottom:
                        performMoveAction(new BasicItemsMoveBottomAction<>(mBasicNoteData, selectedNoteItems), selectedNoteItems);
                        break;
                    case R.id.priority_up:
                        performMoveAction(new BasicItemsMovePriorityUpAction<>(mBasicNoteData, selectedNoteItems), selectedNoteItems);
                        break;
                    case R.id.priority_down:
                        performMoveAction(new BasicItemsMovePriorityDownAction<>(mBasicNoteData, selectedNoteItems), selectedNoteItems);
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

            hideInputActionLayout();

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            hideInputActionLayout();

            if (mBottomToolbarHelper != null) {
                mBottomToolbarHelper.hideLayout();
            }

            if (mRecyclerViewSelector != null)
                mRecyclerViewSelector.destroyActionMode();
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            hideInputActionLayout();

            if (mBottomToolbarHelper == null) {
                setupBottomToolbarHelper();
            }

            if (mBottomToolbarHelper != null) {
                mBottomToolbarHelper.showLayout(mRecyclerViewSelector.getSelectedItems().size(), mBasicNoteData.getNote().getSummary().getItemCount());
            }

            List<BasicNoteItemA> selectedNoteItems = getSelectedNoteItems();
            if (selectedNoteItems.size() == 1) {
                mRecyclerView.scrollToPosition(mBasicNoteData.getNote().getItems().indexOf(selectedNoteItems.get(0)));
            }

            updateActionMenu(menu);
            return true;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_note_named_item_list, container, false);

        Context context = view.getContext();

        mRecyclerView = view.findViewById(R.id.list);
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

        //add action panel
        mInputActionHelper = new InputActionHelper(view.findViewById(R.id.add_panel_include));
        mInputActionHelper.setOnAddInteractionListener(new InputActionHelper.OnAddInteractionListener() {
            @Override
            public void onAddFragmentInteraction(int actionType, String text) {
                switch (actionType) {
                    case InputActionHelper.INPUT_ACTION_TYPE_EDIT:
                        hideInputActionLayout();
                        performEditValueAction(text);
                        break;
                }
            }
        });

        //restore selected items
        restoreSelectedItems(savedInstanceState, view);

        // add decoration
        mRecyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

        return view;
    }
}
