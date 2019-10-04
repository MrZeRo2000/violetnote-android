package com.romanpulov.violetnote;

import android.os.Parcel;
import android.util.Log;

import android.support.test.filters.SmallTest;
import android.util.LongSparseArray;

import org.junit.*;
import static org.junit.Assert.*;

import com.romanpulov.violetnote.loader.document.DocumentPassDataLoader;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteItemParams;
import com.romanpulov.violetnote.model.vo.BasicNoteSummary;
import com.romanpulov.violetnote.model.ParcelableUtils;
import com.romanpulov.violetnote.model.vo.BasicNoteItemParamValueA;
import com.romanpulov.violetnote.model.vo.BasicParamValueA;
import com.romanpulov.violetnote.model.PassDataA;

import java.util.Collections;

/**
 * Created by romanpulov on 01.12.2016.
 */
@SmallTest
public class ParcelableTest {
    private final static String TAG = "ParcelableTest";
    private static void log(String message) {
        Log.d(TAG, message);
    }

    @Test
    public void test1() {
        log("Test message");
        assertEquals(1, 1);
    }

    @Test
    public void testParcelable() {
        PassDataA data = DocumentPassDataLoader.loadSamplePassData();

        Parcel parcel = Parcel.obtain();
        data.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);

        PassDataA readData = PassDataA.CREATOR.createFromParcel(parcel);

        assertEquals(data.getPassCategoryData().size(), readData.getPassCategoryData().size());
        assertEquals(data.getPassNoteData().size(), readData.getPassNoteData().size());

        //selective check category name
        assertEquals(data.getPassCategoryData().get(0).getCategoryName(), readData.getPassCategoryData().get(0).getCategoryName());

        //selective check attributes
        assertEquals(data.getPassNoteData().get(0).getNoteAttr().size(), readData.getPassNoteData().get(0).getNoteAttr().size());

        //selective check category for note
        assertEquals(data.getPassNoteData().get(0).getCategory().getCategoryName(), readData.getPassNoteData().get(0).getCategory().getCategoryName());
    }

    @Test
    public void testParcelableUtils() {
        PassDataA data = DocumentPassDataLoader.loadSamplePassData();
        PassDataA readData = ParcelableUtils.duplicateParcelableObject(data, PassDataA.CREATOR);

        assertEquals(data.getPassCategoryData().size(), readData.getPassCategoryData().size());
        assertEquals(data.getPassNoteData().size(), readData.getPassNoteData().size());

        //selective check category name
        assertEquals(data.getPassCategoryData().get(0).getCategoryName(), readData.getPassCategoryData().get(0).getCategoryName());

        //selective check attributes
        assertEquals(data.getPassNoteData().get(0).getNoteAttr().size(), readData.getPassNoteData().get(0).getNoteAttr().size());

        //selective check category for note
        assertEquals(data.getPassNoteData().get(0).getCategory().getCategoryName(), readData.getPassNoteData().get(0).getCategory().getCategoryName());
    }


    @Test
    public void testNoteItem() {
        BasicNoteItemA noteItem = BasicNoteItemA.newInstance(
                17,
                136,
                "last modified 136",
                2,
                1,
                0,
                "the name",
                "the value",
                false
        );

        noteItem.setNoteItemParams(
                BasicNoteItemParams.fromList(
                        Collections.singletonList(
                                BasicNoteItemParamValueA.newInstance(
                                        15,
                                        BasicParamValueA.newInstance(
                                                5,
                                                "z"
                                        )
                                )
                        )
                )
        );

        Parcel parcel = Parcel.obtain();
        noteItem.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);

        BasicNoteItemA newNoteItem = BasicNoteItemA.CREATOR.createFromParcel(parcel);

        assertEquals(noteItem.getName(), newNoteItem.getName());
        assertEquals(1, newNoteItem.getNoteItemParams().paramValues.size());
        assertEquals(5L, newNoteItem.getNoteItemParams().get(15).vInt);
        assertNotEquals(6L, newNoteItem.getNoteItemParams().get(15).vInt);
    }

    @Test
    public void testNote() {
        BasicNoteA note = BasicNoteA.newInstanceWithTotals(
          2,
          45,
          "last modified",
          4,
          5,
          1,
          "The Note",
          false,
          null,
          24,
          16
        );

        BasicNoteSummary summary = BasicNoteSummary.fromItemCounts(45, 22);
        LongSparseArray<Long> params = new LongSparseArray<>();
        params.append(8L, 53L);
        summary.appendParams(params);
        note.setSummary(summary);

        Parcel parcel = Parcel.obtain();
        note.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);

        BasicNoteA newNote = BasicNoteA.CREATOR.createFromParcel(parcel);
        BasicNoteSummary newSummary = newNote.getSummary();

        assertEquals(note.getNoteType(), newNote.getNoteType());
        assertEquals(note.getId(), note.getId());

        assertEquals(summary.getItemCount(), newSummary.getItemCount());
        assertEquals(summary.getCheckedItemCount(), newSummary.getCheckedItemCount());
        assertEquals(summary.getParams().get(8L), newSummary.getParams().get(8L));


    }

    @Test
    public void testNoteGroup() {
        BasicNoteGroupA noteGroup = BasicNoteGroupA.newInstance(22, 20,"Name1", 5234, 2);

        String noteGroupString = noteGroup.toString();

        Parcel parcel = Parcel.obtain();
        noteGroup.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);

        BasicNoteGroupA newNoteGroup = BasicNoteGroupA.CREATOR.createFromParcel(parcel);

        String newNoteGroupString = newNoteGroup.toString();

        assertEquals(newNoteGroupString, noteGroupString);

        assertEquals(noteGroup.getSummary().getNoteCount(), newNoteGroup.getSummary().getNoteCount());
    }
}
