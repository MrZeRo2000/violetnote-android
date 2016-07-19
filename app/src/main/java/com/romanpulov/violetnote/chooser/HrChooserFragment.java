package com.romanpulov.violetnote.chooser;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.RecyclerViewHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class HrChooserFragment extends Fragment {
    public static final String HR_CHOOSER_INITIAL_PATH = "InitialPath";

    public static final int HR_MODE_SYNC = 0;
    public static final int HR_MODE_ASYNC = 1;

    public interface OnChooserInteractionListener {
        void onChooserInteraction(ChooseItem item);
    }

    protected String mInitialPath;

    protected TextView mHeader;
    protected RecyclerView.Adapter mAdapter;
    protected List<ChooseItem> mChooseItemList;
    protected int mFillMode;

    protected OnChooserInteractionListener mListener;

    public void setFillMode(int fillMode) {
        mFillMode = fillMode;
    }

    public static class ChooserAdapter extends RecyclerView.Adapter<ChooserAdapter.ViewHolder> {
        private final List<ChooseItem> mItems;
        private final OnChooserInteractionListener mListener;

        public ChooserAdapter(List<ChooseItem> items, OnChooserInteractionListener listener) {
            mItems = items;
            mListener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hr_chooser_list_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            switch (mItems.get(position).getItemType()) {
                case ChooseItem.ITEM_PARENT:
                    holder.mTextView.setText(null);
                    holder.mTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_arrow_top, 0, 0, 0);
                    break;
                case ChooseItem.ITEM_DIRECTORY:
                    holder.mTextView.setText(mItems.get(position).getItemName());
                    holder.mTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_folder_closed, 0, 0, 0);
                    break;
                case ChooseItem.ITEM_FILE:
                    holder.mTextView.setText(mItems.get(position).getItemName());
                    holder.mTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_document, 0, 0, 0);
                    break;
                default:
                    holder.mTextView.setText(mItems.get(position).getItemName());
                    break;
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mListener) {
                        mListener.onChooserInteraction(mItems.get(holder.getAdapterPosition()));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView mTextView;
            public final View mView;

            public ViewHolder(View v) {
                super(v);
                mView = v;
                mTextView = (TextView) (v.findViewById(R.id.hr_chooser_text));
            }
        }
    }

    public HrChooserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mInitialPath = getArguments().getString(HR_CHOOSER_INITIAL_PATH);
        }
    }

    protected abstract ChooseItem getChooseItem();

    /**
     * Fills sub items for given item
     * @param item Initial item, may be empty
     * @return item with filled internal items
     */
    private ChooseItem fillChooseItem (ChooseItem item) {
        ChooseItem result;
        if (item == null)
            result = getChooseItem();
        else
            result = item;
        result.fillItems();
        return result;
    }

    /**
     * Update UI to reflect item change
     * @param item Item to reflect changes
     */
    private void updateChooseItem(ChooseItem item) {
        if (item.getFillItemsError() != null) {
            mHeader.setText(getText(R.string.error_load).toString());
            Toast.makeText(getActivity(), item.getFillItemsError(), Toast.LENGTH_SHORT).show();
        } else {
            mHeader.setText(item.getDisplayItemPath());
        }
        Collections.sort(item.getItems(), new ChooseItemComparator());
        mChooseItemList.clear();
        mChooseItemList.addAll(item.getItems());
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Handles item changed event depending on FillMode ; sync or async
     * @param item Item to process changes
     */
    private void chooseItemChanged(ChooseItem item) {
        switch (mFillMode) {
            case HR_MODE_SYNC:
                updateChooseItem(fillChooseItem(item));
                break;
            case HR_MODE_ASYNC:
                new ChooseItemUpdaterTask().execute(item);
        }
    }

    private class ChooseItemUpdaterTask extends AsyncTask<ChooseItem, Void, ChooseItem> {
        @Override
        protected void onPreExecute() {
            mHeader.setText(getActivity().getText(R.string.caption_loading));
        }

        @Override
        protected ChooseItem doInBackground(ChooseItem... params) {
            return fillChooseItem(params[0]);
        }

        @Override
        protected void onPostExecute(ChooseItem chooseItem) {
            if (isAdded()) {
                updateChooseItem(chooseItem);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_hr_chooser, container, false);

        //UI components
        mHeader = (TextView) (v.findViewById(R.id.chooser_header));
        RecyclerView recyclerView = (RecyclerView) (v.findViewById(R.id.chooser_list));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // add decoration
        recyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(getActivity(), RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_gray_solid));

        // reserve for incoming data
        mChooseItemList = new ArrayList<>();

        mAdapter = new ChooserAdapter(mChooseItemList, new OnChooserInteractionListener() {
            @Override
            public void onChooserInteraction(ChooseItem item) {
                switch (item.getItemType()) {
                    case ChooseItem.ITEM_FILE:
                        if (mListener != null)
                            mListener.onChooserInteraction(item);
                        break;
                    case ChooseItem.ITEM_DIRECTORY:
                    case ChooseItem.ITEM_PARENT:
                        chooseItemChanged(item);
                        break;
                }
            }
        });
        recyclerView.setAdapter(mAdapter);

        chooseItemChanged(null);

        return v;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnChooserInteractionListener) {
            mListener = (OnChooserInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChooserInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
