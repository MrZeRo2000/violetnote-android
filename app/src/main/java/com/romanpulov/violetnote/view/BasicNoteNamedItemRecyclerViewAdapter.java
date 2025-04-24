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
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.core.ViewSelectorHelper;
import com.romanpulov.violetnote.view.helper.DiffUtilHelper;

import java.util.List;
import java.util.function.Consumer;

public class BasicNoteNamedItemRecyclerViewAdapter extends RecyclerView.Adapter<BasicNoteNamedItemRecyclerViewAdapter.ViewHolder> implements ViewSelectorHelper.ChangeNotificationListener {
    @Override
    public void notifySelectionChanged() {
        this.notifyDataSetChanged();
    }

    private List<BasicNoteItemA> mItems;
    private final ViewSelectorHelper.AbstractViewSelector<Integer> mRecyclerViewSelector;
    private final Consumer<BasicNoteItemA> mBasicNoteItemConsumer;

    public ViewSelectorHelper.AbstractViewSelector<Integer> getRecyclerViewSelector() {
        return mRecyclerViewSelector;
    }

    public BasicNoteNamedItemRecyclerViewAdapter(
            List<BasicNoteItemA> items,
            ActionMode.Callback actionModeCallback,
            Consumer<BasicNoteItemA> basicNoteItemConsumer) {
        mItems = items;
        mRecyclerViewSelector = new ViewSelectorHelper.ViewSelectorMultiple<>(this, actionModeCallback);
        mBasicNoteItemConsumer = basicNoteItemConsumer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_basic_note_named_item, parent, false);
        return new ViewHolder(view, mRecyclerViewSelector);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mItems.get(position);
        holder.mNameView.setText(mItems.get(position).getName());
        holder.mValueView.setText(mItems.get(position).getValue());
        holder.mLastModifiedView.setText(holder.mItem.getLastModifiedString());

        // background
        holder.updateBackground();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerViewHelper.SelectableViewHolder {
        private final TextView mNameView;
        private final TextView mValueView;
        private final TextView mLastModifiedView;
        private BasicNoteItemA mItem;

        public ViewHolder(View view, ViewSelectorHelper.AbstractViewSelector<Integer> viewSelector) {
            super(view, viewSelector);
            mNameView = view.findViewById(R.id.name);
            mValueView = view.findViewById(R.id.value);
            mLastModifiedView = view.findViewById(R.id.last_modified);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if ((!mRecyclerViewSelector.isSelected()) && (mBasicNoteItemConsumer != null) && (getBindingAdapterPosition() != -1))
                mBasicNoteItemConsumer.accept(mItems.get(getBindingAdapterPosition()));
        }

        @Override
        @NonNull
        public String toString() {
            return super.toString() +
                    " name= '" + mNameView.getText() + "'" +
                    " value= '" + mValueView.getText() + "'";
        }
    }

    public void updateItems(List<BasicNoteItemA> items) {
        DiffUtil.DiffResult result = DiffUtilHelper.getEntityListDiffResult(mItems, items);
        this.mItems = items;
        result.dispatchUpdatesTo(this);
    }
}
