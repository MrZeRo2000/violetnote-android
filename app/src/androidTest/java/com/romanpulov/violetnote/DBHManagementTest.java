package com.romanpulov.violetnote;

import android.support.test.filters.SmallTest;

import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.DBDictionaryCache;
import com.romanpulov.violetnote.model.BasicHEventA;
import com.romanpulov.violetnote.model.BasicHEventTypeA;

import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.*;

@SmallTest
public final class DBHManagementTest extends DBBaseTest {
    @Override
    void prepareDatabase() {
        deleteDatabase();
    }

    @Test
    public void testMain() {
        synchronized (DBLock.instance) {
            prepareTestData();
            testEventTypes();
            testEvents();
        }
    }

    private void prepareTestData() {
        initDB();
    }

    private void testEventTypes() {
        List<BasicHEventTypeA> hEventTypes = mDBHManager.getHEventTypes();
        Assert.assertEquals(2, hEventTypes.size());

        Assert.assertEquals(1, mDBHelper.getDBDictionaryCache().getNoteItemsHEventParamId());
        Assert.assertEquals(2, mDBHelper.getDBDictionaryCache().getCheckoutHEventParamId());
    }

    private void testEvents() {
        assertNotEquals(-1, mDBHManager.insertHEvent(mDBHelper.getDBDictionaryCache().getNoteItemsHEventParamId(), null));
        sleep(500);
        assertNotEquals(-1, mDBHManager.insertHEvent(mDBHelper.getDBDictionaryCache().getNoteItemsHEventParamId(), null));
        sleep(600);
        assertNotEquals(-1, mDBHManager.insertHEvent(mDBHelper.getDBDictionaryCache().getCheckoutHEventParamId(), "Some summary"));

        List<BasicHEventA> testEvents = new ArrayList<>();

        mDBHManager.queryHEvents(testEvents);
        assertEquals(3, testEvents.size());

        mDBHManager.queryHEventsByType(testEvents, mDBHelper.getDBDictionaryCache().getNoteItemsHEventParamId());
        assertEquals(2, testEvents.size());
        mDBHManager.queryHEventsByType(testEvents, mDBHelper.getDBDictionaryCache().getCheckoutHEventParamId());
        assertEquals(1, testEvents.size());
    }

}
