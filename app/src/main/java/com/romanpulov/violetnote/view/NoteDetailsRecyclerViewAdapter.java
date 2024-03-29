package com.romanpulov.violetnote.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.PassNoteA;
import com.romanpulov.violetnote.view.helper.ClipboardHelper;
import com.romanpulov.violetnote.view.helper.DisplayMessageHelper;

import java.util.List;

public class NoteDetailsRecyclerViewAdapter extends RecyclerView.Adapter<NoteDetailsRecyclerViewAdapter.ViewHolder> {

    private final List<PassNoteA.AttrItem> mValues;

    public NoteDetailsRecyclerViewAdapter(List<PassNoteA.AttrItem> items) {
        mValues = items;
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

        holder.mView.setOnLongClickListener(v -> {
            Context context = v.getContext();
            String message = context.getString(R.string.ui_info_copy_to_clipboard, holder.mItem.mName);
            ClipboardHelper.copyPlainText(context, holder.mItem.mValue);
            DisplayMessageHelper.displayInfoMessage(v, message);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
