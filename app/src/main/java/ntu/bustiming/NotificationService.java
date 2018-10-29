package ntu.bustiming;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

public class NotificationService {
    private static final String CHANNEL_ID = "Primary Channel";
    private NotificationManager notifyManager;
    private Context context;
    private static final int NOTIFICATION_ID = 0;
    private static final String ACTION_DISMISS_NOTIFICATION = "ntu.bustiming.ACTION_DISMISS_NOTIFICATION";
    public NotificationService(Context context){
        this.context = context;
        createNotificationChannel();
        NotificationReceiver receiver = new NotificationReceiver();
        context.registerReceiver(receiver,new IntentFilter(ACTION_DISMISS_NOTIFICATION));
    }



    private void createNotificationChannel(){
        notifyManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    "Notification", NotificationManager
                    .IMPORTANCE_HIGH);
            notificationChannel.setLightColor(Color.WHITE);
            notificationChannel.enableVibration(true);
            notifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder(){
        Intent notificationIntent = new Intent(context,MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        notifyBuilder.setContentTitle("Notification"); //Bus stop name
        notifyBuilder.setContentText("Notification text"); //Bus number arriving in x,y,z minutes
        notifyBuilder.setSmallIcon(R.drawable.ic_android);
        notifyBuilder.setContentIntent(notificationPendingIntent);
        notifyBuilder.setAutoCancel(true);
        return notifyBuilder;
    }

    public void sendNotification(){
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        Intent cancelIntent = new Intent(ACTION_DISMISS_NOTIFICATION);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast
                (context, NOTIFICATION_ID, cancelIntent, PendingIntent.FLAG_ONE_SHOT);
        notifyBuilder.addAction(R.drawable.ic_android, "DISMISS", cancelPendingIntent);

        notifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

    }

    public void updateNotification(){
        String s = "something";
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyBuilder.setContentText(s);
        notifyManager.notify(NOTIFICATION_ID,notifyBuilder.build());
    }

    public void cancelNotification(){
        notifyManager.cancel(NOTIFICATION_ID);
    }

    public class NotificationReceiver extends BroadcastReceiver {
        public NotificationReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            cancelNotification();
        }
    }
}
