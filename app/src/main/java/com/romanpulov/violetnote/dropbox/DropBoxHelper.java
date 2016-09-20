package com.romanpulov.violetnote.dropbox;

import android.content.Context;
import android.content.SharedPreferences;

import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.DbxClientV2;

/**
 * Created by romanpulov on 30.06.2016.
 */
public class DropBoxHelper {
    public class DBHException extends Exception {
        public DBHException() {}
    }

    public class DBHNoAccessTokenException extends DBHException {
        public DBHNoAccessTokenException() {
            super();
        }
    }

    // public constants
    public final static String CLIENT_IDENTIFIER = "com.romanpulov.violetnote";

    //private constants
    private static final String SHARED_PREFERENCES_NAME = "dropbox-preferences";
    private static final String SHARED_PREFERENCES_ACCESS_TOKEN = "access-token";

    private final Context mContext;
    private final SharedPreferences mPrefs;
    private String mAccessToken;

    public static DropBoxHelper getInstance(Context context) {
        return new DropBoxHelper(context);
    }

    private DropBoxHelper(Context context) {
        mContext = context;
        mPrefs = mContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        mAccessToken = getAccessToken();
    }

    public String getAccessToken() {
        return mPrefs.getString(SHARED_PREFERENCES_ACCESS_TOKEN, null);
    }

    private void setAccessToken(String accessToken) {
        mPrefs.edit().putString(SHARED_PREFERENCES_ACCESS_TOKEN, accessToken).apply();
        mAccessToken = accessToken;
    }

    public void invokeAuthActivity(String appKey) {
        Auth.startOAuth2Authentication(mContext, appKey);
    }

    private String getAuthToken() {
        return Auth.getOAuth2Token();
    }

    public void refreshAccessToken() {
        String newAccessToken = getAuthToken();
        if (newAccessToken != null) {
            setAccessToken(newAccessToken);
        }
    }

    public DbxClientV2 getClient() {
        refreshAccessToken();
        DropboxClientFactory.init(mAccessToken);
        return DropboxClientFactory.getClient();
    }

    public void validateDropBox() throws DBHException {
        if (getAccessToken() == null)
            throw new DBHNoAccessTokenException();
    }
}
