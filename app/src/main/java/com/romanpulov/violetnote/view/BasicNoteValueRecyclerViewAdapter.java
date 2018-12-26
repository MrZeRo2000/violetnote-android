package com.romanpulov.violetnote.view;

import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteValueA;
import com.romanpulov.violetnote.model.BasicNoteValueDataA;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;

import java.util.List;

public class BasicNoteValueRecyclerViewAdapter extends RecyclerView.Adapter<BasicNoteValueRecyclerViewAdapter.ViewHolder> {

    private final BasicNoteValueDataA mBasicNoteValueDataA;
    private final RecyclerViewHelper.RecyclerViewSelector mRecyclerViewSelector;
    private final BasicNoteValueFragment.OnNoteValueFragmentInteractionListener mListener;

    public RecyclerViewHelper.RecyclerViewSelector getRecyclerViewSelector() {
        return mRecyclerViewSelector;
    }

    public BasicNoteValueRecyclerViewAdapter(BasicNoteValueDataA basicNoteValueDataA, ActionMode.Callback actionModeCallback, BasicNoteValueFragment.OnNoteValueFragmentInteractionListener listener) {
        mBasicNoteValueDataA = basicNoteValueDataA;
        mRecyclerViewSelector = new RecyclerViewHelper.RecyclerViewSelector(this, actionModeCallback);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_basic_note_value, parent, false);
        return new ViewHolder(view, mRecyclerViewSelector);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mBasicNoteValueDataA.getValues().get(position);
        holder.mValueView.setText(mBasicNoteValueDataA.getValues().get(position).getValue());

        // background
        holder.updateBackground();
    }

    @Override
    public int getItemCount() {
        return mBasicNoteValueDataA.getValues().size();
    }

    public class ViewHolder extends RecyclerViewHelper.SelectableViewHolder {
        public final TextView mValueView;
        public BasicNoteValueA mItem;

        public ViewHolder(View view, RecyclerViewHelper.RecyclerViewSelector viewSelector) {
            super(view, viewSelector);
            mValueView = view.findViewById(R.id.value);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if ((!mRecyclerViewSelector.isSelected()) && (mListener != null) && (getAdapterPosition() != -1))
                mListener.onNoteValueClicked(mBasicNoteValueDataA.getValues().get(getAdapterPosition()), getAdapterPosition());
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mValueView.getText() + "'";
        }
    }
}
