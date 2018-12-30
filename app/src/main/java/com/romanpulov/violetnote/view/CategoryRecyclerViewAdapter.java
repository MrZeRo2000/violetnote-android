package com.romanpulov.violetnote.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romanpulov.violetnote.view.CategoryFragment.OnPassCategoryInteractionListener;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.PassCategoryA;

import java.util.List;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

    private final List<PassCategoryA> mValues;
    private final OnPassCategoryInteractionListener mListener;

    public CategoryRecyclerViewAdapter(List<PassCategoryA> items, OnPassCategoryInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mCategoryName.setText(mValues.get(position).getCategoryName());
        holder.mCategoryDescription.setText(String.valueOf(mValues.get(position).getNotesCount()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onPassCategorySelection(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mCategoryName;
        public final TextView mCategoryDescription;
        public PassCategoryA mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCategoryName = view.findViewById(R.id.category_name);
            mCategoryDescription = view.findViewById(R.id.category_description);
        }

        @Override
        @NonNull
        public String toString() {
            return super.toString() + " '" + mCategoryDescription.getText() + "'";
        }
    }
}
