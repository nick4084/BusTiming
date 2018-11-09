package ntu.bustiming.control;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ntu.bustiming.entity.BusStops;
import ntu.bustiming.R;

import static ntu.bustiming.control.Bus.Param_busstopcode;
import static ntu.bustiming.control.Bus.Param_direction;
import static ntu.bustiming.control.Bus.Param_serviceno;
import static ntu.bustiming.control.Bus.Param_stopsequence;
import static ntu.bustiming.entity.BusStops.Param_busstop_code;
import static ntu.bustiming.entity.BusStops.Param_description;
import static ntu.bustiming.entity.BusStops.Param_roadname;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * this class handles the search request when the application request for it
 */
public class SearchableActivity extends Activity{

    Context mContext ;
    JSONArray BusStop_list;
    JSONArray Bus_list;
    JSONArray list_of_sBus;
    ListView list;
    BusStopSearchBaseAdapter adapter1;
    BusStopSearchBaseAdapter adapter2;
    ArrayList<String> mylist = new ArrayList<String>();
    ArrayList<SimplifiedBus> simplifiedBuses = new ArrayList<>();
    ArrayList<SimplifiedBus> simplifiedBusesR = new ArrayList<>();
    ArrayList<SimplifiedBus> simplifiedBusesF = new ArrayList<>();
    int count;
    ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        JSONArray jsonArray = new JSONArray();
        JSONObject ab = getBusStopobjectByCode(message);
        jsonArray.put(ab);
        final String description1;
        final String code1;
        final String roadname1;
        Button btn1 = new Button(this);
        Button btn2 = new Button(this);
        listView = findViewById(R.id.list_search);




        TextView textView = findViewById(R.id.firstLine);
        TextView textView2 = findViewById(R.id.secondLine);
        TextView textView3 = findViewById(R.id.thirdLine);


        if (message.length() == 5) {
            description1 = getBusStopDescriptionByCode(message);
            roadname1 = getBusStopRdByCode(message);
            code1 = message;
            textView.setText(code1);
            textView2.setText(description1);
            textView3.setText(roadname1);
        } else {
            code1 = getBusStopCodeByDescrption(message);
            roadname1 = getBusStopRdNameByDescrption(message);
            description1 = message;
            textView.setText(code1);
            textView2.setText(description1);
            textView3.setText(roadname1);
        }

        if(message.length()<=3){
            getBusByBusService(message);
            simplifiedBusesR = getSimplifiedBusesReverse();
            simplifiedBusesF = getSimplifiedBusesForward();
            LinearLayout l_layout = findViewById(R.id.linearLayout);
            listView = listView.findViewById(R.id.list_search);
            if(simplifiedBusesR == null){
                adapter1 = new BusStopSearchBaseAdapter(this,simplifiedBuses);

            }else {
                adapter1 = new BusStopSearchBaseAdapter(this,simplifiedBusesF);
                adapter2 = new BusStopSearchBaseAdapter(this,simplifiedBusesR);

                btn1.setText("Direction 1");
                btn2.setText("Direction 2");
                l_layout.addView(btn1);
                l_layout.addView(btn2);
            }
            listView.setAdapter(adapter1);
                textView.setVisibility(View.GONE);
                textView2.setVisibility(View.GONE);
                textView3.setVisibility(View.GONE);

        }

        btn1.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v) {
                                        listView.setAdapter(adapter1);
                                    }
                                });
        btn2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                listView.setAdapter(adapter2);
            }
        });


            textView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    LTADatamallController LTADatamallController = new LTADatamallController(getApplicationContext());
                    JSONObject bus_arrival_timing = LTADatamallController.getBusArrivalByBusStopCode(code1);
                    try {
                        displayBusTiming(bus_arrival_timing, code1, description1, roadname1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            textView2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                }
            });

            textView3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

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

    public String getBusByBusService1(String service){
        if(Bus_list==null){
            setUpBusList();
        }
        JSONObject bus;
        for (int i = 0; i<Bus_list.length(); i++) {
            try{
                bus = Bus_list.getJSONObject(i);
                String current_service =  bus.getString(Param_serviceno);
                if(service.equals(current_service)){
                    return bus.getString(Param_serviceno);
                }
            } catch(JSONException e){
                e.printStackTrace();
            }
        }
        return "";
    }

    public ArrayList<SimplifiedBus> getSimplifiedBusesReverse() {
        ArrayList<SimplifiedBus> reversebus = new ArrayList<>();
        int i;
        for( i=0;i<simplifiedBuses.size();i++){
            if(Integer.parseInt(simplifiedBuses.get(i).getDirection())==2){
                reversebus.add(simplifiedBuses.get(i));
            }
        }
        return reversebus;
    }

    public ArrayList<SimplifiedBus> getSimplifiedBusesForward() {
        ArrayList<SimplifiedBus> forwardbus = new ArrayList<>();
        int i;
        for( i=0;i<simplifiedBuses.size();i++){
            if(Integer.parseInt(simplifiedBuses.get(i).getDirection())==1){
                forwardbus.add(simplifiedBuses.get(i));
            }
        }
        return forwardbus;
    }

    public void getBusByBusService(String service){
        if(Bus_list==null){
            setUpBusList();
        }
        JSONObject bus;
        for (int i = 0; i<Bus_list.length(); i++) {
            try{
                bus = Bus_list.getJSONObject(i);
                String current_service =  bus.getString(Param_serviceno);
                if(service.equals(current_service)){
                    String serviceno = bus.getString(Param_serviceno);
                    String busstopcode = bus.getString(Param_busstopcode);
                    String direction = bus.getString(Param_direction);
                    String stopsequence = bus.getString(Param_stopsequence);
                    String discription = getBusStopDescriptionByCode(busstopcode);
                    String roadname = getBusStopRdByCode(busstopcode);

                    SimplifiedBus bus1 = new SimplifiedBus(serviceno,busstopcode,roadname,direction,discription,stopsequence);
                    simplifiedBuses.add(bus1);
                }
            } catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * this method get the list of busstop
     */
    public void setUpBusStopList(){
        BusStops bs_data = new BusStops(this);
        BusStop_list  = bs_data.readBusStopfile();
    }

    public void setUpBusList(){
        Bus b_data = new Bus(this);
        Bus_list = b_data.readBusfile();
    }

    /**
     * this method display the bus display timing
     */


    /** this method display the bus display timing
     * @param busTiming json object of busstop
     * @param BusStopCode bus stop code
     * @param BusStopDescription bus stop description
     */
    public void displayBusTiming(JSONObject busTiming, String BusStopCode, String BusStopDescription, String road) {
        //display bus timing dialog pop up
        BusTimingDialog TimingDialog = new BusTimingDialog(SearchableActivity.this, busTiming, BusStopCode, BusStopDescription, road, new BusTimingDialog.OnDialogClickListener() {
            /**
             * Must Implement method
             * Observer will call this method when Favorite fragment is changed
             */
            @Override
            public void notifyFavoriteDataChange() {
                FavoriteFragment f = (FavoriteFragment) MainActivity.adapter.getItem(1);
                f.refreshData();
            }
        });
        TimingDialog.show();
    }
}


