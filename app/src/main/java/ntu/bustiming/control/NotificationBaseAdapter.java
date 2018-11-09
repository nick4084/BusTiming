package ntu.bustiming.control;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;

import ntu.bustiming.entity.Notification;
import ntu.bustiming.R;

/**
 * This class is the base adapter that populate the list view of notification
 * It is implemented as a singleton to ensure only one instance is created
 */
public class NotificationBaseAdapter extends BaseAdapter{
    //how to write adapter
    //https://abhiandroid.com/ui/baseadapter-tutorial-example.html
    //this is better https://www.journaldev.com/10416/android-listview-with-custom-adapter-example-tutorial
    private ArrayList<Notification> routeList;
    private Context context;
    private static NotificationBaseAdapter instance = null;
    private static NotificationDataController notificationDataController;
    private static int ntfCount = 0;

    /**
     * This is the inner class of notificationBaseAdapter
     * It contains the data needed for every entry in the list view
     */
    private static class ViewHolder {
        int position;
        TextView ntf_name;
        ImageView ntf_menu;
        ToggleButton ntf_onoff;
    }

    /**
     * This method is the initialisation method that calls the private constructor
     * @param routeList The arraylist that contains the notification entries
     * @param context The context of the app
     */
    public static void init(ArrayList<Notification> routeList, Context context){
        if(instance==null){
            instance = new NotificationBaseAdapter(routeList,context);
        }
    }

    /**
     * This method will return the instance of this class
     * @return The instance of this class
     */
    public static NotificationBaseAdapter getInstance(){
        return instance;
    }

    private NotificationBaseAdapter(ArrayList<Notification> routeList, Context context) {
        this.routeList = routeList;
        this.context = context;
        notificationDataController = NotificationDataController.getInstance();
        //ntfCount = routeList.size();

        //init ntfCount
        for(int i=0;i<routeList.size();i++){
            Notification ntf = routeList.get(i);
            int tmp = ntfCount;
            if(!ntf.isActivated())continue;
            for(int j=0;j<7;j++){
                if(routeList.get(i).getNtf_days().get(j)){
                    ntfCount++;
                }
            }
            if(tmp==ntfCount) ntfCount++;
        }
    }


    @Override
    public int getCount() {
        return routeList.size();
    }

    @Override
    public Object getItem(int i) {
        return routeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Notification ntf = routeList.get(i);
        final ViewHolder viewHolder;
        final View result;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.route_viewholder, null);
            viewHolder.position = i;
            viewHolder.ntf_name = view.findViewById(R.id.ntf_name);
            viewHolder.ntf_menu = view.findViewById(R.id.ntf_menu);
            viewHolder.ntf_onoff = view.findViewById(R.id.ntf_onoff);
            result = view;
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            result = view;
        }


        final int position = i;
        viewHolder.ntf_name.setText(ntf.getName());
        viewHolder.ntf_onoff.setChecked(ntf.isActivated());
        //viewHolder.ntf_onoff.setSelected(ntf.isActivated());
        viewHolder.ntf_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()){
                            case R.id.routeMenuEdit:
                                NotificationEditDialog editDialog = new NotificationEditDialog(context, routeList.get(position), position);
                                editDialog.show();
                                editDialog.setDialogResult(new NotificationEditDialog.OnMyDialogResult(){
                                    public void finish(Notification ntf){
                                        NotificationBaseAdapter notificationBaseAdapter = NotificationBaseAdapter.getInstance();
                                        notificationBaseAdapter.replaceItem(ntf,position);
                                        notificationBaseAdapter.notifyDataSetChanged();
                                    }
                                });
                                return true;
                            case R.id.routeMenuDelete:
                                routeList.remove(position);
                                notifyDataSetChanged();
                                return true;
                            case R.id.routeMenuNotify:
                                Notification tmp = routeList.get(position);
                                NotificationService ns = new NotificationService(context);
                                ns.sendNotification(tmp.getBus_code(),tmp.getBusstop_code());
                                return true;
                            default:
                                return true;

                        }
                    }
                });
                popupMenu.inflate(R.menu.route_popup);
                popupMenu.show();

            }
        });

        viewHolder.ntf_onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                routeList.get(position).setActivated(viewHolder.ntf_onoff.isChecked());
                notifyDataSetChanged();
            }
        });
        return view;
    }

    /**
     * This method will replace an item in the notification list
     * @param ntf The new notification entry
     * @param position
     */
    public void replaceItem(Notification ntf,int position){
        routeList.set(position,ntf);
    }

    public void replaceList(ArrayList<Notification> routeList){
        this.routeList = routeList;
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        notificationDataController.refreshList(routeList);

        //cancel
        for(int i=0;i<ntfCount;i++){
            Intent alarmIntent = new Intent(context, NotificationAlarmReceiver.class);
            PendingIntent pendingAlarm = PendingIntent.getBroadcast(context,i,alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingAlarm);
        }


        for(int i=0;i<routeList.size();i++){
            int tmp = ntfCount;
            Notification ntf = routeList.get(i);

            if(!ntf.isActivated()) continue;

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            //cal.set(Calendar.HOUR_OF_DAY,(ntf.getNtf_minute()<2)?ntf.getNtf_hour()-1:ntf.getNtf_hour());
            //cal.set(Calendar.MINUTE,(ntf.getNtf_minute()<2)?60+ntf.getNtf_minute()-2:ntf.getNtf_minute()-2);
            cal.set(Calendar.HOUR_OF_DAY,ntf.getNtf_hour());
            cal.set(Calendar.MINUTE,ntf.getNtf_minute());
            //cal.add(Calendar.SECOND,5);
            for(int j=0;j<7;j++){
                if(routeList.get(i).getNtf_days().get(j)){
                    cal.set(Calendar.DAY_OF_WEEK,(j+1)%7);
                    Intent alarmIntent = new Intent(context, NotificationAlarmReceiver.class);
                    alarmIntent.putExtra("busStop",ntf.getBusstop_code().substring(0,5));
                    alarmIntent.putExtra("busService",ntf.getBus_code());

                    PendingIntent pendingAlarm = PendingIntent.getBroadcast(context,ntfCount++,alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);

                    AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingAlarm);
                }
            }

            if(tmp!=ntfCount) continue;
            //create the intent and put extra
            Intent alarmIntent = new Intent(context, NotificationAlarmReceiver.class);
            alarmIntent.putExtra("busStop",ntf.getBusstop_code().substring(0,5));
            alarmIntent.putExtra("busService",ntf.getBus_code());

            PendingIntent pendingAlarm = PendingIntent.getBroadcast(context,ntfCount++,alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingAlarm);
            Log.d("test","alarm set");
        }
    }
}
