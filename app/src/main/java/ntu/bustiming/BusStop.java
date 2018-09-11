package ntu.bustiming;

import android.content.Context;
import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class BusStop {
    JSONArray AllBusStop;
    Context mContext;
    Double range =0.01;
    Double Long= 0.0;
    Double Lat= 0.0;
    String Param_Lat="Latitude";
    String Param_Lon="Longitude";
    String Param_busstop_code="BusStopCode";
    String Param_roadname="RoadName";
    String Param_description="Description";



    public BusStop(Context context) {
        mContext = context;
    }

    public JSONArray getBusStopByLocation(Location location){
        JSONArray bs_array = readJSONFile();
        JSONArray location_bs=null;
        if(bs_array!=null){
            location_bs= new JSONArray();
            try {
                Lat= location.getLatitude();
                Long=location.getLongitude();
                //loop every entry to find nearby bus stop
                for (int i = 0; i < bs_array.length(); i++) {
                    JSONObject current_jobject = bs_array.getJSONObject(i);
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
    public String getDestinationNameByCode(String DestinationCode){
        if(AllBusStop==null){
            AllBusStop = readJSONFile();
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
}
