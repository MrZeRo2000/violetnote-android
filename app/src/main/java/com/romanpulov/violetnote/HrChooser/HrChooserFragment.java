package com.romanpulov.violetnote.HrChooser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romanpulov.violetnote.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class HrChooserFragment extends Fragment {
    public static final String HR_CHOOSER_INITIAL_PATH = "InitialPath";
    public static final String HR_CHOOSER_RESULT_PATH = "ResultPath";
    public static final String HR_CHOOSER_RESULT_NAME = "ResultName";

    public interface OnChooserInteractionListener {
        void onChooserInteraction(ChooseItem item);
    }

    private String mInitialPath;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<ChooseItem> mChooseItemList;

    private OnChooserInteractionListener mListener;


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
        public void onBindViewHolder(ViewHolder holder, final int position) {
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
                        mListener.onChooserInteraction(mItems.get(position));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;
            public final View mView;

            public ViewHolder(View v) {
                super(v);
                mView = v;
                mTextView = (TextView) v.findViewById(R.id.hr_chooser_text);
            }
        }
    }

    public HrChooserFragment() {
        // Required empty public constructor
    }

    public static HrChooserFragment newInstance(String initialPath) {
        HrChooserFragment fragment = new HrChooserFragment();
        Bundle args = new Bundle();
        args.putString(HR_CHOOSER_INITIAL_PATH, initialPath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mInitialPath = getArguments().getString(HR_CHOOSER_INITIAL_PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_hr_chooser, container, false);

        TextView header = (TextView) (v.findViewById(R.id.chooser_header));
        header.setText(mInitialPath);

        mRecyclerView = (RecyclerView) (v.findViewById(R.id.chooser_list));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FileChooseItem item = new FileChooseItem(new File(mInitialPath));
        mChooseItemList = new ArrayList<>();
        mChooseItemList.add(item);
        mChooseItemList.addAll(item.getItems());

        mAdapter = new ChooserAdapter(mChooseItemList, new OnChooserInteractionListener() {
            @Override
            public void onChooserInteraction(ChooseItem item) {
                switch (item.getItemType()) {
                    case ChooseItem.ITEM_FILE:
                        if (mListener != null)
                            mListener.onChooserInteraction(item);
                        /*
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(HR_CHOOSER_RESULT_PATH, item.getItemPath());
                        resultIntent.putExtra(HR_CHOOSER_RESULT_NAME, item.getItemName());
                        getActivity().setResult(Activity.RESULT_OK, resultIntent);
                        getActivity().finish();
                        */
                        break;
                    case ChooseItem.ITEM_DIRECTORY:
                    case ChooseItem.ITEM_PARENT:
                        mChooseItemList.clear();
                        mChooseItemList.addAll(item.getItems());
                        mAdapter.notifyDataSetChanged();
                        break;
                }

            }
        });
        mRecyclerView.setAdapter(mAdapter);

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
