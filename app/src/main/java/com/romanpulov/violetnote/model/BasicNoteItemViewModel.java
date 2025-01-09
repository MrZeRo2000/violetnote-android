package com.romanpulov.violetnote.model;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.db.dao.BasicNoteItemDAO;
import com.romanpulov.violetnote.model.vo.BasicNoteSummary;
import com.romanpulov.violetnote.view.core.BasicCommonNoteViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BasicNoteItemViewModel extends BasicCommonNoteViewModel<BasicNoteItemA> {
    private BasicNoteItemDAO mBasicNoteItemDAO;

    private long mPriceNoteParamTypeId;
    private BasicNoteA mBasicNote;
    private MutableLiveData<List<BasicNoteItemA>> mBasicNoteItems;
    private BasicNoteSummary mBasicNoteSummary;
    private BasicNoteItemParamsSummary mBasicNoteItemParamsSummary;

    public void setPriceNoteParamTypeId(long mPriceNoteParamTypeId) {
        this.mPriceNoteParamTypeId = mPriceNoteParamTypeId;
    }

    public void setBasicNote(BasicNoteA basicNote) {
        if (!Objects.equals(this.mBasicNote, basicNote)) {
            this.mBasicNote = basicNote;
            loadNoteItems();
        }
    }

    public LiveData<List<BasicNoteItemA>> getBasicNoteItems() {
        if (mBasicNoteItems == null) {
            mBasicNoteItems = new MutableLiveData<>();
            loadNoteItems();
        }
        return mBasicNoteItems;
    }

    public BasicNoteSummary getBasicNoteSummary() {
        return mBasicNoteSummary;
    }

    public BasicNoteItemParamsSummary getBasicNoteItemParamsSummary() {
        List<BasicNoteItemA> items;
        if ((mBasicNoteItemParamsSummary == null) && ((items = getBasicNoteItems().getValue()) != null)) {
            mBasicNoteItemParamsSummary = BasicNoteItemParamsSummary.fromNoteItems(items, mPriceNoteParamTypeId);
        }
        return mBasicNoteItemParamsSummary;
    }

    public BasicNoteItemViewModel(@NotNull Application application) {
        super(application);
    }

    @Override
    protected BasicNoteItemDAO getDAO() {
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
        BasicNoteItemDAO.BasicNoteItemsWithSummary basicNoteItemsWithSummary = getDAO()
                .getNoteItemsWithSummary(mBasicNote);
        mBasicNoteSummary = basicNoteItemsWithSummary.summary();
        if (mBasicNoteItems == null) {
            mBasicNoteItems = new MutableLiveData<>();
        }
        mBasicNoteItems.setValue(basicNoteItemsWithSummary.items());

        mBasicNoteItemParamsSummary = null;
    }
}
