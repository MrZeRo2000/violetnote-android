package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.PassNoteItemCryptService;

/**
 * Created by romanpulov on 13.09.2016.
 */
public class BasicNoteDataInsertCheckedItemAction extends BasicNoteDataAction {

    public BasicNoteDataInsertCheckedItemAction(BasicNoteDataA basicNoteData, BasicNoteItemA item) {
        super(basicNoteData);
        mItem = item;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        //encrypt
        if (mBasicNoteData.getNote().getIsEncrypted()) {
            if (!PassNoteItemCryptService.encryptBasicNoteItem(mItem, mBasicNoteData.getPassword()))
                return false;
        }

        //insert
        noteManager.insertNoteItem(mBasicNoteData.getNote(), mItem);

        return true;
    }
}
