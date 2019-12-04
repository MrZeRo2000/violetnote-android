package com.romanpulov.violetnote.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.PassNoteA;

import java.util.List;

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder> {

    private final List<PassNoteA> mValues;
    private final OnPassNoteItemInteractionListener mListener;

    public NoteRecyclerViewAdapter(List<PassNoteA> items, OnPassNoteItemInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mSystemView.setText(mValues.get(position).getAttrId(1));
        holder.mUserView.setText(mValues.get(position).getAttrId(2));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onPassNoteItemInteraction(holder.mItem);
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
            mSystemView = view.findViewById(R.id.system);
            mUserView = view.findViewById(R.id.user);
        }

        @Override
        @NonNull
        public String toString() {
            return super.toString() + " '" + mSystemView.getText() + " '" + mUserView.getText() + "'";
        }
    }
}
