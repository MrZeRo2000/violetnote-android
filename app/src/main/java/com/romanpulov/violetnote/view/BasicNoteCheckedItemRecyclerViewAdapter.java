package com.romanpulov.violetnote.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.dummy.DummyContent.DummyItem;

import java.util.List;

public class BasicNoteCheckedItemRecyclerViewAdapter extends RecyclerView.Adapter<BasicNoteCheckedItemRecyclerViewAdapter.ViewHolder> {

    private final List<BasicNoteItemA> mValues;
    private final BasicNoteCheckedItemFragment.OnBasicNoteItemFragmentInteractionListener mListener;

    public BasicNoteCheckedItemRecyclerViewAdapter(List<BasicNoteItemA> items, BasicNoteCheckedItemFragment.OnBasicNoteItemFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_basic_note_checked_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mCheckedView.setChecked(holder.mItem.getChecked());
        holder.mValueView.setText(holder.mItem.getValue());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onBasicNoteItemFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CheckBox mCheckedView;
        public final TextView mValueView;
        public BasicNoteItemA mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCheckedView = (CheckBox) view.findViewById(R.id.checked);
            mValueView = (TextView) view.findViewById(R.id.value);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCheckedView.isChecked() + "', " +  " '" + mValueView.getText() + "'";
        }
    }
}
