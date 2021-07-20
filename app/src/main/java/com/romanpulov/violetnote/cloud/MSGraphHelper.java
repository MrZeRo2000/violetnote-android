package com.romanpulov.violetnote.cloud;

import android.content.Context;

import com.romanpulov.library.msgraph.MSALConfig;
import com.romanpulov.library.msgraph.MSGraphBaseHelper;
import com.romanpulov.library.msgraph.OnMSActionListener;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.picker.HrPickerItem;
import com.romanpulov.violetnote.picker.HrPickerNavigationProcessor;
import com.romanpulov.violetnote.picker.HrPickerNavigator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MSGraphHelper extends MSGraphBaseHelper implements HrPickerNavigator {
    @Override
    protected void configure() {
        MSALConfig.configure(R.raw.auth_config_single_account, "Files.ReadWrite.All".toLowerCase().split(" "));
    }

    private static MSGraphHelper instance;

    private final AtomicBoolean mNavigating = new AtomicBoolean(false);

    public static MSGraphHelper getInstance() {
        if (instance == null) {
            instance = new MSGraphHelper();
            instance.configure();
        }

        return instance;
    }

    private MSGraphHelper() {

    }

    private static List<HrPickerItem> parseJSONObject(JSONObject data) throws JSONException {
        List<HrPickerItem> result = new ArrayList<>();

        JSONArray jsonItems = data.getJSONArray("value");
        for (int i = 0; i < jsonItems.length(); i ++) {
            JSONObject o = (JSONObject) jsonItems.get(i);

            int itemType;

            if (!o.isNull("folder")) {
                itemType = HrPickerItem.ITEM_TYPE_FOLDER;
            } else if (!o.isNull("file")) {
                itemType = HrPickerItem.ITEM_TYPE_FILE;
            } else {
                itemType = HrPickerItem.ITEM_TYPE_UNKNOWN;
            }

            result.add(HrPickerItem.createItem(itemType, o.getString("name")));
        }

        return result;
    }

    @Override
    synchronized public void onNavigate(Context context, final String path, final HrPickerNavigationProcessor processor) {
        if (!mNavigating.get()) {
            mNavigating.set(true);

            listItems(
                    context,
                    path,
                    new OnMSActionListener<JSONObject>() {
                        @Override
                        public void onActionSuccess(int action, JSONObject data) {
                            //Log.d(TAG, "Obtained data:" + data.toString());
                            mNavigating.set(false);
                            try {
                                processor.onNavigationSuccess(path, parseJSONObject(data));
                            } catch (JSONException e) {
                                processor.onNavigationFailure(path, "Error parsing response: " + e.getMessage());
                            }
                        }

                        @Override
                        public void onActionFailure(int action, String errorMessage) {
                            //Log.d(TAG, "Navigation error, passing error message: " + errorMessage);
                            mNavigating.set(false);
                            processor.onNavigationFailure(path, errorMessage);
                        }
                    });
        }
    }
}
