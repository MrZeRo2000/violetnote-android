package com.romanpulov.violetnote.view.core;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import android.view.View;

import com.romanpulov.violetnote.R;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Helper class for view selection
 */
public class ViewSelectorHelper {
    public static final String KEY_SELECTED_ITEMS_RETURN_DATA = "selected items return data";

    private static final String KEY_SELECTED_ITEMS_ARRAY = "selected items array";
    private static final String KEY_SELECTION_TITLE = "selection title";


    public interface ChangeNotificationListener {
        void notifySelectionChanged();
    }

    public static <T> void updateSelectedViewBackground(@NonNull View v, @NonNull AbstractViewSelector<T> selector, @NonNull T item) {
        Collection<T> selectedItems = selector.getSelectedItems();
        int bgResId;

        if (selectedItems.size() == 0)
            bgResId = R.drawable.list_selector;
        else
        if (selectedItems.contains(item))
            bgResId = R.color.colorAccent;
        else
            bgResId = R.color.windowBackground;

        v.setBackgroundResource(bgResId);
    }

    public static <T extends Parcelable> void saveInstanceState(@NonNull Bundle outState, @NonNull T[] items, ActionMode actionMode) {
        if (items.length > 0) {
            outState.putParcelableArray(KEY_SELECTED_ITEMS_ARRAY, items);

            if (actionMode != null) {
                String title = actionMode.getTitle() == null ? null : actionMode.getTitle().toString();
                outState.putString(KEY_SELECTION_TITLE, title);
            }
        }
    }

    public abstract static class AbstractViewSelector<T> {

        final Set<T> mSelectedItems = new HashSet<>();

        public Collection<T> getSelectedItems() {
            return mSelectedItems;
        }

        public boolean isSelectedSingle() {
            return mSelectedItems.size() == 1;
        }

        public boolean isSelected() {
            return mSelectedItems.size() > 0;
        }

        private final ChangeNotificationListener mChangeNotificationListener;
        private final ActionMode.Callback mActionModeCallback;
        private ActionMode mActionMode;

        public ActionMode getActionMode() {
            return mActionMode;
        }

        public void finishActionMode() {
            if (mActionMode != null) {
                mActionMode.finish();
                mActionMode = null;
            }
        }

        void selectionChanged() {
            mChangeNotificationListener.notifySelectionChanged();
            if (mActionMode != null) {
                mActionMode.invalidate();
            }
        }

        public AbstractViewSelector(ChangeNotificationListener changeNotificationListener, ActionMode.Callback actionModeCallback) {
            mChangeNotificationListener = changeNotificationListener;
            mActionModeCallback = actionModeCallback;
        }

        public void startActionModeForView(@NonNull View v){
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            mActionMode = activity.startSupportActionMode(mActionModeCallback);
        }

        public void startActionMode(@NonNull View v, @NonNull T item) {
            if (mSelectedItems.size() == 0) {
                mSelectedItems.add(item);
                selectionChanged();
                startActionModeForView(v);
            } else
                setSelectedView(v, item);
        }

        public abstract void setSelectedView(View v, T item);

        public void setSelectedItems(T[] items) {
            mSelectedItems.clear();
            mSelectedItems.addAll(Arrays.asList(items));

            selectionChanged();
        }

        public void destroyActionMode() {
            mSelectedItems.clear();
            mActionMode = null;
            mChangeNotificationListener.notifySelectionChanged();
        }

        public void saveInstanceState(Bundle outState, Class<T> clazz) {
            Collection<T> selectedItems = getSelectedItems();
            if (selectedItems.size() > 0) {
                @SuppressWarnings("unchecked")
                T[] selectedItemsArray = selectedItems.toArray((T[]) Array.newInstance(clazz, 0));

                outState.putParcelableArray(KEY_SELECTED_ITEMS_ARRAY, (Parcelable[]) selectedItemsArray);

                if (mActionMode != null) {
                    String title = mActionMode.getTitle() == null ? null : mActionMode.getTitle().toString();
                    outState.putString(KEY_SELECTION_TITLE, title);
                }
            }
        }

        public void restoreSelectedItems(Bundle savedInstanceState, View view) {
            if (savedInstanceState != null) {
                @SuppressWarnings("unchecked")
                T[] savedSelectedItems = (T[])savedInstanceState.getParcelableArray(KEY_SELECTED_ITEMS_ARRAY);

                if (savedSelectedItems != null) {
                    setSelectedItems(savedSelectedItems);
                    startActionModeForView(view);
                }

                if (mActionMode!= null) {
                    String defaultTitle = mActionMode.getTitle() == null ? null : mActionMode.getTitle().toString();
                    mActionMode.setTitle(savedInstanceState.getString(KEY_SELECTION_TITLE, defaultTitle));
                }
            }
        }

    }

    public static class ViewSelectorMultiple<T> extends AbstractViewSelector<T> {

        public ViewSelectorMultiple(ChangeNotificationListener changeNotificationListener, ActionMode.Callback actionModeCallback) {
            super(changeNotificationListener, actionModeCallback);
        }

        @Override
        public void setSelectedView(View v, T item) {
            if (mSelectedItems.size() > 0) {
                if (mSelectedItems.contains(item)) {
                    mSelectedItems.remove(item);
                    if (mSelectedItems.size() == 0) {
                        finishActionMode();
                    }
                } else
                    mSelectedItems.add(item);
                selectionChanged();
            }
        }
    }

    public static class ViewSelectorSingle<T> extends AbstractViewSelector<T> {

        public ViewSelectorSingle(ChangeNotificationListener changeNotificationListener, ActionMode.Callback actionModeCallback) {
            super(changeNotificationListener, actionModeCallback);
        }

        @Override
        public void setSelectedView(View v, T item) {
            if (mSelectedItems.size() > 0) {
                if (mSelectedItems.contains(item)) {
                    mSelectedItems.remove(item);
                    if (mSelectedItems.size() == 0) {
                        finishActionMode();
                    }
                } else {
                    mSelectedItems.clear();
                    mSelectedItems.add(item);
                }
                selectionChanged();
            }
        }

        @Override
        public void setSelectedItems(T[] items) {
            mSelectedItems.clear();

            if (items.length > 0) {
                mSelectedItems.add(items[0]);
            }

            selectionChanged();
        }
    }
}
