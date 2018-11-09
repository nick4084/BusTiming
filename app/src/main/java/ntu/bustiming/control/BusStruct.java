package ntu.bustiming.control;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BusStruct {
    private JSONArray Bus;
    private int Bs_Last_Skip_Count;
    private int Bs_Last_Count;
    private String BS_LAST_COUNT = "Bs_Last_count";
    private String BS_LIST= "Bs_list";
    private String BS_LAST_SKIP_COUNT = "BS_LAST_SKIP_COUNT";

    public BusStruct(JSONObject file) {
        try{
            Bus = file.getJSONArray(BS_LIST);
            Bs_Last_Count = file.getInt(BS_LAST_COUNT);
            Bs_Last_Skip_Count = file.getInt(BS_LAST_SKIP_COUNT);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
    public int getBs_Last_Count() {
        return Bs_Last_Count;
    }

    public int getBs_Last_Skip_Count() {
        return Bs_Last_Skip_Count;
    }



}
