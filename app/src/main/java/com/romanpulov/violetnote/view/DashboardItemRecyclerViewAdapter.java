package com.romanpulov.violetnote.view;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
        viewHolder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onBasicGroupSelection(item);
                }
            }
        });

        long noteCount = item.getSummary().getNoteCount();
        viewHolder.mItemCountView.setVisibility(noteCount == 0 ? View.GONE : View.VISIBLE);
        if (noteCount > 0) {
            viewHolder.mItemCountView.setText(String.valueOf(noteCount));
            if (noteCount == mMaxNoteCount) {
                viewHolder.mItemCountView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        }

        long noteUncheckedCount = item.getSummary().getNoteItemUncheckedCount();
        viewHolder.mItemUncheckedCountView.setVisibility(noteUncheckedCount == 0 ? View.GONE : View.VISIBLE);
        if (noteUncheckedCount > 0) {
            viewHolder.mItemUncheckedCountView.setText(String.valueOf(noteUncheckedCount));
        }

        long noteCheckedCount = item.getSummary().getNoteItemCheckedCount();
        viewHolder.mItemCheckedCountView.setVisibility(noteCheckedCount == 0 ? View.GONE : View.VISIBLE);
        if (noteCheckedCount > 0) {
            viewHolder.mItemCheckedCountView.setText(String.valueOf(noteCheckedCount));
        }

    }

    @Override
    public int getItemCount() {
        return mBasicNoteGroupList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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
