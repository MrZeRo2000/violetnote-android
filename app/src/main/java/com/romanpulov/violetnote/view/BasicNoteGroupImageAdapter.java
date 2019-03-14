package com.romanpulov.violetnote.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SpinnerAdapter;

import com.romanpulov.violetnote.R;

public class BasicNoteGroupImageAdapter extends ArrayAdapter<Integer> {

    private final Integer[] mImages;

    public BasicNoteGroupImageAdapter(Context context, int resource, Integer[] images) {
        super(context, resource, images);
        mImages = images;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return internalGetView(position, convertView);
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return internalGetView(position, convertView);
    }

    @NonNull
    private View internalGetView(int position, View convertView) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_img_selector_spinner, null);
        }

        Integer imageId = getItem(position);
        if (imageId != null) {
            ImageView imgSelectorView = convertView.findViewById(R.id.img_selector_view);
            imgSelectorView.setImageDrawable(getContext().getResources().getDrawable(imageId));
        }

        return convertView;
    }
}
