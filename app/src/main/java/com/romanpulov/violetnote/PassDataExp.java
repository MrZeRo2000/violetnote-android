package com.romanpulov.violetnote;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassNote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rpulov on 04.04.2016.
 */
public class PassDataExp {

    private List<String> mPassCategoryList;

    public List<String> getPassCategoryList() {
        return mPassCategoryList;
    }

    private Map<String, List<PassNote>> mPassNoteList;

    public Map<String, List<PassNote>> getPassNoteList() {
        return mPassNoteList;
    }

    private PassDataExp() {

    }

    private void readPassData(PassData passData) {
        mPassCategoryList = new ArrayList<>(passData.getPassCategoryList().size());
        mPassNoteList = new HashMap<>(passData.getPassNoteList().size());

        for (PassCategory passCategory : passData.getPassCategoryList()) {
            mPassCategoryList.add(passCategory.getCategoryName());

            mPassNoteList.put(passCategory.getCategoryName(), new ArrayList<PassNote>());
        }

        for (PassNote passNote : passData.getPassNoteList()) {
            List<PassNote> list = mPassNoteList.get(passNote.getPassCategory().getCategoryName());
            if (list != null)
                list.add(passNote);
        }
    }

    public static PassDataExp newInstance(PassData passData) {
        PassDataExp instance = new PassDataExp();
        if (passData != null)
            instance.readPassData(passData);
        return  instance;
    }
}
