package ntu.bustiming;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FavouriteItemMenuDialog extends Dialog implements DialogInterface.OnClickListener{
    Context mcontext;
    String name;
    int code;
    ListView lv_fav;
    BaseAdapter mAdapter;
    OnDialogClickListener listener;
    public interface OnDialogClickListener {
        void onSaveClicked();
    }
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    public FavouriteItemMenuDialog(@NonNull Context context, int code, String name, OnDialogClickListener listener) {
        super(context);
        mcontext = context;
        this.code = code;
        this.name = name;
        this.listener = listener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.favourite_item_menu_dialog);

        final EditText pname = findViewById(R.id.et_favourite_edit_pname);
        pname.setText(name);
        Button cancel = findViewById(R.id.btnFavouriteMenuCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });
        Button save = findViewById(R.id.btnFavouriteMenuSavebtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_pname = pname.getText().toString();
                FavoritePersistentData dataset = new FavoritePersistentData(mcontext);
                dataset.editFavoritePersonalisedNameByCode(code, new_pname);
                JSONArray new_fav_list = dataset.fetchFavoriteBusStop();
                listener.onSaveClicked();
                dismiss();
            }
        });



    }
}
