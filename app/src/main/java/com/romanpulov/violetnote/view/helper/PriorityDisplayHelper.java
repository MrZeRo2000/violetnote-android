package com.romanpulov.violetnote.view.helper;

import android.view.View;
import android.widget.ImageView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicOrderedEntityNoteA;

/**
 * Priority display helper class
 * Created by romanpulov on 18.08.2017.
 */

public class PriorityDisplayHelper {

    /**
     * Updates ImageView according to priority value
     * @param imageView priority control
     * @param priority priority value
     */
    public static void updateImageViewPriority(ImageView imageView, long priority) {
        if (priority == BasicOrderedEntityNoteA.PRIORITY_HIGH) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.ic_arrow_up_bold_red);
        } else if (priority == BasicOrderedEntityNoteA.PRIORITY_LOW) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.ic_arrow_down_bold_green);
        } else {
            imageView.setVisibility(View.GONE);
            imageView.setImageResource(android.R.color.transparent);
        }
    }
}
