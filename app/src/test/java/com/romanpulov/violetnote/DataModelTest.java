package com.romanpulov.violetnote;

import com.romanpulov.violetnote.model.PassCategoryA;
import com.romanpulov.violetnote.model.PassDataA;
import com.romanpulov.violetnote.model.PassDataReader;
import com.romanpulov.violetnotecore.Model.PassCategory2;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Model.PassNote2;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by rpulov on 04.04.2016.
 */
public class DataModelTest {

    private PassData2 createTestPassData() {
        PassData2 result = new PassData2();
        result.setCategoryList(new ArrayList<PassCategory2>());

        PassCategory2 pc = new PassCategory2("Category 1");
        pc.setNoteList(new ArrayList<PassNote2>());

        result.getCategoryList().add(pc);

        PassNote2 pn = new PassNote2("System 1", "Alexander", "Password 1", null, null, null, null);
        pc.getNoteList().add(pn);
        pn = new PassNote2("System 12", "Michael", "Password 12", null, null, null, null);
        pc.getNoteList().add(pn);
        pn = new PassNote2("System 13", "George", "Password 13", null, null, null, null);
        pc.getNoteList().add(pn);

        pc = new PassCategory2("Category 2");
        pc.setNoteList(new ArrayList<PassNote2>());

        result.getCategoryList().add(pc);

        pn = new PassNote2("System 2", "User 2", "Password 2", null, null, null, null);
        pc.getNoteList().add(pn);

        return result;
    }

    private PassDataA createTestPassDataA() {
        PassData2 pd = createTestPassData();
        return PassDataA.newInstance(null, pd);
    }

    @Test
    public void PassDataReaderTest() {
        PassData2 pd = createTestPassData();
        PassDataReader pdr = new PassDataReader(pd);
        pdr.readPassData();

        assertEquals(pd.getCategoryList().size(),  pdr.getPassCategoryDataA().size());
        for (PassCategoryA p : pdr.getPassCategoryDataA()) {
            System.out.println(p);
        }
    }

    @Test
    public void SearchTest() {
        String searchString = "George";
        assertTrue(searchString.matches("(?i:.*geor.*)"));
        assertFalse(searchString.matches("(?i:.*ger.*)"));
        assertTrue(searchString.matches("(?i:.*GeOr.*)"));
        assertTrue(searchString.matches("(?i:.*eOr.*)"));
        assertTrue(searchString.matches("(?i:.*rge.*)"));
        assertTrue(searchString.matches("(?i:.*rGe.*)"));
        assertFalse(searchString.matches("(?i:.*tge.*)"));
    }

    @Test
    public void SearchInstanceTest() {
        PassDataA pda = createTestPassDataA();

        PassDataA sda = PassDataA.newSearchInstance(pda, "eOr", true, true);
        assertEquals(sda.getPassCategoryData().size(), 1);
        assertEquals(sda.getPassNoteData().size(), 1);

        sda = PassDataA.newSearchInstance(pda, "yste", true, true);
        assertEquals(sda.getPassCategoryData().size(), 2);

        sda = PassDataA.newSearchInstance(pda, "stem 12", true, true);
        assertEquals(sda.getPassCategoryData().size(), 1);

        sda = PassDataA.newSearchInstance(pda, "xxx", true, true);
        assertNull(sda.getPassCategoryData());
        assertNull(sda.getPassNoteData());
    }

}
