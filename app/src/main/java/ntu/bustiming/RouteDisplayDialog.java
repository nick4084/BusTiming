package ntu.bustiming;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class RouteDisplayDialog extends Dialog{
    Notification ntf;
    EditText routeNameTxt;
    EditText busStopTxt;
    EditText busNumberTxt;
    EditText routeTimeTxt;
    EditText routeDaysTxt;
    Button editBtn;
    int position;

    public RouteDisplayDialog(@NonNull Context context,Notification ntf, int position) {
        //TODO: Pass in the index number
        super(context);
        this.position=position;
        this.ntf = ntf;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.route_display);

        routeNameTxt=findViewById(R.id.displayRouteNameTxt);
        busStopTxt=findViewById(R.id.displayRouteBusstopTxt);
        busNumberTxt=findViewById(R.id.displayRouteBusnumberTxt);
        routeTimeTxt=findViewById(R.id.displayRouteTimeTxt);
        routeDaysTxt=findViewById(R.id.displayRouteDayTxt);
        editBtn=findViewById(R.id.displayEditBtn);


        routeNameTxt.setEnabled(false);
        busStopTxt.setEnabled(false);
        busNumberTxt.setEnabled(false);
        routeTimeTxt.setEnabled(false);
        routeDaysTxt.setEnabled(false);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Call the edit dialog
                //TODO: Pass in a ntf
                //TODO: Pass in a value

                RouteEditDialog editDialog = new RouteEditDialog(getContext(), ntf, position);
                editDialog.show();
                editDialog.setDialogResult(new RouteEditDialog.OnMyDialogResult(){
                    public void finish(Notification ntf){
                        //TODO: Use singleton here
                        RouteAdapter routeAdapter = RouteAdapter.getInstance();
                        routeAdapter.replaceItem(ntf,position);
                        routeAdapter.notifyDataSetChanged();
                        dismiss();
                    }
                });

            }
        });

        routeNameTxt.setText(ntf.getName());
        busStopTxt.setText(""+ntf.getBusstop_code());
        busNumberTxt.setText(""+ntf.getBus_code());
        routeTimeTxt.setText(ntf.displayTime());
        routeDaysTxt.setText(ntf.displayDays());
    }
}