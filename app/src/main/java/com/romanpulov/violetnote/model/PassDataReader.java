package com.romanpulov.violetnote.model;

import com.romanpulov.violetnotecore.Model.PassCategory2;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Model.PassNote2;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.romanpulov.violetnotecore.Model.PassNote2.ATTR_INFO;
import static com.romanpulov.violetnotecore.Model.PassNote2.ATTR_PASSWORD;
import static com.romanpulov.violetnotecore.Model.PassNote2.ATTR_SYSTEM;
import static com.romanpulov.violetnotecore.Model.PassNote2.ATTR_URL;
import static com.romanpulov.violetnotecore.Model.PassNote2.ATTR_USER;

/**
 * PassDataA reader from PassData
 * Created by rpulov on 25.04.2016.
 */
public class PassDataReader {
    private final PassData2 mPassData2;

    private List<PassCategoryA> mPassCategoryDataA;

    public List<PassCategoryA> getPassCategoryDataA() {
        return mPassCategoryDataA;
    }

    private List<PassNoteA> mPassNoteDataA;

    public List<PassNoteA> getPassNoteDataA() {
        return mPassNoteDataA;
    }

    public PassDataReader(PassData2 passData2) {
        mPassData2 = passData2;
    }

    public void readPassData() {
        mPassCategoryDataA = new ArrayList<>(mPassData2.getCategoryList().size());
        mPassNoteDataA = new ArrayList<>();

        for (PassCategory2 passCategory2 : mPassData2.getCategoryList()) {
            PassCategoryA passCategoryA = new PassCategoryA(passCategory2.getCategoryName());
            passCategoryA.setNotesCount(passCategory2.getNoteList().size());
            mPassCategoryDataA.add(passCategoryA);

            for (PassNote2 passNote2 : passCategory2.getNoteList()) {
                Map<String, String> passNoteAttr = new LinkedHashMap<>();
                passNoteAttr.put(ATTR_SYSTEM, passNote2.getSystem());
                passNoteAttr.put(ATTR_USER, passNote2.getUser());
                passNoteAttr.put(ATTR_PASSWORD, passNote2.getPassword());
                passNoteAttr.put(ATTR_URL, passNote2.getUrl());
                passNoteAttr.put(ATTR_INFO, passNote2.getInfo());

                mPassNoteDataA.add(new PassNoteA(passCategoryA, passNoteAttr));
            }
        }
    }
}
