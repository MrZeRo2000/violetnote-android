package com.romanpulov.violetnote.view;

import android.graphics.Paint;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;

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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_basic_note_checked_item, parent, false);
        return new ViewHolder(view, mRecyclerViewSelector);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mItems.get(position);
        holder.mCheckedView.setChecked(holder.mItem.isChecked());
        holder.mValueView.setText(holder.mItem.getValue());

        //strike thru for checked
        if (holder.mItem.isChecked())
            holder.mValueView.setPaintFlags(holder.mValueView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else
            holder.mValueView.setPaintFlags(holder.mValueView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        holder.mLastModifiedView.setText(holder.mItem.getLastModifiedString());
        // background
        holder.updateBackground();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerViewHelper.SelectableViewHolder {
        public final View mView;
        public final CheckBox mCheckedView;
        public final TextView mValueView;
        public final TextView mLastModifiedView;
        public BasicNoteItemA mItem;

        public ViewHolder(View view, RecyclerViewHelper.RecyclerViewSelector viewSelector) {
            super(view, viewSelector);
            mView = view;
            mCheckedView = (CheckBox) view.findViewById(R.id.checked);
            mValueView = (TextView) view.findViewById(R.id.value);
            mLastModifiedView = (TextView) view.findViewById(R.id.last_modified);
        }

        private void updateSelectedTitle() {
            ActionMode actionMode;
            if ((mRecyclerViewSelector != null) && ((actionMode = mRecyclerViewSelector.getActionMode()) != null) && (mRecyclerViewSelector.getSelectedItemPos() != -1))
                actionMode.setTitle(mItems.get(mRecyclerViewSelector.getSelectedItemPos()).getValue());
        }

        @Override
        public boolean onLongClick(View v) {
            super.onLongClick(v);
            updateSelectedTitle();
            return true;
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            updateSelectedTitle();
            if ((mListener != null) && (mRecyclerViewSelector.getSelectedItemPos() == -1))
                mListener.onBasicNoteItemFragmentInteraction(mItems.get(getAdapterPosition()), getAdapterPosition());
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCheckedView.isChecked() + "', " +  " '" + mValueView.getText() + "'";
        }
    }
}
