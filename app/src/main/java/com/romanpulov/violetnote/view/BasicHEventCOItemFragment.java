package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
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
import com.romanpulov.violetnote.db.manager.DBHManager;
import com.romanpulov.violetnote.model.*;
import com.romanpulov.violetnote.view.core.AlertOkCancelSupportDialogFragment;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;
import com.romanpulov.violetnote.view.core.ViewSelectorHelper;
import com.romanpulov.violetnote.view.helper.DisplayTitleBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.romanpulov.violetnote.view.core.ViewSelectorHelper.KEY_SELECTED_ITEMS_RETURN_DATA;

public class BasicHEventCOItemFragment extends BasicCommonNoteFragment {
    public static final String RESULT_KEY = BasicCommonNoteFragment.class.getName() + "_RESULT_KEY";
    public static final String RESULT_VALUE_KEY = BasicCommonNoteFragment.class.getName() + "_RESULT_VALUE_KEY";

    private FragmentExpandableListViewBinding binding;
    private BasicHNoteCOItemViewModel model;

    private ViewSelectorHelper.AbstractViewSelector<BasicHNoteCOItemA> mViewSelector;

    public static BasicHEventCOItemFragment newInstance(BasicNoteA note) {
        BasicHEventCOItemFragment instance = new BasicHEventCOItemFragment();

        Bundle args = new Bundle();
        args.putParcelable(BasicNoteA.class.getName(), note);
        instance.setArguments(args);

        return instance;
    }

    public void refreshList(DBHManager hManager) {
        /*
        List<BasicHEventA> hEventList = hManager.mBasicHEventDAO.getByCOItemsNoteId(mNote.getId());
        List<BasicHNoteCOItemA> hCOItemList = hManager.mBasicHNoteCOItemDAO.getByNoteId(mNote.getId());

        BasicHEventA.fillArrayFromList(mHEvents, hEventList);
        BasicHNoteCOItemA.fillArrayFromList(mHEventCOItems, hCOItemList);


         */
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BasicHEventCOItemFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        Bundle args = getArguments();
        if (args != null) {
            mNote = args.getParcelable(BasicNoteA.class.getName());
        }

         */
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
        model.setBasicNote(BasicHEventCOItemFragmentArgs.fromBundle(getArguments()).getNote());

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

        /*

        refreshList(new DBHManager(view.getContext()));

        //controls
        ExpandableListView exListView = binding.exList;

        BasicHEventCOItemExpandableListViewAdapter exListViewAdapter = new BasicHEventCOItemExpandableListViewAdapter(
                getContext(),
                mHEvents,
                mHEventCOItems,
                BasicNoteItemA.getBasicNoteItemValues(mNote.getItems()),
                new ActionBarCallBack()
        );
        exListView.setAdapter(exListViewAdapter);
        mViewSelector = exListViewAdapter.getViewSelector();

        mViewSelector.restoreSelectedItems(savedInstanceState, view);

         */
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
                /*

                Activity activity = getActivity();
                if (activity != null) {
                    Intent intent = activity.getIntent();
                    intent.putExtra(KEY_SELECTED_ITEMS_RETURN_DATA, selectedListArray);
                    activity.setResult(Activity.RESULT_OK, intent);
                    activity.finish();
                }

                 */
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
