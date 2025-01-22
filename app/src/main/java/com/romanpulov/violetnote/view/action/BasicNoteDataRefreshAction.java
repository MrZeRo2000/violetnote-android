package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.service.PassNoteItemJSONCryptService;

/**
 * BasicNoteDataA refresh action
 * Created by romanpulov on 13.09.2016.
 */
public class BasicNoteDataRefreshAction extends BasicAction<BasicNoteDataA> {
    private final String mPassword;
    private boolean mRequireValues = false;

    public BasicNoteDataRefreshAction(BasicNoteDataA basicNoteData, String password) {
        super(basicNoteData);
        mPassword = password;
    }

    public BasicNoteDataRefreshAction requireValues() {
        mRequireValues = true;
        return this;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        //retrieve data
        noteManager.mBasicNoteItemDAO.fillNoteDataItemsWithSummary(mData.getNote());
        if (mRequireValues) {
            noteManager.mBasicNoteDAO.fillNoteValues(mData.getNote());
        }

        //decrypt
        if (mData.getNote().isEncrypted()) {
            for (BasicNoteItemA item : mData.getNote().getItems()) {
                if (!PassNoteItemJSONCryptService.decryptBasicNoteItem(item, mPassword))
                    return false;
            }
        }
        mData.setPassword(mPassword);
        return true;
    }
}
