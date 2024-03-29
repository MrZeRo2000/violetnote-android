package com.romanpulov.violetnote.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.appcompat.content.res.AppCompatResources;
import com.romanpulov.violetnote.R;

public class BasicNoteGroupImageAdapter extends ArrayAdapter<Integer> {

    public BasicNoteGroupImageAdapter(Context context, int resource, Integer[] images) {
        super(context, resource, images);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return internalGetView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return internalGetView(position, convertView, parent);
    }

    @NonNull
    private View internalGetView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_img_selector_spinner, parent, false);
        }

        Integer imageId = getItem(position);
        if (imageId != null) {
            ImageView imgSelectorView = convertView.findViewById(R.id.img_selector_view);
            imgSelectorView.setImageDrawable(AppCompatResources.getDrawable(getContext(), imageId));
        }

        return convertView;
    }
}
