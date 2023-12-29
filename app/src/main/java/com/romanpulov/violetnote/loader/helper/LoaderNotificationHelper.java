package com.romanpulov.violetnote.loader.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import androidx.core.app.NotificationCompat;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.SettingsActivity;

import java.text.DateFormat;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Notification helper for loader operations
 * Created by romanpulov on 07.03.2018.
 */

public final class LoaderNotificationHelper {
    public static final int NOTIFICATION_TYPE_SUCCESS = 1;
    public static final int NOTIFICATION_TYPE_FAILURE = 2;

    private static final String NOTIFICATION_SUCCESS_CHANNEL_ID = "SUCCESS_NOTIFICATION_CHANNEL";
    private static final String NOTIFICATION_FAILURE_CHANNEL_ID = "FAILURE_NOTIFICATION_CHANNEL";

    private static final SparseIntArray NOTIFICATION_ICONS = new SparseIntArray();
    private static final SparseArray<String> NOTIFICATION_CHANNELS = new SparseArray<>();

    static {
        NOTIFICATION_ICONS.put(NOTIFICATION_TYPE_SUCCESS, R.drawable.ic_action_info);
        NOTIFICATION_ICONS.put(NOTIFICATION_TYPE_FAILURE, R.drawable.ic_action_warning);
        NOTIFICATION_CHANNELS.put(NOTIFICATION_TYPE_SUCCESS, NOTIFICATION_SUCCESS_CHANNEL_ID);
        NOTIFICATION_CHANNELS.put(NOTIFICATION_TYPE_FAILURE, NOTIFICATION_FAILURE_CHANNEL_ID);
    }

    private static boolean mChannelsCreated = false;

    private static void createNotificationChannels(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel successChannel = new NotificationChannel(
                    NOTIFICATION_SUCCESS_CHANNEL_ID,
                    context.getString(R.string.notification_success_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            successChannel.setDescription(context.getString(R.string.notification_success_channel_description));

            NotificationChannel failureChannel = new NotificationChannel(
                    NOTIFICATION_FAILURE_CHANNEL_ID,
                    context.getString(R.string.notification_failure_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            failureChannel.setDescription(context.getString(R.string.notification_failure_channel_description));

            // Register the successChannel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(successChannel);
            notificationManager.createNotificationChannel(failureChannel);
        }
    }

    private static void setupNotificationChannels(Context context) {
        if (!mChannelsCreated) {
            createNotificationChannels(context);
            mChannelsCreated = true;
        }
    }

    public static void notify(Context context, String message, int notificationId, int notificationType) {
        setupNotificationChannels(context);

        Intent intent = new Intent(context, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, NOTIFICATION_CHANNELS.get(notificationType))
                        .setSmallIcon(LoaderNotificationHelper.NOTIFICATION_ICONS.get(notificationType))
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(message)
                        .setSubText(DateFormat.getDateTimeInstance().format(new Date()))
                        .setContentIntent(pendingIntent)
                        .setPriority(Notification.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        ;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null)
            notificationManager.notify(notificationId, builder.build());
    }
}
