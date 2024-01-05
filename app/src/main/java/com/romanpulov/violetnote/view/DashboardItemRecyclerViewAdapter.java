package com.romanpulov.violetnote.view;

import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.view.helper.DrawableSelectionHelper;

import java.util.List;

public class DashboardItemRecyclerViewAdapter extends RecyclerView.Adapter<DashboardItemRecyclerViewAdapter.ViewHolder> {

    private final List<BasicNoteGroupA> mBasicNoteGroupList;
    private final OnBasicGroupInteractionListener mListener;

    //calculated
    private long mMaxNoteCount = 0;

    private void calcMaxNoteCount() {
        for (BasicNoteGroupA item : mBasicNoteGroupList) {
            if (item.getSummary().getNoteCount() > mMaxNoteCount) {
                mMaxNoteCount = item.getSummary().getNoteCount();
            }
        }
    }

    public DashboardItemRecyclerViewAdapter(List<BasicNoteGroupA> basicNoteGroupList, OnBasicGroupInteractionListener listener) {
        this.mBasicNoteGroupList = basicNoteGroupList;
        this.mListener = listener;
        calcMaxNoteCount();
    }

    @NonNull
    @Override
    public DashboardItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_dashboard_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DashboardItemRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        final BasicNoteGroupA item = mBasicNoteGroupList.get(position);
        viewHolder.mButton.setText(item.getGroupName());
        viewHolder.mButton.setCompoundDrawablesWithIntrinsicBounds(0, DrawableSelectionHelper.getDrawableForNoteGroup(item), 0, 0);
        viewHolder.mButton.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onBasicGroupSelection(item);
            }
        });

        long noteCount = item.getSummary().getNoteCount();
        int itemCountVisibility;
        if (!item.getDisplayOptions().getTotalFlag() || noteCount == 0) {
            itemCountVisibility = View.GONE;
        } else {
            itemCountVisibility = View.VISIBLE;
        }
        viewHolder.mItemCountView.setVisibility(itemCountVisibility);
        if (itemCountVisibility == View.VISIBLE) {
            viewHolder.mItemCountView.setText(String.valueOf(noteCount));
            if (noteCount == mMaxNoteCount) {
                viewHolder.mItemCountView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        }

        long noteUncheckedCount = item.getSummary().getNoteItemUncheckedCount();
        int itemUncheckedCountVisibility;
        if (!item.getDisplayOptions().getUncheckedFlag() || noteUncheckedCount == 0) {
            itemUncheckedCountVisibility = View.GONE;
        } else {
            itemUncheckedCountVisibility = View.VISIBLE;
        }
        viewHolder.mItemUncheckedCountView.setVisibility(itemUncheckedCountVisibility);
        if (itemUncheckedCountVisibility == View.VISIBLE) {
            viewHolder.mItemUncheckedCountView.setText(String.valueOf(noteUncheckedCount));
        }

        long noteCheckedCount = item.getSummary().getNoteItemCheckedCount();
        int itemCheckedCountVisibility;
        if (!item.getDisplayOptions().getCheckedFlag() || noteCheckedCount == 0) {
            itemCheckedCountVisibility = View.GONE;
        } else {
            itemCheckedCountVisibility = View.VISIBLE;
        }
        viewHolder.mItemCheckedCountView.setVisibility(itemCheckedCountVisibility);
        if (itemCheckedCountVisibility == View.VISIBLE) {
            viewHolder.mItemCheckedCountView.setText(String.valueOf(noteCheckedCount));
        }

    }

    @Override
    public int getItemCount() {
        return mBasicNoteGroupList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button mButton;
        private final TextView mItemCountView;
        private final TextView mItemUncheckedCountView;
        private final TextView mItemCheckedCountView;

        public ViewHolder(View view) {
            super(view);
            mButton = view.findViewById(R.id.button);
            mItemCountView = view.findViewById(R.id.item_count);
            mItemUncheckedCountView = view.findViewById(R.id.item_unchecked_count);
            mItemCheckedCountView = view.findViewById(R.id.item_checked_count);
        }
    }
}
