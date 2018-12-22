package com.romanpulov.violetnote;

import android.app.Application;
import android.os.Parcel;
import android.util.Log;

import android.support.test.filters.SmallTest;
import org.junit.*;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.*;

import com.romanpulov.violetnote.loader.document.DocumentPassDataLoader;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteItemParamValueA;
import com.romanpulov.violetnote.model.BasicNoteItemParams;
import com.romanpulov.violetnote.model.BasicParamValueA;
import com.romanpulov.violetnote.model.PassDataA;

import java.util.Arrays;
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

        noteItem.setNoteItemParams(BasicNoteItemParams.fromList(Collections.singletonList(new BasicNoteItemParamValueA(15, new BasicParamValueA(5, "z")))));

        Parcel parcel = Parcel.obtain();
        noteItem.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);

        BasicNoteItemA newNoteItem = BasicNoteItemA.CREATOR.createFromParcel(parcel);

        assertEquals(noteItem.getName(), newNoteItem.getName());
        assertEquals(1, newNoteItem.getNoteItemParams().paramValues.size());
        assertEquals(5L, newNoteItem.getNoteItemParams().get(15).vInt);
        assertNotEquals(6L, newNoteItem.getNoteItemParams().get(15).vInt);
    }
}
