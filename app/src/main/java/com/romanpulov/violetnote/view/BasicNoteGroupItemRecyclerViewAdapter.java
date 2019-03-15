package com.romanpulov.violetnote.view;

import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.helper.DrawableSelectionHelper;

import java.util.List;

public class BasicNoteGroupItemRecyclerViewAdapter extends RecyclerView.Adapter<BasicNoteGroupItemRecyclerViewAdapter.ViewHolder> {

    private final List<BasicNoteGroupA> mBasicNoteGroupList;
    private final RecyclerViewHelper.RecyclerViewSelector mRecyclerViewSelector;

    public RecyclerViewHelper.RecyclerViewSelector getRecyclerViewSelector() {
        return mRecyclerViewSelector;
    }


    public BasicNoteGroupItemRecyclerViewAdapter(List<BasicNoteGroupA> basicNoteGroupList, ActionMode.Callback actionModeCallback) {
        this.mBasicNoteGroupList = basicNoteGroupList;
        this.mRecyclerViewSelector = new RecyclerViewHelper.RecyclerViewSelector(this, actionModeCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_basic_note_group_list_item, parent, false);
        return new ViewHolder(view, mRecyclerViewSelector);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        BasicNoteGroupA item = mBasicNoteGroupList.get(position);
        viewHolder.mButton.setText(item.getGroupName());
        viewHolder.mButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, DrawableSelectionHelper.getDrawableForNoteGroup(item), 0);
        // background
        viewHolder.updateBackground();
    }

    @Override
    public int getItemCount() {
        return mBasicNoteGroupList.size();
    }

    class ViewHolder extends RecyclerViewHelper.SelectableViewHolder {
        private final Button mButton;

        public ViewHolder(View view, RecyclerViewHelper.RecyclerViewSelector viewSelector) {
            super(view, viewSelector);
            mButton = view.findViewById(R.id.group_name);
        }
    }
}
