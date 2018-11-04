package ntu.bustiming.control;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import ntu.bustiming.R;

public class NotificationSearchDialog extends Dialog{
    Context context;
    LTADatamallController lta;
    private ListView listView;
    BaseAdapter adapter;
    OnMyDialogResult mDialogResult;

    public NotificationSearchDialog(@NonNull Context context,BaseAdapter adapter) {
        super(context);
        this.context = context;
        this.adapter = adapter;
        //lta = new LTADatamallController(getContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View dialogView = getLayoutInflater().inflate(R.layout.route_search,null);
        setContentView(dialogView);
        ListView lv = dialogView.findViewById(R.id.ntfSearchList);
        //final NS_BSBaseAdapter nsbsadapter = NS_BSBaseAdapter.getInstance();
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (mDialogResult != null) {
                            if(adapter instanceof NS_BSBaseAdapter){
                                SimplifiedBusStop sbs = ((NS_BSBaseAdapter)adapter).findItem(i);
                                mDialogResult.finish(sbs.getBusStopCode()+" - "+sbs.getDescription());
                            }else if(adapter instanceof  NS_BusBaseAdapter){
                                mDialogResult.finish(((NS_BusBaseAdapter)adapter).findItem(i));
                            }
                        }
                        dismiss();
                    }
                }
        );

        final EditText searchTxt = dialogView.findViewById(R.id.ntfSearchTxt);
        Button searchBtn = dialogView.findViewById(R.id.ntfSearchButton);
        searchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(adapter instanceof NS_BSBaseAdapter){
                    ((NS_BSBaseAdapter)adapter).search(searchTxt.getText().toString());
                }else if(adapter instanceof  NS_BusBaseAdapter){
                    ((NS_BusBaseAdapter)adapter).search(searchTxt.getText().toString());
                }

            }
        });



        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.route_search);
        //JSONObject busServices = lta.getBusServices();

        //listView = (ListView) findViewById(R.id.ntfSearchList);
        //NS_BSBaseAdapter nsbsadapter = NS_BSBaseAdapter.getInstance();
        //listView.setAdapter(nsbsadapter);


    }
    public void setDialogResult(OnMyDialogResult dialogResult) {
        mDialogResult = dialogResult;
    }

    /**
     * This interface is needed to implement the Observer pattern
     */
    public interface OnMyDialogResult {
        void finish(String result);
    }
}
