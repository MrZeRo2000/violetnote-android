package com.romanpulov.violetnote.view;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;
import com.romanpulov.violetnote.view.core.ViewSelectorHelper;
import com.romanpulov.violetnote.view.helper.PriorityDisplayHelper;

public class BasicNoteNamedItemRecyclerViewAdapter extends RecyclerView.Adapter<BasicNoteNamedItemRecyclerViewAdapter.ViewHolder> implements ViewSelectorHelper.ChangeNotificationListener {
    @Override
    public void notifySelectionChanged() {
        this.notifyDataSetChanged();
    }

    private final BasicNoteDataA mBasicNoteData;
    private final ViewSelectorHelper.AbstractViewSelector<Integer> mRecyclerViewSelector;
    private final BasicNoteCheckedItemFragment.OnBasicNoteItemFragmentInteractionListener mListener;

    public ViewSelectorHelper.AbstractViewSelector getRecyclerViewSelector() {
        return mRecyclerViewSelector;
    }

    public BasicNoteNamedItemRecyclerViewAdapter(BasicNoteDataA basicNoteData, ActionMode.Callback actionModeCallback, BasicNoteCheckedItemFragment.OnBasicNoteItemFragmentInteractionListener listener) {
        mBasicNoteData = basicNoteData;
        mRecyclerViewSelector = new ViewSelectorHelper.ViewSelectorMultiple<>(this, actionModeCallback);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_basic_note_named_item, parent, false);
        return new ViewHolder(view, mRecyclerViewSelector);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mBasicNoteData.getNote().getItems().get(position);
        holder.mNameView.setText(mBasicNoteData.getNote().getItems().get(position).getName());
        holder.mValueView.setText(mBasicNoteData.getNote().getItems().get(position).getValue());
        holder.mLastModifiedView.setText(holder.mItem.getLastModifiedString());

        // priority display
        PriorityDisplayHelper.updateImageViewPriority(holder.mPriorityView, holder.mItem.getPriority());

        // background
        holder.updateBackground();
    }

    @Override
    public int getItemCount() {
        return mBasicNoteData.getNote().getItems().size();
    }

    class ViewHolder extends RecyclerViewHelper.SelectableViewHolder {
        private final TextView mNameView;
        private final TextView mValueView;
        private final TextView mLastModifiedView;
        private final ImageView mPriorityView;
        private BasicNoteItemA mItem;

        public ViewHolder(View view, ViewSelectorHelper.AbstractViewSelector viewSelector) {
            super(view, viewSelector);
            mNameView = view.findViewById(R.id.name);
            mValueView = view.findViewById(R.id.value);
            mLastModifiedView = view.findViewById(R.id.last_modified);
            mPriorityView = view.findViewById(R.id.priority);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if ((!mRecyclerViewSelector.isSelected()) && (mListener != null) && (getAdapterPosition() != -1))
                mListener.onBasicNoteItemFragmentInteraction(mBasicNoteData.getNote().getItems().get(getAdapterPosition()), getAdapterPosition());
        }

        @Override
        @NonNull
        public String toString() {
            return super.toString() +
                    " name= '" + mNameView.getText() + "'" +
                    " value= '" + mValueView.getText() + "'";
        }
    }
}
