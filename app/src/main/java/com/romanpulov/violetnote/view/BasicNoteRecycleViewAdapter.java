package com.romanpulov.violetnote.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteA;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by romanpulov on 18.08.2016.
 */
public class BasicNoteRecycleViewAdapter extends RecyclerView.Adapter<BasicNoteRecycleViewAdapter.ViewHolder> {
    private final List<BasicNoteA> mItems;
    private final View.OnCreateContextMenuListener mContextMenuListener;

    public BasicNoteRecycleViewAdapter(List<BasicNoteA> items, View.OnCreateContextMenuListener contextMenuListener) {
        mItems = items;
        mContextMenuListener = contextMenuListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_basic_note, parent, false);

        view.setOnCreateContextMenuListener(mContextMenuListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTitle.setText(mItems.get(position).getTitle());
        if (!mItems.get(position).getIsEncrypted())
            holder.mEncryptedImage.setVisibility(View.GONE);
        else
            holder.mEncryptedImage.setVisibility(View.VISIBLE);
        holder.mLastModified.setText(DateFormat.getDateInstance().format(new Date(mItems.get(position).getLastModified())));
        //holder.mLastModified.setText(mItems.get(position).getLastModified());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Clicked " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });

        /*
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(v.getContext(), "Long pressed " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        */

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitle;
        public final TextView mLastModified;
        public final ImageView mEncryptedImage;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.title);
            mLastModified = (TextView) view.findViewById(R.id.last_modified);
            mEncryptedImage = (ImageView) view.findViewById(R.id.encrypted_image);
        }
    }
}
