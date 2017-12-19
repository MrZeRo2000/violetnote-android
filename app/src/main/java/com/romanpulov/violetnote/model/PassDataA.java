package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.romanpulov.violetnotecore.Model.PassData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * PassData class
 * Created by rpulov on 26.04.2016.
 */
public class PassDataA implements Parcelable, PasswordProvider {

    private final String mPassword;

    @Override
    public String getPassword() {
        return mPassword;
    }

    private List<PassCategoryA> mPassCategoryDataA;

    public List<PassCategoryA> getPassCategoryData() {
        return mPassCategoryDataA;
    }

    private List<PassNoteA> mPassNoteDataA;

    public List<PassNoteA> getPassNoteData() {
        return mPassNoteDataA;
    }

    private List<PassNoteA> getPassNoteData(PassCategoryA category) {
        List<PassNoteA> passNoteData = new ArrayList<>();
        for (PassNoteA p : mPassNoteDataA) {
            if (p.getCategory().equals(category)) {
                passNoteData.add(p);
            }
        }

        return passNoteData;
    }

    private PassDataA(String password) {
        mPassword = password;
    }

    public static PassDataA newInstance(String password, PassData passData) {
        PassDataReader reader = new PassDataReader(passData);
        reader.readCategoryData();
        reader.readNoteData();

        PassDataA newPassDataA = new PassDataA(password);
        newPassDataA.mPassCategoryDataA = reader.getPassCategoryDataA();
        newPassDataA.mPassNoteDataA = reader.getPassNoteDataA();

        return newPassDataA;
    }

    public static PassDataA newSearchInstance(PassDataA source, String searchString, boolean isSearchSystem, boolean isSearchUser) {
        final int max_attr_count = 2;

        Set<PassCategoryA> categorySet = new HashSet<>();
        List<PassNoteA> noteList = null;

        for (PassNoteA note : source.getPassNoteData()) {
            int attrCount = 0;
            String searchRegExpString = "(?i:.*" + searchString + ".*)";
            for (String a : note.getNoteAttr().keySet()) {
                //check for allowed search criteria
                if (((attrCount == 0) && isSearchSystem) || ((attrCount == 1) && isSearchUser)) {
                    //check if matches expression
                    if (note.getNoteAttr().get(a).matches(searchRegExpString)) {
                        if (noteList == null)
                            noteList = new ArrayList<>();
                        noteList.add(note);
                        categorySet.add(note.getCategory());
                        break;
                    }
                }

                attrCount ++;
                if (attrCount > max_attr_count)
                    break;
            }
        }

        List<PassCategoryA> categoryList = null;
        if (noteList != null) {
            categoryList = new ArrayList<>();
            categoryList.addAll(categorySet);
        }

        PassDataA searchInstance = new PassDataA(source.mPassword);
        searchInstance.mPassCategoryDataA = categoryList;
        searchInstance.mPassNoteDataA = noteList;

        return searchInstance;
    }

    public Collection<String> getSearchValues(boolean isSearchSystem, boolean isSearchUser) {
        final int max_attr_count = 2;

        Set<String> values = new HashSet<>();

        if (getPassNoteData() != null) {
            for (PassNoteA note : getPassNoteData()) {
                int attrCount = 0;

                for (String a : note.getNoteAttr().keySet()) {
                    if (((attrCount == 0) && isSearchSystem) || ((attrCount == 1) && isSearchUser)) {
                        values.add(note.getNoteAttr().get(a));
                    }

                    if (++attrCount > max_attr_count)
                        break;
                }
            }
        }

        return values;
    }

    public static PassDataA newCategoryInstance(PassDataA source, PassCategoryA category) {
        List<PassCategoryA> categoryList = new ArrayList<>(1);
        categoryList.add(category);

        List<PassNoteA> noteList = source.getPassNoteData(category);

        PassDataA categoryInstance = new PassDataA(source.mPassword);
        categoryInstance.mPassCategoryDataA = categoryList;
        categoryInstance.mPassNoteDataA = noteList;
        return categoryInstance;
    }

    public static PassDataA newNoteInstance(PassDataA source, PassNoteA note) {
        List<PassCategoryA> categoryList = new ArrayList<>(1);
        categoryList.add(note.getCategory());

        List<PassNoteA> noteList = new ArrayList<>(1);
        noteList.add(note);

        PassDataA noteInstance = new PassDataA(source.mPassword);
        noteInstance.mPassCategoryDataA = categoryList;
        noteInstance.mPassNoteDataA = noteList;
        return noteInstance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPassword);
        dest.writeTypedList(mPassCategoryDataA);
        dest.writeTypedList(mPassNoteDataA);
    }

    private PassDataA(Parcel in) {
        //password
        mPassword = in.readString();

        //category
        mPassCategoryDataA = new ArrayList<>();
        in.readTypedList(mPassCategoryDataA, PassCategoryA.CREATOR);

        //note
        mPassNoteDataA = new ArrayList<>();
        in.readTypedList(mPassNoteDataA, PassNoteA.CREATOR);

        //category map
        Map<String, PassCategoryA> categoryMap = new HashMap<>(mPassCategoryDataA.size());
        for (PassCategoryA category : mPassCategoryDataA) {
            categoryMap.put(category.getCategoryName(), category);
        }

        //category references
        for (PassNoteA note : mPassNoteDataA) {
            //set reference
            PassCategoryA newCategory = categoryMap.get(note.getCategoryName());
            note.setCategory(newCategory);
        }
    }

    public static final Parcelable.Creator<PassDataA> CREATOR = new Parcelable.Creator<PassDataA>() {
        @Override
        public PassDataA createFromParcel(Parcel source) {
            return new PassDataA(source);
        }

        @Override
        public PassDataA[] newArray(int size) {
            return new PassDataA[size];
        }
    };
}
