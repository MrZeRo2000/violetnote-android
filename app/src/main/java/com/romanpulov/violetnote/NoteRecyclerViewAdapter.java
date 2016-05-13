package com.romanpulov.violetnote;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romanpulov.violetnote.NoteFragment.OnListFragmentInteractionListener;
import com.romanpulov.violetnote.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link NoteFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder> {

    private final List<PassNoteA> mValues;
    private final OnListFragmentInteractionListener mListener;

    public NoteRecyclerViewAdapter(List<PassNoteA> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mSystemView.setText(mValues.get(position).getSystem());
        holder.mUserView.setText(mValues.get(position).getUser());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
        public final TextView mSystemView;
        public final TextView mUserView;
        public PassNoteA mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSystemView = (TextView) view.findViewById(R.id.system);
            mUserView = (TextView) view.findViewById(R.id.user);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mSystemView.getText() + " '" + mUserView.getText() + "'";
        }
    }
}
