package ntu.bustiming;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationAlarmReceiver extends BroadcastReceiver {

    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String CHANNEL_ID =
            "primary_notification_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        //TODO: SEND Notification
        //TODO: Get from API
        NotificationService ns = new NotificationService(context);
        ns.sendNotification();
    }
}
