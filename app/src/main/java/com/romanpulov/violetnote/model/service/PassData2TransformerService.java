package com.romanpulov.violetnote.model.service;

import com.romanpulov.violetnote.model.PassCategoryA;
import com.romanpulov.violetnote.model.PassNoteA;
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
public class PassData2TransformerService {

    public record CategoryNoteRecord(List<PassCategoryA> categories, List<PassNoteA> notes) {}

    public static CategoryNoteRecord transform(PassData2 passData2) {
        List<PassCategoryA> categories = new ArrayList<>(passData2.getCategoryList().size());
        List<PassNoteA> notes = new ArrayList<>();

        for (PassCategory2 passCategory2 : passData2.getCategoryList()) {
            PassCategoryA passCategoryA = new PassCategoryA(passCategory2.getCategoryName());
            passCategoryA.setNotesCount(passCategory2.getNoteList().size());
            categories.add(passCategoryA);

            for (PassNote2 passNote2 : passCategory2.getNoteList()) {
                Map<String, String> passNoteAttr = new LinkedHashMap<>();
                passNoteAttr.put(ATTR_SYSTEM, passNote2.getSystem());
                passNoteAttr.put(ATTR_USER, passNote2.getUser());
                passNoteAttr.put(ATTR_PASSWORD, passNote2.getPassword());
                passNoteAttr.put(ATTR_URL, passNote2.getUrl());
                passNoteAttr.put(ATTR_INFO, passNote2.getInfo());

                notes.add(new PassNoteA(passCategoryA, passNoteAttr));
            }
        }

        return new CategoryNoteRecord(categories, notes);
    }
}
