package com.romanpulov.violetnote.model;

import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.db.dao.AbstractDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemDAO;
import com.romanpulov.violetnote.view.core.BasicCommonNoteViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BasicNoteItemViewModel extends BasicCommonNoteViewModel<BasicNoteItemA> {
    private BasicNoteItemDAO mBasicNoteItemDAO;

    private BasicNoteA mBasicNoteA;
    private MutableLiveData<List<BasicNoteItemA>> mBasicNoteItems;

    public void setBasicNote(BasicNoteA basicNote) {
        if (!Objects.equals(this.mBasicNoteA, basicNote)) {
            this.mBasicNoteA = basicNote;
            loadNoteItems();
        }
    }

    public MutableLiveData<List<BasicNoteItemA>> getBasicNoteItems() {
        if (mBasicNoteItems == null) {
            mBasicNoteItems = new MutableLiveData<>();
            loadNoteItems();
        }
        return mBasicNoteItems;
    }

    public BasicNoteItemViewModel(@NotNull Application application) {
        super(application);
    }

    @Override
    protected AbstractDAO<BasicNoteItemA> getDAO() {
        if (mBasicNoteItemDAO == null) {
            mBasicNoteItemDAO = new BasicNoteItemDAO(getApplication());
        }
        return mBasicNoteItemDAO;
    }

    @Override
    protected void onDataChangeActionCompleted() {
        loadNoteItems();
    }

    private void loadNoteItems() {

    }
}
