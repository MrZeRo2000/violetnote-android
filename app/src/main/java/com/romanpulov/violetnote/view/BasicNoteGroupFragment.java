package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.view.ActionMode;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.databinding.FragmentRecyclerViewBottomToolbarBinding;
import com.romanpulov.violetnote.model.BasicEntityNoteSelectionPosA;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.model.BasicNoteGroupViewModel;
import com.romanpulov.violetnote.model.MovementDirection;
import com.romanpulov.violetnote.view.action.*;
import com.romanpulov.violetnote.view.core.AlertOkCancelSupportDialogFragment;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.helper.BottomToolbarHelper;
import com.romanpulov.violetnote.view.helper.DisplayTitleBuilder;

import java.util.List;

public class BasicNoteGroupFragment extends BasicCommonNoteFragment {
    public static final int ACTIVITY_REQUEST_INSERT = 0;
    public static final int ACTIVITY_REQUEST_EDIT = 1;

    FragmentRecyclerViewBottomToolbarBinding binding;
    private BasicNoteGroupViewModel model;
    private List<BasicNoteGroupA> mBasicNoteGroupList;
    private BasicNoteGroupItemRecyclerViewAdapter mRecyclerViewAdapter;

    public static BasicNoteGroupFragment newInstance() {
        return new BasicNoteGroupFragment();
    }

    public BasicNoteGroupFragment() {

    }

    private void setupBottomToolbar() {
        mBottomToolbarHelper = BottomToolbarHelper.from(binding.fragmentToolbarBottom, this::processMoveMenuItemClick);
        requireActivity().getMenuInflater().inflate(R.menu.menu_listitem_bottom_move_actions, binding.fragmentToolbarBottom.getMenu());
        binding.fragmentToolbarBottom.setVisibility(View.GONE);
    }

    protected boolean processMoveMenuItemClick(MenuItem menuItem) {
        List<BasicNoteGroupA> selectedNoteItems = BasicEntityNoteSelectionPosA.getItemsByPositions(mBasicNoteGroupList, mRecyclerViewSelector.getSelectedItems());

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

    public void performAddAction(@NonNull final BasicNoteGroupA item) {
        model.add(item, new BasicUIAddAction<>(mRecyclerView));
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

    private void performEditAction(@NonNull final List<BasicNoteGroupA> items) {
        if (items.size() == 1) {
            BasicNoteGroupA item = items.get(0);
            Intent intent = new Intent(getContext(), BasicNoteGroupEditActivity.class);
            intent.putExtra(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA, item);
            startActivityForResult(intent, ACTIVITY_REQUEST_EDIT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final BasicNoteGroupA noteGroup;
        if ((requestCode == BasicNoteGroupFragment.ACTIVITY_REQUEST_EDIT)
                && (resultCode == Activity.RESULT_OK)
                && (data != null)
                && ((noteGroup = data.getParcelableExtra(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA)) != null)) {
            model.edit(noteGroup, new BasicUICallbackAction<>(() -> updateTitle(mRecyclerViewSelector.getActionMode())));
        }
    }

    private void updateTitle(ActionMode mode) {
        if ((mRecyclerViewSelector != null) && (mRecyclerViewSelector.isSelected())) {
            mode.setTitle(DisplayTitleBuilder.buildItemsDisplayTitle(getContext(), mBasicNoteGroupList, mRecyclerViewSelector.getSelectedItems()));
        }
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            List<BasicNoteGroupA> selectedNoteItems = BasicEntityNoteSelectionPosA.getItemsByPositions(mBasicNoteGroupList, mRecyclerViewSelector.getSelectedItems());

            //int selectedItemPos = mRecyclerViewSelector.getSelectedItemPos();
            if (selectedNoteItems.size() == 1) {
                int itemId = item.getItemId();
                if (itemId == R.id.delete) {
                    performDeleteAction(mode, selectedNoteItems);
                } else if (itemId == R.id.edit) {
                    performEditAction(selectedNoteItems);
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
            //menu.getItem(R.menu.)
            MenuItem menuItem = menu.findItem(R.id.edit);
            if (menuItem != null)
                menuItem.setVisible(mRecyclerViewSelector.isSelectedSingle());

            if (mBottomToolbarHelper == null) {
                setupBottomToolbar();
            }

            if (mBottomToolbarHelper != null) {
                mBottomToolbarHelper.showLayout(mRecyclerViewSelector.getSelectedItems().size(), mBasicNoteGroupList.size());
            }

            updateTitle(mode);
            return false;
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.view_recycler_view_list, container, false);
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
        mRecyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

        model = new ViewModelProvider(requireActivity()).get(BasicNoteGroupViewModel.class);

        final Observer<List<BasicNoteGroupA>> noteGroupsObserver = newBasicNoteGroups -> {
            if (mBasicNoteGroupList == null) {
                mBasicNoteGroupList = newBasicNoteGroups;

                mRecyclerViewAdapter = new BasicNoteGroupItemRecyclerViewAdapter(mBasicNoteGroupList, new ActionBarCallBack());
                mRecyclerView.setAdapter(mRecyclerViewAdapter);
                mRecyclerViewSelector = mRecyclerViewAdapter.getRecyclerViewSelector();

                //restore selected items
                restoreSelectedItems(savedInstanceState, view);
            } else {
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                    @Override
                    public int getOldListSize() {
                        return mBasicNoteGroupList.size();
                    }

                    @Override
                    public int getNewListSize() {
                        return newBasicNoteGroups.size();
                    }

                    @Override
                    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                        return mBasicNoteGroupList.get(oldItemPosition).getId() ==
                                newBasicNoteGroups.get(newItemPosition).getId();
                    }

                    @Override
                    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                        return mBasicNoteGroupList.get(oldItemPosition).equals(
                                newBasicNoteGroups.get(newItemPosition));
                    }
                });
                mBasicNoteGroupList = newBasicNoteGroups;
                mRecyclerViewAdapter.setBasicNoteGroupList(mBasicNoteGroupList);
                result.dispatchUpdatesTo(mRecyclerViewAdapter);

                UIAction<List<BasicNoteGroupA>> action = model.getAction();
                if (action != null) {
                    action.execute(mBasicNoteGroupList);
                    model.resetAction();

                    DialogFragment dialogFragment = (DialogFragment)getParentFragmentManager().findFragmentByTag(AlertOkCancelSupportDialogFragment.TAG);
                    if (dialogFragment != null) {
                        dialogFragment.dismiss();
                    }
                }
            }
        };
        model.getGroups().observe(this, noteGroupsObserver);
    }
}
