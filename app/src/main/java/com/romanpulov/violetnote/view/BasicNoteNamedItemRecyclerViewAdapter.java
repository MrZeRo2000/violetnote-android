package com.romanpulov.violetnote.view;

import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;

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
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerViewHelper.SelectableViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mValueView;
        public final TextView mLastModifiedView;
        public BasicNoteItemA mItem;

        public ViewHolder(View view, RecyclerViewHelper.RecyclerViewSelector viewSelector) {
            super(view, viewSelector);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
            mValueView = (TextView) view.findViewById(R.id.value);
            mLastModifiedView = (TextView) view.findViewById(R.id.last_modified);
        }

        @Override
        public String toString() {
            return super.toString() +
                    " name= '" + mNameView.getText() + "'" +
                    " value= '" + mValueView.getText() + "'";
        }
    }
}
