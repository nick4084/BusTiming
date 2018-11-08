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

import ntu.bustiming.control.LTADatamallController;
import ntu.bustiming.entity.*;
import ntu.bustiming.R;

public class NS_BusBaseAdapter extends BaseAdapter{
    private ArrayList<String> fullBusList;
    private ArrayList<String> busList;

    private Context context;
    private static NS_BusBaseAdapter instance = null;

    private static class ViewHolder{
        int position;
        TextView busName;
    }

    public static void init(Context context){
        if(instance==null){
            instance = new NS_BusBaseAdapter(context);
        }
    }

    public static NS_BusBaseAdapter getInstance(){
        return instance;
    }

    public NS_BusBaseAdapter(Context context) {
        this.context = context;
        busList = new ArrayList<>();
        fullBusList = new ArrayList<>();

        LTADatamallController lta = new LTADatamallController(context);
        try{
            JSONObject jsonObject = lta.getBusServices();
            JSONArray jsonArray = jsonObject.getJSONArray("value");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject bus = jsonArray.getJSONObject(i);
                String busCode = bus.getString("ServiceNo");
                fullBusList.add(busCode);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        busList = fullBusList;
        Collections.sort(busList);
        notifyDataSetChanged();
    }

    public void setList(ArrayList<String> newList){
        busList = newList;
        notifyDataSetChanged();
    }

    public String findItem(int i){
        return busList.get(i);
    }

    public void search(String sTerm){
        busList = new ArrayList<>();
        if(sTerm.equals("")){
            busList = fullBusList;
            notifyDataSetChanged();
            return;
        }
        for(String s:fullBusList){
            if(s.toLowerCase().contains(sTerm.toLowerCase())){
                busList.add(s);
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return busList.size();
    }

    @Override
    public Object getItem(int i) {
        return busList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String s = busList.get(i);
        ViewHolder viewHolder;
        View result;

        if(view==null){
            viewHolder = new NS_BusBaseAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.route_search_viewholder, null);
            viewHolder.position=i;
            viewHolder.busName = view.findViewById(R.id.searchItem);
            result=view;
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
            result = view;
        }
        final int position = i;
        viewHolder.busName.setText(s);

        return view;
    }
}
