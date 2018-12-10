package com.romanpulov.violetnote.db.tabledef;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBDefFactory {

    @Nullable
    private static DBCommonDef.TableDefSQLProvider createInstance(@NonNull Class<? extends DBCommonDef.TableDefSQLProvider> clazzProvider) {
        try {
            return clazzProvider.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
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
                result.addAll(providerInstance.getSQLUpgrade(oldVersion));
            }
        }

        return result;
    }

    public static List<String> buildDBCreate(){
        return buildDBDef(DBDefRepository.getCreateTableDefs());
    }
}
