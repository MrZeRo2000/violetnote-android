package com.romanpulov.violetnote.model;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import org.jetbrains.annotations.NotNull;

public class AppViewModel extends AndroidViewModel {
    private long mPriceNoteParamTypeId;

    public AppViewModel(@NotNull Application application) {
        super(application);
    }

    public long getPriceNoteParamTypeId() {
        if (mPriceNoteParamTypeId == 0) {
            mPriceNoteParamTypeId = DBBasicNoteHelper.getInstance(getApplication())
                    .getDBDictionaryCache()
                    .getPriceNoteParamTypeId();
        }
        return mPriceNoteParamTypeId;
    }
}
