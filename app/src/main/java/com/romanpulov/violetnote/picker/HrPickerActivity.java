package com.romanpulov.violetnote.picker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.romanpulov.violetnote.cloud.CloudAccountFacadeFactory;

public class HrPickerActivity extends AppCompatActivity {
    public static final String PICKER_SOURCE_TYPE = "PickerSourceType";
    public static final String PICKER_INITIAL_PATH = "PickerInitialPath";
    public static final String PICKER_RESULT = "PickerResult";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();

        fm.setFragmentResultListener(
                HrPickerFragment.RESULT_KEY,
                this,
                (requestKey, bundle) -> {
                    if (requestKey.equals(HrPickerFragment.RESULT_KEY)) {
                        String result = bundle.getString(HrPickerFragment.RESULT_VALUE_KEY);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(PICKER_RESULT, result);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                });

        Fragment fragment = fm.findFragmentById(android.R.id.content);
        if (fragment == null) {
            int sourceType = getIntent().getIntExtra(PICKER_SOURCE_TYPE, 0);

            HrPickerNavigator hrPickerNavigator = CloudAccountFacadeFactory.fromDocumentSourceType(sourceType).getHrPickerNavigator();
            HrPickerFragment hrPickerFragment = HrPickerFragment.newInstance("/", hrPickerNavigator);

            fm.beginTransaction().add(android.R.id.content, hrPickerFragment).commit();
        }
    }
}