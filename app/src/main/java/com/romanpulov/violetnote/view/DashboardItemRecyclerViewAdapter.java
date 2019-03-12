package com.romanpulov.violetnote.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.view.helper.DrawableSelectionHelper;

import java.util.List;

public class DashboardItemRecyclerViewAdapter extends RecyclerView.Adapter<DashboardItemRecyclerViewAdapter.ViewHolder> {

    private final List<BasicNoteGroupA> mBasicNoteGroupList;
    private final OnBasicGroupInteractionListener mListener;

    public DashboardItemRecyclerViewAdapter(List<BasicNoteGroupA> basicNoteGroupList, OnBasicGroupInteractionListener listener) {
        this.mBasicNoteGroupList = basicNoteGroupList;
        this.mListener = listener;
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
    }

    @Override
    public int getItemCount() {
        return mBasicNoteGroupList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final Button mButton;

        public ViewHolder(View view) {
            super(view);
            mButton = (Button)view;
        }
    }
}
