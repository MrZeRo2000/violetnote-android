package com.romanpulov.violetnote.db.provider;

import com.romanpulov.violetnote.db.tabledef.NotesTableDef;
import com.romanpulov.violetnote.model.BasicNoteA;

public class BasicNoteDBManagementProvider extends AbstractBasicOrderedEntityNoteDBManagementProvider {

    public BasicNoteDBManagementProvider(BasicNoteA basicNoteA) {
        super(basicNoteA);
    }

    @Override
    public String getTableName() {
        return NotesTableDef.TABLE_NAME;
    }

}
