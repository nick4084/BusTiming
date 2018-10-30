package ntu.bustiming;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * this class is adapter for bus stop search
 */
public class BusStopSearchBaseAdapter extends BaseAdapter {
    Context mcontext;
    JSONArray mBus_search_JSONArray;
    String Param_Lat="Latitude";
    String Param_Lon="Longitude";
    String Param_busstop_code="BusStopCode";
    String Param_roadname="RoadName";
    String Param_description="Description";
    LayoutInflater mInflater;


    /** constructor for the base adapter
     * @param context application context
     * @param bus_search_array json array of bus stop
     */
    public BusStopSearchBaseAdapter(Context context, JSONArray bus_search_array) {
        mcontext = context;
        mBus_search_JSONArray = bus_search_array;
        mInflater = LayoutInflater.from(context);
    }

    /** get the size of the list
     * @return int length of bus stop
     */
    @Override
    public int getCount() {
        return mBus_search_JSONArray.length();
    }

    /** get object based on its position
     * @param position integer position of the object
     * @return object of the specified row
     */
    @Override
    public Object getItem(int position) {
        try {
            return mBus_search_JSONArray.get(position);
        } catch (JSONException e) {
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
            JSONObject jObject = mBus_search_JSONArray.getJSONObject(position);
            Id = jObject.getLong(Param_busstop_code);
        } catch (JSONException e) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = mInflater.inflate(R.layout.bus_search_list_item, null);
        TextView Textview1 = (TextView) vi.findViewById(R.id.firstLine);
        TextView Textview2 = (TextView) vi.findViewById(R.id.secondLine);
        TextView Textview3 = (TextView) vi.findViewById(R.id.thirdLine);

        try {
            Textview1.setText(mBus_search_JSONArray.getJSONObject(position).getString(Param_busstop_code));
            Textview1.setText(mBus_search_JSONArray.getJSONObject(position).getString(Param_description));
            Textview1.setText(mBus_search_JSONArray.getJSONObject(position).getString(Param_roadname));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return vi;

    }
}
