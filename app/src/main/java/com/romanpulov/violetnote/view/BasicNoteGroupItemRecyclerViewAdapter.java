package com.romanpulov.violetnote.view;

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

public class BasicNoteGroupItemRecyclerViewAdapter extends RecyclerView.Adapter<BasicNoteGroupItemRecyclerViewAdapter.ViewHolder> {

    private final List<BasicNoteGroupA> mBasicNoteGroupList;

    public BasicNoteGroupItemRecyclerViewAdapter(List<BasicNoteGroupA> basicNoteGroupList) {
        this.mBasicNoteGroupList = basicNoteGroupList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_basic_note_group_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        BasicNoteGroupA item = mBasicNoteGroupList.get(position);
        viewHolder.mButton.setText(item.getGroupName());
        viewHolder.mButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, DrawableSelectionHelper.getDrawableForNoteGroup(item), 0);
    }

    @Override
    public int getItemCount() {
        return mBasicNoteGroupList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final Button mButton;

        public ViewHolder(View view) {
            super(view);
            mButton = view.findViewById(R.id.group_name);
        }
    }
}
