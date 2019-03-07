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

import java.util.List;

public class DashboardItemRecyclerViewAdapter extends RecyclerView.Adapter<DashboardItemRecyclerViewAdapter.ViewHolder> {

    private final List<BasicNoteGroupA> mBasicNoteGroupList;
    private final Context mContext;

    public DashboardItemRecyclerViewAdapter(Context context, List<BasicNoteGroupA> basicNoteGroupList) {
        this.mContext = context;
        this.mBasicNoteGroupList = basicNoteGroupList;
    }

    @NonNull
    @Override
    public DashboardItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_dashboard_list_item, parent, false);


        return new ViewHolder(view);
    }

    private int getDrawableForItem(BasicNoteGroupA item) {
        int drawableId;

        if (item.getGroupType() == BasicNoteGroupA.PASSWORD_NOTE_GROUP_TYPE) {
            drawableId = R.drawable.img_pass_note;
        } else if (item.getGroupIcon() == 0) {
            drawableId = R.drawable.img_simple_note;
        } else {
            drawableId = (int)item.getGroupIcon();
        }

        return drawableId;
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardItemRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        BasicNoteGroupA item = mBasicNoteGroupList.get(i);
        viewHolder.mButton.setText(item.getGroupName());
        viewHolder.mButton.setCompoundDrawablesWithIntrinsicBounds(0,getDrawableForItem(item), 0, 0);
    }

    @Override
    public int getItemCount() {
        return mBasicNoteGroupList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final Button mButton;

        public ViewHolder(View view) {
            super(view);
            mButton = view.findViewById(R.id.button);
        }
    }
}
