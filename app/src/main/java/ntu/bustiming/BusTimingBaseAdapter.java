package ntu.bustiming;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * API V4.6 19mar 2018
 */

public class BusTimingBaseAdapter extends BaseAdapter {
    Context mcontext;
    LayoutInflater mInflater;
    JSONArray mBus_Timing_JSONArray;
    String param_ServiceNo = "ServiceNo";
    //String param_Status ="Status";
    String param_Operator = "Operator";
    String param_OriginCode = "OriginCode";
    String param_DestinationCode = "DestinationCode";
    String param_EstimatedArrival = "EstimatedArrival"; //eg. "2018-06-10T13:10:19+08:00"
    String param_Latitude = "Latitude";
    String param_Longitude = "Longitude";
    String param_VisitNumber = "VisitNumber";
    String param_Load = "Load"; //SEA, SDA, LSD
    String Load_Seat_Available = "SEA";
    String Load_Standing_Available = "SDA";
    String Load_Limited_Standing = "LSD";
    String param_Feature = "Feature"; //if wab
    String param_Type = "Type"; //SD (single), DD (double) or BD (bendy)
    String param_Type_SD = "SD";
    String param_Type_DD = "DD";
    String param_Type_BD = "BD";
    String param_NextBus = "NextBus";
    String param_NextBus2 = "NextBus2";
    String param_NextBus3 = "NextBus3";
    String SBST_Transport_operator_code = "SBST";
    String SMRT_Transport_operator_code = "SMRT";
    String TTS_Transport_operator_code = "TTS";
    String GAS_Transport_operator_code = "GAS";
    String param_wab = "WAB";

    public BusTimingBaseAdapter(Context context, JSONArray bus_timing_array) {
        mcontext = context;
        mInflater = LayoutInflater.from(context);
        mBus_Timing_JSONArray = bus_timing_array;
    }

    @Override
    public int getCount() {
        return mBus_Timing_JSONArray.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return mBus_Timing_JSONArray.get(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public long getItemId(int i) {
        long Id = 0;
        try {
            JSONObject jObject = mBus_Timing_JSONArray.getJSONObject(i);
            Id = jObject.getLong(param_ServiceNo);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
        return Id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder view_item;
        if (view == null) {
            view = mInflater.inflate(R.layout.bus_timing_list_item, null);
            view_item = new ViewHolder();
            view_item.tv_busserviceno = (TextView) view.findViewById(R.id.tv_bustiming_serviceno);
            view_item.tv_nextbustiming = (TextView) view.findViewById(R.id.tvbustiming_nextbus_timing);
            view_item.tv_subbustiming = (TextView) view.findViewById(R.id.tvbustiming_subsequentbus_timing);
            view_item.tv_sub3bustiming = (TextView) view.findViewById(R.id.tvbustiming_subsequentbus3_timing);
            view_item.iv_nextbuswab = (ImageView) view.findViewById(R.id.iv_bustiming_nextbus_wab);
            view_item.iv_subbuswab = (ImageView) view.findViewById(R.id.iv_bustiming_subsequentbus_wab);
            view_item.iv_subbus3wab = (ImageView) view.findViewById(R.id.iv_bustiming_subsequentbus3_wab);
            view_item.iv_nextbusload = (ImageView) view.findViewById(R.id.iv_bustiming_nextbus_load);
            view_item.iv_subbusload = (ImageView) view.findViewById(R.id.iv_bustiming_subsequentbus_load);
            view_item.iv_subbus3load = (ImageView) view.findViewById(R.id.iv_bustiming_subsequentbus3_load);
            view_item.timingcontainer = (LinearLayout) view.findViewById(R.id.lly_bustiming_container);
            view_item.sub3_timingcontainer = (LinearLayout) view.findViewById(R.id.lly_bustiming_SubBus3_container);
            view_item.sub_timingcontainer = (LinearLayout) view.findViewById(R.id.lly_bustiming_SubBus_container);
            view_item.next_timingcontainer = (LinearLayout) view.findViewById(R.id.lly_bustiming_NextBus_container);
            view_item.position = i;

            view.setTag(view_item);
        } else {
            view_item = (ViewHolder) view.getTag();

        }

        try {
            JSONObject currentBusService = mBus_Timing_JSONArray.getJSONObject(i);
            if (currentBusService != null) {
                view_item.tv_busserviceno.setText(currentBusService.getString(param_ServiceNo));

                view_item.tv_busserviceno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                JSONObject NextBus = currentBusService.getJSONObject(param_NextBus);
                String BusCode = NextBus.getString(param_DestinationCode);

                //if (NextBus.getString(param_EstimatedArrival) != null) {
                JSONObject NextBus2 = currentBusService.getJSONObject(param_NextBus2);
                JSONObject NextBus3 = currentBusService.getJSONObject(param_NextBus3);

                //display bus load and bus type
                setbusTypeandLoad(view_item.iv_nextbusload, NextBus.getString(param_Load), NextBus.getString(param_Type));
                setbusTypeandLoad(view_item.iv_subbusload, NextBus2.getString(param_Load), NextBus2.getString(param_Type));
                setbusTypeandLoad(view_item.iv_subbus3load, NextBus3.getString(param_Load), NextBus3.getString(param_Type));

                //display if bus is WAB
                setWAB(view_item.iv_nextbuswab, NextBus.getString(param_Feature));
                setWAB(view_item.iv_subbuswab, NextBus2.getString(param_Feature));
                setWAB(view_item.iv_subbus3wab, NextBus3.getString(param_Feature));

                //if est arrival "" or null, display 'no est available'
                String NextBusEstArrival = NextBus.getString(param_EstimatedArrival);
                String SubBusEstArrival = NextBus2.getString(param_EstimatedArrival);
                String SubBus3EstArrival = NextBus3.getString(param_EstimatedArrival);

                if (NextBusEstArrival != "" || NextBusEstArrival != null) {
                    setbusTiming(view_item.tv_nextbustiming, NextBusEstArrival);
                } else {
                    removeContainerItem(view_item.next_timingcontainer, viewGroup, "No Est");
                }

                if (SubBusEstArrival != "" || SubBusEstArrival != null) {
                    setbusTiming(view_item.tv_subbustiming, SubBusEstArrival);
                } else {
                    removeContainerItem(view_item.sub_timingcontainer, viewGroup, "No Est");
                }

                if (SubBus3EstArrival != "" || SubBus3EstArrival != null) {
                    setbusTiming(view_item.tv_sub3bustiming, SubBus3EstArrival);
                } else {
                    removeContainerItem(view_item.sub3_timingcontainer, viewGroup, "No Est");
                }


                //} else {
                //    removeContainerItem(view_item.timingcontainer, viewGroup, "Not operating now");


                //}
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }


        return view;
    }



    public void setWAB(ImageView imageview, String Feature) {
        if (Feature.equals(param_wab)) {
            imageview.setImageResource(R.drawable.wab);
        }
    }

    public void setbusTypeandLoad(ImageView imageview, String Occupancy, String Type) {
        if (imageview != null && Occupancy != "" && Type != "") {

            if (Occupancy.equals(Load_Seat_Available)) {
                imageview.setImageResource(R.drawable.green);
            } else if (Occupancy.equals(Load_Standing_Available)) {
                imageview.setImageResource(R.drawable.orange);
            } else if (Occupancy.equals(Load_Limited_Standing)) {
                imageview.setImageResource(R.drawable.red);
            }
        }
    }

    public void setbusTiming(TextView textviewtiming, String EstArrival) {
        if (textviewtiming != null && EstArrival != "") {
            //String Mockdate = "2017-06-27T02:47:19+08:00";

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
            SimpleDateFormat SystemdateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

            try {
                Date convertedDate = dateFormat.parse(EstArrival);
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+08:00"));
                //get standard time
                Calendar cal = Calendar.getInstance();
                Date currently = cal.getTime();
                String date = currently.toString();
                Date systemDate = SystemdateFormat.parse(date);
                String Str_systemDate = dateFormat.format(systemDate);
                systemDate = dateFormat.parse(Str_systemDate);

                //get diff in date
                Long arrival_inMilSec = convertedDate.getTime() - systemDate.getTime();
                Double arrival_inMin = (arrival_inMilSec / 60000.00);
                if (arrival_inMin < 1) {
                    textviewtiming.setText("Arr");
                } else {
                    int Arrival = Integer.valueOf(arrival_inMin.intValue());
                    textviewtiming.setText(String.valueOf(Arrival) + "mins");
                }

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {

        }
    }

    public String getDestinationNameByCode(String DestinationCode) {
        BusStop bs = new BusStop(mcontext);
        return bs.getDestinationNameByCode(DestinationCode);
    }

    public void removeContainerItem(LinearLayout llyContainer, ViewGroup viewgroup, String Display_Message) {
        //remove all child of lly
        int number_of_child_to_remove = llyContainer.getChildCount();
        for (int ii = 0; ii <= number_of_child_to_remove; ii++) {
            llyContainer.removeView(llyContainer.getChildAt(ii));
        }
        //generate not in service display
        TextView tv_not_in_operation = new TextView(mcontext);
        tv_not_in_operation.setText(Display_Message);
        tv_not_in_operation.setGravity(Gravity.CENTER);
        tv_not_in_operation.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        llyContainer.addView(tv_not_in_operation);
    }

    static class ViewHolder {
        TextView tv_busserviceno;
        TextView tv_nextbustiming;
        TextView tv_subbustiming;
        TextView tv_sub3bustiming;
        ImageView iv_nextbuswab;
        ImageView iv_subbuswab;
        ImageView iv_subbus3wab;
        ImageView iv_nextbusload;
        ImageView iv_subbusload;
        ImageView iv_subbus3load;
        LinearLayout timingcontainer;
        LinearLayout sub3_timingcontainer;
        LinearLayout sub_timingcontainer;
        LinearLayout next_timingcontainer;
        int position;
    }

}
