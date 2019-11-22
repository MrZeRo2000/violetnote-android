package com.romanpulov.violetnote.onedrive;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.onedrive.sdk.authentication.MSAAuthenticator;
import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.core.DefaultClientConfig;
import com.onedrive.sdk.core.IClientConfig;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.Item;
import com.onedrive.sdk.logger.LoggerLevel;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;

public class OneDriveHelper {
    public static final int ONEDRIVE_ACTION_LOGIN = 0;
    public static final int ONEDRIVE_ACTION_LOGOUT = 2;

    public interface OnOneDriveActionListener {
        void onActionCompleted(int action, boolean result, String message);
    }

    //download:
    //item.children.getCurrentPage().get(1).getRawObject().get("@content.downloadUrl")

    private WeakReference<Activity> mActivity;

    public void setActivity(Activity activity) {
        mActivity = new WeakReference<>(activity);
    }

    private OnOneDriveActionListener mListener;

    public void setOnOneDriveActionListener(OnOneDriveActionListener listener) {
        this.mListener = listener;
    }

    /**
     * Expansion options to get all children, thumbnails of children, and thumbnails
     */
    private static final String EXPAND_OPTIONS_FOR_CHILDREN_AND_THUMBNAILS = "children(expand=thumbnails),thumbnails";

    /**
     * Expansion options to get all children, thumbnails of children, and thumbnails when limited
     */
    private static final String EXPAND_OPTIONS_FOR_CHILDREN_AND_THUMBNAILS_LIMITED = "children,thumbnails";


    private static OneDriveHelper instance;

    public static OneDriveHelper getInstance() {
        if (instance == null) {
            instance = new OneDriveHelper();
        }

        return instance;
    }

    private OneDriveHelper() {

    }

    private final AtomicReference<IOneDriveClient> mClient = new AtomicReference<>();

    private final ICallback<IOneDriveClient> clientCreateCallback = new ICallback<IOneDriveClient>() {
        @Override
        public void success(final IOneDriveClient result) {
            mClient.set(result);
            if (mListener != null) {
                mListener.onActionCompleted(ONEDRIVE_ACTION_LOGIN, true, null);
            }
        }

        @Override
        public void failure(final ClientException error) {
            mClient.set(null);
            if (mListener != null) {
                mListener.onActionCompleted(ONEDRIVE_ACTION_LOGIN, false, error.getMessage());
            }
        }
    };

    private final ICallback<IOneDriveClient> clientCreateWithLogoutCallback = new ICallback<IOneDriveClient>() {
        @Override
        public void success(final IOneDriveClient result) {
            mClient.set(result);
            mClient.get().getAuthenticator().logout(clientLogoutCallback);
        }

        @Override
        public void failure(final ClientException error) {
            mClient.set(null);
        }
    };

    private class ClientCreateWithItemCallback implements ICallback<IOneDriveClient> {
        private final String mPath;

        private ClientCreateWithItemCallback(String path) {
            mPath = path;
        }

        @Override
        public void success(IOneDriveClient result) {
            mClient.set(result);
            mClient.get()
                    .getDrive()
                    .getItems(mPath)
                    .buildRequest()
                    .expand(getExpansionOptions(mClient.get()))
                    .get(itemCallback);
        }

        @Override
        public void failure(ClientException ex) {
            mClient.set(null);
        }
    }

    private final ICallback<Void> clientLogoutCallback = new ICallback<Void>() {
        @Override
        public void success(final Void result) {
            mClient.set(null);
            if (mListener != null) {
                mListener.onActionCompleted(ONEDRIVE_ACTION_LOGOUT, true, null);
            }
        }

        @Override
        public void failure(final ClientException error) {
            mClient.set(null);
            if (mListener != null) {
                mListener.onActionCompleted(ONEDRIVE_ACTION_LOGOUT, false, error.getMessage());
            }
        }
    };

    private final ICallback<Item> itemCallback = new ICallback<Item>() {
        @Override
        public void success(Item item) {
            String text = null;
            try {
                String rawString = item.getRawObject().toString();
                final JSONObject object = new JSONObject(rawString);
                final int intentSize = 3;
                text = object.toString(intentSize);
                if ((mActivity != null) && (mActivity.get() != null)) {
                    Toast.makeText(mActivity.get() , "Navigated: " + item, Toast.LENGTH_SHORT).show();
                }
            } catch (final Exception e) {
                Log.e(getClass().getName(), "Unable to parse the response body to json");
            }
        }

        @Override
        public void failure(ClientException ex) {
            if ((mActivity != null) && (mActivity.get() != null)) {
                Toast.makeText(mActivity.get(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * Create the client configuration
     * @return the newly created configuration
     */
    private IClientConfig createConfig() {
        final MSAAuthenticator msaAuthenticator = new MSAAuthenticator() {
            @Override
            public String getClientId() {
                return "8ed347b1-d21c-4f8d-bdc4-6e53e17f120d";
            }

            @Override
            public String[] getScopes() {
                return new String[] {"onedrive.readwrite", "onedrive.appfolder", "wl.offline_access"};
            }
        };

        final IClientConfig config = DefaultClientConfig.createWithAuthenticator(msaAuthenticator);
        config.getLogger().setLoggingLevel(LoggerLevel.Debug);
        return config;
    }

    /**
     * Gets the expansion options for requests on items
     * @see {https://github.com/OneDrive/onedrive-api-docs/issues/203}
     * @param oneDriveClient the OneDrive client
     * @return The string for expand options
     */
    @NonNull
    private String getExpansionOptions(final IOneDriveClient oneDriveClient) {
        final String expansionOption;
        switch (oneDriveClient.getAuthenticator().getAccountInfo().getAccountType()) {
            case MicrosoftAccount:
                expansionOption = EXPAND_OPTIONS_FOR_CHILDREN_AND_THUMBNAILS;
                break;

            default:
                expansionOption = EXPAND_OPTIONS_FOR_CHILDREN_AND_THUMBNAILS_LIMITED;
                break;
        }
        return expansionOption;
    }

    private void internalCreateClient(final Activity activity) {
        new com.onedrive.sdk.extensions.OneDriveClient
                .Builder()
                .fromConfig(createConfig())
                .loginAndBuildClient(activity, clientCreateCallback);
    }

    public void createClient(Activity activity) {
        internalCreateClient(activity);
    }

    public void logout(Activity activity) {
        new com.onedrive.sdk.extensions.OneDriveClient
                .Builder()
                .fromConfig(createConfig())
                .loginAndBuildClient(activity, clientCreateWithLogoutCallback);
    }

    public void listItems(Activity activity, String path){
        if (mClient.get() == null) {
            new com.onedrive.sdk.extensions.OneDriveClient
                    .Builder()
                    .fromConfig(createConfig())
                    .loginAndBuildClient(activity, new ClientCreateWithItemCallback(path));
        } else {
            mClient.get()
                    .getDrive()
                    .getItems(path)
                    .buildRequest()
                    .expand(getExpansionOptions(mClient.get()))
                    .get(itemCallback);
        }
    }

    private void test() {
    }

}
