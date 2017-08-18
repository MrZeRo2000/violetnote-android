package com.romanpulov.violetnote.view;

import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.BasicOrderedEntityNoteA;
import com.romanpulov.violetnote.view.helper.DisplayTitleBuilder;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.helper.PriorityDisplayHelper;

import java.util.Collection;
import java.util.List;

public class BasicNoteNamedItemRecyclerViewAdapter extends RecyclerView.Adapter<BasicNoteNamedItemRecyclerViewAdapter.ViewHolder> {

    private final List<BasicNoteItemA> mItems;
    private final RecyclerViewHelper.RecyclerViewSelector mRecyclerViewSelector;
    private final BasicNoteCheckedItemFragment.OnBasicNoteItemFragmentInteractionListener mListener;

    public RecyclerViewHelper.RecyclerViewSelector getRecyclerViewSelector() {
        return mRecyclerViewSelector;
    }

    public BasicNoteNamedItemRecyclerViewAdapter(List<BasicNoteItemA> items, ActionMode.Callback actionModeCallback, BasicNoteCheckedItemFragment.OnBasicNoteItemFragmentInteractionListener listener) {
        mItems = items;
        mRecyclerViewSelector = new RecyclerViewHelper.RecyclerViewSelector(this, actionModeCallback);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_basic_note_named_item, parent, false);
        return new ViewHolder(view, mRecyclerViewSelector);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mItems.get(position);
        holder.mNameView.setText(mItems.get(position).getName());
        holder.mValueView.setText(mItems.get(position).getValue());
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
        private final View mView;
        private final TextView mNameView;
        private final TextView mValueView;
        private final TextView mLastModifiedView;
        private final ImageView mPriorityView;
        private BasicNoteItemA mItem;

        public ViewHolder(View view, RecyclerViewHelper.RecyclerViewSelector viewSelector) {
            super(view, viewSelector);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
            mValueView = (TextView) view.findViewById(R.id.value);
            mLastModifiedView = (TextView) view.findViewById(R.id.last_modified);
            mPriorityView = (ImageView) view.findViewById(R.id.priority);
        }

        @Override
        protected String getSelectedTitle(Collection<Integer> selectedItems) {
            return DisplayTitleBuilder.buildItemsDisplayTitle(mView.getContext(), mItems, selectedItems);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if ((!mRecyclerViewSelector.isSelected()) && (mListener != null) && (getAdapterPosition() != -1))
                mListener.onBasicNoteItemFragmentInteraction(mItems.get(getAdapterPosition()), getAdapterPosition());
        }

        @Override
        public String toString() {
            return super.toString() +
                    " name= '" + mNameView.getText() + "'" +
                    " value= '" + mValueView.getText() + "'";
        }
    }
}
