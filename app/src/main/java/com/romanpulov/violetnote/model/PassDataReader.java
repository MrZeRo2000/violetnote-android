package com.romanpulov.violetnote.model;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassNote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PassDataA reader from PassData
 * Created by rpulov on 25.04.2016.
 */
public class PassDataReader {
    private final PassData mPassData;

    private List<PassCategoryA> mPassCategoryDataA;

    public List<PassCategoryA> getPassCategoryDataA() {
        return mPassCategoryDataA;
    }

    private List<PassNoteA> mPassNoteDataA;

    public List<PassNoteA> getPassNoteDataA() {
        return mPassNoteDataA;
    }

    private PassCategoryA findSourcePassCategory(PassCategory passCategory) {
        for (PassCategoryA p : mPassCategoryDataA) {
            if (p.getSourcePassCategory().equals(passCategory))
                return p;
        }
        return null;
    }

    public PassDataReader(PassData passData) {
        mPassData = passData;
    }

    public void readCategoryData() {
        if (mPassData != null) {
            mPassCategoryDataA = new ArrayList<>(mPassData.getPassCategoryList().size());
            for (PassCategory p : mPassData.getPassCategoryList()) {
                mPassCategoryDataA.add(new PassCategoryA(p));
            }
        } else
            mPassCategoryDataA = new ArrayList<>();
    }

    public void readNoteData() {
        if (mPassData != null) {
            mPassNoteDataA = new ArrayList<>(mPassData.getPassNoteList().size());
            Map<PassCategoryA, Integer> noteCount = new HashMap<>();
            for (PassNote p : mPassData.getPassNoteList()) {
                PassCategoryA categoryA = findSourcePassCategory(p.getPassCategory());
                if (categoryA != null) {
                    Integer oldNoteCount = noteCount.get(categoryA);
                    if (oldNoteCount == null)
                        noteCount.put(categoryA, 1);
                    else
                        noteCount.put(categoryA, oldNoteCount + 1);
                    mPassNoteDataA.add(new PassNoteA(categoryA, p.getNoteAttr()));
                }
            }

            for (PassCategoryA p : mPassCategoryDataA) {
                Integer count = noteCount.get(p);
                if (count != null)
                    p.setNotesCount(count);
            }

        } else {
            mPassNoteDataA = new ArrayList<>();
        }
    }
}
