package com.romanpulov.violetnote.view;

import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by romanpulov on 18.08.2016.
 */
public class BasicNoteRecycleViewAdapter extends RecyclerView.Adapter<BasicNoteRecycleViewAdapter.ViewHolder> {
    private final List<BasicNoteA> mItems;
    private final RecyclerViewHelper.RecyclerViewSelector mRecyclerViewSelector;

    public RecyclerViewHelper.RecyclerViewSelector getRecyclerViewSelector() {
        return mRecyclerViewSelector;
    }

    public BasicNoteRecycleViewAdapter(List<BasicNoteA> items, ActionMode.Callback actionModeCallback) {
        mItems = items;
        mRecyclerViewSelector = new RecyclerViewHelper.RecyclerViewSelector(
                this,
                actionModeCallback
        );
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_basic_note, parent, false);

        return new ViewHolder(view, mRecyclerViewSelector);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // title
        holder.mTitle.setText(mItems.get(position).getTitle());
        // encrypted icon
        holder.mEncryptedImage.setVisibility(mItems.get(position).getIsEncrypted() ? View.VISIBLE : View.GONE);
        // last modified
        holder.mLastModified.setText(DateFormat.getDateInstance().format(new Date(mItems.get(position).getLastModified())));
        // background
        holder.updateBackground();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerViewHelper.SelectableViewHolder {
        public final TextView mTitle;
        public final TextView mLastModified;
        public final ImageView mEncryptedImage;

        public ViewHolder(View view, RecyclerViewHelper.RecyclerViewSelector viewSelector) {
            super(view, viewSelector);
            mTitle = (TextView) view.findViewById(R.id.title);
            mLastModified = (TextView) view.findViewById(R.id.last_modified);
            mEncryptedImage = (ImageView) view.findViewById(R.id.encrypted_image);
        }
    }
}
