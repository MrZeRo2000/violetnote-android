package com.romanpulov.violetnote.view;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteValueA;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.core.ViewSelectorHelper;
import com.romanpulov.violetnote.view.helper.DiffUtilHelper;

import java.util.List;

public class BasicNoteValueRecyclerViewAdapter extends RecyclerView.Adapter<BasicNoteValueRecyclerViewAdapter.ViewHolder> implements ViewSelectorHelper.ChangeNotificationListener {
    @Override
    public void notifySelectionChanged() {
        this.notifyDataSetChanged();
    }

    private List<BasicNoteValueA> mNoteValues;
    private final ViewSelectorHelper.AbstractViewSelector<Integer> mRecyclerViewSelector;

    public ViewSelectorHelper.AbstractViewSelector<Integer> getRecyclerViewSelector() {
        return mRecyclerViewSelector;
    }

    public BasicNoteValueRecyclerViewAdapter(List<BasicNoteValueA> noteValues, ActionMode.Callback actionModeCallback) {
        mNoteValues = noteValues;
        mRecyclerViewSelector = new ViewSelectorHelper.ViewSelectorMultiple<>(this, actionModeCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_basic_note_value, parent, false);
        return new ViewHolder(view, mRecyclerViewSelector);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mValueView.setText(mNoteValues.get(position).getValue());

        // background
        holder.updateBackground();
    }

    @Override
    public int getItemCount() {
        return mNoteValues.size();
    }

    public static class ViewHolder extends RecyclerViewHelper.SelectableViewHolder {
        public final TextView mValueView;

        public ViewHolder(View view, ViewSelectorHelper.AbstractViewSelector<Integer> viewSelector) {
            super(view, viewSelector);
            mValueView = view.findViewById(R.id.value);
        }

        @Override
        @NonNull
        public String toString() {
            return super.toString() + " '" + mValueView.getText() + "'";
        }
    }

    public void updateNoteValues(List<BasicNoteValueA> noteValues) {
        DiffUtil.DiffResult diffResult = DiffUtilHelper.getEntityListDiffResult(mNoteValues, noteValues);
        this.mNoteValues = noteValues;
        diffResult.dispatchUpdatesTo(this);
    }
}
