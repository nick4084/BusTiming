package ntu.bustiming.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ntu.bustiming.R;

/**
 * this class is adapter for bus stop search
 */
public class BusStopSearchBaseAdapter extends BaseAdapter {
    Context mcontext;
    static String Param_serviceno="ServiceNo";
    static String Param_direction="Direction";
    static String Param_stopsequence="StopSequence";
    static String Param_busstopcode="BusStopCode";
    static String Param_distance="Distance";
    LayoutInflater mInflater;
    ArrayList<SimplifiedBus> simplifiedBuses ;


    /** constructor for the base adapter
     * @param context application context
     * @param bus_search_array json array of bus stop
     */
    public BusStopSearchBaseAdapter(Context context, ArrayList<SimplifiedBus>  busArray ) {
        mcontext = context;
        mInflater = LayoutInflater.from(context);
        simplifiedBuses = busArray;
    }

    /** get the size of the list
     * @return int length of bus stop
     */
    @Override
    public int getCount() {
        return simplifiedBuses.size();
    }

    /** get object based on its position
     * @param position integer position of the object
     * @return object of the specified row
     */
    @Override
    public SimplifiedBus getItem(int position) {
        try {
            return simplifiedBuses.get(position);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }


    }
    /** return item id of the row
     * @param position integer of the row
     * @return ID
     */
    @Override
    public long getItemId(int position) {
        long Id = 0;
        try {
            Id = (long)Integer.parseInt(simplifiedBuses.get(position).getServiceNo());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return Id;
    }

    /** set the row of UI based on number of data given
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SimplifiedBus bus = simplifiedBuses.get(i);
        final ViewHolder viewHolder;
        final View result;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mcontext);
            view = inflater.inflate(R.layout.bus_search_list_item, null);
            viewHolder.position = i;
            viewHolder.busStopCode = view.findViewById(R.id.text_view_item_name);
            viewHolder.busStopDescription = view.findViewById(R.id.text_view_item_description);
            result = view;
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            result = view;
        }

        final int position = i;
        viewHolder.busStopCode.setText(bus.getBusStopCode());
        viewHolder.busStopDescription.setText(bus.getRoadName() + " " + bus.getDirection());






        return view;

    }

    private static class ViewHolder {
        int position;
        TextView busStopCode;
        TextView busStopDescription;
    }
}
