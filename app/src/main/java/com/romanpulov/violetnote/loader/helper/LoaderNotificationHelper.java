package com.romanpulov.violetnote.loader.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.util.SparseIntArray;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.SettingsActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Notification helper for loader operations
 * Created by romanpulov on 07.03.2018.
 */

public final class LoaderNotificationHelper {
    public static final int NOTIFICATION_TYPE_SUCCESS = 1;
    public static final int NOTIFICATION_TYPE_FAILURE = 2;

    private static final SparseIntArray NOTIFICATION_ICONS = new SparseIntArray();

    static {
        NOTIFICATION_ICONS.put(NOTIFICATION_TYPE_SUCCESS, R.drawable.ic_action_info);
        NOTIFICATION_ICONS.put(NOTIFICATION_TYPE_FAILURE, R.drawable.ic_action_warning);
    }

    public static void notify(Context context, String message, int notificationId, int notificationType) {
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Notification.Builder builder =
                new Notification.Builder(context)
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
