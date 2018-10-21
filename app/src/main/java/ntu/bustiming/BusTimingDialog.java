package ntu.bustiming;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BusTimingDialog extends Dialog implements DialogInterface.OnClickListener{
        Context mcontext;
        JSONObject mBusTiming;
        JSONArray jsonarray_bustiming;
        String mbusStopDescription;
        String mbusStopCode;
        String mbusStopRoad;
        double mLat;
        double mLng;
        OnDialogClickListener mlistener;
    public interface OnDialogClickListener {
        void notifyFavoriteDataChange();
    }
@Override
public void onClick(DialogInterface dialogInterface, int i) {

        }

public BusTimingDialog(@NonNull Context context, JSONObject BusTimingJSON, String BusStopCode, String BusStopSescription, double lat, double lng, String road, OnDialogClickListener listener) {
        super(context);
        mcontext = context;
        mBusTiming = BusTimingJSON;
        mbusStopDescription = BusStopSescription;
        mbusStopCode= BusStopCode;
        mLat = lat;
        mLng = lng;
        mlistener = listener;
        mbusStopRoad = road;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bus_timing_dialog);
        TextView Tv_Description = (TextView)findViewById(R.id.tv_bustiming_description);
        TextView Tv_Code = (TextView)findViewById(R.id.tv_bustiming_code);
        CheckBox cb_favorite = (CheckBox)findViewById(R.id.busTimingLikeIcon);
        ImageView iv_weather_icon = findViewById(R.id.iv_weather_icon);
        Tv_Description.setText(mbusStopDescription);
        Tv_Code.setText(mbusStopCode);

        ListView lv_bustiming = (ListView) findViewById(R.id.lv_bustiming);

        try{
        jsonarray_bustiming = mBusTiming.getJSONArray("Services");
        } catch(JSONException ex){
        ex.printStackTrace();
        }
        final FavoritePersistentData data = new FavoritePersistentData(mcontext);
        cb_favorite.setChecked(data.checkIfFavorite(Integer.parseInt(mbusStopCode)));
            cb_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        data.insertFavoriteBusStop(Integer.parseInt(mbusStopCode), mbusStopDescription, mbusStopDescription, mLat, mLng, mbusStopRoad);
                    } else {
                        data.deleteFavoriteByCode(Integer.parseInt(mbusStopCode));
                    }
                    mlistener.notifyFavoriteDataChange();
                }
            });
        lv_bustiming.setAdapter(new BusTimingBaseAdapter(mcontext, jsonarray_bustiming));
        lv_bustiming.setEmptyView(findViewById(R.id.tv_bustiming_empty));
        lv_bustiming.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(view.getId()!= 0){
                    TextView tv_bus_svc = (TextView) view.findViewById(R.id.tv_bustiming_serviceno);
                    String Bus_Svc = tv_bus_svc.getText().toString();
                }
            }
        });

        //handles the weather api
            Weather_controller weather_controller = new Weather_controller(mcontext);
            weather_controller.get2HWeatherByLatLng(mLat, mLng, iv_weather_icon);
            iv_weather_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(mcontext, "", Toast.LENGTH_LONG).show();
                }
            });

        }
}
