package com.romanpulov.violetnote.view;

import android.content.Context;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.view.ActionMode;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.databinding.FragmentRecyclerViewBottomToolbarBinding;
import com.romanpulov.violetnote.model.*;
import com.romanpulov.violetnote.view.action.*;
import com.romanpulov.violetnote.view.core.AlertOkCancelSupportDialogFragment;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.helper.BottomToolbarHelper;
import com.romanpulov.violetnote.view.helper.DisplayTitleBuilder;

import java.util.List;
import java.util.Objects;

public class BasicNoteGroupFragment extends BasicCommonNoteFragment {

    FragmentRecyclerViewBottomToolbarBinding binding;
    private BasicNoteGroupViewModel model;
    private BasicNoteGroupItemRecyclerViewAdapter mRecyclerViewAdapter;

    private @NonNull List<BasicNoteGroupA> getBasicNoteGroupList() {
        return Objects.requireNonNull(model.getCurrentGroups().getValue());
    }

    private void setupBottomToolbar() {
        mBottomToolbarHelper = BottomToolbarHelper.from(binding.fragmentToolbarBottom, this::processMoveMenuItemClick);
        requireActivity().getMenuInflater().inflate(R.menu.menu_listitem_bottom_move_actions, binding.fragmentToolbarBottom.getMenu());
        binding.fragmentToolbarBottom.setVisibility(View.GONE);
    }

    protected boolean processMoveMenuItemClick(MenuItem menuItem) {
        List<BasicNoteGroupA> selectedNoteItems = BasicEntityNoteSelectionPosA.getItemsByPositions(
                getBasicNoteGroupList(), mRecyclerViewSelector.getSelectedItems());

        if (!selectedNoteItems.isEmpty()) {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.move_up) {
                model.moveUp(selectedNoteItems,
                        new BasicUIMoveAction<>(
                                selectedNoteItems,
                                MovementDirection.DIRECTION_UP,
                                mRecyclerViewSelector,
                                mRecyclerView));
                return true;
            } else if (itemId == R.id.move_top) {
                model.moveTop(selectedNoteItems,
                        new BasicUIMoveAction<>(
                                selectedNoteItems,
                                MovementDirection.DIRECTION_UP,
                                mRecyclerViewSelector,
                                mRecyclerView));
                return true;
            } else if (itemId == R.id.move_down) {
                model.moveDown(selectedNoteItems,
                        new BasicUIMoveAction<>(
                                selectedNoteItems,
                                MovementDirection.DIRECTION_DOWN,
                                mRecyclerViewSelector,
                                mRecyclerView));
                return true;
            } else if (itemId == R.id.move_bottom) {
                model.moveBottom(selectedNoteItems,
                        new BasicUIMoveAction<>(
                                selectedNoteItems,
                                MovementDirection.DIRECTION_DOWN,
                                mRecyclerViewSelector,
                                mRecyclerView));
                return true;
            }
        }
        return false;
    }

    private void performDeleteAction(@NonNull final ActionMode mode, @NonNull final List<BasicNoteGroupA> items) {
        // initial check
        if (items.size() != 1) {
            return;
        }

        BasicNoteGroupA item = items.get(0);

        AlertOkCancelSupportDialogFragment dialog;

        // check notes
        if (!model.isGroupEmpty(item)) {
            dialog = AlertOkCancelSupportDialogFragment.newAlertOkInfoDialog(getString(R.string.ui_error_group_delete_contain_notes, item.getDisplayTitle()));
        } else {
            dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(getString(R.string.ui_question_delete_item_are_you_sure, item.getDisplayTitle()));
            dialog.setOkButtonClickListener(dialog1 -> model.delete(items.get(0), new BasicUIDeleteAction<>(mode)));
        }

        dialog.show(getParentFragmentManager(), AlertOkCancelSupportDialogFragment.TAG);
    }

    private void performEditAction(@NonNull ActionMode mode, @NonNull final List<BasicNoteGroupA> items) {
        if (items.size() == 1) {
            mode.finish();
            BasicNoteGroupA item = items.get(0);
            BasicNoteGroupFragmentDirections.ActionBasicNoteGroupToBasicNoteGroupEdit action =
                    BasicNoteGroupFragmentDirections.actionBasicNoteGroupToBasicNoteGroupEdit();
            action.setEditItem(item);
            NavHostFragment.findNavController(BasicNoteGroupFragment.this).navigate(action);
        }
    }

    private void updateTitle(ActionMode mode) {
        if ((mRecyclerViewSelector != null) && (mRecyclerViewSelector.isSelected())) {
            mode.setTitle(DisplayTitleBuilder.buildItemsDisplayTitle(
                    getContext(), getBasicNoteGroupList(), mRecyclerViewSelector.getSelectedItems()));
        }
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            List<BasicNoteGroupA> selectedNoteItems = BasicEntityNoteSelectionPosA.getItemsByPositions(
                    getBasicNoteGroupList(), mRecyclerViewSelector.getSelectedItems());

            //int selectedItemPos = mRecyclerViewSelector.getSelectedItemPos();
            if (selectedNoteItems.size() == 1) {
                int itemId = item.getItemId();
                if (itemId == R.id.delete) {
                    performDeleteAction(mode, selectedNoteItems);
                } else if (itemId == R.id.edit) {
                    performEditAction(mode, selectedNoteItems);
                }
            }
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_edit_delete_actions, menu);
            updateTitle(mode);
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
            MenuItem menuItem = menu.findItem(R.id.edit);
            if (menuItem != null)
                menuItem.setVisible(mRecyclerViewSelector.isSelectedSingle());

            if (mBottomToolbarHelper == null) {
                setupBottomToolbar();
            }

            if (mBottomToolbarHelper != null) {
                mBottomToolbarHelper.showLayout(
                        mRecyclerViewSelector.getSelectedItems().size(), getBasicNoteGroupList().size());
            }

            if (mRecyclerViewSelector.isSelectedSingle()) {
                mRecyclerView.scrollToPosition(mRecyclerViewSelector.getSelectedItems().iterator().next());
            }

            updateTitle(mode);
            return false;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecyclerViewBottomToolbarBinding.inflate(getLayoutInflater());
        setupBottomToolbar();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = view.getContext();
        mRecyclerView = binding.fragmentList;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        // add decoration
        mRecyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(
                getActivity(),
                RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST,
                R.drawable.divider_white_black_gradient));

        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_add_action, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_add) {
                    NavHostFragment.findNavController(BasicNoteGroupFragment.this).navigate(
                            BasicNoteGroupFragmentDirections.actionBasicNoteGroupToBasicNoteGroupEdit());
                    return true;
                } else {
                    return false;
                }
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        model = new ViewModelProvider(this).get(BasicNoteGroupViewModel.class);

        final Observer<List<BasicNoteGroupA>> noteGroupsObserver = newBasicNoteGroups -> {
            if (model.getCurrentGroups().getValue() == null) {
                model.getCurrentGroups().setValue(newBasicNoteGroups);

                mRecyclerViewAdapter = new BasicNoteGroupItemRecyclerViewAdapter(newBasicNoteGroups, new ActionBarCallBack());
                mRecyclerView.setAdapter(mRecyclerViewAdapter);
                mRecyclerViewSelector = mRecyclerViewAdapter.getRecyclerViewSelector();

                //restore selected items
                restoreSelectedItems(savedInstanceState, view);
            } else {
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                    @Override
                    public int getOldListSize() {
                        return getBasicNoteGroupList().size();
                    }

                    @Override
                    public int getNewListSize() {
                        return newBasicNoteGroups.size();
                    }

                    @Override
                    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                        return getBasicNoteGroupList().get(oldItemPosition).getId() ==
                                newBasicNoteGroups.get(newItemPosition).getId();
                    }

                    @Override
                    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                        return getBasicNoteGroupList().get(oldItemPosition).equals(
                                newBasicNoteGroups.get(newItemPosition));
                    }
                });
                model.getCurrentGroups().setValue(newBasicNoteGroups);
                mRecyclerViewAdapter.setBasicNoteGroupList(newBasicNoteGroups);
                result.dispatchUpdatesTo(mRecyclerViewAdapter);

                UIAction<List<? extends BasicCommonNoteA>> action = model.getAction();
                if (action != null) {
                    action.execute(newBasicNoteGroups);
                    model.resetAction();

                    DialogFragment dialogFragment = (DialogFragment)getParentFragmentManager().findFragmentByTag(AlertOkCancelSupportDialogFragment.TAG);
                    if (dialogFragment != null) {
                        dialogFragment.dismiss();
                    }
                }
            }
        };
        model.getGroups().observe(this, noteGroupsObserver);

        getParentFragmentManager().setFragmentResultListener(
                BasicNoteGroupEditFragment.RESULT_KEY, this, (requestKey, bundle) -> {
                    BasicNoteGroupA item = bundle.getParcelable(BasicNoteGroupEditFragment.RESULT_VALUE_KEY);
                    if (item != null) {
                        if (item.getId() == 0) {
                            model.add(item, new BasicUIAddAction<>(mRecyclerView));
                        } else {
                            model.edit(item, new BasicUICallbackAction<>(
                                    () -> updateTitle(mRecyclerViewSelector.getActionMode())));
                        }
                    }
                });
    }
}
