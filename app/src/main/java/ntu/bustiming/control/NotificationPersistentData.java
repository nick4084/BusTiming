package ntu.bustiming.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.BitSet;

import ntu.bustiming.entity.Notification;

/**
 * This class saves the notification entries in sharedPreference
 * It is implemented as a singleton to ensure only one instance is created
 */
public class NotificationPersistentData {
    private JSONArray entryList;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    Context context;
    private final String key = "data";
    private static NotificationPersistentData instance;
    /**
     * This method is the initialisation method that calls the private constructor
     * @param context The context of the app
     */
    public static void init(Context context){
        if(instance==null){
            instance = new NotificationPersistentData(context);
        }
    }
    /**
     * This method will return the instance of this class
     * @return The instance of this class
     */
    public static NotificationPersistentData getInstance(){
        return instance;
    }

    private NotificationPersistentData(Context context) {
        this.context = context;
        entryList = new JSONArray();
        try {
            sharedPref = context.getSharedPreferences("myPref", 0);
            editor = sharedPref.edit();
            if (sharedPref.getString(key, null) != null) {
                entryList = new JSONArray(sharedPref.getString(key, null));
//                Log.d("test","Length of entry list:"+entryList.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        Log.d("test", "Add new entry");
        addNewEntry(new Notification());
        sharedPrefInit();
        editor.putString("data",entryList.toString());
        editor.commit();
        Log.d("test", "Update entry");
        updateEntry();
        Log.d("test", "Delete entry");
        deleteEntry();
        Log.d("test", "Check shared preference");
        testSharedPref();
        */
    }

    /*
        public void testSharedPref(){
            try{
                sharedPref = context.getSharedPreferences("myPrefTest", 0);
                JSONArray newList;
                if(sharedPref.getString("data",null)!=null){
                    newList = new JSONArray(sharedPref.getString("data",null));
                    entryList = newList;
                    listEntry();
                }else{
                    newList = new JSONArray();
                    entryList = newList;
                    listEntry();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    */

    /**
     * This method is called when the notification list is updated
     * The sharedPreference will be updated
     * @param routeList
     */
    public void refreshList(ArrayList<Notification> routeList) {
        try {
            entryList = new JSONArray();
            for (int i = 0; i < routeList.size(); i++) {
                JSONObject obj = new JSONObject();
                obj.put("id", routeList.get(i).getId());
                obj.put("name", routeList.get(i).getName());
                obj.put("busstop_code", routeList.get(i).getBusstop_code());
                obj.put("bus_code", routeList.get(i).getBus_code());
                obj.put("ntf_hour", routeList.get(i).getNtf_hour());
                obj.put("ntf_minute", routeList.get(i).getNtf_minute());
                obj.put("ntf_days", routeList.get(i).getNtf_days());
                obj.put("ntf_onoff",routeList.get(i).isActivated());
                entryList.put(obj);
            }
            editor.putString(key,entryList.toString());
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will return a list of notification entries
     * @return The arraylist that contains the notification entries
     */
    public ArrayList<Notification> getEntryList() {
        ArrayList<Notification> ntfList = new ArrayList<>();
        try {
            for (int i = 0; i < entryList.length(); i++) {
                Notification ntf = new Notification();
                ntf.setId(entryList.getJSONObject(i).getInt("id"));
                ntf.setName(entryList.getJSONObject(i).getString("name"));
                ntf.setBusstop_code(entryList.getJSONObject(i).getInt("busstop_code"));
                ntf.setBus_code(entryList.getJSONObject(i).getInt("bus_code"));
                ntf.setNtf_hour(entryList.getJSONObject(i).getInt("ntf_hour"));
                ntf.setNtf_minute(entryList.getJSONObject(i).getInt("ntf_minute"));
                String ntfDays = (String) entryList.getJSONObject(i).get("ntf_days");
                BitSet ntfDaysBS = new BitSet(7);
                if(ntfDays.length()>=3){
                    for(int j=1;j<ntfDays.length();j+=3){
                        //Log.d("test",ntfDays);
                        //Log.d("test",""+(ntfDays.charAt(j)-'0'));
                        ntfDaysBS.set(ntfDays.charAt(j)-'0');
                    }
                }
                ntf.setNtf_days(ntfDaysBS);
                ntf.setActivated(entryList.getJSONObject(i).getBoolean("ntf_onoff"));
                ntfList.add(ntf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Log.d("test","ntfList.length(): "+ntfList.size());
            return ntfList;
        }

    }

/*
    public void addNewEntry(Notification ntf) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("test1", "Tom Hardy");
            obj.put("test2", "Ryan Gosling");
            entryList.put(obj);
            JSONObject obj2 = new JSONObject();
            obj2.put("test1", "Venom");
            obj2.put("test2", "First Man");
            entryList.put(obj2);
            listEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listEntry() {
        try {
            for (int i = 0; i < entryList.length(); i++) {
                Log.d("test", "entry" + i);
                Log.d("test", (String) entryList.getJSONObject(i).get("test1"));
                Log.d("test", (String) entryList.getJSONObject(i).get("test2"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateEntry() {
        try {
            JSONObject obj = entryList.getJSONObject(1);
            obj.put("test1", "Carnage");
            listEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteEntry() {
        try {
            entryList.remove(0);
            listEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
