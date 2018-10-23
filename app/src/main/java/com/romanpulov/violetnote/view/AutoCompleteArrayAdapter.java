package com.romanpulov.violetnote.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * Adapter for AutoComplete
 * Created by romanpulov on 06.12.2016.
 */

public class AutoCompleteArrayAdapter extends ArrayAdapter<String> {

    public interface OnAutoCompleteTextListener {
        void onSelectText(String text);
        void onCheckText(String text);
    }

    private final int mResId;
    private final OnAutoCompleteTextListener mAutoCompleteTextListener;

    public AutoCompleteArrayAdapter(Context context, int resId, String[] autoCompleteList, OnAutoCompleteTextListener autoCompleteTextListener) {
        super(context, resId, android.R.id.text1, autoCompleteList);
        mResId = resId;
        mAutoCompleteTextListener = autoCompleteTextListener;
    }

    private class ViewHolder implements View.OnClickListener {
        final TextView mTextView;
        final ImageButton mButton;

        ViewHolder(View view) {
            mTextView = view.findViewById(android.R.id.text1);
            mButton = view.findViewById(android.R.id.button1);

            mButton.setTag(mTextView);

            mTextView.setOnClickListener(this);
            mButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            TextView textView;

            if (v instanceof ImageButton) {
                textView = (TextView)v.getTag();
                if (mAutoCompleteTextListener != null)
                    mAutoCompleteTextListener.onCheckText(textView.getText().toString());
            } else if (v instanceof TextView) {
                textView = (TextView)v;
                if (mAutoCompleteTextListener != null)
                    mAutoCompleteTextListener.onSelectText(textView.getText().toString());
            }
        }
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view;
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(mResId, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.mTextView.setText(getItem(position));

        return view;
    }
}
