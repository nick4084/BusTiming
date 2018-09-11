package ntu.bustiming;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class LTA_API {
    String API_KEY= "yx6RmXTRhXhBAodoKDvWxQ==";
    String UNIQUE_USERID= "dc5eed1e-a2be-4323-aa3e-eb9680180eb2";
    String API_URL="http://datamall2.mytransport.sg/ltaodataservice/";
    String API_BUS_ARRIVAL="BusArrivalv2?BusStopCode=";
    String API_BUS_SERVICE="BusServices";
    String API_BUS_STOP="BusStop";
    String API_BUS_ROUTE="BusRoute";
    String REQUEST_HEADER_ACCOUNT_KEY = "AccountKey";
    String REQUEST_HEADER_UNIQUE_USER_ID = "UniqueUserID";
    String REQUEST_HEADER_ACCEPT = "accept";
    String ACCEPT_HEADER="application/json";

    public LTA_API(){

    }
    public JSONObject SingleBusArrivalTiming(String BUS_SERVICE, String BUSSTOP_ID){
        JSONObject RETURN = null;
        String URL_PATH = API_URL + API_BUS_ARRIVAL + BUSSTOP_ID +"&ServiceNo=" + BUS_SERVICE+ "&SST=True";
        try {
            RETURN = new LoadData().execute(URL_PATH).get();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return RETURN;
    }
    public JSONObject getBusServices(){
        JSONObject RETURN = null;
        String URL_PATH = API_URL + API_BUS_SERVICE;
        try {
            RETURN = new LoadData().execute(URL_PATH).get();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return RETURN;
    }

    public JSONObject getBusStops(){
        JSONObject RETURN = null;
        String URL_PATH = API_URL + API_BUS_STOP;
        try {
            RETURN = new LoadData().execute(URL_PATH).get();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return RETURN;
    }

    public JSONObject getBusRoute(){
        JSONObject RETURN = null;
        String URL_PATH = API_URL + API_BUS_ROUTE;
        try {
            RETURN = new LoadData().execute(URL_PATH).get();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return RETURN;
    }
    public JSONObject getBusArrivalByBusStopCode(String BusStop_Code){
        JSONObject RETURN = null;
        String URL_PATH = API_URL + API_BUS_ARRIVAL+ BusStop_Code + "&SST=True";
        try {
            RETURN = new LoadData().execute(URL_PATH).get();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return RETURN;
    }

    private class LoadData extends AsyncTask<String, Void, JSONObject> {


        @Override
        protected JSONObject doInBackground(String... params) {
            InputStream is = null;
            JSONObject JsonObject=null;
            String response="";
            try {
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();

                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.addRequestProperty(REQUEST_HEADER_ACCOUNT_KEY, API_KEY);
                //httpConnection.addRequestProperty(REQUEST_HEADER_UNIQUE_USER_ID, UNIQUE_USERID);
                httpConnection.addRequestProperty(REQUEST_HEADER_ACCEPT, ACCEPT_HEADER);
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream content = httpConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            try{
                JsonObject= new JSONObject(response);
            }catch (JSONException je){
                je.printStackTrace();
                JsonObject=null;
            }
            return JsonObject;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            super.onPostExecute(object);
        }

    }
}
