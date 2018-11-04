package ntu.bustiming.control;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
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
    private static NotificationPersistentData notificationPersistentData;
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
        notificationPersistentData = NotificationPersistentData.getInstance();
        ntfCount = routeList.size();
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
                Log.d("test","oncheck"+viewHolder.ntf_onoff.isChecked());
                notifyDataSetChanged();
            }
        });

 /*       viewHolder.ntf_onoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routeList.get(position).setActivated(viewHolder.ntf_onoff.isChecked());
                Log.d("test",""+viewHolder.ntf_onoff.isChecked());
                notifyDataSetChanged();
            }
        });
*/
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

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        notificationPersistentData.refreshList(routeList);
        //TODO: Loop through the items, set alarm manager
        NotificationManager notificationManager;
        final int NOTIFICATION_ID = 0;
        final String PRIMARY_CHANNEL_ID =
                "primary_notification_channel";
        Intent notifyIntent = new Intent(context, NotificationAlarmReceiver.class);
        PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (context, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(alarmManager!=null){
            //cancel all task
        }
        long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        long triggerTime = SystemClock.elapsedRealtime()
                + repeatInterval;

//If the Toggle is turned on, set the repeating alarm with a 15 minute interval
        if (alarmManager != null) {
            alarmManager.setInexactRepeating
                    (AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            triggerTime, repeatInterval, notifyPendingIntent);
        }


        for(int i=0;i<routeList.size();i++){

        }
    }
}
