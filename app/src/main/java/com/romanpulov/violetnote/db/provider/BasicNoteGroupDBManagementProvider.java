package com.romanpulov.violetnote.db.provider;

import com.romanpulov.violetnote.db.tabledef.NoteGroupsTableDef;
import com.romanpulov.violetnote.model.BasicNoteGroupA;

public class BasicNoteGroupDBManagementProvider extends AbstractBasicOrderedEntityNoteDBManagementProvider {

    public BasicNoteGroupDBManagementProvider(BasicNoteGroupA basicNoteGroup) {
        super(basicNoteGroup);
    }

    @Override
    public String getTableName() {
        return NoteGroupsTableDef.TABLE_NAME;
    }
}
