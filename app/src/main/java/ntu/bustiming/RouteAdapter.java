package ntu.bustiming;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class RouteAdapter extends BaseAdapter{
    //how to write adapter
    //https://abhiandroid.com/ui/baseadapter-tutorial-example.html
    //this is better https://www.journaldev.com/10416/android-listview-with-custom-adapter-example-tutorial
    private ArrayList<Notification> routeList;
    private Context context;
    private static RouteAdapter instance = null;
    private static RoutePersistentData routePersistentData;


    private static class ViewHolder {
        int position;
        TextView ntf_name;
        ImageView ntf_menu;
        ToggleButton ntf_onoff;
    }

    public static void init(ArrayList<Notification> routeList, Context context){
        if(instance==null){
            instance = new RouteAdapter(routeList,context);
        }
    }

    public static RouteAdapter getInstance(){
        return instance;
    }

    private RouteAdapter(ArrayList<Notification> routeList, Context context) {
        this.routeList = routeList;
        this.context = context;
        routePersistentData = RoutePersistentData.getInstance();

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
        viewHolder.ntf_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()){
                            case R.id.routeMenuEdit:
                                RouteEditDialog editDialog = new RouteEditDialog(context, routeList.get(position), position);
                                editDialog.show();
                                editDialog.setDialogResult(new RouteEditDialog.OnMyDialogResult(){
                                    public void finish(Notification ntf){
                                        //TODO: Use singleton here
                                        RouteAdapter routeAdapter = RouteAdapter.getInstance();
                                        routeAdapter.replaceItem(ntf,position);
                                        routeAdapter.notifyDataSetChanged();
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

        viewHolder.ntf_onoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routeList.get(position).setActivated(viewHolder.ntf_onoff.isActivated());
                notifyDataSetChanged();
            }
        });

        return view;
    }

    public void replaceItem(Notification ntf,int position){
        routeList.set(position,ntf);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        routePersistentData.refreshList(routeList);
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
