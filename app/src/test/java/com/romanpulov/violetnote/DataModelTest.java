package com.romanpulov.violetnote;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassNote;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by rpulov on 04.04.2016.
 */
public class DataModelTest {

    public PassData createTestPassData() {
        PassData result = new PassData();

        PassCategory pc = new PassCategory("Category 1");
        result.getPassCategoryList().add(pc);

        PassNote pn = new PassNote(pc, "System 1", "User 1", "Password 1", null, null, null);
        result.getPassNoteList().add(pn);
        pn = new PassNote(pc, "System 12", "User 12", "Password 12", null, null, null);
        result.getPassNoteList().add(pn);
        pn = new PassNote(pc, "System 13", "User 13", "Password 13", null, null, null);
        result.getPassNoteList().add(pn);

        pc = new PassCategory("Category 2");
        result.getPassCategoryList().add(pc);

        pn = new PassNote(pc, "System 2", "User 2", "Password 2", null, null, null);
        result.getPassNoteList().add(pn);

        return result;
    }

    @Test
    public void Test1() {

        PassData pd = createTestPassData();
        PassDataExp pde = PassDataExp.newInstance(pd);

        assertEquals(pd.getPassCategoryList().size(), pde.getPassCategoryList().size());

        long totalSize = 0;
        for (List<?> list : pde.getPassNoteList().values()) {
            totalSize = totalSize + list.size();
        }

        assertEquals(pd.getPassNoteList().size(), totalSize);
    }

    @Test
    public void PassDataReaderTest() {
        PassData pd = createTestPassData();
        PassDataReader pdr = new PassDataReader(pd);
        pdr.readCategoryData();
        pdr.readNoteData();

        assertEquals(pd.getPassCategoryList().size(),  pdr.getPassCategoryDataA().size());
        for (PassCategoryA p : pdr.getPassCategoryDataA()) {
            System.out.println(p);
        }
    }

}
