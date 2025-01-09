package com.romanpulov.violetnote.view;

import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteItemParamsSummary;
import com.romanpulov.violetnote.model.InputParser;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.core.ViewSelectorHelper;
import com.romanpulov.violetnote.view.helper.DiffUtilHelper;
import com.romanpulov.violetnote.view.helper.PriorityDisplayHelper;

import java.util.List;

public class BasicNoteCheckedItemRecyclerViewAdapter extends RecyclerView.Adapter<BasicNoteCheckedItemRecyclerViewAdapter.ViewHolder> implements ViewSelectorHelper.ChangeNotificationListener {
    @Override
    public void notifySelectionChanged() {
        this.notifyDataSetChanged();
    }

    private List<BasicNoteItemA> mItems;
    private BasicNoteItemParamsSummary mParamsSummary;
    private final ViewSelectorHelper.AbstractViewSelector<Integer> mRecyclerViewSelector;
    private final OnBasicNoteCheckedItemInteractionListener mListener;

    public ViewSelectorHelper.AbstractViewSelector<Integer> getRecyclerViewSelector() {
        return mRecyclerViewSelector;
    }

    public BasicNoteCheckedItemRecyclerViewAdapter(
            List<BasicNoteItemA> items,
            BasicNoteItemParamsSummary paramsSummary,
            ActionMode.Callback actionModeCallback,
            OnBasicNoteCheckedItemInteractionListener listener) {
        mItems = items;
        mParamsSummary = paramsSummary;
        mRecyclerViewSelector = new ViewSelectorHelper.ViewSelectorMultiple<>(this, actionModeCallback);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_basic_note_checked_item, parent, false);
        return new ViewHolder(view, mRecyclerViewSelector);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mItems.get(position);
        holder.mCheckedView.setChecked(holder.mItem.isChecked());
        holder.mValueView.setText(holder.mItem.getValue());

        //strike thru for checked
        if (holder.mItem.isChecked())
            holder.mValueView.setPaintFlags(holder.mValueView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else
            holder.mValueView.setPaintFlags(holder.mValueView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        if (mParamsSummary.getTotalValue() > 0) {
            holder.mPriceView.setVisibility(View.VISIBLE);
            holder.mPriceView.setText(
                    InputParser.getDisplayValue(
                            holder.mItem.getParamLong(mParamsSummary.getNoteItemParamTypeId()),
                            InputParser.getNumberDisplayStyle(mParamsSummary.getIsInt()))
            );
        } else {
            holder.mPriceView.setVisibility(View.GONE);
        }

        // priority display
        PriorityDisplayHelper.updateImageViewPriority(holder.mPriorityView, holder.mItem.getPriority());

        // background
        holder.updateBackground();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerViewHelper.SelectableViewHolder {
        private final CheckBox mCheckedView;
        private final TextView mValueView;
        private final TextView mPriceView;
        private final ImageView mPriorityView;
        private BasicNoteItemA mItem;

        public ViewHolder(View view, ViewSelectorHelper.AbstractViewSelector<Integer> viewSelector) {
            super(view, viewSelector);
            mCheckedView = view.findViewById(R.id.checked);
            mValueView = view.findViewById(R.id.value);
            mPriceView = view.findViewById(R.id.price);
            mPriorityView = view.findViewById(R.id.priority);
            mPriceView.setOnClickListener(v -> {
                if ((!mRecyclerViewSelector.isSelected()) && (mListener != null) && (getBindingAdapterPosition() != -1))
                    mListener.onBasicNoteItemPriceClick(mItems.get(getBindingAdapterPosition()), getBindingAdapterPosition());
            });
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if ((!mRecyclerViewSelector.isSelected()) && (mListener != null) && (getBindingAdapterPosition() != -1))
                mListener.onBasicNoteItemFragmentInteraction(mItems.get(getBindingAdapterPosition()));
        }

        @Override
        @NonNull
        public String toString() {
            return super.toString() + " '" + mCheckedView.isChecked() + "', " +  " '" + mValueView.getText() + "'";
        }
    }

    public void updateItemsWithSummary(List<BasicNoteItemA> items, BasicNoteItemParamsSummary paramsSummary) {
        mParamsSummary = paramsSummary;
        DiffUtil.DiffResult result = DiffUtilHelper.getEntityListDiffResult(mItems, items);
        this.mItems = items;
        result.dispatchUpdatesTo(this);
    }
}
