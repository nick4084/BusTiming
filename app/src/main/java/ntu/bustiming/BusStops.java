package ntu.bustiming;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class handles reading bus stops stored in the app
 */
public class BusStops {
    JSONArray AllBusStop;
    Context mContext;
    Double range =0.01;
    Double Long= 0.0;
    Double Lat= 0.0;
    static String Param_Lat="Latitude";
    static String Param_Lon="Longitude";
    static String Param_busstop_code="BusStopCode";
    static String Param_roadname="RoadName";
    static String Param_description="Description";


    /**
     * Constructor for the class
     * @param context context of the application
     */
    public BusStops(Context context) {
        mContext = context;
    }

    /**
     * This method get busstops in the nearby location
     * @param location object containing lat lng
     * @return a JSONArray of bus stops object
     */
    public JSONArray getBusStopByLocation(Location location){
        //JSONArray bs_array = readJSONFile();
        AllBusStop = readBusStopfile();
        JSONArray location_bs=null;
        if(AllBusStop!=null){
            location_bs= new JSONArray();
            try {
                Lat= location.getLatitude();
                Long=location.getLongitude();
                //loop every entry to find nearby bus stop
                for (int i = 0; i < AllBusStop.length(); i++) {
                    JSONObject current_jobject = AllBusStop.getJSONObject(i);
                    if (current_jobject != null){
                        Double cur_long = current_jobject.getDouble(Param_Lon);
                        Double cur_lat = current_jobject.getDouble(Param_Lat);
                        //check if within range. if yes, add to array
                        if( (cur_long<(Long+range)&&(cur_long>(Long-range))) && (cur_lat<(Lat+range)&& (cur_lat>(Lat-range))) ){
                            location_bs.put(current_jobject);
                        }
                    }
                }
            } catch(JSONException ex){
                ex.printStackTrace();
                return null;
            }
        }


        return location_bs;
    }

    /**
     * This method fetch the bus stop name by bus stop code
     * @param DestinationCode the bus stop code
     * @return name of the specified bus stop code
     */
    public String getDestinationNameByCode(String DestinationCode){
        if(AllBusStop==null){
            AllBusStop = readBusStopfile();
        }
        JSONObject busstop;
        for (int i = 0; i<AllBusStop.length(); i++) {
            try{
                busstop = AllBusStop.getJSONObject(i);
                String code =  busstop.getString(Param_busstop_code);
                if(code.equals(DestinationCode)){
                    return busstop.getString(Param_description);
                }
            } catch(JSONException e){
                e.printStackTrace();
            }

        }
        return null;
    }

    /**
     * This method read the text of busstops in json array format
     * @return a JSONArray of bus stops
     */
    public JSONArray readJSONFile(){
        JSONObject jobject = null;
        JSONArray jArray =null;
        String json_raw = null;
        try{
            InputStream is = mContext.getAssets().open("json_busstop_caa230617");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json_raw = new String(buffer, "UTF-8");

        } catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
        try {
            json_raw = json_raw.replace("//\r//\n", "");
            JSONObject jsonraw = new JSONObject(json_raw);
            jArray = jsonraw.getJSONArray("value");
        } catch (JSONException ex){
            ex.printStackTrace();
            return null;
        }

        return jArray;
    }

    /**
     *  This method return the text of busstops in json array format that is previously saved
     * @return JSONArray of bus stops
     */
    public JSONArray readBusStopfile(){
        try {
            File path = new File(mContext.getFilesDir(), "BusTiming/BusStops.txt");
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
            Log.i("readBusStopfile", "I/O error");
            return null;
        } catch (JSONException e){
            Log.i("readBusStopfile", "conversion error");
            return null;
        }


    }
}
