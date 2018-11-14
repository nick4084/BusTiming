package ntu.bustiming.control;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONObject;

import java.time.LocalTime;

import ntu.bustiming.R;

/**
 * This class handles the sending and creation of the notification
 */
public class NotificationService {
    private static final String CHANNEL_ID = "Primary Channel";
    private NotificationManager notifyManager;
    private Context context;
    private static final int NOTIFICATION_ID = 0;
    private static final String ACTION_DISMISS_NOTIFICATION = "ntu.bustiming.ACTION_DISMISS_NOTIFICATION";

    private String busService;
    private String busStop;
    //private Intent alarmIntent;

    /**
     * This is the constructor for NotificationService
     * @param context The context of the app
     */
    public NotificationService(Context context){
        this.context = context;
        createNotificationChannel();
        NotificationReceiver receiver = new NotificationReceiver();
        context.getApplicationContext().registerReceiver(receiver,new IntentFilter(ACTION_DISMISS_NOTIFICATION));
    }

    /**
     * This method will create a new notification channel
     */
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


    /**
     * This method will get the notification builder to build the notification for sending
     * @param title The title of the notification
     * @param content The content of the notification
     * @return The notification builder
     */
    private NotificationCompat.Builder getNotificationBuilder(String title, String content){
        Intent notificationIntent = new Intent(context,MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        notifyBuilder.setContentTitle(title); //Bus stop name
        notifyBuilder.setContentText(content); //Bus number arriving in x,y,z minutes
        notifyBuilder.setSmallIcon(R.drawable.ic_android);
        notifyBuilder.setContentIntent(notificationPendingIntent);
        notifyBuilder.setAutoCancel(true);
        return notifyBuilder;
    }

    /**
     * This method will send a request to the API server with the passed in arguments, and push a notification to the user
     * @param busService The specified busService as a parameter in the API request
     * @param busStop The specified busStop as a parameter in the API request
     */
    public void sendNotification(String busService, String busStop){
        //Bundle bundle = intent.getExtras();
        this.busService = busService;
        this.busStop = busStop;

        //alarmIntent = intent;

        LTADatamallController lta = new LTADatamallController(context);
        //Bundle bundle = alarmIntent.getExtras();

        String curTime = "";
        String title="";
        String content="";

        try{
            JSONObject jsonObject = lta.SingleBusArrivalTiming(busService,busStop.substring(0,5));
            JSONObject[] arrivalTimings = new JSONObject[3];
            arrivalTimings[0] = jsonObject.getJSONArray("Services").getJSONObject(0).getJSONObject("NextBus");
            arrivalTimings[1] = jsonObject.getJSONArray("Services").getJSONObject(0).getJSONObject("NextBus2");
            arrivalTimings[2] = jsonObject.getJSONArray("Services").getJSONObject(0).getJSONObject("NextBus3");

            title=busStop.substring(8);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d("test", LocalTime.now().toString());
                curTime = LocalTime.now().toString().substring(0,5);
            }
            //Log.d("test",calTimeDiff(curTime,arrivalTimings[0].getString("EstimatedArrival").substring(11,16))+"minute");
            //Log.d("test",calTimeDiff(curTime,arrivalTimings[1].getString("EstimatedArrival").substring(11,16))+"minute");
            //Log.d("test",calTimeDiff(curTime,arrivalTimings[2].getString("EstimatedArrival").substring(11,16))+"minute");

            int[] timings = new int[3];
            timings[0]=calTimeDiff(curTime,arrivalTimings[0].getString("EstimatedArrival").substring(11,16));
            timings[1]=calTimeDiff(curTime,arrivalTimings[1].getString("EstimatedArrival").substring(11,16));
            timings[2]=calTimeDiff(curTime,arrivalTimings[2].getString("EstimatedArrival").substring(11,16));


            content = busService+" arriving in "+timings[0]+", "+timings[1]+", "+timings[2]+" minutes";

            //String content =
        }catch (Exception e){
            e.printStackTrace();
        }


        NotificationCompat.Builder notifyBuilder = getNotificationBuilder(title,content);
        Intent updateIntent = new Intent(ACTION_DISMISS_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast
                (context, NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);
        notifyBuilder.addAction(R.drawable.ic_android, "Refresh", updatePendingIntent);
        notifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

    }


    /**
     * This method will update and change the content of the notification
     */
    public void updateNotification(){

        LTADatamallController lta = new LTADatamallController(context);
        //Bundle bundle = alarmIntent.getExtras();

        String curTime = "";
        String title="";
        String content="";

        try{
            JSONObject jsonObject = lta.SingleBusArrivalTiming(busService,busStop.substring(0,5));
            JSONObject[] arrivalTimings = new JSONObject[3];
            arrivalTimings[0] = jsonObject.getJSONArray("Services").getJSONObject(0).getJSONObject("NextBus");
            arrivalTimings[1] = jsonObject.getJSONArray("Services").getJSONObject(0).getJSONObject("NextBus2");
            arrivalTimings[2] = jsonObject.getJSONArray("Services").getJSONObject(0).getJSONObject("NextBus3");

            title=busStop.substring(8);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d("test", LocalTime.now().toString());
                curTime = LocalTime.now().toString().substring(0,5);
            }
            //Log.d("test",calTimeDiff(curTime,arrivalTimings[0].getString("EstimatedArrival").substring(11,16))+"minute");
            //Log.d("test",calTimeDiff(curTime,arrivalTimings[1].getString("EstimatedArrival").substring(11,16))+"minute");
            //Log.d("test",calTimeDiff(curTime,arrivalTimings[2].getString("EstimatedArrival").substring(11,16))+"minute");

            int[] timings = new int[3];
            timings[0]=calTimeDiff(curTime,arrivalTimings[0].getString("EstimatedArrival").substring(11,16));
            timings[1]=calTimeDiff(curTime,arrivalTimings[1].getString("EstimatedArrival").substring(11,16));
            timings[2]=calTimeDiff(curTime,arrivalTimings[2].getString("EstimatedArrival").substring(11,16));

            //Log.d("test",calTimeDiff(curTime,arrivalTimings[0].getString("EstimatedArrival").substring(11,16))+"minute");
            //Log.d("test",calTimeDiff(curTime,arrivalTimings[1].getString("EstimatedArrival").substring(11,16))+"minute");
            //Log.d("test",calTimeDiff(curTime,arrivalTimings[2].getString("EstimatedArrival").substring(11,16))+"minute");
            content = busService+" arriving in "+timings[0]+", "+timings[1]+", "+timings[2]+" minutes";

            //String content =
        }catch (Exception e){
            e.printStackTrace();
        }

        NotificationCompat.Builder notifyBuilder = getNotificationBuilder(title,content);
        Intent updateIntent = new Intent(ACTION_DISMISS_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast
                (context, NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);
        notifyBuilder.addAction(R.drawable.ic_android, "Refresh", updatePendingIntent);
        notifyManager.notify(NOTIFICATION_ID,notifyBuilder.build());
    }


    /**
     * This method will cancel the notification
     */
    public void cancelNotification(){
        notifyManager.cancel(NOTIFICATION_ID);
    }

    private int calTimeDiff(String time1, String time2){
        int hourDiff = Integer.parseInt(time2.substring(0,2))-Integer.parseInt(time1.substring(0,2));
        int minDiff = Integer.parseInt(time2.substring(3))-Integer.parseInt(time1.substring(3));
        minDiff+=hourDiff*60;
        return (minDiff<0)?0:minDiff;
    }

    /**
     * This inner class will receive the notification broadcast intent
     */
    public class NotificationReceiver extends BroadcastReceiver {
        public NotificationReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            updateNotification();
        }
    }
}
