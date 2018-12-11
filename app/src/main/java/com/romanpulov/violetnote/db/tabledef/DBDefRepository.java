package com.romanpulov.violetnote.db.tabledef;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class DBDefRepository {
    private static List<Class<? extends DBCommonDef.TableDefSQLProvider>> mTableDefs = new ArrayList<>();

    static {
        mTableDefs.add(HEventTypesTableDef.class);
        mTableDefs.add(HEventsTableDef.class);
    }

    public static List<Class<? extends DBCommonDef.TableDefSQLProvider>> getCreateTableDefs() {
        return mTableDefs;
    }
}
