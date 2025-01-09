package com.romanpulov.violetnote.model;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import org.jetbrains.annotations.NotNull;

public class AppViewModel extends AndroidViewModel {
    private long mPriceNoteParamTypeId;
    private final MutableLiveData<Boolean> mNoteGroupsChanged = new MutableLiveData<>(false);

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

    public MutableLiveData<Boolean> getNoteGroupsChanged() {
        return mNoteGroupsChanged;
    }
}
