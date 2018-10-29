package ntu.bustiming;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This Class handles reading bus in the app
 */
public class Bus {
    JSONArray AllBusStop;
    Context mContext;
    static String Param_serviceno="ServiceNo";
    static String Param_direction="Direction";
    static String Param_stopsequence="StopSequence";
    static String Param_busstopcode="BusStopCode";
    static String Param_distance="Distance";

    /** Constructor for the class
     * @param context context of the application
     */
    public Bus(Context context){
        mContext=context;

    }

    /** this method get the bus route
     * @param serviceNo service number of bus
     * @return JSON array of Bus route information
     */
    public JSONArray getBusByServiceNo(String serviceNo){
        JSONArray jArray =null;
        return jArray;
    }

    /**
     * This method return the text of Bus stored in json format that is previously  saved
     * @return  JSON array of Bus
     */
    public JSONArray readBusfile(){
        try {
            File path = new File(mContext.getFilesDir(), "BusTiming/Bus.txt");
            StringBuilder total = new StringBuilder();
            FileInputStream fis = new FileInputStream(path);
            int numRead =0;
            byte[] bytes = new byte[fis.available()];
            while ((numRead = fis.read(bytes)) >= 0) {
                total.append(new String(bytes, 0, numRead));
            }
            JSONObject jsonraw = new JSONObject(total.toString());
            return jsonraw.getJSONArray("Bs_list");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("readBusfile", "I/O error");
            return null;
        } catch (JSONException e){
            Log.i("readBusfile", "conversion error");
            return null;
        }
    }
}
