package com.romanpulov.violetnote.model;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.db.dao.BasicNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemDAO;
import com.romanpulov.violetnote.model.vo.BasicNoteSummary;
import com.romanpulov.violetnote.view.core.BasicCommonNoteViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BasicNoteItemViewModel extends BasicCommonNoteViewModel<BasicNoteItemA> {
    private BasicNoteDAO mBasicNoteDAO;
    private BasicNoteItemDAO mBasicNoteItemDAO;

    private long mPriceNoteParamTypeId;
    private BasicNoteA mBasicNote;
    private MutableLiveData<List<BasicNoteItemA>> mBasicNoteItems;
    private BasicNoteSummary mBasicNoteSummary;
    private BasicNoteItemParamsSummary mBasicNoteItemParamsSummary;
    private MutableLiveData<List<BasicNoteA>> mRelatedNotes;

    public void setPriceNoteParamTypeId(long mPriceNoteParamTypeId) {
        this.mPriceNoteParamTypeId = mPriceNoteParamTypeId;
    }

    public void setBasicNote(BasicNoteA basicNote) {
        if (!Objects.equals(this.mBasicNote, basicNote)) {
            mBasicNote = basicNote;
            mRelatedNotes = null;
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

    public LiveData<List<BasicNoteA>> getRelatedNotes() {
        if (mRelatedNotes == null) {
            mRelatedNotes = new MutableLiveData<>();
            mRelatedNotes.setValue(getBasicNoteDAO().getRelatedNotes(mBasicNote));
        }

        return mRelatedNotes;
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

    private BasicNoteDAO getBasicNoteDAO() {
        if (mBasicNoteDAO == null) {
            mBasicNoteDAO = new BasicNoteDAO(getApplication());
        }
        return mBasicNoteDAO;
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

    public void toggleChecked(BasicNoteItemA item) {
        if (mBasicNoteItems.getValue() != null) {
            getDAO().updateChecked(item, !item.isChecked());
            BasicNoteItemA updatedItem = getDAO().getById(item.getId());
            List<BasicNoteItemA> newItems = mBasicNoteItems.getValue()
                    .stream()
                    .map(v -> v.getId() == item.getId() ? updatedItem : v)
                    .collect(Collectors.toList());
            mBasicNoteItems.setValue(newItems);
        }
    }

    public void refresh() {
        loadNoteItems();
    }
}
