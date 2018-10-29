package ntu.bustiming;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class Setup and Establish connection.
 * Call API from LTA datamall.
 */
public class LTADatamallController {
    String API_KEY= "yx6RmXTRhXhBAodoKDvWxQ==";
    String UNIQUE_USERID= "dc5eed1e-a2be-4323-aa3e-eb9680180eb2";
    String API_URL="http://datamall2.mytransport.sg/ltaodataservice/";
    String API_BUS_ARRIVAL="BusArrivalv2?BusStopCode=";
    String API_BUS_SERVICE="BusServices";
    String API_BUS_STOP="BusStops";
    String API_BUS_ROUTE="BusRoute";
    String REQUEST_HEADER_ACCOUNT_KEY = "AccountKey";
    String REQUEST_HEADER_UNIQUE_USER_ID = "UniqueUserID";
    String REQUEST_HEADER_ACCEPT = "accept";
    String ACCEPT_HEADER="application/json";
    Context mContext;

    /**
     * Constructor method
     * @param mContext application context
     */
    public LTADatamallController(Context mContext){
    this.mContext = mContext;
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

    /**
     * get Bus service of bus
     * @return JSONObject of bus service
     */
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

    /**
     * get bus stop in group of 50
     * @param skip define the starting skipped index of bus stop
     * @return JSONObject of 50 bus stop
     */
    public JSONObject getBusStops(int skip){
        JSONObject RETURN = null;
        String URL_PATH = API_URL + API_BUS_STOP + "?$skip=" + skip;
        try {
            RETURN = new LoadData().execute(URL_PATH).get();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return RETURN;
    }

    /**
     * get Bus Route of buses
     * @return a JSONObject of all bus route
     */
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

    /**
     * get Bus Arrival details of bus stop by bus stop code
     * @param BusStop_Code bus stop code
     * @return JSONObject of bus stop bus arrival details
     */
    public JSONObject getBusArrivalByBusStopCode(String BusStop_Code){
        JSONObject RETURN;
        String URL_PATH = API_URL + API_BUS_ARRIVAL+ BusStop_Code + "&SST=True";
        try {
            RETURN = new LoadData().execute(URL_PATH).get();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return RETURN;
    }

    /**
     * fetch the whole list of bus stops and write it to a text file
     */
    public void fetchAllBusStop(){
        int skip = 0, size =0;
        JSONObject lot;
        JSONArray current;
        boolean not_done = true;
        JSONObject dataToFile = new JSONObject();
        ProgressDialog p_dialog;
        p_dialog = new ProgressDialog(mContext);
        p_dialog.setMessage("fetching bus stops...");
        p_dialog.setIndeterminate(true);
        p_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        p_dialog.setCancelable(false);
        p_dialog.show();

        try {
            JSONArray BusStops = new JSONArray();
            while(not_done) {
                p_dialog.setMessage("fetching bus stops..."+ Integer.toString(skip));
                lot = new Async().execute(Integer.toString(skip)).get();
                current = lot.getJSONArray("value");
                size = current.length();
                if(size > 0){
                    for (int i = 0; i < size; i++) {
                        BusStops.put(current.getJSONObject(i));
                    }
                    skip += size;
                } else {
                    break;
                }
            }
            p_dialog.setMessage("saving bus stops...");
            dataToFile.put("Bs_Last_count", size);
            dataToFile.put("BS_LAST_SKIP_COUNT", skip);
            dataToFile.put("Bs_list", BusStops);

            Writer output = null;
            //create directory if not exist
            File path = new File(mContext.getFilesDir(), "BusTiming");
            if (!path.exists()) {
                path.mkdir();
            }
            File fw = new File(path, "BusStops.txt");
            output = new BufferedWriter(new FileWriter(fw, false));
            output.write(dataToFile.toString());
            output.close();
        } catch(Exception ex){
            ex.printStackTrace();
        }
        p_dialog.dismiss();
    }

    /**
     * Asynchronously execute DoInBackground by passing in the URL as param[0]
     * Send HTTP GET request
     */
    private class LoadData extends AsyncTask<String, Void, JSONObject> {


        /**
         * The Async Method that send the HTTP request
         * @param params array of strings
         * @return JSONObject of the HTTP request result
         */
        @Override
        protected JSONObject doInBackground(String... params) {
            StringBuilder total;
            JSONObject JsonObject=null;
            String response="";
            try {
                total = new StringBuilder();
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
                JsonObject= new JSONObject(response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
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


    private class Async extends AsyncTask<String, Integer, JSONObject> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            int size = 0;
            JSONObject lot = null;
            JSONArray current_array = null;
            JSONArray BusStops = new JSONArray();
            String response="";
            String s="";
            URL url;
            URLConnection connection;
            HttpURLConnection httpConnection;
            InputStream content;
            BufferedReader buffer;
            String datamall_url;
            //fetch busstop
            try {
                    datamall_url = "http://datamall2.mytransport.sg/ltaodataservice/BusStops?$skip=" + params[0];
                    url = new URL(datamall_url);
                    connection = url.openConnection();
                    httpConnection = (HttpURLConnection) connection;
                    httpConnection.setRequestMethod("GET");
                    httpConnection.addRequestProperty(REQUEST_HEADER_ACCOUNT_KEY, API_KEY);
                    httpConnection.addRequestProperty(REQUEST_HEADER_ACCEPT, ACCEPT_HEADER);
                    httpConnection.connect();

                    if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        content = httpConnection.getInputStream();
                        buffer = new BufferedReader(new InputStreamReader(content));
                        s = "";
                        while ((s = buffer.readLine()) != null) {
                            response += s;
                        }
                    }
                    httpConnection.disconnect();
                    lot = new JSONObject(response);
                    return lot;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

        }
    }
}
