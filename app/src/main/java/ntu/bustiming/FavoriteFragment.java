package ntu.bustiming;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class defines the control logic of Favourite bus stop
 */
public class FavoriteFragment extends ListFragment implements AdapterView.OnItemClickListener {
    FavoritesBaseAdapter adapter;
    JSONArray liked_busstop;
    FavoriteDataController data;
    Activity mainAct;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
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
     * @return A new instance of fragment FavoriteFragment.
     */
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Default Constructor
     */
    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * called during the onCreateActivity
     * Initialise the listview
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        data = new FavoriteDataController(getActivity());
        liked_busstop = data.getLikedBusstop();
        adapter = new FavoritesBaseAdapter(liked_busstop, getActivity());
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        TextView tv_empty_text = getView().findViewById(R.id.favorite_empty_list_item);
        getListView().setEmptyView(tv_empty_text);


    }

    /**
     * Override method.
     * called when a row is selected from the listview
     * @param l listview object
     * @param v view of row
     * @param position index of the row
     * @param id id of row
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        LTADatamallController LTADatamallController = new LTADatamallController(this.getActivity());
        TextView code = v.findViewById(R.id.tv_favorite_hidden_code);
        String bs_code = code.getText().toString();
        JSONObject bus_arrival_timing = LTADatamallController.getBusArrivalByBusStopCode(bs_code);
        if(bus_arrival_timing!=null){
            //display bus timing
            try {
                JSONObject current = (JSONObject) getListView().getAdapter().getItem(position);
                String bs_desc=current.get(FavoriteBusStopStruct.PARAM_DESC).toString();
                double lat = Double.parseDouble(current.get(FavoriteBusStopStruct.PARAM_LAT).toString());
                double lng = Double.parseDouble(current.get(FavoriteBusStopStruct.PARAM_LNG).toString());
                String road = current.get(FavoriteBusStopStruct.PARAM_RD).toString();
                displayBusTiming(bus_arrival_timing, bs_code, bs_desc, lat, lng, road);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * called when this class is created
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * This class define the UI to inflate
     * @param inflater inflater object
     * @param container container to inflate
     * @param savedInstanceState saved state
     * @return return the UI
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.favorite_fragment, container, false);
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

    /**
     * This method must be Implemented
     * Handles on click event of listview selection
     * @param adapterView adapter of the listview
     * @param view UI of the row
     * @param i index of the selected row
     * @param l id of the selected row
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        LTADatamallController LTADatamallController = new LTADatamallController(this.getActivity());
        TextView code = view.findViewById(R.id.tv_favorite_hidden_code);
        String bs_code = code.getText().toString();
        JSONObject bus_arrival_timing = LTADatamallController.getBusArrivalByBusStopCode(bs_code);
        if(bus_arrival_timing!=null){
            //display bus timing
            try {
                JSONObject current = (JSONObject) adapterView.getItemAtPosition(i);
                String bs_desc=current.get(FavoriteBusStopStruct.PARAM_DESC).toString();
                double lat = Double.parseDouble(current.get(FavoriteBusStopStruct.PARAM_LAT).toString());
                double lng = Double.parseDouble(current.get(FavoriteBusStopStruct.PARAM_LNG).toString());
                String road = current.get(FavoriteBusStopStruct.PARAM_RD).toString();
                displayBusTiming(bus_arrival_timing, bs_code, bs_desc, lat, lng, road);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Displays the bus timing dialog
     * @param busTiming
     * @param BusStopCode
     * @param BusStopDescription
     * @param lat
     * @param lng
     * @param road
     */
    public void displayBusTiming(JSONObject busTiming, String BusStopCode, String BusStopDescription, double lat, double lng, String road){
        //display bus timing dialog pop up
        road = ((MainActivity)getActivity()).getBusStopRdByCode(BusStopCode);
        BusTimingDialog TimingDialog = new BusTimingDialog(getActivity(), busTiming, BusStopCode, BusStopDescription, lat, lng, road, new BusTimingDialog.OnDialogClickListener(){
            /**
             * This method must be Implemented
             * It will notify and call this method when there is changes to favorite bus stops
             */
            @Override
            public void notifyFavoriteDataChange() {
                refreshData();
                //notifyFavoriteDataChange();
            }
        });
        TimingDialog.show();
    }

    /**
     * Refresh the listview with updated data
     */
    public void refreshData(){
        if(getActivity()!= null) {
            data = new FavoriteDataController(getActivity());
            liked_busstop = data.getLikedBusstop();
            adapter = new FavoritesBaseAdapter(liked_busstop, getActivity());
            setListAdapter(adapter);
            getListView().setOnItemClickListener(this);
        }
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
