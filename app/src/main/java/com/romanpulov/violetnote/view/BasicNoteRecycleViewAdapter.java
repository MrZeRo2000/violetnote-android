package com.romanpulov.violetnote.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteA;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by romanpulov on 18.08.2016.
 */
public class BasicNoteRecycleViewAdapter extends RecyclerView.Adapter<BasicNoteRecycleViewAdapter.ViewHolder> {
    private  final List<BasicNoteA> mItems;

    public BasicNoteRecycleViewAdapter(List<BasicNoteA> items) {
        mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_basic_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTitle.setText(mItems.get(position).getTitle());
        if (!mItems.get(position).getIsEncrypted())
            holder.mEncryptedImage.setVisibility(View.GONE);
        else
            holder.mEncryptedImage.setVisibility(View.VISIBLE);
        DateFormat.getDateInstance().format(new Date(mItems.get(position).getLastModified()));
        //holder.mLastModified.setText(mItems.get(position).getLastModified());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTitle;
        public final TextView mLastModified;
        public final ImageView mEncryptedImage;

        public ViewHolder(View view) {
            super(view);
            mTitle = (TextView) view.findViewById(R.id.title);
            mLastModified = (TextView) view.findViewById(R.id.last_modified);
            mEncryptedImage = (ImageView) view.findViewById(R.id.encrypted_image);
        }
    }
}
