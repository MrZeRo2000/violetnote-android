package com.romanpulov.violetnote.view;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicEntityNoteSelectionPosA;
import com.romanpulov.violetnote.model.BasicNoteValueA;
import com.romanpulov.violetnote.model.BasicNoteValueDataA;
import com.romanpulov.violetnote.view.helper.ActionHelper;
import com.romanpulov.violetnote.view.helper.DisplayTitleBuilder;
import com.romanpulov.violetnote.view.core.AlertOkCancelSupportDialogFragment;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.helper.InputActionHelper;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnNoteValueFragmentInteractionListener}
 * interface.
 */
public class BasicNoteValueFragment extends BasicCommonNoteFragment {
    protected BasicNoteValueDataA mBasicNoteValueData;
    private InputActionHelper mInputActionHelper;

    @Override
    public void refreshList(DBNoteManager noteManager) {
        mBasicNoteValueData.setValues(noteManager.mBasicNoteValueDAO.getByNoteId(mBasicNoteValueData.getNote().getId()));
    }

    private OnNoteValueFragmentInteractionListener mListener;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mBasicNoteValueData = getArguments().getParcelable(BasicNoteValueDataA.class.getName());
        }
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
        if (mInputActionHelper != null)
            mInputActionHelper.hideLayout();
    }

    private void performDeleteAction(final ActionMode mode, final List<BasicNoteValueA> items) {
        AlertOkCancelSupportDialogFragment dialog = AlertOkCancelSupportDialogFragment.newAlertOkCancelDialog(getString(R.string.ui_question_are_you_sure));
        dialog.setOkButtonClickListener(new AlertOkCancelSupportDialogFragment.OnClickListener() {
            @Override
            public void OnClick(DialogFragment dialog) {
                DBNoteManager noteManager = new DBNoteManager(getActivity());

                //delete
                for (BasicNoteValueA item : items)
                    noteManager.mBasicNoteValueDAO.delete(item);

                refreshList(noteManager);

                //finish action
                mode.finish();
            }
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
                    int position = mBasicNoteValueData.indexOf(item);
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
        mode.setTitle(DisplayTitleBuilder.buildItemsDisplayTitle(getActivity(), mBasicNoteValueData.getValues(), mRecyclerViewSelector.getSelectedItems()));
    }

    @NonNull
    private List<BasicNoteValueA> getSelectedNoteItems() {
        return BasicEntityNoteSelectionPosA.getItemsByPositions(mBasicNoteValueData.getValues(), mRecyclerViewSelector.getSelectedItems());
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
            ActionHelper.updateActionMenu(menu, mRecyclerViewSelector.getSelectedItems().size(), mBasicNoteValueData.getValues().size());
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
                    mInputActionHelper.showLayout(selectedNoteItems.get(0).getValue(), InputActionHelper.INPUT_ACTION_TYPE_EDIT);
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
        mRecyclerViewSelector.setSelectedItems(ActionHelper.createSelectAllItems(mBasicNoteValueData.getValues().size()));
    }

    private void performAddAction(BasicNoteValueA value) {
        DBNoteManager mNoteManager = new DBNoteManager(getActivity());
        try {
            if (mNoteManager.mBasicNoteValueDAO.insertWithNote(mBasicNoteValueData.getNote(), value.getValue()) != -1) {
                // refresh list
                refreshList(mNoteManager);
                RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);

                //ensure added element is visible
                int newItemPos = -1;
                for (int i = 0; i < mBasicNoteValueData.getValues().size(); i++) {
                    if (mBasicNoteValueData.getValues().get(i).getValue().equals(value.getValue())) {
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
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_note_value_list, container, false);

        Context context = view.getContext();
        mRecyclerView = view.findViewById(R.id.list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        BasicNoteValueRecyclerViewAdapter recyclerViewAdapter = new BasicNoteValueRecyclerViewAdapter(
                mBasicNoteValueData, new ActionBarCallBack(), null
        );
        mRecyclerViewSelector = recyclerViewAdapter.getRecyclerViewSelector();
        mRecyclerView.setAdapter(recyclerViewAdapter);

        //restore selected items
        restoreSelectedItems(savedInstanceState, view);

        // add decoration
        mRecyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_white_black_gradient));

        //add action panel
        mInputActionHelper = new InputActionHelper(view.findViewById(R.id.add_panel_include));
        mInputActionHelper.setOnAddInteractionListener(new InputActionHelper.OnAddInteractionListener() {
            @Override
            public void onAddFragmentInteraction(final int actionType, final String text) {
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

            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNoteValueFragmentInteractionListener {
        void onNoteValueClicked(BasicNoteValueA item, int adapterPosition);
    }
}
