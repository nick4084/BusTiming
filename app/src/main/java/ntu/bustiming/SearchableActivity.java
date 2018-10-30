package ntu.bustiming;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ntu.bustiming.FavoriteFragment.OnFragmentInteractionListener;

import static ntu.bustiming.BusStops.Param_busstop_code;
import static ntu.bustiming.BusStops.Param_description;
import static ntu.bustiming.BusStops.Param_roadname;

/**
 * this class handles the search request when the application request for it
 */
public class SearchableActivity extends Activity {

    Context mContext;
    JSONArray BusStop_list;
    ListView list;
    BusStopSearchBaseAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String description1 ="";
        String roadname1 = "";
        String code1="";
        JSONArray jsonArray = new JSONArray();
        JSONObject ab = getBusStopobjectByCode(message);
        jsonArray.put(ab);
/*        list = (ListView) findViewById(R.id.list_notice2);
        adapter = new BusStopSearchBaseAdapter(this,jsonArray);
        list.setAdapter(adapter);*/



        TextView textView = findViewById(R.id.firstLine);
        TextView textView2 = findViewById(R.id.secondLine);
        TextView textView3 = findViewById(R.id.thirdLine);

        if(message.length()==5) {
            description1 = getBusStopDescriptionByCode(message);
            roadname1 = getBusStopRdByCode(message);
            code1= message;
        }else{
            code1 = getBusStopCodeByDescrption(message);
            roadname1 = getBusStopRdNameByDescrption(message);
            description1= message;
        }


        textView.setText(code1);
        textView2.setText(description1);
        textView3.setText(roadname1);

        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();

            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();

            }
        });

        textView3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();
            }
        });

    }

    /** this method get the bus stop road string
     * @param code bus stop code
     * @return bus stop road name
     */
    public String getBusStopRdByCode(String code){
        if(BusStop_list==null){
            setUpBusStopList();
        }
        JSONObject busstop;
        for (int i = 0; i<BusStop_list.length(); i++) {
            try{
                busstop = BusStop_list.getJSONObject(i);
                String current_code =  busstop.getString(Param_busstop_code);
                if(code.equals(current_code)){
                    return busstop.getString(Param_roadname);
                }
            } catch(JSONException e){
                e.printStackTrace();
            }

        }
        return "";
    }

    /** this method get the bus stop object
     * @param code bus stop code
     * @return the json object containg the bus stop information in json format
     */
    public JSONObject getBusStopobjectByCode(String code){
        if(BusStop_list==null){
            setUpBusStopList();
        }
        JSONObject busstop;
        for (int i = 0; i<BusStop_list.length(); i++) {
            try{
                busstop = BusStop_list.getJSONObject(i);
                String current_code =  busstop.getString(Param_busstop_code);
                if(code.equals(current_code)){
                    return busstop;
                }
            } catch(JSONException e){
                e.printStackTrace();
            }

        }
        return null;
    }

    /** get bus stop code
     * @param code bus stop description
     * @return bus stop code
     */
    public String getBusStopCodeByDescrption(String code){
        if(BusStop_list==null){
            setUpBusStopList();
        }
        JSONObject busstop;
        for (int i = 0; i<BusStop_list.length(); i++) {
            try{
                busstop = BusStop_list.getJSONObject(i);
                String current_code =  busstop.getString(Param_description);
                if(code.equals(current_code)){
                    return busstop.getString(Param_busstop_code);
                }
            } catch(JSONException e){
                e.printStackTrace();
            }

        }
        return "";
    }

    /** get the bus stop road name
     * @param code bus stop code
     * @return bus stop road name
     */
    public String getBusStopRdNameByDescrption(String code){
        if(BusStop_list==null){
            setUpBusStopList();
        }
        JSONObject busstop;
        for (int i = 0; i<BusStop_list.length(); i++) {
            try{
                busstop = BusStop_list.getJSONObject(i);
                String current_code =  busstop.getString(Param_description);
                if(code.equals(current_code)){
                    return busstop.getString(Param_roadname);
                }
            } catch(JSONException e){
                e.printStackTrace();
            }

        }
        return "";
    }

    /** get bus stop description
     * @param code bus stop code
     * @return bus stop description
     */
    public String getBusStopDescriptionByCode(String code){
        if(BusStop_list==null){
            setUpBusStopList();
        }
        JSONObject busstop;
        for (int i = 0; i<BusStop_list.length(); i++) {
            try{
                busstop = BusStop_list.getJSONObject(i);
                String current_code =  busstop.getString(Param_busstop_code);
                if(code.equals(current_code)){
                    return busstop.getString(Param_description);
                }
            } catch(JSONException e){
                e.printStackTrace();
            }

        }
        return "";
    }

    /**
     * this method get the list of busstop
     */
    public void setUpBusStopList(){
        BusStops bs_data = new BusStops(this);
        BusStop_list  = bs_data.readBusStopfile();
    }

    /**
     * this method display the bus display timing
     */
    public void setUpAndDisplayBusTiming(){
        LTADatamallController LTADatamallController = new LTADatamallController(this);
        JSONObject bus_arrival_timing = LTADatamallController.getBusStops(50);
        if(bus_arrival_timing!=null){
            //display bus timing
            try {
                /* displayBusTiming(bus_arrival_timing, Busstop_code, Busstop_description);*/
                //mListener.onNearbyFragmentInteraction();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /** this method display the bus display timing
     * @param busTiming json object of busstop
     * @param BusStopCode bus stop code
     * @param BusStopDescription bus stop description
     */
    public void displayBusTiming(JSONObject busTiming, String BusStopCode, String BusStopDescription){
        //display bus timing dialog pop up
        String road = getBusStopRdByCode(BusStopCode);
        BusTimingDialog TimingDialog = new BusTimingDialog(this, busTiming, BusStopCode, BusStopDescription, 0, 0, road,  new BusTimingDialog.OnDialogClickListener(){
            @Override
            public void notifyFavoriteDataChange() {

            }
        });
        TimingDialog.show();
    }
}
