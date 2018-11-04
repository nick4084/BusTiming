package ntu.bustiming.control;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class defines the data structure of favorite bus stop
 */
public class FavoriteBusStopStruct {
    private int busstop_code;
    private String busstop_description;
    private String busstop_personalised_name;
    private String busstop_rd;
    private double busstop_Lat;
    private double busstop_Lng;
    private JSONObject busstop_obj;

    public static String PARAM_CODE = "code";
    public static String PARAM_DESC = "description";
    public static String PARAM_PNAME = "personalised_name";
    public static String PARAM_LAT = "lat";
    public static String PARAM_LNG = "lng";
    public static String PARAM_RD = "road";
    public static String PARAM_OBJ = "favorite_busstop_struct";

    /**
     * Constructor method that can instantiate the attributes from a JSONObject
     * @param busstop_obj JSONObject of favorite bus stop
     */
    public FavoriteBusStopStruct(JSONObject busstop_obj){
        try {
            this.busstop_code = busstop_obj.getInt(PARAM_CODE);
            this.busstop_description = busstop_obj.getString(PARAM_DESC);
            this.busstop_personalised_name = busstop_obj.getString(PARAM_PNAME);
            this.busstop_Lat = busstop_obj.getDouble(PARAM_LAT);
            this.busstop_Lng = busstop_obj.getDouble(PARAM_LNG);
            this.busstop_rd = busstop_obj.getString(PARAM_RD);
        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * Constructor method that instantiate
     * @param busstop_code Bus stop code
     * @param busstop_description Bus stop description
     * @param busstop_personalised_name Bus Stop Personalised name
     * @param busstop_lat Bus Stop latitude
     * @param busstop_lng Bus stop Longitude
     * @param busstop_rd
     */
    public FavoriteBusStopStruct(int busstop_code, String busstop_description, String busstop_personalised_name, double busstop_lat, double busstop_lng, String busstop_rd) {
        this.busstop_code = busstop_code;
        this.busstop_description = busstop_description;
        this.busstop_personalised_name = busstop_personalised_name;
        this.busstop_Lat = busstop_lat;
        this.busstop_Lng = busstop_lng;
        this.busstop_rd = busstop_rd;

        generateJSONObj();

    }

    /**
     * generate a JSONObject of favorite bus stop data structure
     */
    public void generateJSONObj(){
        try {
            busstop_obj = new JSONObject();
            busstop_obj.put(PARAM_RD, busstop_rd);
            busstop_obj.put(PARAM_DESC, busstop_description);
            busstop_obj.put(PARAM_PNAME, busstop_personalised_name);
            busstop_obj.put(PARAM_CODE, busstop_code);
            busstop_obj.put(PARAM_LAT, busstop_Lat);
            busstop_obj.put(PARAM_LNG, busstop_Lng);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * getter method
     * @return bus stop
     */
    public int getBusstop_code() {
        return busstop_code;
    }
    /**
     * setter method for bus stop code
     */
    public void setBusstop_code(int busstop_code) {
        this.busstop_code = busstop_code;
    }
    /**
     * getter method
     * @return bus stop
     */
    public String getBusstop_description() {
        return busstop_description;
    }
    /**
     * setter method for bus stop latitude
     */
    public void setBusstop_description(String busstop_description) {
        this.busstop_description = busstop_description;
    }
    /**
     * getter method
     * @return bus stop
     */
    public String getBusstop_personalised_name() {
        return busstop_personalised_name;
    }
    /**
     * setter method for bus stop personalised name
     */
    public void setBusstop_personalised_name(String busstop_personalised_name) {
        this.busstop_personalised_name = busstop_personalised_name;
    }
    /**
     * getter method
     * @return bus stop latitude
     */
    public double getBusstop_Lat() {
        return busstop_Lat;
    }
    /**
     * setter method for bus stop latitude
     */
    public void setBusstop_Lat(double busstop_Lat) {
        this.busstop_Lat = busstop_Lat;
    }
    /**
     * getter method
     * @return bus stop longitude
     */
    public double getBusstop_Lng() {
        return busstop_Lng;
    }
    /**
     * setter method for bus stop longitude
     */
    public void setBusstop_Lng(double busstop_Lng) {
        this.busstop_Lng = busstop_Lng;
    }
    /**
     * setter method for bus stop road
     */
    public String getBusstop_rd() {
        return busstop_rd;
    }
    /**
     * setter method
     * @param busstop_rd stop rd
     */
    public void setBusstop_rd(String busstop_rd) {
        this.busstop_rd = busstop_rd;
    }
    /**
     * getter method
     * @return JSONObject of favorite bus stop data structure
     */
    public JSONObject getFavoriteBusStopJSONObject(){
        if(busstop_obj == null){
            generateJSONObj();
        }
        return busstop_obj;
    }
}
