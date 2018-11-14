package ntu.bustiming.control;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

/**
 * This class receives the broadcast intent to acc
 */
public class NotificationAlarmReceiver extends BroadcastReceiver {

    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String CHANNEL_ID =
            "primary_notification_channel";


    @Override
    public void onReceive(Context context, Intent intent) {
        //notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bundle bundle = intent.getExtras();
        //LTADatamallController lta = new LTADatamallController(context);
//
        //String curTime = "";
        //String title="";
        //String content="";
//
        //try{
        //    JSONObject jsonObject = lta.SingleBusArrivalTiming(bundle.getString("busService"),bundle.getString("busStop").substring(0,5));
        //    JSONObject[] arrivalTimings = new JSONObject[3];
        //    arrivalTimings[0] = jsonObject.getJSONArray("Services").getJSONObject(0).getJSONObject("NextBus");
        //    arrivalTimings[1] = jsonObject.getJSONArray("Services").getJSONObject(0).getJSONObject("NextBus2");
        //    arrivalTimings[2] = jsonObject.getJSONArray("Services").getJSONObject(0).getJSONObject("NextBus3");
//
        //    title=bundle.getString("busStop").substring(8);
//
        //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //        Log.d("test", LocalTime.now().toString());
        //        curTime = LocalTime.now().toString().substring(0,5);
        //    }
        //    Log.d("test",calTimeDiff(curTime,arrivalTimings[0].getString("EstimatedArrival").substring(11,16))+"minute");
        //    Log.d("test",calTimeDiff(curTime,arrivalTimings[1].getString("EstimatedArrival").substring(11,16))+"minute");
        //    Log.d("test",calTimeDiff(curTime,arrivalTimings[2].getString("EstimatedArrival").substring(11,16))+"minute");
//
        //    int[] timings = new int[3];
        //    timings[0]=calTimeDiff(curTime,arrivalTimings[0].getString("EstimatedArrival").substring(11,16));
        //    timings[1]=calTimeDiff(curTime,arrivalTimings[1].getString("EstimatedArrival").substring(11,16));
        //    timings[2]=calTimeDiff(curTime,arrivalTimings[2].getString("EstimatedArrival").substring(11,16));
//
//
        //    content = bundle.getString("busService")+" arriving in "+timings[0]+", "+timings[1]+", "+timings[2]+" minutes";
//
        //    //String content =
        //}catch (Exception e){
        //    e.printStackTrace();
        //}


        //TODO: SEND Notification
        //TODO: Get from API
        Log.d("test","Alarm awake");
        NotificationService ns = new NotificationService(context);
        ns.sendNotification(bundle.getString("busService"),bundle.getString("busStop"));
    }

    /**
     * This method calculate the time difference between two time strings in the format of hh:mm
     * @param time1 start time
     * @param time2 end time
     * @return time difference in minutes
     */
    private int calTimeDiff(String time1, String time2){
        int hourDiff = Integer.parseInt(time2.substring(0,2))-Integer.parseInt(time1.substring(0,2));
        int minDiff = Integer.parseInt(time2.substring(3))-Integer.parseInt(time1.substring(3));
        minDiff+=hourDiff*60;
        return (minDiff<0)?0:minDiff;
    }
}
