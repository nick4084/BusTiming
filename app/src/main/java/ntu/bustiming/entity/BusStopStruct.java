package ntu.bustiming.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class defines the data structure of the bus stop object
 */
public class BusStopStruct {
    private JSONArray BusStops;
    private int Bs_Last_Skip_Count;
    private int Bs_Last_Count;
    private String BS_LAST_COUNT = "Bs_Last_count";
    private String BS_LIST= "Bs_list";
    private String BS_LAST_SKIP_COUNT = "BS_LAST_SKIP_COUNT";

    /**
     * Constructor
     * @param file JSONObject of bus stop
     */
    public BusStopStruct(JSONObject file) {
        try{
            BusStops = file.getJSONArray(BS_LIST);
            Bs_Last_Count = file.getInt(BS_LAST_COUNT);
            Bs_Last_Skip_Count = file.getInt(BS_LAST_SKIP_COUNT);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * getter for a JSONArray of Bus stops
     * @return JSONArray of Bus Stops
     */
    public JSONArray getBusStops() {
        return BusStops;
    }

    /**
     * setter for JSONArray Bus stops
     * @param busStops JSON array of the collection of busstop objects
     */
    public void setBusStops(JSONArray busStops) {
        BusStops = busStops;
    }

    /**
     * getter the size of the list of bus stops in the list
     * @return an integer of the number of bus stops
     */
    public int getBs_Last_Count() {
        return Bs_Last_Count;
    }

    /**
     * setter the size of the list of bus stops in the list
     * @param Bs_Last_Count an integer of the number of bus stops
     */
    public void setBs_Last_Count(int Bs_Last_Count) {
        Bs_Last_Count = Bs_Last_Count;
    }

    /**
     * getter for the last skip count used
     * @return last skip count
     */
    public int getBs_Last_Skip_Count() {
        return Bs_Last_Skip_Count;
    }

    /**
     * setter for the last skip count used
     * @param bs_Last_Skip_Count last skip count
     */
    public void setBs_Last_Skip_count(int bs_Last_Skip_Count) {
        Bs_Last_Skip_Count = bs_Last_Skip_Count;
    }
}
