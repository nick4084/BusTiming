package ntu.bustiming.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import ntu.bustiming.R;

import ntu.bustiming.entity.*;
import ntu.bustiming.R;

/**
 * This class is the base adapter that populate the bus stop list view of notification search
 * It is implemented as a singleton to ensure only one instance is created
 */
public class NS_BSBaseAdapter extends BaseAdapter{
    private ArrayList<SimplifiedBusStop> bsList;
    private ArrayList<SimplifiedBusStop> fullBsList;
    private Context context;
    private static NS_BSBaseAdapter instance = null;

    /**
     * This is the inner class of NS_BSBaseAdapter
     * It contains the data needed for every entry in the list view
     */
    private static class ViewHolder{
        int position;
        TextView busStopName;
    }

    /**
     * This method is the initialisation method that calls the private constructor
     * @param context The context of the app
     */
    public static void init(Context context){
        if(instance==null){
            instance = new NS_BSBaseAdapter(context);
        }
    }

    /**
     * This method will display the full list of the bus stop
     */
    public void refreshList(){
        bsList = fullBsList;
    }

    /**
     * This method will return the instance of this class
     * @return The instance of this class
     */
    public static NS_BSBaseAdapter getInstance(){
        return instance;
    }

    private NS_BSBaseAdapter(Context context) {
        this.context = context;
        bsList = new ArrayList<>();
        fullBsList = new ArrayList<>();
        LTADatamallController lta = new LTADatamallController(context);
        BusStops busstops = new BusStops(context);

        //JSONObject busstops = lta.getBusStops(0);
        try{
            //JSONArray busstopList = busstops.getJSONArray("value");
            JSONArray busstopList = busstops.readBusStopfile();
            //for(int n=1;busstopList.length()!=0;n++){
                for (int i = 0; i < busstopList.length(); i++) {
                    JSONObject jsonobject = busstopList.getJSONObject(i);
                    String bsCode = jsonobject.getString("BusStopCode");
                    //if(lta.getBusArrivalByBusStopCode(bsCode).getJSONArray("Services").length()==0){
                    //    continue;
                    //}
                    String roadName = jsonobject.getString("RoadName");
                    String description = jsonobject.getString("Description");
                    SimplifiedBusStop bs = new SimplifiedBusStop(bsCode,roadName,description);
                    fullBsList.add(bs);
                }
                //busstops = lta.getBusStops(n*500);
                //busstopList = busstops.getJSONArray("value");
            //}


        }catch(Exception e){
            e.printStackTrace();
        }

        bsList = fullBsList;
        Collections.sort(bsList);
        notifyDataSetChanged();
    }

    /**
     * This method will return an item from the list
     * @param i The position of the item the list
     * @return The item that user requested
     */
    public SimplifiedBusStop findItem(int i){
        return bsList.get(i);
    }

    /**
     * This method will search for the bus stops that match the search term and display it in the list view
     * @param sTerm The search term
     */
    public void search(String sTerm){
        bsList = new ArrayList<>();
        if(sTerm.equals("")){
            bsList=fullBsList;
            notifyDataSetChanged();
            return;
        }
        for(SimplifiedBusStop s : fullBsList){
            if(s.getDescription().toLowerCase().contains(sTerm.toLowerCase())){
                bsList.add(s);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return bsList.size();
    }

    @Override
    public Object getItem(int i) {
        return bsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        SimplifiedBusStop s = bsList.get(i);
        ViewHolder viewHolder;
        View result;
        if(view==null){
            viewHolder = new NS_BSBaseAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.route_search_viewholder, null);
            viewHolder.position=i;
            viewHolder.busStopName = view.findViewById(R.id.searchItem);
            result=view;
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
            result = view;
        }

        final int position = i;

        viewHolder.busStopName.setText(s.getBusStopCode()+" - "+s.getDescription());
        return view;
    }
}
