package com.romanpulov.violetnote.model.vm;

import android.app.Application;
import android.util.LongSparseArray;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.romanpulov.violetnote.db.dao.BasicHEventDAO;
import com.romanpulov.violetnote.db.dao.BasicHNoteCOItemDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemDAO;
import com.romanpulov.violetnote.model.BasicHEventA;
import com.romanpulov.violetnote.model.BasicHNoteCOItemA;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.view.core.BasicEntityNoteViewModel;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BasicHNoteCOItemViewModel extends BasicEntityNoteViewModel<BasicHNoteCOItemA> {
    private BasicHNoteCOItemDAO mBasicHNoteCOItemDAO;
    private BasicHEventDAO mBasicHEventDAO;
    private BasicNoteItemDAO mBasicNoteItemDAO;

    public record BasicHEventHNoteCOItems(
            LongSparseArray<BasicHEventA> hEvents,
            LongSparseArray<List<BasicHNoteCOItemA>> hNoteCOItems,
            Collection<String> values) {}

    private MutableLiveData<BasicHEventHNoteCOItems> mBasicHEventHNoteCOItems;

    public LiveData<BasicHEventHNoteCOItems> getBasicHEventHNoteCOItems() {
        if (mBasicHEventHNoteCOItems == null) {
            mBasicHEventHNoteCOItems = new MutableLiveData<>();
            loadItems();
        }
        return mBasicHEventHNoteCOItems;
    }

    private BasicNoteA mBasicNote;

    public void setBasicNote(BasicNoteA basicNote) {
        if (!Objects.equals(this.mBasicNote, basicNote)) {
            this.mBasicNote = basicNote;
            loadItems();
        }
    }

    public BasicHNoteCOItemViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onDataChangeActionCompleted() {
        loadItems();
    }

    @Override
    protected BasicHNoteCOItemDAO getDAO() {
        if (mBasicHNoteCOItemDAO == null) {
            mBasicHNoteCOItemDAO = new BasicHNoteCOItemDAO(getApplication());
        }
        return mBasicHNoteCOItemDAO;
    }

    private BasicHEventDAO getBasicHEventDAO() {
        if (mBasicHEventDAO == null) {
            mBasicHEventDAO = new BasicHEventDAO(getApplication());
        }
        return mBasicHEventDAO;
    }

    public BasicNoteItemDAO getBasicNoteItemDAO() {
        if (mBasicNoteItemDAO == null) {
            mBasicNoteItemDAO = new BasicNoteItemDAO(getApplication());
        }
        return mBasicNoteItemDAO;
    }

    private void loadItems() {
        if (mBasicHEventHNoteCOItems == null) {
            mBasicHEventHNoteCOItems = new MutableLiveData<>();
        }

        mBasicHEventHNoteCOItems.setValue(new BasicHEventHNoteCOItems(
                BasicHEventA.getArrayFromList(
                        getBasicHEventDAO().getByCOItemsNoteId(mBasicNote.getId())),
                BasicHNoteCOItemA.getArrayFromList(
                        getDAO().getByNoteId(mBasicNote.getId())),
                getBasicNoteItemDAO().getByNote(mBasicNote)
                        .stream()
                        .map(BasicNoteItemA::getValue)
                        .collect(Collectors.toSet())));
    }
}
