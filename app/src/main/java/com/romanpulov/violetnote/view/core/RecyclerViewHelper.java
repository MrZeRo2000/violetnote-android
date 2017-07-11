package com.romanpulov.violetnote.view.core;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.romanpulov.violetnote.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rpulov on 15.05.2016.
 */
public class RecyclerViewHelper {
    public static void log(String message) {
        Log.d("RecyclerViewHelper", message);
    }

    /**
     * Taken from
     * \Android\sdk\extras\android\support\samples\Support7Demos\src\com\example\android\supportv7\widget\decorator\

     */
    public static class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private static final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };

        public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

        public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

        private Drawable mDivider;

        private int mOrientation;

        public DividerItemDecoration(Context context, int orientation, int drawableId) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();
            setOrientation(orientation);
            //R.drawable.divider_white_black_gradient
            mDivider = ContextCompat.getDrawable(context, drawableId);
        }

        public void setOrientation(int orientation) {
            if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
                throw new IllegalArgumentException("invalid orientation");
            }
            mOrientation = orientation;
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            if (mOrientation == VERTICAL_LIST) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        }

        public void drawVertical(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin +
                        Math.round(ViewCompat.getTranslationY(child));
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        public void drawHorizontal(Canvas c, RecyclerView parent) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int left = child.getRight() + params.rightMargin +
                        Math.round(ViewCompat.getTranslationX(child));
                final int right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (mOrientation == VERTICAL_LIST) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }
    }

    public static abstract class SelectableViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        protected final View mView;
        protected final RecyclerViewSelector mViewSelector;

        public RecyclerViewSelector getViewSelector() {
            return  mViewSelector;
        }

        public SelectableViewHolder(View view, RecyclerViewSelector viewSelector) {
            super(view);
            mView = view;
            mViewSelector = viewSelector;

            //setup handlers
            mView.setOnLongClickListener(this);
            mView.setOnClickListener(this);
        }

        protected abstract String getSelectedTitle(Collection<Integer> selectedItems);

        private void updateSelectedTitle() {
            ActionMode actionMode = mViewSelector.getActionMode();
            if ((actionMode != null) && (mViewSelector.getSelectedItems().size() > 0)) {
                String selectedTitle = getSelectedTitle(mViewSelector.getSelectedItems());
                if (selectedTitle != null)
                    actionMode.setTitle(selectedTitle);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            mViewSelector.startActionMode(v, getAdapterPosition());
            updateSelectedTitle();
            return true;
        }

        @Override
        public void onClick(View v) {
            mViewSelector.setSelectedView(v, getAdapterPosition());
            updateSelectedTitle();
        }

        public void updateBackground() {
            Collection<?> selectedItems = mViewSelector.getSelectedItems();
            int bgResId;

            if (selectedItems.size() == 0)
                bgResId = R.drawable.list_selector;
            else
                if (selectedItems.contains(getAdapterPosition()))
                    bgResId = R.color.colorAccent;
                else
                    bgResId = R.color.windowBackground;

            mView.setBackgroundResource(bgResId);
        }
    }

    public static class RecyclerViewSelector {
        //private int mSelectedItemPos = -1;
        private Set<Integer> mSelectedItems = new HashSet<>();

        public Collection<Integer> getSelectedItems() {
            return mSelectedItems;
        }

        public boolean isSelectedSingle() {
            return mSelectedItems.size() == 1;
        }

        public boolean isSelectedMultiple() {
            return mSelectedItems.size() > 1;
        }

        public boolean isSelected() {
            return mSelectedItems.size() > 0;
        }

        private final RecyclerView.Adapter<?> mAdapter;
        private final ActionMode.Callback mActionModeCallback;
        private ActionMode mActionMode;

        /*
        public int getSelectedItemPos() {
            return mSelectedItemPos;
        }
        */

        public ActionMode getActionMode() {
            return mActionMode;
        }

        public void finishActionMode() {
            if (mActionMode != null) {
                mActionMode.finish();
                mActionMode = null;
            }
        }

        private void selectionChanged() {
            mAdapter.notifyDataSetChanged();
            if (mActionMode != null) {
                mActionMode.invalidate();
            }
        }

        public RecyclerViewSelector(RecyclerView.Adapter<?> adapter, ActionMode.Callback actionModeCallback) {
            mAdapter = adapter;
            mActionModeCallback = actionModeCallback;
        }

        public void startActionMode(View v, int position) {
            if (mSelectedItems.size() == 0) {
                mSelectedItems.add(position);
                selectionChanged();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                mActionMode = activity.startSupportActionMode(mActionModeCallback);
            } else
                setSelectedView(v, position);
        }

        public void setSelectedView(View v, int position) {
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

        public void setSelectedItems(Integer[] items) {
            mSelectedItems.clear();
            mSelectedItems.addAll(Arrays.asList(items));

            selectionChanged();
        }

        public void destroyActionMode() {
            //mSelectedItemPos = -1;
            mSelectedItems.clear();
            mActionMode = null;
            mAdapter.notifyDataSetChanged();
        }
    }
}
