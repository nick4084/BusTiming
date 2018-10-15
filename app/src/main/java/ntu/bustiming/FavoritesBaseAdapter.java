package ntu.bustiming;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.zip.Inflater;

public class FavoritesBaseAdapter extends BaseAdapter {
    private Context mContext;
    private JSONArray Busstop_list;
    private LayoutInflater mInflater;
    ListView lv_parent;
    FavoritesBaseAdapter mAdapter;
    FavoritePersistentData data;
    FavoriteBusStopStruct f_struct;
    public FavoritesBaseAdapter(JSONArray busstop_list, Context context){
        this.mContext = context;
        this.Busstop_list = busstop_list;
        this.mInflater = LayoutInflater.from(context);
        this.lv_parent = lv_parent;
        this.mAdapter = mAdapter;
    }
    @Override
    public int getCount() {
        if(Busstop_list != null) {
            return Busstop_list.length();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        try {
            return Busstop_list.getJSONObject(i);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        try {
            FavoriteBusStopStruct item = new FavoriteBusStopStruct(Busstop_list.getJSONObject(i));
            return Long.parseLong(Integer.toString(item.getBusstop_code()));
        } catch(JSONException e){
            e.printStackTrace();
            return 0;
        }
    }

    public JSONArray getData(){
        return Busstop_list;
    }

    public void removeDataBypos(int i) {
            Busstop_list.remove(i);
    }

    public void refresh(){
            data = new FavoritePersistentData(mContext);
            Busstop_list = data.getLikedBusstop();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder view_item;
        if (view == null) {
            view = mInflater.inflate(R.layout.favorite_busstop_item, null);
            view_item = new ViewHolder();
            view_item.position = i;
            view_item.tv_favorite_busstop_name = view.findViewById(R.id.tv_busstop_name);
            view_item.ib_favorite_busstop_menu = view.findViewById(R.id.ib_fav_busstop_menu);
            view_item.tv_hidden_code = view.findViewById(R.id.tv_favorite_hidden_code);
            view_item.ib_favourite_busstop_delete = view.findViewById(R.id.ib_fav_busstop_delete);
            view_item.tv_favourite_busstop_road = view.findViewById(R.id.tv_busstop_road);

            view.setTag(view_item);
        } else {
            view_item = (ViewHolder) view.getTag();

        }
        try{
            JSONObject current_busstop = Busstop_list.getJSONObject(i);
            f_struct = new FavoriteBusStopStruct(current_busstop);
            //final int code = current_busstop.getInt(FavoriteBusStopStruct.PARAM_CODE);
            //final String bs_name = current_busstop.get(FavoriteBusStopStruct.PARAM_PNAME).toString();
            //final String bus_stop_road = current_busstop.get(FavoriteBusStopStruct.PARAM_RD).toString();
            final int code = f_struct.getBusstop_code();
            final String bs_name = f_struct.getBusstop_personalised_name();
            final String bus_stop_road = f_struct.getBusstop_rd();
            view_item.tv_favorite_busstop_name.setText(bs_name);
            view_item.tv_hidden_code.setText(Integer.toString(code));
            view_item.tv_favourite_busstop_road.setText(bus_stop_road);

            //on click event of menu button
            view_item.ib_favorite_busstop_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   TextView tv_code = view.getRootView().findViewById(R.id.tv_favorite_hidden_code);
                   TextView tv_name = view.getRootView().findViewById(R.id.tv_busstop_name);
                    //int code = Integer.parseInt(tv_code.getText().toString());
                    //String pname = tv_name.getText().toString();
                    FavouriteItemMenuDialog EditDialog = new FavouriteItemMenuDialog(mContext, code, bs_name, new FavouriteItemMenuDialog.OnDialogClickListener() {
                        @Override
                        public void onSaveClicked() {
                            refresh();
                            notifyDataSetChanged();
                        }
                    });
                    EditDialog.show();
                    //pull new data and refresh listview
                    refresh();
                    notifyDataSetChanged();
                }
            });

            view_item.ib_favourite_busstop_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FavoritePersistentData data = new FavoritePersistentData(mContext);
                    data.deleteFavoriteByCode(code);
                    removeDataBypos(view_item.position);
                    refresh();
                    notifyDataSetChanged();
                }
            });
        } catch(JSONException e){
            e.printStackTrace();
        }

        return view;
    }

    static class ViewHolder{
        int position;
        TextView tv_favorite_busstop_name;
        TextView tv_hidden_code;
        TextView tv_favourite_busstop_road;
        ImageView ib_favorite_busstop_menu;
        ImageView ib_favourite_busstop_delete;


    }
}
