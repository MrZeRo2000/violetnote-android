package com.romanpulov.violetnote.view.helper;

import android.support.annotation.NonNull;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteGroupA;

public class DrawableSelectionHelper {

    public static int DEFAULT_BASIC_NOTE_GROUP_DRAWABLE = R.drawable.img_notebook;

    public static int getDrawableForNoteGroup(@NonNull BasicNoteGroupA item) {
        int drawableId;

        if (item.getGroupType() == BasicNoteGroupA.PASSWORD_NOTE_GROUP_TYPE) {
            drawableId = R.drawable.img_personal_storage_box;
        } else if (item.getGroupIcon() == 0) {
            drawableId = DEFAULT_BASIC_NOTE_GROUP_DRAWABLE;
        } else {
            drawableId = (int)item.getGroupIcon();
        }

        return drawableId;
    }

}
