package ntu.bustiming.control;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.BitSet;

import ntu.bustiming.entity.*;
import ntu.bustiming.R;

/**
 * This class is the edit dialog of the notification
 */
public class NotificationEditDialog extends Dialog {
    OnMyDialogResult mDialogResult;
    EditText nameTxt;
    EditText busstopNameTxt;
    EditText busnumberNameTxt;
    TimePicker ntfTime;
    ToggleButton ntfDays[];
    Notification ntf;
    Boolean isEditMode;
    Context context;
    int position;

    /**
     * This is the constructor of NotificationEditDialog
     * @param context The context of the app
     */
    public NotificationEditDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        isEditMode = false;
    }

    /** This is the constructor of NotificationEditDialog
     *
     * @param context The context of the app
     * @param ntf The notification object
     * @param position The index number of the object within the list
     */
    public NotificationEditDialog(@NonNull Context context, Notification ntf, int position) {
        super(context);
        this.context = context;
        this.position = position;
        this.ntf=ntf;
        isEditMode = true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.route_edit);
        Button saveBtn = findViewById(R.id.editSaveBtn);

        nameTxt = (EditText) findViewById(R.id.routeNameTf);
        busstopNameTxt = (EditText) findViewById(R.id.busstopNameTf);
        busnumberNameTxt = (EditText) findViewById(R.id.busnumberNameTf);
        ntfTime = (TimePicker) findViewById(R.id.timePicker);
        ntfTime.setIs24HourView(true);
        ntfDays = new ToggleButton[7];
        for (int i = 0; i < 7; i++) {
            int resID = getContext().getResources().getIdentifier("edit_day" + i + "_tb", "id", getContext().getPackageName());
            ntfDays[i] = (ToggleButton) findViewById(resID);
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        if(busstopNameTxt.getText().toString().equals(""))busnumberNameTxt.setEnabled(false);
        busstopNameTxt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                NS_BSBaseAdapter.getInstance().refreshList();
                NotificationSearchDialog searchDialog = new NotificationSearchDialog(getContext(), NS_BSBaseAdapter.getInstance());
                searchDialog.setDialogResult(new NotificationSearchDialog.OnMyDialogResult() {
                    @Override
                    public void finish(String result) {
                        busstopNameTxt.setText(result);
                        busnumberNameTxt.setText(""); //when busStop is set, bus number cleared
                        busnumberNameTxt.setEnabled(true);
                        //TODO: get the bus code and run the query
                        String bsCode = result.substring(0,5);
                        LTADatamallController lta = new LTADatamallController(context);
                        ArrayList<String> tmpBusList = new ArrayList<>();
                        try{
                            //TODO: filter the buses out
                            JSONObject jsonObject = lta.getBusArrivalByBusStopCode(bsCode);
                            JSONArray jsonArray = jsonObject.getJSONArray("Services");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject bus = jsonArray.getJSONObject(i);
                                String tmpBus = bus.getString("ServiceNo");
                                tmpBusList.add(tmpBus);
                            }

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        //TODO: Set adapter for bus
                        NS_BusBaseAdapter.getInstance().setList(tmpBusList);
                    }
                });
                searchDialog.show();
            }
        });

        busnumberNameTxt.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                NotificationSearchDialog searchDialog = new NotificationSearchDialog(getContext(),NS_BusBaseAdapter.getInstance());
                searchDialog.setDialogResult(new NotificationSearchDialog.OnMyDialogResult() {
                    @Override
                    public void finish(String result) {
                        busnumberNameTxt.setText(result);
                    }
                });
                searchDialog.show();

            }
        });
        if(ntf!=null){
            nameTxt.setText(ntf.getName());
            busstopNameTxt.setText(""+ntf.getBusstop_code());
            busnumberNameTxt.setText(""+ntf.getBus_code());
            ntfTime.setHour(ntf.getNtf_hour());
            ntfTime.setMinute(ntf.getNtf_minute());
            for(int i=0;i<7;i++){
                if(ntf.getNtf_days().get(i)){
                    ntfDays[i].setChecked(true);
                }
            }
        }

    }



    private void save() {
        Notification ntf = new Notification();
        if (TextUtils.isEmpty(nameTxt.getText().toString())) {
            nameTxt.setError("Name cannot be null");
            return;
        }
        ntf.setName(nameTxt.getText().toString());
        if (TextUtils.isEmpty(busstopNameTxt.getText().toString())) {
            busstopNameTxt.setError("Bus stop cannot be null");
            return;
        }
        ntf.setBusstop_code(busstopNameTxt.getText().toString());
        if (TextUtils.isEmpty(busnumberNameTxt.getText().toString())) {
            busstopNameTxt.setError("Bus number cannot be null");
            return;
        }
        ntf.setBus_code(busnumberNameTxt.getText().toString());
        ntf.setNtf_hour(ntfTime.getHour());
        ntf.setNtf_minute(ntfTime.getMinute());
        ntf.setActivated(true);
        BitSet days = new BitSet(7);
        for (int i = 0; i < 7; i++) {
            if (ntfDays[i].isChecked()) {
                days.set(i);
            }
        }
        ntf.setNtf_days(days);
        if (mDialogResult != null) {
            mDialogResult.finish(ntf);
        }
        dismiss();
    }

    /**
     * This method is used to implement the Observer pattern
     * It allows user to create an instance of OnMyDialogResult and set it
     * @param dialogResult The class that implements OnMyDialogResult interface
     */
    public void setDialogResult(OnMyDialogResult dialogResult) {
        mDialogResult = dialogResult;
    }

    /**
     * This interface is needed to implement the Observer pattern
     */
    public interface OnMyDialogResult {
        void finish(Notification ntf);
    }

}
