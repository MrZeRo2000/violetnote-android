package com.romanpulov.violetnote.db;

import android.content.Context;

import com.romanpulov.violetnote.db.manager.DBHManager;

/**
 * Upgrades data
 */
public class DBDataUpgradeManager {

    public static void upgradeData(Context context, int oldVersion) {
        switch(oldVersion) {
            case 4:
                DBHManager dbhManager = new DBHManager(context);
                dbhManager.mBasicHNoteCOItemDAO.collapseEvents(10000);
                dbhManager.mBasicHEventDAO.deleteOrphaned();
                break;
            case 100:
                break;
        }
    }
}
