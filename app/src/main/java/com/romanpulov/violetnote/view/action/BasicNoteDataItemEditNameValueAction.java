package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.PassNoteItemCryptService;

/**
 * Created by rpulov on 16.09.2016.
 */
public class BasicNoteDataItemEditNameValueAction extends BasicNoteDataItemAction  {

    public BasicNoteDataItemEditNameValueAction(BasicNoteDataA basicNoteData, BasicNoteItemA item) {
        super(basicNoteData, item);
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        //encrypt
        if (mBasicNoteData.getNote().isEncrypted()) {
            if (!PassNoteItemCryptService.encryptBasicNoteItem(mItem, mBasicNoteData.getPassword()))
                return false;
        }

        //update
        return noteManager.updateNoteItemNameValue(mItem) == 1;
    }
}
