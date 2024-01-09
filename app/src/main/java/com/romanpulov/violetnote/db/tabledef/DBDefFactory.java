package com.romanpulov.violetnote.db.tabledef;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.romanpulov.violetnote.view.helper.LoggerHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DBDefFactory {
    private final static String TAG = DBDefFactory.class.getSimpleName();

    @Nullable
    private static DBCommonDef.TableDefSQLProvider createInstance(@NonNull Class<? extends DBCommonDef.TableDefSQLProvider> clazzProvider) {
        try {
            return clazzProvider.getDeclaredConstructor().newInstance();
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            LoggerHelper.logDebug(TAG, "Error creating instance:" + e);
            return null;
        }
    }

    private static List<String> buildDBDef(@NonNull List<Class<? extends DBCommonDef.TableDefSQLProvider>> providers) {
        List<String> result = new ArrayList<>();

        for (Class<? extends DBCommonDef.TableDefSQLProvider> p : providers) {
            DBCommonDef.TableDefSQLProvider providerInstance = createInstance(p);
            if (providerInstance != null) {
                result.addAll(providerInstance.getSQLCreate());
            }
        }

        return result;
    }

    private static List<String> buildDBUpgrade(@NonNull List<Class<? extends DBCommonDef.TableDefSQLProvider>> providers, int oldVersion) {
        List<String> result = new ArrayList<>();

        for (Class<? extends DBCommonDef.TableDefSQLProvider> p : providers) {
            DBCommonDef.TableDefSQLProvider providerInstance = createInstance(p);
            if (providerInstance != null) {
                List<String> sqlUpgrade = providerInstance.getSQLUpgrade(oldVersion);
                if (sqlUpgrade != null) {
                    result.addAll(sqlUpgrade);
                }
            }
        }

        return result;
    }

    public static List<String> buildDBCreate(){
        return buildDBDef(DBDefRepository.getCreateTableDefs());
    }

    public static List<String> buildDBUpgrade(int oldVersion){
        return buildDBUpgrade(DBDefRepository.getCreateTableDefs(), oldVersion);
    }

}
