package com.romanpulov.violetnote.HrChooser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.romanpulov.violetnote.ActionBarCompatActivity;
import com.romanpulov.violetnote.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HrChooserActivity extends ActionBarCompatActivity {
    public static final String HR_CHOOSER_INITIAL_PATH = "InitialPath";
    public static final String HR_CHOOSER_RESULT_PATH = "ResultPath";
    public static final String HR_CHOOSER_RESULT_NAME = "ResultName";

    public interface OnChooserInteractionListener {
        void onChooserInteraction(ChooseItem item);
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<ChooseItem> mChooseItemList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_chooser);

        String initialPath = getIntent().getStringExtra(HR_CHOOSER_INITIAL_PATH);

        TextView header = (TextView) findViewById(R.id.chooser_header);
        header.setText(initialPath);

        mRecyclerView = (RecyclerView)findViewById(R.id.chooser_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FileChooseItem item = new FileChooseItem(new File(initialPath));
        mChooseItemList = new ArrayList<>();
        mChooseItemList.add(item);
        mChooseItemList.addAll(item.getItems());

        mAdapter = new ChooserAdapter(mChooseItemList, new OnChooserInteractionListener() {
            @Override
            public void onChooserInteraction(ChooseItem item) {
                switch (item.getItemType()) {
                    case ChooseItem.ITEM_FILE:
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(HR_CHOOSER_RESULT_PATH, item.getItemPath());
                        resultIntent.putExtra(HR_CHOOSER_RESULT_NAME, item.getItemName());
                        setResult(RESULT_OK, resultIntent);
                        finish();
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
    }
}
