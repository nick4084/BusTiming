package ntu.bustiming.control;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * This class handles the Create, Remove, Update, Delete of favourite bus stop
 */
public class FavoriteDataController {
    private JSONArray LikedBusstop;
    Context mContext;

    /**
     * Constructor method
     * initialize likedbusstop by fetching all favorite bus stop
     * @param context
     */
    public FavoriteDataController(Context context) {
        mContext = context;
        LikedBusstop = fetchFavoriteBusStop();
    }

    /**
     * fetch favorite bus stop by reading from app text file
     * @return JSONArray of all bus stops
     */
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
            //return new JSONArray(); //uncomment this line, run and like a b/s will clear off favorite.txt
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("readBusStopfile", "I/O error");
            return new JSONArray();
        } catch (JSONException e) {
            Log.i("readBusStopfile", "conversion error");
            return new JSONArray();
        }
    }

    /**
     * insert a favorite bus stop entry
     * @param code bus stop code
     * @param Desc bus stop description
     * @param personalised_name bus stop personalised name
     * @param lat bus stop latitude
     * @param lng bus stop longitude
     * @param road bus stop road name
     * @return false if code is empty
     */
    public boolean insertFavoriteBusStop(int code, String Desc, String personalised_name, double lat, double lng, String road) {
        if (code != 0) {
            FavoriteBusStopStruct fbst = new FavoriteBusStopStruct(code, Desc, Desc, lat, lng, road);
            LikedBusstop.put(fbst.getFavoriteBusStopJSONObject());
            saveFavoriteBusStop();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Delete favorite bus stop by bus stop code
     * @param busstop_code bus stop code to delete
     * @return true of removed successfully
     */
    public boolean deleteFavoriteByCode(int busstop_code) {
        try {
            for (int i = 0; i < LikedBusstop.length(); i++) {
                FavoriteBusStopStruct current = new FavoriteBusStopStruct(LikedBusstop.getJSONObject(i));
                int current_code = current.getBusstop_code();
                if (current_code == busstop_code) {
                    removeFromFavoriteBusstopList(i);
                    saveFavoriteBusStop();
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * remove from favorite bus stop list by index
     * @param index index of the favorite bus stop to remove
     */
    public void removeFromFavoriteBusstopList(int index){
        LikedBusstop.remove(index);
    }
    public boolean checkIfFavorite(int code){
        try {
            for (int i = 0; i < LikedBusstop.length(); i++) {
                FavoriteBusStopStruct current = new FavoriteBusStopStruct(LikedBusstop.getJSONObject(i));
                int current_code = current.getBusstop_code();
                if (current_code == code) {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Update favorite bus stop personalised name by bus stop code
     * @param busstop_code bus stop code to update
     * @param new_name bus stop name
     * @return true if update successfully
     */
    public boolean editFavoritePersonalisedNameByCode(int busstop_code, String new_name) {
        try {
            for (int i = 0; i < LikedBusstop.length(); i++) {
                FavoriteBusStopStruct current = new FavoriteBusStopStruct(LikedBusstop.getJSONObject(i));
                int current_code = current.getBusstop_code();
                if (current_code == busstop_code) {
                    //LikedBusstop.getJSONObject(i).remove(FavoriteBusStopStruct.PARAM_PNAME);
                    //LikedBusstop.getJSONObject(i).put(FavoriteBusStopStruct.PARAM_PNAME, new_name);
                    current.setBusstop_personalised_name(new_name);
                    LikedBusstop.remove(i);
                    LikedBusstop.put(current.getFavoriteBusStopJSONObject());
                    saveFavoriteBusStop();
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * save favorite bus stop list into text file
     */
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

    /**
     * get the list of liked bus stop
     * @return JSONArray of liked bus stop
     */
    public JSONArray getLikedBusstop() {
        return LikedBusstop;
    }

    /**
     * setter for liked bus stop
     * @param likedBusstop value to set
     */
    public void setLikedBusstop(JSONArray likedBusstop) {
        LikedBusstop = likedBusstop;
    }

    /**
     * This class executes in the background
     */
    private class Async extends AsyncTask<String, Void, Void> {
        /**
         * asynchronously write to text file
         * @param params array of string
         * @return null
         */
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

        /**
         * called after doInBackground completes
         * @param values
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }
}


