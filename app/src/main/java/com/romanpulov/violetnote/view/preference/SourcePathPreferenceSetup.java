package com.romanpulov.violetnote.view.preference;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceFragmentCompat;

import com.romanpulov.library.common.network.NetworkUtils;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.cloud.CloudAccountFacade;
import com.romanpulov.violetnote.cloud.CloudAccountFacadeFactory;
import com.romanpulov.library.common.account.AbstractCloudAccountManager;
import com.romanpulov.violetnote.picker.HrPickerFragment;
import com.romanpulov.violetnote.view.SettingsFragmentDirections;
import com.romanpulov.violetnote.view.helper.DisplayMessageHelper;

import java.util.Objects;

import static com.romanpulov.violetnote.view.preference.PreferenceRepository.DEFAULT_SOURCE_TYPE;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.PREF_KEY_SOURCE_PATH;
import static com.romanpulov.violetnote.view.preference.PreferenceRepository.PREF_KEY_SOURCE_TYPE;

/**
 * Source path preference setup
 * Created by romanpulov on 08.09.2017.
 */
public class SourcePathPreferenceSetup extends PreferenceSetup {

    private final ActivityResultLauncher<Intent> mOpenDocumentResult;

    public SourcePathPreferenceSetup (PreferenceFragmentCompat preferenceFragment) {
        super(preferenceFragment, PREF_KEY_SOURCE_PATH);

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

        preferenceFragment.getParentFragmentManager().setFragmentResultListener(
                HrPickerFragment.RESULT_KEY, preferenceFragment, (requestKey, bundle) -> {
            String result = bundle.getString(HrPickerFragment.RESULT_VALUE_KEY);
            PreferenceRepository.setSourcePathPreferenceValue(mPreferenceFragment, result);
        });
    }

    @Override
    public void execute() {
        final SharedPreferences sharedPreferences = Objects.requireNonNull(
                mPreference.getPreferenceManager().getSharedPreferences());
        final String sourcePath = sharedPreferences.getString(mPreference.getKey(), null);
        mPreference.setSummary(sourcePath);

        mPreference.setOnPreferenceClickListener(preference -> {
            final int sourceType = sharedPreferences.getInt(PREF_KEY_SOURCE_TYPE, DEFAULT_SOURCE_TYPE);

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
                                SettingsFragmentDirections.ActionSettingsToHrPicker action = SettingsFragmentDirections.actionSettingsToHrPicker();
                                action.setSourceType(sourceType);
                                Navigation.findNavController(Objects.requireNonNull(mPreferenceFragment.getView())).navigate(action);
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
