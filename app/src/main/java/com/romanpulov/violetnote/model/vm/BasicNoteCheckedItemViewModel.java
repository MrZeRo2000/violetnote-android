package com.romanpulov.violetnote.model.vm;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.db.dao.BasicNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemDAO;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.vo.BasicNoteItemParamsSummary;
import com.romanpulov.violetnote.model.vo.BasicNoteSummary;
import com.romanpulov.violetnote.view.action.UIAction;
import com.romanpulov.violetnote.view.core.BasicCommonNoteViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class BasicNoteCheckedItemViewModel extends BasicCommonNoteViewModel<BasicNoteItemA> {
    private BasicNoteDAO mBasicNoteDAO;
    private BasicNoteItemDAO mBasicNoteItemDAO;

    private long mPriceNoteParamTypeId;
    private MutableLiveData<Boolean> mNoteGroupsChanged;
    private MutableLiveData<Boolean> mNoteCheckedItemChanged;

    private BasicNoteA mBasicNote;
    private MutableLiveData<List<BasicNoteItemA>> mBasicNoteItems;
    private MutableLiveData<Collection<String>> mValues;

    private BasicNoteSummary mBasicNoteSummary;
    private BasicNoteItemParamsSummary mBasicNoteItemParamsSummary;
    private MutableLiveData<List<BasicNoteA>> mRelatedNotes;

    public long getPriceNoteParamTypeId() {
        return mPriceNoteParamTypeId;
    }

    public void setPriceNoteParamTypeId(long mPriceNoteParamTypeId) {
        this.mPriceNoteParamTypeId = mPriceNoteParamTypeId;
    }

    public void setNoteGroupsChanged(MutableLiveData<Boolean> mNoteGroupsChanged) {
        this.mNoteGroupsChanged = mNoteGroupsChanged;
    }

    public void setNoteCheckedItemChanged(MutableLiveData<Boolean> mNoteCheckedItemChanged) {
        this.mNoteCheckedItemChanged = mNoteCheckedItemChanged;
    }

    public BasicNoteA getBasicNote() {
        return mBasicNote;
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

    public LiveData<Collection<String>> getValues() {
        if (mValues == null) {
            mValues = new MutableLiveData<>();
        }
        return mValues;
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

    public BasicNoteCheckedItemViewModel(@NotNull Application application) {
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
        onNoteGroupsChanged();
        onNoteCheckedItemChanged();
    }

    private void onNoteGroupsChanged() {
        if (mNoteGroupsChanged != null) {
            mNoteGroupsChanged.setValue(true);
        }
    }

    private void onNoteCheckedItemChanged() {
        if (mNoteCheckedItemChanged != null) {
            mNoteCheckedItemChanged.setValue(true);
        }
    }

    private void loadNoteItems() {
        BasicNoteItemDAO.BasicNoteItemsWithSummary basicNoteItemsWithSummary = getDAO()
                .getNoteItemsWithSummary(mBasicNote);
        mBasicNoteSummary = basicNoteItemsWithSummary.summary();
        mBasicNoteItemParamsSummary = null;

        if (mBasicNoteItems == null) {
            mBasicNoteItems = new MutableLiveData<>();
        }
        mBasicNoteItems.setValue(basicNoteItemsWithSummary.items());

        if (getValues().getValue() == null) {
            loadValues();
        }
    }

    public void loadValues() {
        mValues.setValue(getBasicNoteDAO().getNoteValues(mBasicNote));
    }

    public void toggleChecked(BasicNoteItemA item) {
        if (mBasicNoteItems.getValue() != null) {
            int checkDiff = item.isChecked() ? -1 : 1;

            getDAO().updateChecked(item, !item.isChecked());

            BasicNoteItemA updatedItem = getDAO().getById(item.getId());
            updatedItem.setNoteItemParams(item.getNoteItemParams());

            List<BasicNoteItemA> newItems = mBasicNoteItems.getValue()
                    .stream()
                    .map(v -> v.getId() == item.getId() ? updatedItem : v)
                    .collect(Collectors.toList());

            mBasicNoteSummary.addCheckedItemCount(checkDiff);
            mBasicNoteItemParamsSummary = null;

            mBasicNoteItems.setValue(newItems);

            onNoteGroupsChanged();
            onNoteCheckedItemChanged();
        }
    }

    public void updateAllChecked(boolean checked) {
        if ((mBasicNoteItems.getValue() != null) &&
                (getDAO().updateChecked(mBasicNoteItems.getValue(), checked) > 0)) {
            onDataChangeActionCompleted();
        }
    }

    public void refresh() {
        loadNoteItems();
    }

    public void editNameValue(BasicNoteItemA item, UIAction<BasicNoteItemA> action) {
        if (getDAO().updateNameValue(item) != -1) {
            setAction(action);
            onDataChangeActionCompleted();
        }
    }

    public void checkout() {
        if ((getBasicNoteItems().getValue() != null) && (getValues().getValue() != null)) {
            getBasicNoteDAO().checkOut(
                    getBasicNote(),
                    getBasicNoteItems().getValue(),
                    getValues().getValue());
            onDataChangeActionCompleted();
            loadValues();
        }
    }

    public void moveToOtherNote(
            Collection<BasicNoteItemA> items,
            BasicNoteA otherNote,
            UIAction<BasicNoteItemA> action) {
        long result = items
                .stream()
                .reduce(
                        0L,
                        (a, v) -> a + getDAO().moveToOtherNote(v, otherNote),
                        Long::sum);
        if (result > 0) {
            setAction(action);
            onDataChangeActionCompleted();
        }
    }

    public void addNewUniqueValues(Collection<String> values, UIAction<BasicNoteItemA> action) {
        if (getBasicNoteItems().getValue() != null) {
            Set<String> newValues = new HashSet<>(values);

            Set<String> oldValues = getBasicNoteItems().getValue()
                    .stream()
                    .map(BasicNoteItemA::getValue)
                    .collect(Collectors.toSet());

            //remove existing
            newValues.removeAll(oldValues);

            //insert
            long result = newValues
                    .stream()
                    .map(BasicNoteItemA::newCheckedEditValueInstance)
                    .reduce(
                            0L,
                            (a, v) -> a + getDAO().insertWithNote(mBasicNote, v),
                            Long::sum);
            if (result > 0) {
                setAction(action);
                onDataChangeActionCompleted();
            }
        }
    }
}
