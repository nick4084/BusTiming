package ntu.bustiming;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeatherController {
    private String URL_2h = "https://api.data.gov.sg/v1/environment/2-hour-weather-forecast";
    private String URL_24h = "https://api.data.gov.sg/v1/environment/24-hour-weather-forecast";
    private String PARAM_DATE = "?date_time-";
    String REQUEST_HEADER_ACCEPT = "accept";
    String ACCEPT_HEADER="application/json";
    Context mcontext;
    String PARAM_THUNDERY_SHOWER = "Thundery Showers";
    String PARAM_PARTLY_CLOUDY = "Partly Cloudy";
    String PARAM_LIGHT_SHOWER = "Light Showers";

    public WeatherController(Context context){
        this.mcontext = context;
    }

    public void get2HWeatherByLatLng(double lat, double lng, ImageView icon_holder){
        try {
            String URL_PATH = URL_2h + PARAM_DATE + getDate();
            LoadWeatherData w_api = new LoadWeatherData();
            w_api.setImageView(icon_holder);
            w_api.setLatLng(lat, lng);
            w_api.execute(URL_PATH).get();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    protected String getDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    private class LoadWeatherData extends AsyncTask<String, Void, JSONObject> {
        ImageView iv_icon_holder;
        double lat=0;
        double lng=0;
        public void setImageView(ImageView iv_icon){
            iv_icon_holder = iv_icon;
        }

        public void setLatLng(double lat, double lng){
            this.lat = lat;
            this.lng = lng;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject JsonObject=null;
            String response="";
            try {
                java.net.URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();

                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
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
            try {
                JSONArray arr = object.getJSONArray("area_metadata");
                JSONObject curr, loc = null;
                String nearest_loc_name="";
                double shortest_dist=0;
                double dist =0;
                double nearest_lat, nearest_lng, curr_lat, curr_lng;

                //find nearest location
                for (int i = 0; i< arr.length(); i++){
                    curr= arr.getJSONObject(i);
                    loc = curr.getJSONObject("label_location");
                    curr_lat =loc.getDouble("latitude");
                    curr_lng = loc.getDouble("longitude");
                    dist = distance(lat, lng, curr_lat, curr_lng);

                    if(shortest_dist ==0 || dist < shortest_dist){
                        //curr contains the nearer location
                        nearest_lat = curr_lat;
                        nearest_lng = curr_lng;
                        nearest_loc_name = curr.getString("name");
                        shortest_dist = dist;
                    }
                }
                //by now, nearest_loc_name is the nearest location with this busstop
                //get forecast of nearest location
                JSONArray items = object.getJSONArray("items");
                JSONObject forecast_obj = items.getJSONObject(0);
                JSONArray forecast = forecast_obj.getJSONArray("forecasts");
                String str_forecast="";
                for(int i =0; i< forecast.length(); i++){
                    curr = forecast.getJSONObject(i);
                    if(curr.getString("area").equals(nearest_loc_name)){
                        str_forecast = curr.getString("forecast");
                        DisplayWeatherIcon(str_forecast);
                        break;
                    }
                }

            } catch(JSONException e){
                e.printStackTrace();
            }
        }
        private void DisplayWeatherIcon(String str_forecast){
            String fc_str_arr[] = str_forecast.split("\\(");
            str_forecast = fc_str_arr[0].replaceAll(" ", "");
            String type="";
            if(fc_str_arr.length > 1){
                type = fc_str_arr[1].substring(0, fc_str_arr[1].length()-1);
                switch (type){
                    case "Day":
                        switch(str_forecast){
                            case "PartlyCloudy":
                                iv_icon_holder.setImageResource(R.drawable.partial_cloudy_day);
                                break;
                            default:
                                break;
                        }
                        break;
                    case "Night":
                        switch(str_forecast){
                            case "PartlyCloudy":
                                iv_icon_holder.setImageResource(R.drawable.partial_cloudy_night);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
            } else {
                switch(str_forecast){
                    case "ModerateRain":
                        iv_icon_holder.setImageResource(R.drawable.shower);
                        break;
                    case "Shower":
                        iv_icon_holder.setImageResource(R.drawable.shower);
                        break;
                    case "ThunderyShowers":
                        iv_icon_holder.setImageResource(R.drawable.thundery_shower);
                        break;
                    case "Cloudy":
                        iv_icon_holder.setImageResource(R.drawable.cloudy);
                        break;
                    default:
                        break;
                }

            }
        }

        private double distance(double lat1, double lon1, double lat2, double lon2) {
            // haversine great circle distance approximation, returns meters
            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                    + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                    * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60; // 60 nautical miles per degree of seperation
            dist = dist * 1852; // 1852 meters per nautical mile
            return (dist);
        }

        private double deg2rad(double deg) {
            return (deg * Math.PI / 180.0);
        }

        private double rad2deg(double rad) {
            return (rad * 180.0 / Math.PI);
        }

    }
}
