package com.romanpulov.violetnote;

import com.romanpulov.violetnotecore.Model.PassData;

import java.util.List;

/**
 * Created by rpulov on 26.04.2016.
 */
public class PassDataA {
    private List<PassCategoryA> mPassCategoryDataA;

    public List<PassCategoryA> getPassCategoryData() {
        return mPassCategoryDataA;
    }

    private List<PassNoteA> mPassNoteDataA;

    public List<PassNoteA> getPassNoteData() {
        return mPassNoteDataA;
    }

    private void setData(List<PassCategoryA> passCategoryData, List<PassNoteA> passNoteData) {
        mPassCategoryDataA = passCategoryData;
        mPassNoteDataA = passNoteData;
    }

    public static PassDataA fromPassData(PassData passData) {
        PassDataReader reader = new PassDataReader(passData);
        reader.readCategoryData();
        reader.readNoteData();

        PassDataA newPassDataA = new PassDataA();
        newPassDataA.setData(reader.getPassCategoryDataA(), reader.getPassNoteDataA());

        return newPassDataA;
    }
}