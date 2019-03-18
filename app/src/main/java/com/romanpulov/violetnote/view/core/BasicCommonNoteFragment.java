package com.romanpulov.violetnote.view.core;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.romanpulov.violetnote.db.manager.DBNoteManager;

import java.util.Collection;

/**
 * Base class for Basic common note fragment
 * Created by rpulov on 07.09.2016.
 */
public abstract class BasicCommonNoteFragment extends Fragment {
    protected static final String KEY_SELECTED_ITEMS_ARRAY = "selected items array";
    protected static final String KEY_SELECTION_TITLE = "selection title";
    protected DialogFragment mDialogFragment;

    protected RecyclerView mRecyclerView;
    protected RecyclerViewHelper.RecyclerViewSelector mRecyclerViewSelector;

    public abstract void refreshList(DBNoteManager noteManager);

    public void refreshListWithView() {
        FragmentActivity activity = getActivity();
        if ((activity != null) && (mRecyclerView != null)) {
            refreshList(new DBNoteManager(activity));
            RecyclerViewHelper.adapterNotifyDataSetChanged(mRecyclerView);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mRecyclerViewSelector != null) {
            Collection<Integer> selectedItems = mRecyclerViewSelector.getSelectedItems();
            if (selectedItems.size() > 0) {
                int[] selectedItemsArray = new int[selectedItems.size()];
                int i = 0;
                for (Integer value : selectedItems) {
                    selectedItemsArray[i++] = value;
                }
                outState.putIntArray(KEY_SELECTED_ITEMS_ARRAY, selectedItemsArray);
                ActionMode actionMode = mRecyclerViewSelector.getActionMode();
                if (actionMode != null) {
                    String title = actionMode.getTitle() == null ? null : actionMode.getTitle().toString();
                    outState.putString(KEY_SELECTION_TITLE, title);
                }
            }
        }
        super.onSaveInstanceState(outState);
    }

    protected void restoreSelectedItems(Bundle savedInstanceState, View view) {
        if (savedInstanceState != null) {
            int[] savedSelectedItems = savedInstanceState.getIntArray(KEY_SELECTED_ITEMS_ARRAY);

            if ((mRecyclerViewSelector != null) && (savedSelectedItems != null)) {
                for (int i = 0; i < savedSelectedItems.length; i++) {
                    if (i == 0)
                        mRecyclerViewSelector.startActionMode(view, savedSelectedItems[i]);
                    else
                        mRecyclerViewSelector.setSelectedView(view, savedSelectedItems[i]);
                }

                ActionMode actionMode = mRecyclerViewSelector.getActionMode();
                if (actionMode != null) {
                    String defaultTitle = actionMode.getTitle() == null ? null : actionMode.getTitle().toString();
                    actionMode.setTitle(savedInstanceState.getString(KEY_SELECTION_TITLE, defaultTitle));
                }
            }
        }
    }
}
