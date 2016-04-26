package com.romanpulov.violetnote;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassNote;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpulov on 25.04.2016.
 */
public class PassDataReader {
    private PassData mPassData;

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
        mPassCategoryDataA = new ArrayList<>(mPassData.getPassCategoryList().size());
        for (PassCategory p : mPassData.getPassCategoryList()) {
            mPassCategoryDataA.add(new PassCategoryA(p.getCategoryName()));
        }
    }

    public void readNoteData() {
        mPassNoteDataA = new ArrayList<>(mPassData.getPassNoteList().size());
        for (PassNote p : mPassData.getPassNoteList()) {
            PassCategoryA categoryA = findSourcePassCategory(p.getPassCategory());
            if (categoryA != null) {
                mPassNoteDataA.add(new PassNoteA(categoryA, p.getSystem(), p.getUser(), p.getPassword(), p.getComments(), p.getCustom(), p.getInfo()));
            }
        }
    }
}
