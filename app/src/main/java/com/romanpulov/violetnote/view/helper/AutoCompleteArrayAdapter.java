package com.romanpulov.violetnote.view.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


/**
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
        TextView mTextView;
        Button mButton;

        ViewHolder(View view) {
            mTextView = (TextView) view.findViewById(android.R.id.text1);
            mButton = (Button) view.findViewById(android.R.id.button1);

            mButton.setTag(mTextView);

            mTextView.setOnClickListener(this);
            mButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            TextView textView;

            if (v instanceof Button) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            View v;
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                v = inflater.inflate(mResId, parent, false);
                holder = new ViewHolder(v);
                v.setTag(holder);
            } else {
                v = convertView;
                holder = (ViewHolder) v.getTag();
            }

            String value = getItem(position);
            holder.mTextView.setText(value);

            return v;
        } catch (Exception e) {
            e.printStackTrace();
            return convertView;
        }
    }
}
