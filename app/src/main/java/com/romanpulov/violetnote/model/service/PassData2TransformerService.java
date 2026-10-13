package com.romanpulov.violetnote.model.service;

import com.romanpulov.violetnote.model.PassCategoryA;
import com.romanpulov.violetnote.model.PassNoteA;
import com.romanpulov.violetnotecore.Model.PassCategory2;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Model.PassNote2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                ArrayList<PassNoteA.AttrItem> passNoteAttrs = new ArrayList<>();
                passNoteAttrs.add(new PassNoteA.AttrItem(ATTR_SYSTEM, passNote2.getSystem()));
                passNoteAttrs.add(new PassNoteA.AttrItem(ATTR_USER, passNote2.getUser()));
                passNoteAttrs.add(new PassNoteA.AttrItem(ATTR_PASSWORD, passNote2.getPassword()));
                passNoteAttrs.add(new PassNoteA.AttrItem(ATTR_URL, passNote2.getUrl()));
                passNoteAttrs.add(new PassNoteA.AttrItem(ATTR_INFO, passNote2.getInfo()));

                if (passNote2.getAttributes() != null) {
                    passNoteAttrs.addAll(passNote2.getAttributes()
                            .stream()
                            .map(v -> new PassNoteA.AttrItem(v.name(), v.value()))
                            .collect(Collectors.toList()));
                }

                notes.add(new PassNoteA(passCategoryA, passNoteAttrs));
            }
        }

        return new CategoryNoteRecord(categories, notes);
    }
}
