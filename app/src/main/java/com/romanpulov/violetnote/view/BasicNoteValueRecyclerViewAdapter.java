package com.romanpulov.violetnote.view;

import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteValueA;
import com.romanpulov.violetnote.model.DisplayTitleBuilder;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;

import java.util.Collection;
import java.util.List;

public class BasicNoteValueRecyclerViewAdapter extends RecyclerView.Adapter<BasicNoteValueRecyclerViewAdapter.ViewHolder> {

    private final List<BasicNoteValueA> mItems;
    private final RecyclerViewHelper.RecyclerViewSelector mRecyclerViewSelector;
    private final BasicNoteValueFragment.OnNoteValueFragmentInteractionListener mListener;

    public RecyclerViewHelper.RecyclerViewSelector getRecyclerViewSelector() {
        return mRecyclerViewSelector;
    }

    public BasicNoteValueRecyclerViewAdapter(List<BasicNoteValueA> items, ActionMode.Callback actionModeCallback, BasicNoteValueFragment.OnNoteValueFragmentInteractionListener listener) {
        mItems = items;
        mRecyclerViewSelector = new RecyclerViewHelper.RecyclerViewSelector(this, actionModeCallback);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_basic_note_value, parent, false);
        return new ViewHolder(view, mRecyclerViewSelector);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mItems.get(position);
        holder.mValueView.setText(mItems.get(position).getValue());

        // background
        holder.updateBackground();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerViewHelper.SelectableViewHolder {
        public final TextView mValueView;
        public BasicNoteValueA mItem;

        public ViewHolder(View view, RecyclerViewHelper.RecyclerViewSelector viewSelector) {
            super(view, viewSelector);
            mValueView = (TextView) view.findViewById(R.id.value);
        }

        @Override
        protected String getSelectedTitle(Collection<Integer> selectedItems) {
            return DisplayTitleBuilder.buildItemsDisplayTitle(mItems, selectedItems);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if ((!mRecyclerViewSelector.isSelected()) && (mListener != null) && (getAdapterPosition() != -1))
                mListener.onNoteValueClicked(mItems.get(getAdapterPosition()), getAdapterPosition());
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mValueView.getText() + "'";
        }
    }
}
