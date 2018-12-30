package com.romanpulov.violetnote.chooser;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
    protected static final String HR_CHOOSER_INITIAL_PATH = "InitialPath";

    protected static final int HR_MODE_SYNC = 0;
    protected static final int HR_MODE_ASYNC = 1;

    public interface OnChooserInteractionListener {
        void onChooserInteraction(ChooseItem item);
    }

    protected String mInitialPath;

    private TextView mHeader;
    private RecyclerView.Adapter mAdapter;
    private List<ChooseItem> mChooseItemList;
    private String mHeaderText;
    private int mFillMode;
    private ChooseItemUpdaterTask mTask;

    private OnChooserInteractionListener mListener;

    public void setFillMode(int fillMode) {
        mFillMode = fillMode;
    }

    public static class ChooserAdapter extends RecyclerView.Adapter<ChooserAdapter.ViewHolder> {
        private final List<ChooseItem> mItems;
        private final OnChooserInteractionListener mListener;

        ChooserAdapter(List<ChooseItem> items, OnChooserInteractionListener listener) {
            mItems = items;
            mListener = listener;
        }

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hr_chooser_list_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final @NonNull ViewHolder holder, final int position) {
            switch (mItems.get(position).getItemType()) {
                case ChooseItem.ITEM_PARENT:
                    holder.mTextView.setText(null);
                    holder.mTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_more_horiz, 0, 0, 0);
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
            final TextView mTextView;
            final View mView;

            public ViewHolder(View v) {
                super(v);
                mView = v;
                mTextView = v.findViewById(R.id.hr_chooser_text);
            }
        }
    }

    public HrChooserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            mInitialPath = getArguments().getString(HR_CHOOSER_INITIAL_PATH);

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
        //header text
        if (item.getFillItemsError() != null) {
            mHeaderText = getText(R.string.error_load).toString();
            Toast.makeText(getActivity(), item.getFillItemsError(), Toast.LENGTH_SHORT).show();
        } else {
            mHeaderText = item.getDisplayItemPath();
        }

        //prepare display items
        Collections.sort(item.getItems(), new ChooseItemComparator());

        //update controls
        mHeader.setText(mHeaderText);
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
            case HR_MODE_ASYNC: {
                mTask = new ChooseItemUpdaterTask(this);
                mTask.execute(item);
            }
        }
    }

    private static class ChooseItemUpdaterTask extends AsyncTask<ChooseItem, Void, ChooseItem> {

        private final HrChooserFragment mHost;

        ChooseItemUpdaterTask(HrChooserFragment host) {
            mHost = host;
        }

        @Override
        protected void onPreExecute() {
            FragmentActivity activity = mHost.getActivity();
            if (activity != null)
                mHost.mHeader.setText(activity.getText(R.string.caption_loading));
        }

        @Override
        protected ChooseItem doInBackground(ChooseItem... params) {
            return mHost.fillChooseItem(params[0]);
        }

        @Override
        protected void onPostExecute(ChooseItem chooseItem) {
            if (mHost.isAdded()) {
                mHost.updateChooseItem(chooseItem);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_hr_chooser, container, false);

        //UI components
        mHeader = v.findViewById(R.id.chooser_header);
        if ((mTask != null) && (mTask.getStatus() == AsyncTask.Status.RUNNING)) {
            mHeader.setText(R.string.caption_loading);
        } else {
            mHeader.setText(mHeaderText);
        }

        RecyclerView recyclerView = v.findViewById(R.id.chooser_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // add decoration
        Context context = getActivity();
        if (context != null)
            recyclerView.addItemDecoration(new RecyclerViewHelper.DividerItemDecoration(context,
                    RecyclerViewHelper.DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_gray_solid));

        recyclerView.setAdapter(mAdapter);

        //update for first run
        if (savedInstanceState == null)
            chooseItemChanged(null);

        return v;
    }

    @Override
    public void onAttach(Context context) {
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
