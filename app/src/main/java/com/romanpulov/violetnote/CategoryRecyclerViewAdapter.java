package com.romanpulov.violetnote;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romanpulov.violetnote.CategoryFragment.OnListFragmentInteractionListener;
import com.romanpulov.violetnote.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

    private final List<PassCategoryA> mValues;
    private final OnListFragmentInteractionListener mListener;

    public CategoryRecyclerViewAdapter(List<PassCategoryA> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mCategoryName.setText(mValues.get(position).getCategoryName());
        holder.mCategoryDescription.setText(mValues.get(position).getCategoryName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
            mCategoryName = (TextView) view.findViewById(R.id.category_name);
            mCategoryDescription = (TextView) view.findViewById(R.id.category_description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCategoryDescription.getText() + "'";
        }
    }
}
