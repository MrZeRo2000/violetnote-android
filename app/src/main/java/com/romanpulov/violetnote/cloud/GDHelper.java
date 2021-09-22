package com.romanpulov.violetnote.cloud;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.romanpulov.library.gdrive.GDBaseHelper;
import com.romanpulov.library.gdrive.GDConfig;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.preference.PreferenceRepository;

public class GDHelper extends GDBaseHelper {
    private static final int REQUEST_CODE_SIGN_IN = 7332;

    static void displayMessage(Context context, int displayMessageId) {
        PreferenceRepository.displayMessage(context, context.getString(displayMessageId));
    }

    static void displayMessage(Context context, int displayMessageId, String message) {
        PreferenceRepository.displayMessage(context, context.getString(displayMessageId, message));
    }

    @Override
    protected void configure() {
        GDConfig.configure(R.raw.gd_config, "https://www.googleapis.com/auth/drive", REQUEST_CODE_SIGN_IN);
    }

    private static GDHelper instance;

    private GDHelper() {
        super();
    }

    public static GDHelper getInstance() {
        if (instance == null) {
            instance = new GDHelper();
        }
        return instance;
    }

    public static void handleActivityResult(Context context, int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            GoogleSignIn.getSignedInAccountFromIntent(data)
                    .addOnSuccessListener(account -> {
                        GDHelper.displayMessage(context, R.string.message_gdrive_successfully_logged_in);
                        GDHelper.getInstance().setServerAuthCode(account.getServerAuthCode());
                    })
                    .addOnFailureListener(e -> {
                        GDHelper.displayMessage(context, R.string.error_onedrive_login, e.getMessage());
                    });
        }
    }
}
