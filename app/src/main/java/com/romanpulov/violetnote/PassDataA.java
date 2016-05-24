package com.romanpulov.violetnote;

import com.romanpulov.violetnotecore.Model.PassData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpulov on 26.04.2016.
 */
public class PassDataA {
    private String mPassword;

    private List<PassCategoryA> mPassCategoryDataA;

    public List<PassCategoryA> getPassCategoryData() {
        return mPassCategoryDataA;
    }

    private List<PassNoteA> mPassNoteDataA;

    public List<PassNoteA> getPassNoteData() {
        return mPassNoteDataA;
    }

    public List<PassNoteA> getPassNoteData(PassCategoryA category) {
        List<PassNoteA> passNoteData = new ArrayList<>();
        for (PassNoteA p : mPassNoteDataA) {
            if (p.getCategory().equals(category)) {
                passNoteData.add(p);
            }
        }

        return passNoteData;
    }

    public static PassDataA newInstance(String password, PassData passData) {
        PassDataReader reader = new PassDataReader(passData);
        reader.readCategoryData();
        reader.readNoteData();

        PassDataA newPassDataA = new PassDataA();
        newPassDataA.mPassCategoryDataA = reader.getPassCategoryDataA();
        newPassDataA.mPassNoteDataA = reader.getPassNoteDataA();
        newPassDataA.mPassword = password;

        return newPassDataA;
    }
}
