package com.romanpulov.violetnote.HrChooser;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.RecyclerViewHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class HrChooserFragment extends Fragment {
    public static final String HR_CHOOSER_INITIAL_PATH = "InitialPath";
    public static final String HR_CHOOSER_RESULT_PATH = "ResultPath";
    public static final String HR_CHOOSER_RESULT_NAME = "ResultName";

    public interface OnChooserInteractionListener {
        void onChooserInteraction(ChooseItem item);
    }

    protected String mInitialPath;

    protected TextView mHeader;
    protected RecyclerView.Adapter mAdapter;
    protected List<ChooseItem> mChooseItemList;

    protected OnChooserInteractionListener mListener;

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
                    holder.mTextView.setText(ChooseItem.ITEM_PARENT_NAME);
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

    protected void updateFromChooseItem(ChooseItem item) {
        mHeader.setText(item.getItemPath());
        List<ChooseItem> items = item.getItems();
        Collections.sort(items, new ChooseItemComparator());
        mChooseItemList.clear();
        mChooseItemList.addAll(items);
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

        ChooseItem item = getChooseItem();
        mChooseItemList = new ArrayList<>();
        updateFromChooseItem(item);

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
                        updateFromChooseItem(item);
                        mAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });
        recyclerView.setAdapter(mAdapter);

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
