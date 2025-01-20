package com.romanpulov.violetnote.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.databinding.FragmentBasicNoteNamedItemListBinding;
import com.romanpulov.violetnote.model.*;
import com.romanpulov.violetnote.view.action.*;
import com.romanpulov.violetnote.view.core.*;
import com.romanpulov.violetnote.view.helper.*;

import java.util.List;
import java.util.Objects;

public class BasicNoteNamedItemFragment extends BasicCommonNoteFragment {

    private FragmentBasicNoteNamedItemListBinding binding;
    private BasicNoteNamedItemViewModel model;

    private BasicNoteNamedItemRecyclerViewAdapter mRecyclerViewAdapter;

    private InputActionHelper mInputActionHelper;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BasicNoteNamedItemFragment() {
    }

    private @NonNull List<BasicNoteItemA> getNoteItems() {
        return Objects.requireNonNull(model.getBasicNoteItems().getValue());
    }

    @NonNull
    private List<BasicNoteItemA> getSelectedNoteItems() {
        return getSelectedItems(this::getNoteItems);
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
    public void onPause() {
        super.onPause();
        hideInputActionLayout();
    }

    public void hideInputActionLayout() {
        if (mInputActionHelper != null)
            mInputActionHelper.hideLayout();
    }

    private void performEditValueAction(String text) {
        List<BasicNoteItemA> selectedNoteItems = getSelectedNoteItems();
        if (selectedNoteItems.size() == 1) {
            BasicNoteItemA item = selectedNoteItems.get(0);

            if (!(text.trim().equals(item.getValue()))) {
                item.setValue(text.trim());

                model.editNameValue(item, new BasicUIFinishAction<>(mRecyclerViewSelector.getActionMode()));
            }
        }
    }

    protected void performDeleteAction(final List<BasicNoteItemA> items) {
        AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment
                .newAlertOkCancelDialog(getResources().getQuantityString(R.plurals.ui_question_delete_items_are_you_sure, items.size(), items.size()));
        dialog.setOkButtonClickListener(dialog1 ->
                model.delete(items, new BasicUIFinishAction<>(mRecyclerViewSelector.getActionMode())));

        dialog.show(getParentFragmentManager(), AlertOkCancelSupportDialogFragment.TAG);
    }

    private void performSelectAll() {
        List<BasicNoteItemA> noteItems = model.getBasicNoteItems().getValue();
        if ((noteItems != null) && (!noteItems.isEmpty())) {
            mRecyclerViewSelector.setSelectedItems(ActionHelper.createSelectAllItems(noteItems.size()));
        }
    }

    protected void performMoveToOtherNoteAction(final List<BasicNoteItemA> items, final BasicNoteA otherNote) {
        String confirmationQuestion = getResources()
                .getQuantityString(R.plurals.ui_question_selected_note_items_move_to_other_note, items.size(), items.size(), otherNote.getTitle());
        AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(confirmationQuestion);
        dialog.setOkButtonClickListener(dialog1 ->
                model.moveToOtherNote(items, otherNote, new BasicUIFinishAction<>(mRecyclerViewSelector.getActionMode())));
        dialog.show(getParentFragmentManager(), AlertOkCancelSupportDialogFragment.TAG);
    }

    public void startHEventHistoryActivity(BasicNoteItemA item) {
        Intent intent = new Intent(getActivity(), BasicHEventNamedItemActivity.class);
        intent.putExtra(BasicHEventNamedItemActivity.class.getName(), item);
        startActivity(intent);
    }

    private void updateTitle(ActionMode mode) {
        if (mRecyclerViewSelector.isSelected()) {
            mode.setTitle(DisplayTitleBuilder.buildItemsDisplayTitle(
                    getActivity(), getNoteItems(), mRecyclerViewSelector.getSelectedItems()));
        }
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            List<BasicNoteItemA> selectedNoteItems = getSelectedNoteItems();

            if (!selectedNoteItems.isEmpty()) {
                if ((item.getGroupId() == MenuHelper.MENU_GROUP_OTHER_ITEMS) && (model.getRelatedNotes().getValue() != null)) {
                    // move to other items
                    BasicNoteA otherNote = model.getRelatedNotes().getValue().get(item.getItemId());
                    performMoveToOtherNoteAction(selectedNoteItems, otherNote);
                } else
                    // regular menu
                {
                    int itemId = item.getItemId();
                    if (itemId == R.id.delete) {
                        performDeleteAction(selectedNoteItems);
                    } else if (itemId == R.id.edit_value) {
                        mInputActionHelper.showEditLayout(selectedNoteItems.get(0).getValue());
                    } else if (itemId == R.id.edit) {
                        NavHostFragment.findNavController(BasicNoteNamedItemFragment.this).navigate(
                                BasicNoteNamedItemFragmentDirections.actionBasicNoteNamedItemToBasicNoteNamedItemEdit()
                                        .setEditItem(selectedNoteItems.get(0)));
                    } else if (itemId == R.id.history) {
                        if (selectedNoteItems.size() == 1) {
                            startHEventHistoryActivity(selectedNoteItems.get(0));
                        }
                    } else if (itemId == R.id.select_all) {
                        performSelectAll();
                    }
                }
            }
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_listitem_namevalue_actions, menu);

            model.getRelatedNotes().observe(BasicNoteNamedItemFragment.this, relatedNotes ->
                    MenuHelper.buildMoveToOtherSubMenu(requireContext(), menu, relatedNotes));

            updateTitle(mode);

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
                setupBottomToolbar();
            }

            if (mBottomToolbarHelper != null) {
                mBottomToolbarHelper.showLayout(mRecyclerViewSelector.getSelectedItems().size(), getNoteItems().size());
            }

            List<BasicNoteItemA> selectedNoteItems = getSelectedNoteItems();
            if (selectedNoteItems.size() == 1) {
                mRecyclerView.scrollToPosition(getNoteItems().indexOf(selectedNoteItems.get(0)));
            }

            ActionHelper.updateActionMenu(
                    menu,
                    mRecyclerViewSelector.getSelectedItems().size(),
                    getNoteItems().size());
            return true;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBasicNoteNamedItemListBinding.inflate(getLayoutInflater());
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

        //add action panel
        mInputActionHelper = new InputActionHelper(view.findViewById(R.id.add_panel_include));
        mInputActionHelper.setOnAddInteractionListener((actionType, text) -> {
            if (actionType == InputActionHelper.INPUT_ACTION_TYPE_EDIT) {
                hideInputActionLayout();
                performEditValueAction(text);
            }
        });

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_add_action, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_add) {
                    NavHostFragment.findNavController(BasicNoteNamedItemFragment.this).navigate(
                            BasicNoteNamedItemFragmentDirections.actionBasicNoteNamedItemToBasicNoteNamedItemEdit());
                    return true;
                } else {
                    return false;
                }
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        model = new ViewModelProvider(this).get(BasicNoteNamedItemViewModel.class);
        model.setBasicNote(BasicNoteNamedItemFragmentArgs.fromBundle(getArguments()).getNote());

        AppViewModel appModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        model.setNoteGroupsChanged(appModel.getNoteGroupsChanged());

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).
                setTitle(model.getBasicNote().getTitle());

        Observer<List<BasicNoteItemA>> noteItemsObserver = newNoteItems -> {
            if (mRecyclerViewAdapter == null) {
                mRecyclerViewAdapter = new BasicNoteNamedItemRecyclerViewAdapter(
                        newNoteItems,
                        new ActionBarCallBack(),
                        BasicNoteNamedItemFragment.this::startHEventHistoryActivity
                );

                mRecyclerViewSelector = mRecyclerViewAdapter.getRecyclerViewSelector();
                mRecyclerView.setAdapter(mRecyclerViewAdapter);

                //restore selected items
                restoreSelectedItems(savedInstanceState, view);
            } else {
                mRecyclerViewAdapter.updateItems(newNoteItems);
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

        getParentFragmentManager().setFragmentResultListener(
                BasicNoteNamedItemEditFragment.RESULT_KEY, this, (requestKey, bundle) -> {
                    BasicNoteItemA item = bundle.getParcelable(BasicNoteNamedItemEditFragment.RESULT_VALUE_KEY);
                    if (item != null) {
                        if (item.getId() == 0) {
                            model.add(item, new BasicUIAddAction<>(mRecyclerView));
                        } else {
                            model.editNameValue(item, new BasicUICallbackAction<>(
                                    () -> updateTitle(mRecyclerViewSelector.getActionMode())));
                        }
                    }
                });
    }
}
