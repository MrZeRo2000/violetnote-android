package com.romanpulov.violetnote.view.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collection;

/**
 * Created by romanpulov on 06.12.2016.
 */

public class AutoCompleteArrayAdapter extends ArrayAdapter<String> {

    private final int mResId;

    public AutoCompleteArrayAdapter(Context context, int resId, String[] autoCompleteList) {
        super(context, resId, autoCompleteList);
        mResId = resId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            View v;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                v = inflater.inflate(mResId, parent, false);
            } else
                v = convertView;

            String value = getItem(position);

            TextView textView = (TextView) v.findViewById(android.R.id.text1);
            textView.setText(value);

            return v;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
