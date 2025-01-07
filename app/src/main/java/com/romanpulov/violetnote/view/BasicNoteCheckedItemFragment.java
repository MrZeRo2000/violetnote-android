package com.romanpulov.violetnote.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.databinding.FragmentBasicNoteCheckedItemListBinding;
import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.dao.BasicNoteItemDAO;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.*;
import com.romanpulov.violetnote.view.action.BasicNoteDataActionExecutorHost;
import com.romanpulov.violetnote.view.action.UIAction;
import com.romanpulov.violetnote.view.core.*;
import com.romanpulov.violetnote.view.helper.*;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.util.List;

public class BasicNoteCheckedItemFragment extends BasicCommonNoteFragment implements OnBasicNoteCheckedItemInteractionListener {
    private final static String TAG = BasicNoteCheckedItemFragment.class.getSimpleName();

    public static final int RESULT_CODE_VALUES = 0;
    public static final int RESULT_CODE_HISTORY = 1;

    private FragmentBasicNoteCheckedItemListBinding binding;
    private BasicNoteItemViewModel model;

    private BasicNoteCheckedItemRecyclerViewAdapter mRecyclerViewAdapter;

    private InputActionHelper mInputActionHelper;
    private CheckoutProgressHelper mCheckoutProgressHelper;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private long mPriceNoteParamTypeId;
    private int mCheckedUpdateInterval;

    private BasicNoteItemParamsSummary mParamsSummary;

    public static final Handler mRefreshHandler = new Handler(Looper.getMainLooper());

    private final Runnable mRefreshRunnable = () -> {
        Context context = getContext();
        if (context != null) {
            try {
                refreshListWithView();
            } catch (Exception e) {
                LoggerHelper.logContext(getContext(), TAG, "Error refreshing list:" + e);
            }
        }
    };

    private void setupBottomToolbar() {
        /*
        mBottomToolbarHelper = BottomToolbarHelper.from(binding.fragmentToolbarBottom, this::processMoveMenuItemClick);
        requireActivity().getMenuInflater().inflate(R.menu.menu_listitem_bottom_move_actions, binding.fragmentToolbarBottom.getMenu());
        binding.fragmentToolbarBottom.setVisibility(View.GONE);

         */
    }

    public void refreshListWithView() {
        /*
        Context context = getContext();
        if (context != null) {
            BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(context, mBasicNoteData);
            executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
            executor.setOnExecutionCompletedListener((BasicNoteDataActionExecutor.OnExecutionCompletedListener) (basicNoteData, result) -> {
                afterExecutionCompleted();
                RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);
                PassDataPasswordActivity.getPasswordValidityChecker().startPeriod();
            });
            executeActions(executor);
        }

         */
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mPriceNoteParamTypeId = DBBasicNoteHelper.getInstance(context).getDBDictionaryCache().getPriceNoteParamTypeId();
        if (PreferenceRepository.isInterfaceCheckedLast(context)) {
            mCheckedUpdateInterval = PreferenceRepository.getInterfaceCheckedUpdateInterval(getContext());
        } else {
            mCheckedUpdateInterval = 0;
        }
    }

    @Override
    public void onBasicNoteItemPriceClick(BasicNoteItemA item, int position) {
        if (mRecyclerViewSelector.getActionMode() == null) {
            mRecyclerViewSelector.startActionMode(requireView(), position);
            mInputActionHelper.showEditNumberLayout(
                    item.getParamLong(mPriceNoteParamTypeId),
                    InputParser.getNumberDisplayStyle(mParamsSummary.getIsInt()));
        }
    }

    @Override
    public void onBasicNoteItemFragmentInteraction(BasicNoteItemA item) {
        hideAddLayout();

        DBNoteManager manager = new DBNoteManager(getActivity());
        //update item
        manager.mBasicNoteItemDAO.updateChecked(item, !item.isChecked());
        //ensure item is updated and reload
        BasicNoteItemA updatedItem = manager.mBasicNoteItemDAO.getById(item.getId());
        item.updateChecked(updatedItem);

        //update summary
        updateParamsSummary();

        //update checked
                        /*
                        mBasicNoteData.getNote().getSummary().addCheckedItemCount(item.isChecked() ? 1 : - 1);
                        updateCheckedItems();

                         */

        // update groups totals
        notifyNoteGroupsChanged();

        //refresh display
        refreshCheckedItemsDisplay();

        RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);
    }

    private void setupBottomToolbarHelper() {/*
        FragmentActivity activity = getActivity();
        if (activity != null) {
            mBottomToolbarHelper = BottomToolbarHelper.fromContext(activity, this::processMoveMenuItemClick);
        }
        */
    }

    private void notifyNoteGroupsChanged() {
        BasicNoteGroupViewModel.setAppNoteGroupsChanged(requireActivity().getApplication());
    }

    private void afterExecutionCompleted() {
        updateParamsSummary();
        updateCheckedItems();
        notifyNoteGroupsChanged();
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
        //fragment.setExecutorHost(host);
        fragment.setArguments(args);
        return fragment;
    }

    private void updateParamsSummary() {
        /*
        mParamsSummary = mBasicNoteData.getParamsSummary(mPriceNoteParamTypeId);

         */
    }

    private void updateCheckedItems() {
        /*
        //update checkout progress
        if ((mCheckoutProgressHelper != null) && (mParamsSummary != null)) {
            mCheckoutProgressHelper.setProgressData(
                    mBasicNoteData.getNote().getSummary().getCheckedItemCount(),
                    mBasicNoteData.getNote().getSummary().getItemCount(),
                    mParamsSummary.getCheckedDisplayValue(),
                    mParamsSummary.getTotalDisplayValue(),
                    mParamsSummary.getIsInt()
            );
        }

         */
    }

    private void refreshCheckedItemsDisplay() {
        if (mCheckedUpdateInterval > 0) {
            mRefreshHandler.removeMessages(0);
            mRefreshHandler.postDelayed(mRefreshRunnable, mCheckedUpdateInterval * 1000L);
        }
    }

    private void performEditAction(NoteItemDataUpdater noteItemDataUpdater) {
        /*
        List<BasicNoteItemA> selectedNoteItems = getSelectedNoteItems();
        if (selectedNoteItems.size() == 1) {
            BasicNoteItemA item = selectedNoteItems.get(0);

            //change
            noteItemDataUpdater.updateNoteItemData(item);
            //item.setValueWithParams(text);

            BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity(), mBasicNoteData);
            executor.addAction(getString(R.string.caption_processing), new BasicNoteDataItemEditNameValueAction(mBasicNoteData, item));
            executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));
            executor.setOnExecutionCompletedListener((BasicNoteDataActionExecutor.OnExecutionCompletedListener) (basicNoteData, result) -> {
                mBasicNoteData = basicNoteData;
                afterExecutionCompleted();

                //clear editor reference
                if (mEditorDialog != null) {
                    mEditorDialog.dismiss();
                    mEditorDialog = null;
                }
            });
            executeActions(executor);
        }

         */
    }

    private void updateTitle(ActionMode mode) {
        /*
        mode.setTitle(DisplayTitleBuilder.buildItemsDisplayTitle(
                getActivity(),
                mBasicNoteData.getNote().getItems(),
                mRecyclerViewSelector.getSelectedItems()));

         */
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            /*
            List<BasicNoteItemA> selectedNoteItems = getSelectedNoteItems();

            if (!selectedNoteItems.isEmpty()) {
                if ((item.getGroupId() == MENU_GROUP_OTHER_ITEMS) && (mRelatedNotes != null)) {
                    // move to other items
                    BasicNoteA otherNote = mRelatedNotes.get(item.getItemId());
                    performMoveToOtherNoteAction(mode, selectedNoteItems, otherNote);
                } else
                    // regular menu
                {
                    int itemId = item.getItemId();
                    if (itemId == R.id.delete) {
                        performDeleteAction(mode, selectedNoteItems);
                    } else if (itemId == R.id.edit_value) {
                        BasicNoteItemA selectedNote = selectedNoteItems.get(0);
                        mInputActionHelper.showEditLayout(
                            InputParser.composeFloatParams(
                                selectedNote.getValue(),
                                selectedNote.getNoteItemParams().getLong(mPriceNoteParamTypeId),
                                InputParser.getNumberDisplayStyle(mParamsSummary.getIsInt())));
                    } else if (itemId == R.id.select_all) {
                        performSelectAll();
                    }
                }
            }

             */
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            /*
            mode.getMenuInflater().inflate(R.menu.menu_listitem_checked_actions, menu);

            buildMoveToOtherNotesSubMenu(menu);

            if (mRecyclerViewSelector.isSelectedSingle()) {
                updateTitle(mode);
            }

            hideAddLayout();

             */

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
            /*
            hideAddLayout();

            if (mBottomToolbarHelper == null) {
                setupBottomToolbarHelper();
            }

            if (mBottomToolbarHelper != null) {
                mBottomToolbarHelper.showLayout(
                        mRecyclerViewSelector.getSelectedItems().size(),
                        mBasicNoteData.getNote().getSummary().getItemCount());
            }

            List<BasicNoteItemA> selectedNoteItems = getSelectedNoteItems();
            if (selectedNoteItems.size() == 1) {
                mRecyclerView.scrollToPosition(
                        mBasicNoteData.getNote().getItems().indexOf(selectedNoteItems.get(0)));
            }

            updateActionMenu(menu);
            updateTitle(mode);

             */
            return true;
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBasicNoteCheckedItemListBinding.inflate(getLayoutInflater());
        setupBottomToolbar();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = view.getContext();
        mRecyclerView = binding.list;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        // add decoration
        mRecyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        //swipe refresh
        mSwipeRefreshLayout = binding.swiperefresh;
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (mRecyclerViewSelector.getActionMode() == null) {
                performRefresh();
            }
        });

        //add action panel
        mInputActionHelper = new InputActionHelper(binding.addPanelInclude.getRoot());
        mInputActionHelper.setOnAddInteractionListener((actionType, text) -> {
            /*
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
                    performEditAction(item -> item.setValueWithParams(
                            mPriceNoteParamTypeId,
                            text));
                    hideAddLayout();
                    mRecyclerViewSelector.finishActionMode();
                    break;
                case InputActionHelper.INPUT_ACTION_TYPE_NUMBER:
                    performEditAction(item -> item.setParamLong(mPriceNoteParamTypeId, InputParser.getLongValueFromString(text)));
                    hideAddLayout();
                    mRecyclerViewSelector.finishActionMode();
                    break;
            }

             */
        });

        updateParamsSummary();

        //add checkout progress
        mCheckoutProgressHelper = new CheckoutProgressHelper(binding.checkoutProgressPanelInclude.getRoot());
        updateCheckedItems();

        // for not encrypted set up AutoComplete and list button
        /*
        if (!mBasicNoteData.getNote().isEncrypted()) {
            mInputActionHelper.setAutoCompleteList(mBasicNoteData.getNote().getValues());

            mInputActionHelper.setOnListClickListener(v -> {
                // new intent for activity
                Intent intent = new Intent(getActivity(), BasicNoteValueActivity.class);

                //retrieve data
                DBNoteManager manager = new DBNoteManager(getActivity());
                List<BasicNoteValueA> values = manager.mBasicNoteValueDAO.getByNoteId(mBasicNoteData.getNote().getId());
                BasicNoteValueDataA noteValueDataA = BasicNoteValueDataA.newInstance(mBasicNoteData.getNote(), values);

                //pass and start activity
                intent.putExtra(BasicNoteValueDataA.class.getName(), noteValueDataA);
                startActivityForResult(intent, RESULT_CODE_VALUES);
            });
        }

         */

        //restore selected items
        restoreSelectedItems(savedInstanceState, view);

        model = new ViewModelProvider(this).get(BasicNoteItemViewModel.class);
        model.setBasicNote(BasicNoteCheckedItemFragmentArgs.fromBundle(getArguments()).getNote());

        final Observer<List<BasicNoteItemA>> noteItemsObserver = newNoteItems -> {
            if (mRecyclerViewAdapter == null) {
                mRecyclerViewAdapter = new BasicNoteCheckedItemRecyclerViewAdapter(
                        newNoteItems,
                        model.getBasicNoteItemParamsSummary(mPriceNoteParamTypeId),
                        mPriceNoteParamTypeId,
                        new ActionBarCallBack(),
                        this);
                mRecyclerViewSelector = mRecyclerViewAdapter.getRecyclerViewSelector();
                mRecyclerView.setAdapter(mRecyclerViewAdapter);
            } else {
                mRecyclerViewAdapter.updateItemsWithSummary(newNoteItems, model.getBasicNoteItemParamsSummary(mPriceNoteParamTypeId));
            }

            UIAction<BasicNoteItemA> action = model.getAction();
            if (action != null) {
                action.execute(newNoteItems);
                model.resetAction();

                DialogFragment dialogFragment = (DialogFragment)getParentFragmentManager().findFragmentByTag(AlertOkCancelSupportDialogFragment.TAG);
                if (dialogFragment != null) {
                    dialogFragment.dismiss();
                }
            }
        };
        model.getBasicNoteItems().observe(this, noteItemsObserver);

    }

    public void startHEventHistoryActivity() {
        /*
        Intent intent = new Intent(getActivity(), BasicHEventCOItemActivity.class);
        intent.putExtra(BasicHEventCOItemActivity.class.getName(), mBasicNoteData.getNote());
        startActivityForResult(intent, RESULT_CODE_HISTORY);

         */
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
        switch (requestCode) {
            case RESULT_CODE_VALUES:
                //update values
                DBNoteManager noteManager = new DBNoteManager(getActivity());
                noteManager.mBasicNoteDAO.fillNoteValues(mBasicNoteData.getNote());

                //update autocomplete
                if (mInputActionHelper != null)
                    mInputActionHelper.setAutoCompleteList(mBasicNoteData.getNote().getValues());
                break;
            case RESULT_CODE_HISTORY:
                Bundle bundle;
                if ((data != null) && ((bundle = data.getExtras()) != null)) {
                    String[] selectedItemsArray = bundle.getStringArray(KEY_SELECTED_ITEMS_RETURN_DATA);
                    if ((selectedItemsArray != null) && (selectedItemsArray.length > 0)) {
                        performAddListValuesAction(selectedItemsArray);
                    }
                }
                break;
        }

         */
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
        /*
        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity(), mBasicNoteData);

        executor.addAction(getString(R.string.caption_processing), new BasicNoteDataItemUpdateCheckedAction(mBasicNoteData, checked));
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData));

        executor.setOnExecutionCompletedListener((BasicNoteDataActionExecutor.OnExecutionCompletedListener) (basicNoteData, result) -> {
            afterExecutionCompleted();
            RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);
        });

        executeActions(executor);

         */
    }

    public void performCheckOutAction() {
        /*
        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity(), mBasicNoteData);

        executor.addAction(getString(R.string.caption_processing), new BasicNoteDataItemCheckOutAction(mBasicNoteData));
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData).requireValues());

        executor.setOnExecutionCompletedListener((BasicNoteDataActionExecutor.OnExecutionCompletedListener) (basicNoteData, result) -> {
            afterExecutionCompleted();
            RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);

            //update autocomplete
            if ((mInputActionHelper != null) && (!mBasicNoteData.getNote().isEncrypted()))
                mInputActionHelper.setAutoCompleteList(mBasicNoteData.getNote().getValues());
        });

        executeActions(executor);

         */
    }

    public void performRefresh() {
        /*
        if (!mBasicNoteData.getNote().isEncrypted() || PasswordActivity.getPasswordValidityChecker().isValid()) {
            refreshListWithView();
            setSwipeRefreshing(false);
        } else {
            setSwipeRefreshing(false);
            Activity activity = getActivity();
            if (activity instanceof BasicNoteCheckedItemActivity) {
                ((BasicNoteCheckedItemActivity)activity).requestInvalidateFragment();
            }
        }

         */
    }

    public void performAddListValuesAction(String[] values) {
        /*
        // create executor
        BasicNoteDataActionExecutor executor = new BasicNoteDataActionExecutor(getActivity(), mBasicNoteData);

        // configure executor
        executor.addAction(getString(R.string.caption_processing), new BasicNoteDataItemAddUniqueValuesAction(mBasicNoteData, values));
        executor.addAction(getString(R.string.caption_loading), new BasicNoteDataRefreshAction(mBasicNoteData).requireValues());

        // on completion
        executor.setOnExecutionCompletedListener((BasicNoteDataActionExecutor.OnExecutionCompletedListener) (basicNoteData, result) -> {
            afterExecutionCompleted();
            RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);
        });

        // execute
        executeActions(executor);

         */
    }

    public void setSwipeRefreshing(boolean value) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(value);
        }
    }

    public void checkOut() {
        //int checkedCount = mBasicNoteData.getCheckedDisplayValue();
        int checkedCount = mParamsSummary.getCheckedCount();
        if (checkedCount > 0) {
            String queryString = getResources().getQuantityString(
                    R.plurals.ui_question_are_you_sure_checkout_items, checkedCount, checkedCount);
            AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(queryString);
            dialog.setOkButtonClickListener(dialog1 -> performCheckOutAction());

            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null)
                dialog.show(fragmentManager, null);
        }
    }

    private interface NoteItemDataUpdater {
        void updateNoteItemData(BasicNoteItemA item);
    }
}
