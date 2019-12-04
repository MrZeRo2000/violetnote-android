package com.romanpulov.violetnote.db.dao;

import android.content.Context;
import androidx.annotation.NonNull;

import com.romanpulov.violetnote.db.provider.DBManagementProvider;
import com.romanpulov.violetnote.model.BasicCommonNoteA;

import static com.romanpulov.violetnote.model.BasicOrderedEntityNoteA.PRIORITY_HIGH;
import static com.romanpulov.violetnote.model.BasicOrderedEntityNoteA.PRIORITY_LOW;

/**
 * BasicCommonNote DAO
 */
public final class BasicCommonNoteDAO extends AbstractDAO<BasicCommonNoteA> {

    public BasicCommonNoteDAO(Context context) {
        super(context);
    }

    public boolean moveUp(@NonNull BasicCommonNoteA note) {
        DBManagementProvider dbManagementProvider = note.getDBManagementProvider();

        long prevOrderId = mDBHelper.getPrevOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getPrevOrderSelection(), dbManagementProvider.getOrderSelectionArgs());

        if (prevOrderId > 0) {
            mDBHelper.exchangeOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getOrderIdSelectionString(), note.getOrderId(), prevOrderId);
            return true;
        } else
            return false;
    }

    public boolean moveDown(@NonNull BasicCommonNoteA note) {
        DBManagementProvider dbManagementProvider = note.getDBManagementProvider();

        long nextOrderId = mDBHelper.getNextOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getNextOrderSelection(), dbManagementProvider.getOrderSelectionArgs());

        if (nextOrderId > 0) {
            mDBHelper.exchangeOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getOrderIdSelectionString(), note.getOrderId(), nextOrderId);
            return true;
        } else
            return false;
    }

    public boolean moveTop(@NonNull BasicCommonNoteA note) {
        DBManagementProvider dbManagementProvider = note.getDBManagementProvider();

        long minOrderId = mDBHelper.getMinOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getOrderIdSelection(), dbManagementProvider.getOrderIdSelectionArgs());
        long orderId = mDBHelper.getOrderId(dbManagementProvider.getTableName(), note.getId());

        if (orderId > minOrderId) {
            mDBHelper.moveOrderIdTop(dbManagementProvider.getTableName(), dbManagementProvider.getOrderIdSelectionString(), orderId, minOrderId);
            return true;
        } else
            return false;
    }

    public boolean moveBottom(@NonNull BasicCommonNoteA note) {
        DBManagementProvider dbManagementProvider = note.getDBManagementProvider();

        long maxOrderId = mDBHelper.getMaxOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getOrderIdSelection(), dbManagementProvider.getOrderIdSelectionArgs());
        long orderId = mDBHelper.getOrderId(dbManagementProvider.getTableName(), note.getId());

        if (orderId < maxOrderId) {
            mDBHelper.moveOrderIdBottom(dbManagementProvider.getTableName(), dbManagementProvider.getOrderIdSelectionString(), orderId, maxOrderId);
            return true;
        } else
            return false;
    }

    public boolean priorityUp(@NonNull BasicCommonNoteA note) {
        long priority = note.getPriority();
        if (priority == PRIORITY_HIGH)
            return false;

        priority ++;

        return updatePriority(note, priority);
    }

    public boolean priorityDown(@NonNull BasicCommonNoteA note) {
        long priority = note.getPriority();
        if (priority == PRIORITY_LOW)
            return false;

        priority --;

        return updatePriority(note, priority);
    }

    private boolean updatePriority(@NonNull BasicCommonNoteA note, long priority) {
        DBManagementProvider dbManagementProvider = note.getDBManagementProvider();

        note.setPriority(priority);

        long maxOrderId = mDBHelper.getMaxOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getOrderIdSelection(), dbManagementProvider.getOrderIdSelectionArgs());

        int result = mDBHelper.updatePriority(dbManagementProvider.getTableName(), note.getId(), priority);
        if (result == 0)
            return false;

        result = mDBHelper.updateOrderId(dbManagementProvider.getTableName(), note.getId(), maxOrderId + 1);
        if (result == 0)
            return false;

        note.setOrderId(maxOrderId + 1);

        return true;
    }
}
