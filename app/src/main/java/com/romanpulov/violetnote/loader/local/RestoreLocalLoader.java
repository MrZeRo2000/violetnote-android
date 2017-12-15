package com.romanpulov.violetnote.loader.local;

import android.content.Context;

import com.romanpulov.library.common.loader.core.AbstractContextLoader;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBStorageManager;

/**
 * Local restore loader
 * Created by romanpulov on 15.12.2017.
 */

public class RestoreLocalLoader extends AbstractContextLoader {

    public RestoreLocalLoader(Context context) {
        super(context);
    }

    @Override
    public void load() throws Exception {
        DBBasicNoteHelper.getInstance(mContext).closeDB();

        DBStorageManager storageManager = new DBStorageManager(mContext);
        String restoreResult = storageManager.restoreLocalBackup();

        DBBasicNoteHelper.getInstance(mContext).openDB();

        if (restoreResult == null)
            throw new Exception(mContext.getString(R.string.error_restore));
    }
}
