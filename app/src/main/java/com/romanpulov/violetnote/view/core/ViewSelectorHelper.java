package com.romanpulov.violetnote.view.core;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.View;

import com.romanpulov.violetnote.R;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ViewSelectorHelper {

    public interface ChangeNotificationListener {
        void notifySelectionChanged();
    }

    public static <T> void updateSelectedViewBackground(View v, AbstractViewSelector<T> selector, T position) {
        Collection<T> selectedItems = selector.getSelectedItems();
        int bgResId;

        if (selectedItems.size() == 0)
            bgResId = R.drawable.list_selector;
        else
        if (selectedItems.contains(position))
            bgResId = R.color.colorAccent;
        else
            bgResId = R.color.windowBackground;

        v.setBackgroundResource(bgResId);
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

        public void startActionMode(View v, T position) {
            if (mSelectedItems.size() == 0) {
                mSelectedItems.add(position);
                selectionChanged();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                mActionMode = activity.startSupportActionMode(mActionModeCallback);
            } else
                setSelectedView(v, position);
        }

        public abstract void setSelectedView(View v, T position);

        public void setSelectedItems(T[] items) {
            mSelectedItems.clear();
            mSelectedItems.addAll(Arrays.asList(items));

            selectionChanged();
        }

        public void destroyActionMode() {
            //mSelectedItemPos = -1;
            mSelectedItems.clear();
            mActionMode = null;
            mChangeNotificationListener.notifySelectionChanged();
        }
    }

    public static class ViewSelectorMultiple<T> extends AbstractViewSelector<T> {

        public ViewSelectorMultiple(ChangeNotificationListener changeNotificationListener, ActionMode.Callback actionModeCallback) {
            super(changeNotificationListener, actionModeCallback);
        }

        @Override
        public void setSelectedView(View v, T position) {
            if (mSelectedItems.size() > 0) {
                if (mSelectedItems.contains(position)) {
                    mSelectedItems.remove(position);
                    if (mSelectedItems.size() == 0) {
                        finishActionMode();
                    }
                } else
                    mSelectedItems.add(position);
                selectionChanged();
            }
        }
    }

    public static class ViewSelectorSingle<T> extends AbstractViewSelector<T> {

        public ViewSelectorSingle(ChangeNotificationListener changeNotificationListener, ActionMode.Callback actionModeCallback) {
            super(changeNotificationListener, actionModeCallback);
        }

        @Override
        public void setSelectedView(View v, T position) {
            if (mSelectedItems.size() > 0) {
                if (mSelectedItems.contains(position)) {
                    mSelectedItems.remove(position);
                    if (mSelectedItems.size() == 0) {
                        finishActionMode();
                    }
                } else {
                    mSelectedItems.clear();
                    mSelectedItems.add(position);
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
