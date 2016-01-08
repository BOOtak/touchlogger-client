package org.leyfer.thesis.TouchLogger.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import org.leyfer.thesis.TouchLogger.MainActivity;
import org.leyfer.thesis.TouchLogger.R;
import org.leyfer.thesis.TouchLogger.notification.helper.NotificationActionEnum;

public class WatchNotification {

    public static final int NOTIFICATION_ID = 33789;

    public WatchNotification(Context context, NotificationActionEnum action) {
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0));
        if (action == NotificationActionEnum.ACTION_INPROGRESS) {
            builder.setContentText(context.getString(R.string.notification_status_inprogress))
                    .addAction(android.R.drawable.ic_media_pause,
                            context.getString(R.string.notification_action_pause),
                            PendingIntent.getService(context, 0, new Intent(context, MainActivity.class), 0));
        } else {
            builder.setContentText(context.getString(R.string.notification_status_paused))
                    .addAction(android.R.drawable.ic_media_play,
                            context.getString(R.string.notification_action_play),
                            PendingIntent.getService(context, 0, new Intent(context, MainActivity.class), 0));
        }

        mNotificatoin = builder.build();
    }

    private Notification mNotificatoin;

    public Notification getNotificatoin() {
        return mNotificatoin;
    }
}
