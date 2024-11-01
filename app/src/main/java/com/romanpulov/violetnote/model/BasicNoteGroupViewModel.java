package com.romanpulov.violetnote.model;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.db.dao.BasicNoteGroupDAO;
import com.romanpulov.violetnote.loader.document.DocumentPassDataLoader;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BasicNoteGroupViewModel extends AndroidViewModel {
    private final BasicNoteGroupDAO mBasicNoteGroupDAO;
    private MutableLiveData<List<BasicNoteGroupA>> mAllWithTotals;

    public BasicNoteGroupViewModel(@NotNull Application application) {
        super(application);
        mBasicNoteGroupDAO = new BasicNoteGroupDAO(application);
    }

    public MutableLiveData<List<BasicNoteGroupA>> getAllWithTotals() {
        if (mAllWithTotals == null) {
            mAllWithTotals = new MutableLiveData<>();
            loadAllWithTotals();
        }

        return mAllWithTotals;
    }

    private void loadAllWithTotals() {
        mAllWithTotals.setValue(mBasicNoteGroupDAO.getAllWithTotals(
                DocumentPassDataLoader.getDocumentFile(getApplication()) == null)
        );
    }
}
