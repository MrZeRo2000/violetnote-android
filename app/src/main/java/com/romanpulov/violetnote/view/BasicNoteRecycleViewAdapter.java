package com.romanpulov.violetnote.view;

import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;

import java.util.List;

/**
 * BasicNoteA recycler view adapter
 * Created by romanpulov on 18.08.2016.
 */
public class BasicNoteRecycleViewAdapter extends RecyclerView.Adapter<BasicNoteRecycleViewAdapter.ViewHolder> {
    private static void log(String message) {
        Log.d("RecycleViewAdapter", message);
    }

    private final List<BasicNoteA> mItems;
    private final RecyclerViewHelper.RecyclerViewSelector mRecyclerViewSelector;
    private final BasicNoteFragment.OnBasicNoteFragmentInteractionListener mListener;

    public RecyclerViewHelper.RecyclerViewSelector getRecyclerViewSelector() {
        return mRecyclerViewSelector;
    }

    public BasicNoteRecycleViewAdapter(List<BasicNoteA> items, ActionMode.Callback actionModeCallback, BasicNoteFragment.OnBasicNoteFragmentInteractionListener listener) {
        mItems = items;
        mRecyclerViewSelector = new RecyclerViewHelper.RecyclerViewSelector(this, actionModeCallback);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_basic_note, parent, false);

        return new ViewHolder(view, mRecyclerViewSelector);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        // title
        holder.mTitle.setText(mItems.get(position).getTitle());
        // item count
        holder.mItemCount.setText(mItems.get(position).getItemCountTitle());
        // encrypted icon
        holder.mEncryptedImage.setVisibility(mItems.get(position).isEncrypted() ? View.VISIBLE : View.GONE);
        // last modified
        holder.mLastModified.setText(mItems.get(position).getLastModifiedString());
        // background
        holder.updateBackground();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerViewHelper.SelectableViewHolder {
        final TextView mTitle;
        final TextView mItemCount;
        final TextView mLastModified;
        final ImageView mEncryptedImage;

        public ViewHolder(View view, RecyclerViewHelper.RecyclerViewSelector viewSelector) {
            super(view, viewSelector);
            mTitle = view.findViewById(R.id.title);
            mItemCount = view.findViewById(R.id.item_count);
            mLastModified = view.findViewById(R.id.last_modified);
            mEncryptedImage = view.findViewById(R.id.encrypted_image);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if ((!mViewSelector.isSelected()) && (mListener != null) && (getAdapterPosition() != -1)) {
                mListener.onBasicNoteFragmentInteraction(mItems.get(getAdapterPosition()));
            }
        }
    }
}
