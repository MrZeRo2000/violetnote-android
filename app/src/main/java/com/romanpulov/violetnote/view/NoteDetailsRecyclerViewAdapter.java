package com.romanpulov.violetnote.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.romanpulov.violetnote.view.NoteDetailsFragment.OnNoteDetailsInteractionListener;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.PassNoteA;
import com.romanpulov.violetnote.view.helper.ClipboardHelper;

import java.util.List;

public class NoteDetailsRecyclerViewAdapter extends RecyclerView.Adapter<NoteDetailsRecyclerViewAdapter.ViewHolder> {

    private final List<PassNoteA.AttrItem> mValues;
    private final OnNoteDetailsInteractionListener mListener;

    public NoteDetailsRecyclerViewAdapter(List<PassNoteA.AttrItem> items, OnNoteDetailsInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_note_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(holder.mItem.mName);
        holder.mValueView.setText(holder.mItem.mValue);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onAttrItemSelection(holder.mItem);
                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Context context = holder.mView.getContext();
                String message = context.getString(R.string.ui_info_copy_to_clipboard, holder.mItem.mName);
                ClipboardHelper.copyPlainText(context, holder.mItem.mValue);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mValueView;
        public PassNoteA.AttrItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.name);
            mValueView = view.findViewById(R.id.value);
        }

        @Override
        @NonNull
        public String toString() {
            return super.toString() + " '" + mValueView.getText() + "'";
        }
    }
}
