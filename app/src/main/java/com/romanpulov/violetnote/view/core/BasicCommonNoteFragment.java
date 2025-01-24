package com.romanpulov.violetnote.view.core;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.core.BasicCommonNoteA;
import com.romanpulov.violetnote.model.vo.BasicEntityNoteSelectionPosA;
import com.romanpulov.violetnote.model.MovementDirection;
import com.romanpulov.violetnote.view.action.BasicUIMoveAction;
import com.romanpulov.violetnote.view.helper.BottomToolbarHelper;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * Base class for Basic common note fragment
 * Created by rpulov on 07.09.2016.
 */
public abstract class BasicCommonNoteFragment extends Fragment {
    protected static final String KEY_SELECTED_ITEMS_ARRAY = "selected items array";
    protected static final String KEY_SELECTION_TITLE = "selection title";
    protected BottomToolbarHelper mBottomToolbarHelper;

    protected RecyclerView mRecyclerView;
    protected ViewSelectorHelper.AbstractViewSelector<Integer> mRecyclerViewSelector;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        saveSelectedItems(outState);
        super.onSaveInstanceState(outState);
    }

    protected void saveSelectedItems(Bundle savedInstanceState) {
        if (mRecyclerViewSelector != null) {
            Collection<Integer> selectedItems = mRecyclerViewSelector.getSelectedItems();
            if (!selectedItems.isEmpty()) {
                int[] selectedItemsArray = new int[selectedItems.size()];
                int i = 0;
                for (Integer value : selectedItems) {
                    selectedItemsArray[i++] = value;
                }
                savedInstanceState.putIntArray(KEY_SELECTED_ITEMS_ARRAY, selectedItemsArray);
                ActionMode actionMode = mRecyclerViewSelector.getActionMode();
                if (actionMode != null) {
                    String title = actionMode.getTitle() == null ? null : actionMode.getTitle().toString();
                    savedInstanceState.putString(KEY_SELECTION_TITLE, title);
                }
            }
        }
    }

    protected void restoreSelectedItems(Bundle savedInstanceState, View view) {
        if (savedInstanceState != null) {
            int[] savedSelectedItems = savedInstanceState.getIntArray(KEY_SELECTED_ITEMS_ARRAY);

            if ((mRecyclerViewSelector != null) && (savedSelectedItems != null)) {
                for (int i = 0; i < savedSelectedItems.length; i++) {
                    if (i == 0)
                        mRecyclerViewSelector.startActionMode(view, savedSelectedItems[i]);
                    else
                        mRecyclerViewSelector.setSelectedView(savedSelectedItems[i]);
                }

                ActionMode actionMode = mRecyclerViewSelector.getActionMode();
                if (actionMode != null) {
                    String defaultTitle = actionMode.getTitle() == null ? null : actionMode.getTitle().toString();
                    actionMode.setTitle(savedInstanceState.getString(KEY_SELECTION_TITLE, defaultTitle));
                }
            }
        }
    }

    protected <T extends BasicCommonNoteA> boolean internalProcessMoveMenuItemClick(
            MenuItem menuItem,
            List<T> selectedItems,
            BasicCommonNoteViewModel<T> model) {
        if (!selectedItems.isEmpty()) {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.move_up) {
                model.moveUp(selectedItems,
                        new BasicUIMoveAction<>(
                                selectedItems,
                                MovementDirection.DIRECTION_UP,
                                mRecyclerViewSelector,
                                mRecyclerView));
                return true;
            } else if (itemId == R.id.move_top) {
                model.moveTop(selectedItems,
                        new BasicUIMoveAction<>(
                                selectedItems,
                                MovementDirection.DIRECTION_UP,
                                mRecyclerViewSelector,
                                mRecyclerView));
                return true;
            } else if (itemId == R.id.move_down) {
                model.moveDown(selectedItems,
                        new BasicUIMoveAction<>(
                                selectedItems,
                                MovementDirection.DIRECTION_DOWN,
                                mRecyclerViewSelector,
                                mRecyclerView));
                return true;
            } else if (itemId == R.id.move_bottom) {
                model.moveBottom(selectedItems,
                        new BasicUIMoveAction<>(
                                selectedItems,
                                MovementDirection.DIRECTION_DOWN,
                                mRecyclerViewSelector,
                                mRecyclerView));
                return true;
            }
        }
        return false;
    }

    protected <T> List<T> getSelectedItems(Supplier<List<T>> itemsSupplier) {
        return BasicEntityNoteSelectionPosA.getItemsByPositions(
                itemsSupplier.get(),
                mRecyclerViewSelector.getSelectedItems());
    }
}
