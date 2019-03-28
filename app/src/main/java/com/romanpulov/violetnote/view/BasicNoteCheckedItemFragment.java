package com.romanpulov.violetnote.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteValueA;
import com.romanpulov.violetnote.model.BasicNoteValueDataA;
import com.romanpulov.violetnote.model.InputParser;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutorHost;
import com.romanpulov.violetnote.view.core.PassDataPasswordActivity;
import com.romanpulov.violetnote.view.helper.BottomToolbarHelper;
import com.romanpulov.violetnote.view.helper.DisplayTitleBuilder;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutor;
import com.romanpulov.violetnote.view.action.BasicNoteDataItemCheckOutAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataItemEditNameValueAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataItemUpdateCheckedAction;
import com.romanpulov.violetnote.view.action.BasicNoteDataRefreshAction;
import com.romanpulov.violetnote.view.core.AlertOkCancelSupportDialogFragment;
import com.romanpulov.violetnote.view.core.PasswordActivity;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.core.TextInputDialog;
import com.romanpulov.violetnote.view.helper.InputActionHelper;
import com.romanpulov.violetnote.view.core.TextEditDialogBuilder;
import com.romanpulov.violetnote.view.helper.CheckoutProgressHelper;
import com.romanpulov.violetnote.view.helper.InputManagerHelper;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.util.List;

public class BasicNoteCheckedItemFragment extends BasicNoteItemFragment {

    private InputActionHelper mInputActionHelper;
    private CheckoutProgressHelper mCheckoutProgressHelper;

    private long mPriceNoteParamTypeId;
    private int mCheckedUpdateInterval;

    public static final Handler mRefreshHandler = new Handler();

    private Runnable mRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            Context context = getContext();
            if (context != null) {
                try {
                    refreshListWithView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void refreshListWithView() {
        Context context = getContext();
        if (context != null) {
            BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(context, mBasicNoteData);
            executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
            executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
                @Override
                public void onExecutionCompleted(BasicNoteDataA basicNoteData, boolean result) {
                    afterExecutionCompleted();
                    RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);
                    PassDataPasswordActivity.getPasswordValidityChecker().startPeriod();
                }
            });
            executeActions(executor);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPriceNoteParamTypeId = DBBasicNoteHelper.getInstance(context).getDBDictionaryCache().getPriceNoteParamTypeId();
        if (PreferenceRepository.isInterfaceCheckedLast(context)) {
            mCheckedUpdateInterval = PreferenceRepository.getInterfaceCheckedUpdateInterval(getContext());
        } else {
            mCheckedUpdateInterval = 0;
        }
    }

    @Override
    public void refreshList(DBNoteManager noteManager) {
        super.refreshList(noteManager);
        updateCheckedItems();
        PassDataPasswordActivity.getPasswordValidityChecker().startPeriod();
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
    protected void afterExecutionCompleted() {
        updateCheckedItems();
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

    private void updateCheckedItems() {
        //update checkout progress
        if (mCheckoutProgressHelper != null) {
            mCheckoutProgressHelper.setProgressData(
                    mBasicNoteData.getNote().getSummary().getCheckedItemCount(),
                    mBasicNoteData.getNote().getSummary().getItemCount(),
                    mBasicNoteData.getCheckedLongParamTotal(mPriceNoteParamTypeId),
                    mBasicNoteData.getLongParamTotal(mPriceNoteParamTypeId)
            );
        }
    }

    private void refreshCheckedItemsDisplay() {
        if (mCheckedUpdateInterval > 0) {
            mRefreshHandler.removeMessages(0);
            mRefreshHandler.postDelayed(mRefreshRunnable, mCheckedUpdateInterval * 1000);
        }
    }

    private void performEditAction(String text, NoteItemDataUpdater noteItemDataUpdater) {
        List<BasicNoteItemA> selectedNoteItems = getSelectedNoteItems();
        if (selectedNoteItems.size() == 1) {
            BasicNoteItemA item = selectedNoteItems.get(0);

            //change
            noteItemDataUpdater.updateNoteItemData(item);
            //item.setValueWithParams(text);

            BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity(), mBasicNoteData);
            executor.addAction(getString(R.string.caption_processing), new BasicNoteDataItemEditNameValueAction(mBasicNoteData, item));
            executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
            executor.setOnExecutionCompletedListener(new BasicNoteDataActionExecutor.OnExecutionCompletedListener() {
                @Override
                public void onExecutionCompleted(BasicNoteDataA basicNoteData, boolean result) {
                    mBasicNoteData = basicNoteData;
                    updateCheckedItems();

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


    private void performEditValueAction(final ActionMode mode, final BasicNoteItemA item) {
        TextEditDialogBuilder textEditDialogBuilder = (new TextEditDialogBuilder(getActivity(), getString(R.string.ui_note_value),
                item.getValueWithFloatParams(mPriceNoteParamTypeId)))
                .setNonEmptyErrorMessage(getString(R.string.error_field_not_empty))
                .setSelectEnd(true);

        final AlertDialog alertDialog = textEditDialogBuilder.execute();

        textEditDialogBuilder.setOnTextInputListener(new TextInputDialog.OnTextInputListener() {
            @Override
            public void onTextInput(final String text) {
                if (!text.equals(item.getValueWithFloatParams(mPriceNoteParamTypeId))) {
                    //hide editor
                    View focusedView = alertDialog.getCurrentFocus();
                    InputManagerHelper.hideInput(focusedView);

                    //change
                    performEditAction(text, new NoteItemDataUpdater() {
                        @Override
                        public void updateNoteItemData(BasicNoteItemA item) {
                            item.setValueWithParams(
                                    mPriceNoteParamTypeId,
                                    text);
                        }
                    });

                    // finish anyway
                    mode.finish();
                }
            }
        });

        mEditorDialog = alertDialog;
    }

    private void updateTitle(ActionMode mode) {
        mode.setTitle(DisplayTitleBuilder.buildItemsDisplayTitle(getActivity(), mBasicNoteData.getNote().getItems(), mRecyclerViewSelector.getSelectedItems()));
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            List<BasicNoteItemA> selectedNoteItems = getSelectedNoteItems();

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
                            mInputActionHelper.showLayout(selectedNoteItems.get(0).getValueWithFloatParams(mPriceNoteParamTypeId),
                                    InputActionHelper.INPUT_ACTION_TYPE_EDIT);
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

            if (mRecyclerViewSelector.isSelectedSingle()) {
                updateTitle(mode);
            }

            hideAddLayout();

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            hideAddLayout();

            if (mBottomToolbarHelper != null) {
                mBottomToolbarHelper.hideLayout();
            }
            if (mRecyclerViewSelector != null)
                mRecyclerViewSelector.destroyActionMode();
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            hideAddLayout();

            if (mBottomToolbarHelper == null) {
                setupBottomToolbarHelper();
            }

            if (mBottomToolbarHelper != null) {
                mBottomToolbarHelper.showLayout(mRecyclerViewSelector.getSelectedItems().size(), mBasicNoteData.getNote().getSummary().getItemCount());
            }
            updateActionMenu(menu);
            updateTitle(mode);
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

        BasicNoteCheckedItemRecyclerViewAdapter recyclerViewAdapter = new BasicNoteCheckedItemRecyclerViewAdapter(mBasicNoteData, mPriceNoteParamTypeId, new ActionBarCallBack(),
                new OnBasicNoteCheckedItemFragmentInteractionListener() {
                    @Override
                    public void onBasicNoteItemFragmentInteraction(BasicNoteItemA item, int position) {
                        hideAddLayout();

                        DBNoteManager manager = new DBNoteManager(getActivity());
                        //update item
                        manager.mBasicNoteItemDAO.updateChecked(item, !item.isChecked());
                        //ensure item is updated and reload
                        BasicNoteItemA updatedItem = manager.mBasicNoteItemDAO.getById(item.getId());
                        item.updateChecked(updatedItem);

                        //update checked
                        mBasicNoteData.getNote().getSummary().addCheckedItemCount(item.isChecked() ? 1 : - 1);
                        updateCheckedItems();

                        //refresh display
                        refreshCheckedItemsDisplay();

                        RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);
                    }

                    @Override
                    public void onBasicNoteItemPriceClick(BasicNoteItemA item, int position) {
                        if (mRecyclerViewSelector.getActionMode() == null) {
                            mRecyclerViewSelector.startActionMode(getView(), position);
                            mInputActionHelper.showEditNumberLayout(item.getParamLong(mPriceNoteParamTypeId));
                        }
                    }
                }
        );
        mRecyclerViewSelector = recyclerViewAdapter.getRecyclerViewSelector();
        mRecyclerView.setAdapter(recyclerViewAdapter);

        //add action panel
        mInputActionHelper = new InputActionHelper(view.findViewById(R.id.add_panel_include));
        mInputActionHelper.setOnAddInteractionListener(new InputActionHelper.OnAddInteractionListener() {
            @Override
            public void onAddFragmentInteraction(final int actionType, final String text) {
                if (mBasicNoteData.getNote().isEncrypted()) {
                    InputManagerHelper.hideInput(view);
                }
                switch (actionType) {
                    case InputActionHelper.INPUT_ACTION_TYPE_ADD:
                        performAddAction(BasicNoteItemA.newCheckedEditInstance(
                                mPriceNoteParamTypeId,
                                text));
                        break;
                    case InputActionHelper.INPUT_ACTION_TYPE_EDIT:
                        performEditAction(text, new NoteItemDataUpdater() {
                            @Override
                            public void updateNoteItemData(BasicNoteItemA item) {
                                item.setValueWithParams(
                                        mPriceNoteParamTypeId,
                                        text);
                            }
                        });
                        hideAddLayout();
                        mRecyclerViewSelector.finishActionMode();
                        break;
                    case InputActionHelper.INPUT_ACTION_TYPE_NUMBER:
                        performEditAction(text, new NoteItemDataUpdater() {
                            @Override
                            public void updateNoteItemData(BasicNoteItemA item) {
                                item.setParamLong(mPriceNoteParamTypeId, InputParser.getLongValueFromString(text));
                            }
                        });
                        hideAddLayout();
                        mRecyclerViewSelector.finishActionMode();
                        break;
                }
            }
        });

        //add checkout progress
        mCheckoutProgressHelper = new CheckoutProgressHelper(view.findViewById(R.id.checkout_progress_panel_include));
        updateCheckedItems();

        // for not encrypted set up AutoComplete and list button
        if (!mBasicNoteData.getNote().isEncrypted()) {
            mInputActionHelper.setAutoCompleteList(mBasicNoteData.getNote().getValues());

            mInputActionHelper.setOnListClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // new intent for activity
                    Intent intent = new Intent(getActivity(), BasicNoteValueActivity.class);

                    //retrieve data
                    DBNoteManager manager = new DBNoteManager(getActivity());
                    List<BasicNoteValueA> values = manager.mBasicNoteValueDAO.getByNoteId(mBasicNoteData.getNote().getId());
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
        noteManager.mBasicNoteDAO.fillNoteValues(mBasicNoteData.getNote());

        //update autocomplete
        if (mInputActionHelper != null)
            mInputActionHelper.setAutoCompleteList(mBasicNoteData.getNote().getValues());
    }

    public void showAddLayout() {
        if (mInputActionHelper != null)
            mInputActionHelper.showAddLayout();
    }

    public void hideAddLayout() {
        if (mInputActionHelper != null)
            mInputActionHelper.hideLayout();
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
                if ((mInputActionHelper != null) && (!mBasicNoteData.getNote().isEncrypted()))
                    mInputActionHelper.setAutoCompleteList(mBasicNoteData.getNote().getValues());
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

    public interface OnBasicNoteCheckedItemFragmentInteractionListener extends OnBasicNoteItemFragmentInteractionListener {
        void onBasicNoteItemPriceClick(BasicNoteItemA item, int position);
    }

    private interface NoteItemDataUpdater {
        void updateNoteItemData(BasicNoteItemA item);
    }
}
