package ntu.bustiming.control;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import ntu.bustiming.entity.Notification;
import ntu.bustiming.R;

public class RouteFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<Notification> routeList;
    private ListView listView;
    private static NotificationBaseAdapter notificationBaseAdapter;
    private static NotificationPersistentData notificationPersistentData;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RouteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RouteFragment newInstance(String param1, String param2) {
        RouteFragment fragment = new RouteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RouteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.route_fragment, container, false);
        Button addNewBtn = view.findViewById(R.id.addNewNotificationBtn);
        addNewBtn.setOnClickListener(this);

        NS_BSBaseAdapter.init(getContext());
        NS_BusBaseAdapter.init(getContext());

        listView = view.findViewById(R.id.notifyList);
        NotificationPersistentData.init(getContext());
        notificationPersistentData = NotificationPersistentData.getInstance();
        routeList = notificationPersistentData.getEntryList();
        NotificationBaseAdapter.init(routeList, getContext());
        notificationBaseAdapter = NotificationBaseAdapter.getInstance();

        listView.setAdapter(notificationBaseAdapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Notification ntf = routeList.get(i);
                        NotificationDisplayDialog displayDialog = new NotificationDisplayDialog(getContext(),routeList.get(i),i);
                        displayDialog.show();
                    }
                }
        );
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onBookmarkFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        NotificationEditDialog editDialog = new NotificationEditDialog(getContext());
        editDialog.show();
        editDialog.setDialogResult(new NotificationEditDialog.OnMyDialogResult(){
            public void finish(Notification ntf){
                routeList.add(ntf);
                notificationBaseAdapter.notifyDataSetChanged();
            }
        });
        NotificationService ns = new NotificationService(getContext());
        ns.sendNotification();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onBookmarkFragmentInteraction(Uri uri);
    }
}
