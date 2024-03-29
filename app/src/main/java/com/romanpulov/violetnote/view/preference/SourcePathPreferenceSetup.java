package com.romanpulov.violetnote.view.preference;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.preference.PreferenceFragmentCompat;

import com.romanpulov.library.common.network.NetworkUtils;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.cloud.CloudAccountFacade;
import com.romanpulov.violetnote.cloud.CloudAccountFacadeFactory;
import com.romanpulov.library.common.account.AbstractCloudAccountManager;
import com.romanpulov.violetnote.picker.HrPickerActivity;
import com.romanpulov.violetnote.view.helper.DisplayMessageHelper;

import static com.romanpulov.violetnote.view.preference.PreferenceRepository.DEFAULT_SOURCE_TYPE;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.PREF_KEY_SOURCE_PATH;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.PREF_KEY_SOURCE_TYPE;

/**
 * Source path preference setup
 * Created by romanpulov on 08.09.2017.
 */
public class SourcePathPreferenceSetup extends PreferenceSetup {

    private final ActivityResultLauncher<Intent> mOpenDocumentResult;
    private final ActivityResultLauncher<Intent> mPickerResult;

    public SourcePathPreferenceSetup (PreferenceFragmentCompat preferenceFragment) {
        super(preferenceFragment, PREF_KEY_SOURCE_PATH);

        mPickerResult = mPreferenceFragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if ((result.getResultCode() == Activity.RESULT_OK) && (result.getData() != null)) {
                        String pickerResult = result.getData().getStringExtra(HrPickerActivity.PICKER_RESULT);
                        PreferenceRepository.setSourcePathPreferenceValue(mPreferenceFragment, pickerResult);
                    }
                }
        );

        mOpenDocumentResult = mPreferenceFragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if ((result.getResultCode() == Activity.RESULT_OK) && (result.getData() != null)) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            PreferenceRepository.setSourcePathPreferenceValue(mPreferenceFragment, uri.toString());
                       }
                    }
                }
        );

    }

    @Override
    public void execute() {
        final String sourcePath = mPreference.getPreferenceManager().getSharedPreferences().getString(mPreference.getKey(), null);
        mPreference.setSummary(sourcePath);

        mPreference.setOnPreferenceClickListener(preference -> {
            final int sourceType = preference.getPreferenceManager().getSharedPreferences().getInt(PREF_KEY_SOURCE_TYPE, DEFAULT_SOURCE_TYPE);

            if (PreferenceRepository.isCloudSourceType(sourceType)) {
                if (!NetworkUtils.isNetworkAvailable(mContext))
                    DisplayMessageHelper.displayErrorMessage(mActivity,mActivity.getResources().getString(R.string.error_internet_not_available));
                else {
                    final CloudAccountFacade cloudAccountFacade = CloudAccountFacadeFactory.fromDocumentSourceType(sourceType);
                    final AbstractCloudAccountManager<?> accountManager = cloudAccountFacade.getAccountManager(mActivity);
                    if (accountManager != null) {
                        accountManager.setOnAccountSetupListener(new AbstractCloudAccountManager.OnAccountSetupListener() {
                            @Override
                            public void onAccountSetupSuccess() {
                                Intent intent = new Intent(mActivity, HrPickerActivity.class);
                                intent.putExtra(
                                        HrPickerActivity.PICKER_INITIAL_PATH,
                                        mPreferenceFragment.getPreferenceManager().getSharedPreferences().getString(PREF_KEY_SOURCE_PATH, "/")
                                );
                                intent.putExtra(
                                        HrPickerActivity.PICKER_SOURCE_TYPE,
                                        sourceType
                                );
                                mPickerResult.launch(intent);
                            }

                            @Override
                            public void onAccountSetupFailure(String errorText) {
                                DisplayMessageHelper.displayErrorMessage(mActivity, errorText);
                            }
                        });

                        accountManager.setupAccount();
                    }
                }
            } else {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                mOpenDocumentResult.launch(intent);
            }

            return true;
        });
    }
}
