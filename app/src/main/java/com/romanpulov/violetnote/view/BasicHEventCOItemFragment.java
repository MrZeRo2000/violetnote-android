package com.romanpulov.violetnote.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.databinding.FragmentExpandableListViewBinding;
import com.romanpulov.violetnote.model.*;
import com.romanpulov.violetnote.view.core.AlertOkCancelSupportDialogFragment;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;
import com.romanpulov.violetnote.view.core.ViewSelectorHelper;
import com.romanpulov.violetnote.view.helper.DisplayTitleBuilder;

import java.util.*;

public class BasicHEventCOItemFragment extends BasicCommonNoteFragment {
    public static final String RESULT_KEY = BasicCommonNoteFragment.class.getName() + "_RESULT_KEY";
    public static final String RESULT_VALUE_KEY = BasicCommonNoteFragment.class.getName() + "_RESULT_VALUE_KEY";

    private FragmentExpandableListViewBinding binding;
    private BasicHNoteCOItemViewModel model;

    private ViewSelectorHelper.AbstractViewSelector<BasicHNoteCOItemA> mViewSelector;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BasicHEventCOItemFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExpandableListViewBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(this).get(BasicHNoteCOItemViewModel.class);
        BasicNoteA note = BasicHEventCOItemFragmentArgs.fromBundle(getArguments()).getNote();
        model.setBasicNote(note);

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).
                setTitle(getString(R.string.title_activity_basic_history_event_co_item,
                        Objects.requireNonNull(note).getTitle()));

        final Observer<BasicHNoteCOItemViewModel.BasicHEventHNoteCOItems> hEventHNoteCOItemsObserver =
                hEventHNoteCOItems -> {
                    //controls
                    ExpandableListView exListView = binding.exList;

                    BasicHEventCOItemExpandableListViewAdapter exListViewAdapter = new BasicHEventCOItemExpandableListViewAdapter(
                            getContext(),
                            hEventHNoteCOItems.hEvents(),
                            hEventHNoteCOItems.hNoteCOItems(),
                            hEventHNoteCOItems.values(),
                            new ActionBarCallBack()
                    );
                    exListView.setAdapter(exListViewAdapter);
                    mViewSelector = exListViewAdapter.getViewSelector();

                    mViewSelector.restoreSelectedItems(savedInstanceState, view);
                };
        model.getBasicHEventHNoteCOItems().observe(this, hEventHNoteCOItemsObserver);
    }

    private void updateTitle(@NonNull ActionMode mode) {
        mode.setTitle(DisplayTitleBuilder.buildItemsTitle(getContext(), mViewSelector.getSelectedItems()));
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_restore_action, menu);
            if (mViewSelector.isSelected()) {
                updateTitle(mode);
            }

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            updateTitle(mode);
            return false;
        }

        private void performRestoreAction(final Collection<BasicHNoteCOItemA> selectedNoteItems) {
            String queryString = getResources().getQuantityString(
                    R.plurals.ui_question_are_you_sure_restore_items, selectedNoteItems.size(), selectedNoteItems.size());
            AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(queryString);
            dialog.setOkButtonClickListener(dialog1 -> {
                Set<String> selectedList = new HashSet<>();
                for (BasicHNoteCOItemA item : selectedNoteItems) {
                    selectedList.add(item.getValue());
                }

                Bundle result = new Bundle();
                result.putStringArrayList(BasicHEventCOItemFragment.RESULT_VALUE_KEY, new ArrayList<>(selectedList));
                BasicHEventCOItemFragment.this.getParentFragmentManager().setFragmentResult(
                        BasicHEventCOItemFragment.RESULT_KEY, result);

                mViewSelector.finishActionMode();

                Navigation.findNavController(BasicHEventCOItemFragment.this.requireView()).navigateUp();
            });
            dialog.show(getParentFragmentManager(), AlertOkCancelSupportDialogFragment.TAG);
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Collection<BasicHNoteCOItemA> selectedNoteItems = mViewSelector.getSelectedItems();

            if (!selectedNoteItems.isEmpty()) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_restore) {
                    //performSelectAll();
                    performRestoreAction(selectedNoteItems);
                }
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (mViewSelector != null)
                mViewSelector.destroyActionMode();
        }
    }
}
