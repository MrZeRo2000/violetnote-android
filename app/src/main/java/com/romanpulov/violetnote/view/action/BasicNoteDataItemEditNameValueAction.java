package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.PassNoteItemJSONCryptService;

import java.util.Collections;

/**
 * BasicNoteItemA edit name and value action
 * Created by rpulov on 16.09.2016.
 */
public class BasicNoteDataItemEditNameValueAction extends BasicItemsAction<BasicNoteDataA, BasicNoteItemA>  {

    public BasicNoteDataItemEditNameValueAction(BasicNoteDataA basicNoteData, BasicNoteItemA item) {
        super(basicNoteData, Collections.singletonList(item));
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        //encrypt
        if (mData.getNote().isEncrypted()) {
            if (!PassNoteItemJSONCryptService.encryptBasicNoteItem(mItems.get(0), mData.getPassword()))
                return false;
        }

        //update
        return noteManager.mBasicNoteItemDAO.updateNameValue(mItems.get(0)) == 1;
    }
}
