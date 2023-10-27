package com.romanpulov.violetnote.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.view.ActionMode;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.manager.DBHManager;
import com.romanpulov.violetnote.model.BasicHEventA;
import com.romanpulov.violetnote.model.BasicHNoteCOItemA;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.core.AlertOkCancelSupportDialogFragment;
import com.romanpulov.violetnote.view.core.ViewSelectorHelper;
import com.romanpulov.violetnote.view.helper.DisplayTitleBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.romanpulov.violetnote.view.core.ViewSelectorHelper.KEY_SELECTED_ITEMS_RETURN_DATA;

public class BasicHEventCOItemFragment extends Fragment {

    //data
    private BasicNoteA mNote;
    private final LongSparseArray<BasicHEventA> mHEvents = new LongSparseArray<>();
    private final LongSparseArray<List<BasicHNoteCOItemA>> mHEventCOItems = new LongSparseArray<>();

    //controls
    private ExpandableListView mExListView;
    private BasicHEventCOItemExpandableListViewAdapter mExListViewAdapter;
    private ViewSelectorHelper.AbstractViewSelector<BasicHNoteCOItemA> mViewSelector;

    public static BasicHEventCOItemFragment newInstance(BasicNoteA note) {
        BasicHEventCOItemFragment instance = new BasicHEventCOItemFragment();

        Bundle args = new Bundle();
        args.putParcelable(BasicNoteA.class.getName(), note);
        instance.setArguments(args);

        return instance;
    }

    public void refreshList(DBHManager hManager) {
        List<BasicHEventA> hEventList = hManager.mBasicHEventDAO.getByCOItemsNoteId(mNote.getId());
        List<BasicHNoteCOItemA> hCOItemList = hManager.mBasicHNoteCOItemDAO.getByNoteId(mNote.getId());

        BasicHEventA.fillArrayFromList(mHEvents, hEventList);
        BasicHNoteCOItemA.fillArrayFromList(mHEventCOItems, hCOItemList);

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

        Bundle args = getArguments();
        if (args != null) {
            mNote = args.getParcelable(BasicNoteA.class.getName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_basic_h_event_expandable_list, container, false);

        refreshList(new DBHManager(view.getContext()));

        mExListView = view.findViewById(R.id.ex_list);
        mExListViewAdapter = new BasicHEventCOItemExpandableListViewAdapter(
                getContext(),
                mHEvents,
                mHEventCOItems,
                BasicNoteItemA.getBasicNoteItemValues(mNote.getItems()),
                new ActionBarCallBack()
        );
        mExListView.setAdapter(mExListViewAdapter);
        mViewSelector = mExListViewAdapter.getViewSelector();

        mViewSelector.restoreSelectedItems(savedInstanceState, view);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mViewSelector != null) {
            mViewSelector.saveInstanceState(outState, BasicHNoteCOItemA.class);
        }

        super.onSaveInstanceState(outState);
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
            String queryString = getString(R.string.ui_question_are_you_sure_restore_items, selectedNoteItems.size());
            AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(queryString);
            dialog.setOkButtonClickListener(new AlertOkCancelSupportDialogFragment.OnClickListener() {
                @Override
                public void OnClick(DialogFragment dialog) {
                    Set<String> selectedList = new HashSet<>();
                    for (BasicHNoteCOItemA item : selectedNoteItems) {
                        selectedList.add(item.getValue());
                    }

                    String[] selectedListArray = selectedList.toArray(new String[]{});

                    Activity activity = getActivity();
                    if (activity != null) {
                        Intent intent = activity.getIntent();
                        intent.putExtra(KEY_SELECTED_ITEMS_RETURN_DATA, selectedListArray);
                        activity.setResult(Activity.RESULT_OK, intent);
                        activity.finish();
                    }
                }
            });

            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                dialog.show(fragmentManager, null);
            }
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
