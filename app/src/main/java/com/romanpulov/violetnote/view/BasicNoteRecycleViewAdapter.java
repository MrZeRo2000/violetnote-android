package com.romanpulov.violetnote.view;

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
 * Created by romanpulov on 18.08.2016.
 */
public class BasicNoteRecycleViewAdapter extends RecyclerView.Adapter<BasicNoteRecycleViewAdapter.ViewHolder> {
    private static void log(String message) {
        Log.d("RecycleViewAdapter", message);
    }

    private final List<BasicNoteA> mItems;
    private final RecyclerViewHelper.RecyclerViewSelector mRecyclerViewSelector;
    private final BasicNoteActivityFragment.OnBasicNoteFragmentInteractionListener mListener;

    public RecyclerViewHelper.RecyclerViewSelector getRecyclerViewSelector() {
        return mRecyclerViewSelector;
    }

    public BasicNoteRecycleViewAdapter(List<BasicNoteA> items, ActionMode.Callback actionModeCallback, BasicNoteActivityFragment.OnBasicNoteFragmentInteractionListener listener) {
        mItems = items;
        mRecyclerViewSelector = new RecyclerViewHelper.RecyclerViewSelector(this, actionModeCallback);
        mListener = listener;
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
        // type icon
        if (mItems.get(position).getNoteType() == 0)
            holder.mTypeImage.setImageResource(R.drawable.ic_action_tick);
        else
            holder.mTypeImage.setImageResource(R.drawable.ic_action_list);
        // encrypted icon
        holder.mEncryptedImage.setVisibility(mItems.get(position).getIsEncrypted() ? View.VISIBLE : View.GONE);
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
        final TextView mLastModified;
        final ImageView mTypeImage;
        final ImageView mEncryptedImage;

        public ViewHolder(View view, RecyclerViewHelper.RecyclerViewSelector viewSelector) {
            super(view, viewSelector);
            mTitle = (TextView) view.findViewById(R.id.title);
            mLastModified = (TextView) view.findViewById(R.id.last_modified);
            mTypeImage = (ImageView) view.findViewById(R.id.type_image);
            mEncryptedImage = (ImageView) view.findViewById(R.id.encrypted_image);
        }

        private void updateSelectedTitle() {
            ActionMode actionMode;
            if ((mRecyclerViewSelector != null) && ((actionMode = mRecyclerViewSelector.getActionMode()) != null) && (mRecyclerViewSelector.getSelectedItemPos() != -1))
                actionMode.setTitle(mItems.get(mRecyclerViewSelector.getSelectedItemPos()).getTitle());
        }

        @Override
        public boolean onLongClick(View v) {
            super.onLongClick(v);
            updateSelectedTitle();
            return true;
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            updateSelectedTitle();
            if ((mRecyclerViewSelector.getSelectedItemPos() == -1) && (mListener != null)) {
                mListener.onBasicNoteFragmentInteraction(mItems.get(getAdapterPosition()));
            }
        }
    }
}
