package com.romanpulov.violetnote.view;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.core.ViewSelectorHelper;
import com.romanpulov.violetnote.view.helper.DiffUtilHelper;

import java.util.List;
import java.util.function.Consumer;

/**
 * BasicNoteA recycler view adapter
 * Created by romanpulov on 18.08.2016.
 */
public class BasicNoteRecycleViewAdapter extends RecyclerView.Adapter<BasicNoteRecycleViewAdapter.ViewHolder> implements ViewSelectorHelper.ChangeNotificationListener {
    /** @noinspection unused*/
    private static void log(String message) {
        Log.d("RecycleViewAdapter", message);
    }

    @Override
    public void notifySelectionChanged() {
        this.notifyDataSetChanged();
    }

    private List<BasicNoteA> mItems;

    public List<BasicNoteA> getItems() {
        return mItems;
    }

    private final ViewSelectorHelper.AbstractViewSelector<Integer> mRecyclerViewSelector;
    private final Consumer<BasicNoteA> mBasicNoteConsumer;

    public ViewSelectorHelper.AbstractViewSelector<Integer> getRecyclerViewSelector() {
        return mRecyclerViewSelector;
    }

    public BasicNoteRecycleViewAdapter(
            List<BasicNoteA> items,
            ActionMode.Callback actionModeCallback,
            Consumer<BasicNoteA> basicNoteConsumer) {
        mItems = items;
        mRecyclerViewSelector = new ViewSelectorHelper.ViewSelectorMultiple<>(this, actionModeCallback);
        mBasicNoteConsumer = basicNoteConsumer;
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

        public ViewHolder(View view, ViewSelectorHelper.AbstractViewSelector<Integer> viewSelector) {
            super(view, viewSelector);
            mTitle = view.findViewById(R.id.title);
            mItemCount = view.findViewById(R.id.item_count);
            mLastModified = view.findViewById(R.id.last_modified);
            mEncryptedImage = view.findViewById(R.id.encrypted_image);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if ((!mViewSelector.isSelected()) && (mBasicNoteConsumer != null) && (getBindingAdapterPosition() != -1)) {
                mBasicNoteConsumer.accept(mItems.get(getBindingAdapterPosition()));
            }
        }
    }

    public void updateItems(List<BasicNoteA> items) {
        DiffUtil.DiffResult result = DiffUtilHelper.getEntityListDiffResult(mItems, items);
        this.mItems = items;
        result.dispatchUpdatesTo(this);
    }
}
