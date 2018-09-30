package ntu.bustiming;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BusStopStruct {
    private JSONArray BusStops;
    private int Bs_Last_Skip_Count;
    private int Bs_Last_Count;
    private String BS_LAST_COUNT = "Bs_Last_count";
    private String BS_LIST= "Bs_list";
    private String BS_LAST_SKIP_COUNT = "BS_LAST_SKIP_COUNT";



    public BusStopStruct(JSONObject file) {
        try{
            BusStops = file.getJSONArray(BS_LIST);
            Bs_Last_Count = file.getInt(BS_LAST_COUNT);
            Bs_Last_Skip_Count = file.getInt(BS_LAST_SKIP_COUNT);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }



    public JSONArray getBusStops() {
        return BusStops;
    }

    public void setBusStops(JSONArray busStops) {
        BusStops = busStops;
    }

    public int getBs_Last_Count() {
        return Bs_Last_Count;
    }

    public void setBs_Last_Count(int Bs_Last_Count) {
        Bs_Last_Count = Bs_Last_Count;
    }
    public int getBs_Last_Skip_Count() {
        return Bs_Last_Skip_Count;
    }

    public void setBs_Last_Skip_count(int bs_Last_Skip_Count) {
        Bs_Last_Skip_Count = bs_Last_Skip_Count;
    }
}
