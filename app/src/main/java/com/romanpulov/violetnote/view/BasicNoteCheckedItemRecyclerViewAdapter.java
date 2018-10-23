package com.romanpulov.violetnote.view;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.helper.PriorityDisplayHelper;

import java.util.List;

public class BasicNoteCheckedItemRecyclerViewAdapter extends RecyclerView.Adapter<BasicNoteCheckedItemRecyclerViewAdapter.ViewHolder> {

    private final List<BasicNoteItemA> mItems;
    private final RecyclerViewHelper.RecyclerViewSelector mRecyclerViewSelector;
    private final BasicNoteCheckedItemFragment.OnBasicNoteItemFragmentInteractionListener mListener;

    public RecyclerViewHelper.RecyclerViewSelector getRecyclerViewSelector() {
        return mRecyclerViewSelector;
    }

    public BasicNoteCheckedItemRecyclerViewAdapter(List<BasicNoteItemA> items, ActionMode.Callback actionModeCallback, BasicNoteCheckedItemFragment.OnBasicNoteItemFragmentInteractionListener listener) {
        mItems = items;
        mRecyclerViewSelector = new RecyclerViewHelper.RecyclerViewSelector(this, actionModeCallback);
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

        holder.mLastModifiedView.setText(holder.mItem.getLastModifiedString());

        // priority display
        PriorityDisplayHelper.updateImageViewPriority(holder.mPriorityView, holder.mItem.getPriority());

        // background
        holder.updateBackground();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerViewHelper.SelectableViewHolder {
        private final CheckBox mCheckedView;
        private final TextView mValueView;
        private final TextView mLastModifiedView;
        private final ImageView mPriorityView;
        private BasicNoteItemA mItem;

        public ViewHolder(View view, RecyclerViewHelper.RecyclerViewSelector viewSelector) {
            super(view, viewSelector);
            mCheckedView = view.findViewById(R.id.checked);
            mValueView = view.findViewById(R.id.value);
            mLastModifiedView = view.findViewById(R.id.last_modified);
            mPriorityView = view.findViewById(R.id.priority);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if ((!mRecyclerViewSelector.isSelected()) && (mListener != null) && (getAdapterPosition() != -1))
                mListener.onBasicNoteItemFragmentInteraction(mItems.get(getAdapterPosition()), getAdapterPosition());
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCheckedView.isChecked() + "', " +  " '" + mValueView.getText() + "'";
        }
    }
}
