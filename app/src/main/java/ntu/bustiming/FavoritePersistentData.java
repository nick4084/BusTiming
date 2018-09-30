package ntu.bustiming;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class FavoritePersistentData {
    private JSONArray LikedBusstop;
    Context mContext;

    public FavoritePersistentData(Context context) {
        mContext = context;
        LikedBusstop = fetchFavoriteBusStop();
    }

    //read list of past saved busstop
    public JSONArray fetchFavoriteBusStop() {
            File file = new File(mContext.getFilesDir().getParent(),"BusTiming/Favorite.txt");
            //Read text from file
                try {
                    //check total bus stop count is the same
                    StringBuilder total = new StringBuilder();
                    FileInputStream fis = new FileInputStream(file);
                    int numRead =0;
                    byte[] bytes = new byte[fis.available()];
                    while ((numRead = fis.read(bytes)) >= 0) {
                        total.append(new String(bytes, 0, numRead));
                    }
                JSONArray jsonraw = new JSONArray(total.toString());
                return jsonraw;
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("readBusStopfile", "I/O error");
            return new JSONArray();
        } catch (JSONException e) {
            Log.i("readBusStopfile", "conversion error");
            return new JSONArray();
        }
    }

    public boolean insertFavoriteBusStop(int code, String Desc, String personalised_name, double lat, double lng, String road) {
        try {
            if (code != 0) {
                JSONObject newFavorite = new JSONObject();
                newFavorite.put(FavoriteBusStopStruct.PARAM_CODE, code);
                newFavorite.put(FavoriteBusStopStruct.PARAM_DESC, Desc);
                newFavorite.put(FavoriteBusStopStruct.PARAM_LAT, lat);
                newFavorite.put(FavoriteBusStopStruct.PARAM_LNG, lng);
                newFavorite.put(FavoriteBusStopStruct.PARAM_PNAME, personalised_name);
                newFavorite.put(FavoriteBusStopStruct.PARAM_RD, road);
                LikedBusstop.put(newFavorite);
                saveFavoriteBusStop();
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFavoriteByCode(int busstop_code) {
        try {
            for (int i = 0; i < LikedBusstop.length(); i++) {
                JSONObject current = LikedBusstop.getJSONObject(i);
                int current_code = Integer.parseInt(current.get(FavoriteBusStopStruct.PARAM_CODE).toString());
                if (current_code == busstop_code) {
                    LikedBusstop.remove(i);
                    saveFavoriteBusStop();
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean checkIfFavorite(int code){
        try {
            for (int i = 0; i < LikedBusstop.length(); i++) {
                JSONObject current = LikedBusstop.getJSONObject(i);
                int current_code = Integer.parseInt(current.get(FavoriteBusStopStruct.PARAM_CODE).toString());
                if (current_code == code) {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;

    }

    public boolean editFavoritePersonalisedNameByCode(int busstop_code, String new_name) {
        try {
            for (int i = 0; i < LikedBusstop.length(); i++) {
                JSONObject current = LikedBusstop.getJSONObject(i);
                int current_code = Integer.parseInt(current.get(FavoriteBusStopStruct.PARAM_CODE).toString());
                if (current_code == busstop_code) {
                    LikedBusstop.getJSONObject(i).remove(FavoriteBusStopStruct.PARAM_PNAME);
                    LikedBusstop.getJSONObject(i).put(FavoriteBusStopStruct.PARAM_PNAME, new_name);
                    saveFavoriteBusStop();
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void saveFavoriteBusStop() {
        File path = new File(mContext.getFilesDir().getParent(), "BusTiming");
        try{
            Writer output = null;
            //create directory if not exist
            if (!path.exists()) {
                path.mkdir();
            }
            File fw = new File(path, "Favorite.txt");
            output = new BufferedWriter(new FileWriter(fw, false));
            output.write(LikedBusstop.toString());
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public JSONArray getLikedBusstop() {
        return LikedBusstop;
    }

    public void setLikedBusstop(JSONArray likedBusstop) {
        LikedBusstop = likedBusstop;
    }

    private class Async extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            File path = new File(params[0], "BusTiming");
            try{
            Writer output = null;
            //create directory if not exist
            if (!path.exists()) {
                path.mkdir();
            }
            File fw = new File(path, "Favorite.txt");
            output = new BufferedWriter(new FileWriter(fw, false));
            output.write(params[1].toString());
            output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }
}


