package com.romanpulov.violetnote.cloud;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.romanpulov.library.gdrive.GDBaseHelper;
import com.romanpulov.library.gdrive.GDConfig;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.helper.DisplayMessageHelper;

public class GDHelper extends GDBaseHelper {
    private static final int REQUEST_CODE_SIGN_IN = 7332;

    static void displayMessage(Activity activity, int displayMessageId) {
        DisplayMessageHelper.displayInfoMessage(activity, activity.getString(displayMessageId));
    }

    static void displayMessage(Activity activity, int displayMessageId, String message) {
        DisplayMessageHelper.displayInfoMessage(activity, activity.getString(displayMessageId, message));
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

    public static void handleActivityResult(Activity activity, int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            GoogleSignIn.getSignedInAccountFromIntent(data)
                    .addOnSuccessListener(account -> {
                        GDHelper.displayMessage(activity, R.string.message_gdrive_successfully_logged_in);
                        GDHelper.getInstance().setServerAuthCode(account.getServerAuthCode());
                    })
                    .addOnFailureListener(e -> {
                        GDHelper.displayMessage(activity, R.string.error_onedrive_login, e.getMessage());
                    });
        }
    }
}
