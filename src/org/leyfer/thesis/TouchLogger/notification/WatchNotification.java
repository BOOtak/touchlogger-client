package org.leyfer.thesis.TouchLogger.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import org.leyfer.thesis.TouchLogger.MainActivity;
import org.leyfer.thesis.TouchLogger.R;
import org.leyfer.thesis.TouchLogger.notification.helper.NotificationAction;
import org.leyfer.thesis.TouchLogger.service.TouchReaderService;

public class WatchNotification {

    public static final int NOTIFICATION_ID = 33789;

    private Notification mActiveNotification;
    private Notification mInactiveNotification;

    public WatchNotification(Context context) {
        Intent resumeIntent = new Intent(context, TouchReaderService.class);
        resumeIntent.setAction(TouchReaderService.ACTION_RESUME);

        Intent pauseIntent = new Intent(context, TouchReaderService.class);
        pauseIntent.setAction(TouchReaderService.ACTION_PAUSE);

        mActiveNotification = new Notification.Builder(context)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0))
                .setContentText(context.getString(R.string.notification_status_inprogress))
                .addAction(android.R.drawable.ic_media_pause,
                        context.getString(R.string.notification_action_pause),
                        PendingIntent.getService(context, 0, pauseIntent, 0))
                .build();

        mInactiveNotification = new Notification.Builder(context)
                .setSmallIcon(android.R.drawable.ic_media_pause)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0))
                .setContentText(context.getString(R.string.notification_status_paused))
                .addAction(android.R.drawable.ic_media_play,
                        context.getString(R.string.notification_action_play),
                        PendingIntent.getService(context, 0, resumeIntent, 0))
                .build();
    }

    public Notification getNotification(NotificationAction action) {
        if (action == NotificationAction.ACTION_INPROGRESS) {
            return mActiveNotification;
        } else {
            return mInactiveNotification;
        }
    }
}
