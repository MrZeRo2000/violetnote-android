package com.romanpulov.violetnote.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.databinding.FragmentBasicNoteCheckedItemListBinding;
import com.romanpulov.violetnote.model.*;
import com.romanpulov.violetnote.view.action.*;
import com.romanpulov.violetnote.view.core.*;
import com.romanpulov.violetnote.view.helper.*;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class BasicNoteCheckedItemFragment extends BasicCommonNoteFragment implements OnBasicNoteCheckedItemInteractionListener {
    private final static String TAG = BasicNoteCheckedItemFragment.class.getSimpleName();
    protected final static int MENU_GROUP_OTHER_ITEMS = Menu.FIRST + 1;

    public static final int RESULT_CODE_VALUES = 0;
    public static final int RESULT_CODE_HISTORY = 1;

    private FragmentBasicNoteCheckedItemListBinding binding;
    private BasicNoteItemViewModel model;
    private AppViewModel appModel;

    private BasicNoteCheckedItemRecyclerViewAdapter mRecyclerViewAdapter;

    private InputActionHelper mInputActionHelper;
    private CheckoutProgressHelper mCheckoutProgressHelper;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private int mCheckedUpdateInterval;

    public static final Handler mRefreshHandler = new Handler(Looper.getMainLooper());

    private final Runnable mRefreshRunnable = () -> {
        Context context = getContext();
        if (context != null) {
            try {
                model.refresh();
            } catch (Exception e) {
                LoggerHelper.logContext(getContext(), TAG, "Error refreshing list:" + e);
            }
        }
    };

    private @NonNull List<BasicNoteItemA> getNoteItemList() {
        return Objects.requireNonNull(model.getBasicNoteItems().getValue());
    }

    @NonNull
    private List<BasicNoteItemA> getSelectedNoteItems() {
        return getSelectedItems(this::getNoteItemList);
    }

    private boolean processMoveMenuItemClick(MenuItem menuItem) {
        List<BasicNoteItemA> selectedNoteItems = getSelectedNoteItems();
        return internalProcessMoveMenuItemClick(menuItem, selectedNoteItems, model);
    }

    private void setupBottomToolbar() {
        mBottomToolbarHelper = BottomToolbarHelper.from(binding.fragmentToolbarBottom, this::processMoveMenuItemClick);
        requireActivity().getMenuInflater().inflate(R.menu.menu_listitem_bottom_move_actions, binding.fragmentToolbarBottom.getMenu());
        binding.fragmentToolbarBottom.setVisibility(View.GONE);
    }

    @Override
    public void onBasicNoteItemPriceClick(BasicNoteItemA item, int position) {
        if (mRecyclerViewSelector.getActionMode() == null) {
            mRecyclerViewSelector.startActionMode(requireView(), position);
            mInputActionHelper.showEditNumberLayout(
                    item.getParamLong(appModel.getPriceNoteParamTypeId()),
                    InputParser.getNumberDisplayStyle(model.getBasicNoteItemParamsSummary().getIsInt()));
        }
    }

    @Override
    public void onBasicNoteItemFragmentInteraction(BasicNoteItemA item) {
        hideAddLayout();
        model.toggleChecked(item);

        //refresh display
        refreshCheckedItemsDisplay();
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

    private void updateCheckedItems() {
        //update checkout progress
        if (mCheckoutProgressHelper != null) {
            mCheckoutProgressHelper.setProgressData(
                    model.getBasicNoteSummary().getCheckedItemCount(),
                    model.getBasicNoteSummary().getItemCount(),
                    model.getBasicNoteItemParamsSummary().getCheckedDisplayValue(),
                    model.getBasicNoteItemParamsSummary().getTotalDisplayValue(),
                    model.getBasicNoteItemParamsSummary().getIsInt()
            );
        }
    }

    private void refreshCheckedItemsDisplay() {
        if (mCheckedUpdateInterval > 0) {
            mRefreshHandler.removeMessages(0);
            mRefreshHandler.postDelayed(mRefreshRunnable, mCheckedUpdateInterval * 1000L);
        }
    }

    private void performEditAction(Consumer<BasicNoteItemA> itemConsumer) {
        List<BasicNoteItemA> selectedNoteItems = getSelectedNoteItems();
        if (selectedNoteItems.size() == 1) {
            BasicNoteItemA item = selectedNoteItems.get(0);

            //change
            itemConsumer.accept(item);
            model.editNameValue(item, new BasicUIFinishAction<>(mRecyclerViewSelector.getActionMode()));
        }
    }

    private void performSelectAll() {
        List<BasicNoteItemA> noteItems = model.getBasicNoteItems().getValue();
        if ((noteItems != null) && (!noteItems.isEmpty())) {
            mRecyclerViewSelector.setSelectedItems(ActionHelper.createSelectAllItems(noteItems.size()));
        }
    }

    protected void performMoveToOtherNoteAction(final ActionMode mode, final List<BasicNoteItemA> items, final BasicNoteA otherNote) {
        String confirmationQuestion = getResources()
                .getQuantityString(R.plurals.ui_question_selected_note_items_move_to_other_note, items.size(), items.size(), otherNote.getTitle());
        AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(confirmationQuestion);
        dialog.setOkButtonClickListener(dialog1 -> {
            model.moveToOtherNote(items, otherNote, new BasicUIFinishAction<>(mRecyclerViewSelector.getActionMode()));
            /*

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

             */
        });
        dialog.show(getParentFragmentManager(), AlertOkCancelSupportDialogFragment.TAG);
    }

    private void updateTitle(ActionMode mode) {
        mode.setTitle(DisplayTitleBuilder.buildItemsDisplayTitle(
                getActivity(),
                getNoteItemList(),
                mRecyclerViewSelector.getSelectedItems()));
    }

    /**
     * Common logic for creation of related menu for movement to other note
     * @param menu Menu to add sub-menu
     */
    protected void buildMoveToOtherNotesSubMenu(Menu menu, Collection<BasicNoteA> relatedNotes) {
        SubMenu subMenu = null;
        int order = 1;
        int relatedNoteIndex = 0;
        for (BasicNoteA relatedNote : relatedNotes) {
            if (subMenu == null) {
                subMenu = menu.addSubMenu(Menu.NONE, Menu.NONE, order++, getString(R.string.action_move_other));
                subMenu.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                subMenu.clearHeader();
            }
            subMenu.add(MENU_GROUP_OTHER_ITEMS, relatedNoteIndex ++, Menu.NONE, relatedNote.getTitle());
        }
    }

    /**
     * Common code to update action menu
     * @param menu Menu to update
     */
    protected void updateActionMenu(Menu menu) {
        ActionHelper.updateActionMenu(
                menu,
                mRecyclerViewSelector.getSelectedItems().size(),
                model.getBasicNoteSummary().getItemCount());
    }


    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            List<BasicNoteItemA> selectedNoteItems = getSelectedNoteItems();

            if (!selectedNoteItems.isEmpty()) {
                if ((item.getGroupId() == MENU_GROUP_OTHER_ITEMS) && (model.getRelatedNotes().getValue() != null)) {
                    // move to other items
                    BasicNoteA otherNote = model.getRelatedNotes().getValue().get(item.getItemId());
                    performMoveToOtherNoteAction(mode, selectedNoteItems, otherNote);
                } else
                    // regular menu
                {
                    int itemId = item.getItemId();
                    if (itemId == R.id.delete) {
                        model.delete(selectedNoteItems, new BasicUIFinishAction<>(mRecyclerViewSelector.getActionMode()));
                    } else if (itemId == R.id.edit_value) {
                        BasicNoteItemA selectedNote = selectedNoteItems.get(0);
                        mInputActionHelper.showEditLayout(
                            InputParser.composeFloatParams(
                                selectedNote.getValue(),
                                selectedNote.getNoteItemParams().getLong(model.getPriceNoteParamTypeId()),
                                InputParser.getNumberDisplayStyle(model.getBasicNoteItemParamsSummary().getIsInt())));
                    } else if (itemId == R.id.select_all) {
                        performSelectAll();
                    }
                }
            }
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_listitem_checked_actions, menu);

            model.getRelatedNotes().observe(BasicNoteCheckedItemFragment.this, relatedNotes ->
                    buildMoveToOtherNotesSubMenu(menu, relatedNotes));

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

            if (mBottomToolbarHelper != null) {
                mBottomToolbarHelper.showLayout(
                        mRecyclerViewSelector.getSelectedItems().size(),
                        model.getBasicNoteSummary().getItemCount());
            }

            List<BasicNoteItemA> selectedNoteItems = getSelectedNoteItems();
            if (selectedNoteItems.size() == 1) {
                mRecyclerView.scrollToPosition(
                        getNoteItemList().indexOf(selectedNoteItems.get(0)));
            }

            updateActionMenu(menu);
            updateTitle(mode);

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
            if (model.getBasicNote().isEncrypted()) {
                InputManagerHelper.hideInput(view);
            }
            switch (actionType) {
                case InputActionHelper.INPUT_ACTION_TYPE_ADD:
                    model.add(
                            BasicNoteItemA.newCheckedEditInstance(
                                    model.getBasicNote().getId(),
                                    model.getPriceNoteParamTypeId(), text),
                                    new BasicUIAddAction<>(mRecyclerView));
                    break;
                case InputActionHelper.INPUT_ACTION_TYPE_EDIT:
                    performEditAction(item -> item.setValueWithParams(
                            model.getPriceNoteParamTypeId(),
                            text));
                    hideAddLayout();
                    mRecyclerViewSelector.finishActionMode();
                    break;
                case InputActionHelper.INPUT_ACTION_TYPE_NUMBER:
                    performEditAction(item -> item.setParamLong(model.getPriceNoteParamTypeId(), InputParser.getLongValueFromString(text)));
                    hideAddLayout();
                    mRecyclerViewSelector.finishActionMode();
                    break;
            }
        });

        //add checkout progress
        mCheckoutProgressHelper = new CheckoutProgressHelper(binding.checkoutProgressPanelInclude.getRoot());

        //restore selected items
        restoreSelectedItems(savedInstanceState, view);

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_checked_item, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.action_add) {
                    showAddLayout();
                    return true;
                } else if (itemId == R.id.action_check_all) {
                    model.updateAllChecked(true);
                    return true;
                } else if (itemId == R.id.action_uncheck_all) {
                    model.updateAllChecked(false);
                    return true;
                } else if (itemId == R.id.action_checkout) {
                    performCheckOut();
                    return true;
                } else if (itemId == R.id.action_history) {
                    NavHostFragment.findNavController(BasicNoteCheckedItemFragment.this).navigate(
                            BasicNoteCheckedItemFragmentDirections.actionBasicNoteCheckedItemToBasicHistoryEventCoItem()
                                    .setNote(model.getBasicNote()));
                    return true;
                } else if (itemId == R.id.action_refresh) {
                    setSwipeRefreshing(true);
                    performRefresh();
                    return true;
                } else {
                    return false;
                }
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        model = new ViewModelProvider(this).get(BasicNoteItemViewModel.class);
        model.setBasicNote(BasicNoteCheckedItemFragmentArgs.fromBundle(getArguments()).getNote());

        appModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        model.setPriceNoteParamTypeId(appModel.getPriceNoteParamTypeId());
        model.setNoteGroupsChanged(appModel.getNoteGroupsChanged());
        model.setNoteCheckedItemChanged(appModel.getNoteCheckedItemChanged());

        final Observer<List<BasicNoteItemA>> noteItemsObserver = newNoteItems -> {
            if (mRecyclerViewAdapter == null) {
                mRecyclerViewAdapter = new BasicNoteCheckedItemRecyclerViewAdapter(
                        newNoteItems,
                        model.getBasicNoteItemParamsSummary(),
                        new ActionBarCallBack(),
                        this);
                mRecyclerViewSelector = mRecyclerViewAdapter.getRecyclerViewSelector();
                mRecyclerView.setAdapter(mRecyclerViewAdapter);
            } else {
                mRecyclerViewAdapter.updateItemsWithSummary(
                        newNoteItems,
                        model.getBasicNoteItemParamsSummary());
            }
            setSwipeRefreshing(false);
            updateCheckedItems();

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

        // for not encrypted set up AutoComplete and list button
        if (!model.getBasicNote().isEncrypted()) {
            final Observer<Collection<String>> valuesObserver = values -> {
                mInputActionHelper.setAutoCompleteList(values);
                mInputActionHelper.setOnListClickListener(v ->
                        NavHostFragment.findNavController(BasicNoteCheckedItemFragment.this).navigate(
                            BasicNoteCheckedItemFragmentDirections
                                    .actionBasicNoteCheckedItemToBasicNoteValue()
                                    .setNote(model.getBasicNote())));
            };
            model.getValues().observe(this, valuesObserver);
        }

        if (PreferenceRepository.isInterfaceCheckedLast(context)) {
            mCheckedUpdateInterval = PreferenceRepository.getInterfaceCheckedUpdateInterval(getContext());
        } else {
            mCheckedUpdateInterval = 0;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Boolean.TRUE.equals(appModel.getNoteValuesChanged().getValue())) {
            model.loadValues();
        }
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
        model.refresh();
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

    public void performCheckOut() {
        //int checkedCount = mBasicNoteData.getCheckedDisplayValue();
        int checkedCount = model.getBasicNoteItemParamsSummary().getCheckedCount();
        if (checkedCount > 0) {
            String queryString = getResources().getQuantityString(
                    R.plurals.ui_question_are_you_sure_checkout_items, checkedCount, checkedCount);
            AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(queryString);
            dialog.setOkButtonClickListener(dialog1 ->
                    model.checkout());

            dialog.show(getParentFragmentManager(), AlertOkCancelSupportDialogFragment.TAG);
        }
    }
}
