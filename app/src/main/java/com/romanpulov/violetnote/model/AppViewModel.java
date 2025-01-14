package com.romanpulov.violetnote.model;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.db.DBBasicNoteHelper;

public class AppViewModel extends AndroidViewModel {
    private long mPriceNoteParamTypeId;
    private final MutableLiveData<Boolean> mNoteGroupsChanged = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mNoteCheckedItemChanged = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mNoteValuesChanged = new MutableLiveData<>(false);

    public AppViewModel(@NonNull Application application) {
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

    @NonNull
    public MutableLiveData<Boolean> getNoteGroupsChanged() {
        return mNoteGroupsChanged;
    }

    @NonNull
    public MutableLiveData<Boolean> getNoteCheckedItemChanged() {
        return mNoteCheckedItemChanged;
    }

    @NonNull
    public MutableLiveData<Boolean> getNoteValuesChanged() {
        return mNoteValuesChanged;
    }
}
