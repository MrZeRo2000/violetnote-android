package com.romanpulov.violetnote.view;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.view.ActionMode;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.databinding.FragmentBasicNoteValueListBinding;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.*;
import com.romanpulov.violetnote.view.action.UIAction;
import com.romanpulov.violetnote.view.helper.ActionHelper;
import com.romanpulov.violetnote.view.helper.DisplayTitleBuilder;
import com.romanpulov.violetnote.view.core.AlertOkCancelSupportDialogFragment;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.helper.InputActionHelper;
import com.romanpulov.violetnote.view.helper.LoggerHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BasicNoteValueFragment extends BasicCommonNoteFragment {
    private final static String TAG = BasicNoteValueFragment.class.getSimpleName();

    private FragmentBasicNoteValueListBinding binding;
    private BasicNoteValueViewModel model;
    private AppViewModel appModel;

    BasicNoteValueRecyclerViewAdapter mRecyclerViewAdapter;

    private InputActionHelper mInputActionHelper;

    private @NonNull List<BasicNoteValueA> getBasicNoteValues() {
        return Objects.requireNonNull(model.getBasicNoteValues().getValue());
    }

    public void refreshList(DBNoteManager noteManager) {
        //mBasicNoteValueData.setValues(noteManager.mBasicNoteValueDAO.getNoteValues(mBasicNoteValueData.getNote()));
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BasicNoteValueFragment() {
    }

    public static BasicNoteValueFragment newInstance(BasicNoteValueDataA basicNoteValueDataA) {
        BasicNoteValueFragment fragment = new BasicNoteValueFragment();

        Bundle args = new Bundle();
        args.putParcelable(BasicNoteValueDataA.class.getName(), basicNoteValueDataA);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        hideAddLayout();
    }

    public void showAddLayout() {
        if (mInputActionHelper != null) {
            mInputActionHelper.showAddLayout();
        }
    }

    public void hideAddLayout() {
        if (mInputActionHelper != null) {
            mInputActionHelper.hideLayout();
        }
    }

    private void performDeleteAction(final ActionMode mode, final List<BasicNoteValueA> items) {
        AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(getString(R.string.ui_question_are_you_sure));
        dialog.setOkButtonClickListener(dialog1 -> {
            DBNoteManager noteManager = new DBNoteManager(getActivity());

            //delete
            for (BasicNoteValueA item : items)
                noteManager.mBasicNoteValueDAO.delete(item);

            refreshList(noteManager);

            //finish action
            mode.finish();
        });

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null)
            dialog.show(fragmentManager, null);

    }

    private void performEditAction(String text) {
        List<BasicNoteValueA> selectedItems = getSelectedNoteItems();
        BasicNoteValueA item;
        if ((selectedItems.size() == 1) && (!(item = selectedItems.get(0)).getValue().equals(text))) {
            //change
            item.setValue(text);

            //update
            DBNoteManager noteManager = new DBNoteManager(getContext());

            try {
                if (noteManager.mBasicNoteValueDAO.update(item) == 1) {
                    refreshList(noteManager);
                    //update list item
                    int position = getBasicNoteValues().indexOf(item);
                    if ((position != -1) && (mRecyclerView != null)) {
                        RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);
                        mRecyclerView.scrollToPosition(position);
                    }
                }
            } catch (SQLiteException e) {
                //catch possible unique index violation
                refreshList(noteManager);
                RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);
            }
        }
    }

    private void updateTitle(@NonNull ActionMode mode) {
        mode.setTitle(DisplayTitleBuilder.buildItemsDisplayTitle(getActivity(), getBasicNoteValues(), mRecyclerViewSelector.getSelectedItems()));
    }

    @NonNull
    private List<BasicNoteValueA> getSelectedNoteItems() {
        return BasicEntityNoteSelectionPosA.getItemsByPositions(getBasicNoteValues(), mRecyclerViewSelector.getSelectedItems());
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            hideAddLayout();
            mode.getMenuInflater().inflate(R.menu.menu_listitem_minimal_actions, menu);
            if (mRecyclerViewSelector.isSelected()) {
                updateTitle(mode);
            }

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            hideAddLayout();
            ActionHelper.updateActionMenu(menu, mRecyclerViewSelector.getSelectedItems().size(), getBasicNoteValues().size());
            updateTitle(mode);
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            List<BasicNoteValueA> selectedNoteItems = getSelectedNoteItems();

            if (!selectedNoteItems.isEmpty()) {
                int itemId = item.getItemId();
                if (itemId == R.id.select_all) {
                    performSelectAll();
                } else if (itemId == R.id.delete) {
                    performDeleteAction(mode, selectedNoteItems);
                    hideAddLayout();
                } else if (itemId == R.id.edit) {//performEditAction(mode, selectedNoteItems.get(0));
                    mInputActionHelper.showEditLayout(selectedNoteItems.get(0).getValue());
                }
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            hideAddLayout();
            if (mRecyclerViewSelector != null)
                mRecyclerViewSelector.destroyActionMode();
        }
    }

    private void performSelectAll() {
        mRecyclerViewSelector.setSelectedItems(ActionHelper.createSelectAllItems(getBasicNoteValues().size()));
    }

    private void performAddAction(BasicNoteValueA value) {
        DBNoteManager mNoteManager = new DBNoteManager(getActivity());
        try {
            if (mNoteManager.mBasicNoteValueDAO.insertWithNote(model.getBasicNote(), value.getValue()) != -1) {
                // refresh list
                refreshList(mNoteManager);
                RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);

                //ensure added element is visible
                int newItemPos = -1;
                for (int i = 0; i < getBasicNoteValues().size(); i++) {
                    if (getBasicNoteValues().get(i).getValue().equals(value.getValue())) {
                        newItemPos = i;
                        break;
                    }
                }
                if (newItemPos > -1)
                    mRecyclerView.scrollToPosition(newItemPos);
            }

        } catch (Exception e) {
            //catch possible unique index violation
            refreshList(mNoteManager);
            LoggerHelper.logContext(getContext(), TAG, "Error performing add action:" + e);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBasicNoteValueListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = view.getContext();

        mRecyclerView = binding.list;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        // add decoration
        mRecyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(
                getActivity(),
                RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST,
                R.drawable.divider_white_black_gradient));
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        //add action panel
        mInputActionHelper = new InputActionHelper(binding.addPanelInclude.getRoot());
        mInputActionHelper.setOnAddInteractionListener((actionType, text) -> {
            switch (actionType) {
                case InputActionHelper.INPUT_ACTION_TYPE_ADD:
                    performAddAction(BasicNoteValueA.newEditInstance(text));
                    break;
                case InputActionHelper.INPUT_ACTION_TYPE_EDIT:
                    //performEditAction();
                    performEditAction(text);
                    hideAddLayout();
                    mRecyclerViewSelector.finishActionMode();
                    break;
            }
        });

        //restore selected items
        restoreSelectedItems(savedInstanceState, view);

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_add_action, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_add) {
                    showAddLayout();
                    return true;
                } else {
                    return false;
                }
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        model = new ViewModelProvider(this).get(BasicNoteValueViewModel.class);
        model.setBasicNote(BasicNoteValueFragmentArgs.fromBundle(getArguments()).getNote());

        appModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        model.setNoteValuesChanged(appModel.getNoteValuesChanged());

        final Observer<List<BasicNoteValueA>> noteValuesObserver = newNoteValues -> {
            if (mRecyclerViewAdapter == null) {
                mRecyclerViewAdapter = new BasicNoteValueRecyclerViewAdapter(
                        newNoteValues, new ActionBarCallBack());
                mRecyclerViewSelector = mRecyclerViewAdapter.getRecyclerViewSelector();
                mRecyclerView.setAdapter(mRecyclerViewAdapter);
            } else {
                mRecyclerViewAdapter.updateNoteValues(newNoteValues);
            }

            UIAction<BasicNoteValueA> action = model.getAction();
            if (action != null) {
                action.execute(newNoteValues);
                model.resetAction();

                DialogFragment dialogFragment = (DialogFragment)getParentFragmentManager().findFragmentByTag(AlertOkCancelSupportDialogFragment.TAG);
                if (dialogFragment != null) {
                    dialogFragment.dismiss();
                }
            }
        };
        model.getBasicNoteValues().observe(this, noteValuesObserver);
    }
}
