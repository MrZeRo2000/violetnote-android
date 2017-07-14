package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.PassNoteItemJSONCryptService;

import java.util.ArrayList;

/**
 * Created by rpulov on 16.09.2016.
 */
public class BasicNoteDataItemEditNameValueAction extends BasicNoteDataItemAction  {

    public BasicNoteDataItemEditNameValueAction(BasicNoteDataA basicNoteData, BasicNoteItemA item) {
        super(basicNoteData, new ArrayList<BasicNoteItemA>());
        mItems.add(item);
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        //encrypt
        if (mBasicNoteData.getNote().isEncrypted()) {
            if (!PassNoteItemJSONCryptService.encryptBasicNoteItem(mItems.get(0), mBasicNoteData.getPassword()))
                return false;
        }

        //update
        return noteManager.updateNoteItemNameValue(mItems.get(0)) == 1;
    }
}
