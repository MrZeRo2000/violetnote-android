package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BasicNoteDataItemAddUniqueValuesAction extends BasicAction<BasicNoteDataA> {
    private final String[] mItems;

    public BasicNoteDataItemAddUniqueValuesAction(BasicNoteDataA basicNoteData, String[] items) {
        super(basicNoteData);
        mItems = items;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        //get new values
        Set<String> newValues = new HashSet<>(Arrays.asList(mItems));

        //get old values
        Set<String> oldValues = new HashSet<>();
        for (BasicNoteItemA item : mData.getNote().getItems()) {
            oldValues.add(item.getValue());
        }

        //remove existing
        newValues.removeAll(oldValues);

        //insert
        for (String value : newValues) {
            BasicNoteItemA newItem = BasicNoteItemA.newCheckedEditValueInstance(value);
            long result = noteManager.mBasicNoteItemDAO.insertWithNote(mData.getNote(), newItem);
            if (result == -1) {
                return false;
            }
        }

        return true;
    }
}
