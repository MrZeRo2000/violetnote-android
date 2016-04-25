package com.romanpulov.violetnote;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpulov on 25.04.2016.
 */
public class PassDataReader {
    private PassData mPassData;

    public PassDataReader(PassData passData) {
        mPassData = passData;
    }

    public List<PassCategoryA> readCategoryData() {
        ArrayList<PassCategoryA> newPassCategoryData = new ArrayList<>(mPassData.getPassCategoryList().size());
        for (PassCategory p : mPassData.getPassCategoryList()) {
            newPassCategoryData.add(new PassCategoryA(p.getCategoryName()));
        }
        return newPassCategoryData;
    }
}
