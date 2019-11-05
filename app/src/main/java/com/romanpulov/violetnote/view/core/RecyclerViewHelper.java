package com.romanpulov.violetnote.view.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.romanpulov.violetnote.R;

import java.util.Collection;

/**
 * RecyclerView helper class
 * Created by rpulov on 15.05.2016.
 */
public class RecyclerViewHelper {

    public static void adapterNotifyDataSetChanged(@NonNull RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public static void adapterNotifyItemChanged(@NonNull RecyclerView recyclerView, int position) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null)
            adapter.notifyItemChanged(position);
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

        private final Drawable mDivider;

        private int mOrientation;

        public DividerItemDecoration(Context context, int orientation, int drawableId) {
            //final TypedArray a = context.obtainStyledAttributes(ATTRS);
            //mDivider = a.getDrawable(0);
            //a.recycle();
            setOrientation(orientation);
            mDivider = ContextCompat.getDrawable(context, drawableId);
        }

        public void setOrientation(int orientation) {
            if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
                throw new IllegalArgumentException("invalid orientation");
            }
            mOrientation = orientation;
        }

        @Override
        public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
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
                        Math.round(child.getTranslationY());
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
                        Math.round(child.getTranslationX());
                final int right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            if (mOrientation == VERTICAL_LIST) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }
    }

    public static abstract class SelectableViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        protected final View mView;
        protected final ViewSelectorHelper.AbstractViewSelector mViewSelector;

        public SelectableViewHolder(View view, ViewSelectorHelper.AbstractViewSelector viewSelector) {
            super(view);
            mView = view;
            mViewSelector = viewSelector;

            //setup handlers
            mView.setOnLongClickListener(this);
            mView.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            mViewSelector.startActionMode(v, getAdapterPosition());
            return true;
        }

        @Override
        public void onClick(View v) {
            mViewSelector.setSelectedView(v, getAdapterPosition());
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

}
