package com.romanpulov.violetnote.view.helper;

import androidx.annotation.NonNull;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteGroupA;

public class DrawableSelectionHelper {

    public static final int DEFAULT_BASIC_NOTE_GROUP_DRAWABLE = R.drawable.img_notebook;

    private static final Integer[] mDrawableList = new Integer[] {
            R.drawable.img_notebook,
            R.drawable.img_app,
            R.drawable.img_bag,
            R.drawable.img_doc,
            R.drawable.img_home,
            R.drawable.img_starry
    };

    public static Integer[] getDrawableList() {
        return mDrawableList;
    }

    public static int getDrawableForNoteGroup(@NonNull BasicNoteGroupA item) {
        int drawableId;
        long groupIcon = item.getGroupIcon();

        if (item.getGroupType() == BasicNoteGroupA.PASSWORD_NOTE_GROUP_TYPE) {
            drawableId = R.drawable.img_personal_storage_box;
        } else if (groupIcon == 0) {
            drawableId = DEFAULT_BASIC_NOTE_GROUP_DRAWABLE;
        } else {
            if ((groupIcon > 0) && (groupIcon < mDrawableList.length)) {
                drawableId = mDrawableList[(int) groupIcon];
            }
            else {
                drawableId = DEFAULT_BASIC_NOTE_GROUP_DRAWABLE;
            }
        }

        return drawableId;
    }

    public static int getDrawablePosition(int drawableId) {
        for (int i=0; i<mDrawableList.length; i++) {
            if (mDrawableList[i] == drawableId) {
                return i;
            }
        }

        return 0;

    }

    public static long getGroupIconByDrawable(int drawableId) {
        return getDrawablePosition(drawableId);
    }

    public static long getGroupIconByPosition(int position) {
        return position;
    }


}
