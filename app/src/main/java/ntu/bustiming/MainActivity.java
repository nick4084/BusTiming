package ntu.bustiming;

import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static ntu.bustiming.BusStop.Param_busstop_code;
import static ntu.bustiming.BusStop.Param_description;
import static ntu.bustiming.BusStop.Param_roadname;

public class MainActivity extends AppCompatActivity implements FavoriteFragment.OnFragmentInteractionListener,
        RouteFragment.OnFragmentInteractionListener,
        NearbyFragment.OnFragmentInteractionListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    int SELECT_IMAGE=0;
    double Longitude=0.0;
    double Latitude=0.0;
    Boolean hasLocation= false;
    Uri selectedImage;
    Toolbar toolbar;
    ViewPager pager;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker;
    ViewPagerAdapter adapter;
    Location mLastLocation;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"Nearby", "Favorite", "Route"};
    int NumbOfTabs = 3;
    GoogleApiClient mGoogleApiClient;
    BusStop busStop_Class= null;
    private FusedLocationProviderClient mFusedLocationClient;
    GoogleMap gMap;
    ListView lv_favorites;
    JSONArray BusStop_list;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter =  new ViewPagerAdapter(getSupportFragmentManager(), Titles, NumbOfTabs, getApplicationContext());
        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        //check if permission to use location is granted by user
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        if (checkLocationPermission()) {
            //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, this);
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        hasLocation = true;
                        Bundle bundle = new Bundle();
                        bundle.putDouble("Lontitude", location.getLongitude());
                        bundle.putDouble("Latitude", location.getLatitude());
                        NearbyFragment nearbyFragment = new NearbyFragment();
                        nearbyFragment.setArguments(bundle);
                    }
                }
            });
        }
    setUpBusStopList();
    }

    public void setUpBusStopList(){
        BusStop bs_data = new BusStop(this);
        BusStop_list  = bs_data.readBusStopfile();
    }

    public String getBusStopRdByCode(String code){
        if(BusStop_list==null){
            setUpBusStopList();
        }
        JSONObject busstop;
        for (int i = 0; i<BusStop_list.length(); i++) {
            try{
                busstop = BusStop_list.getJSONObject(i);
                String current_code =  busstop.getString(Param_busstop_code);
                if(code.equals(current_code)){
                    return busstop.getString(Param_roadname);
                }
            } catch(JSONException e){
                e.printStackTrace();
            }

        }
        return "";
    }

    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("You");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = gMap.addMarker(markerOptions);

        //move map camera
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
        addBusStopToMap();
        mGoogleApiClient.disconnect();

    }

    public GoogleMap setUpMap(GoogleMap map){
        //Check permission.
        gMap = map;
        if(checkLocationPermission()){
            //Initialize Google Play Services
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    map.setMyLocationEnabled(true);

                }
            } else{
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
            }
        }

        return map;
    }

    public void addBusStopToMap(){
        if(busStop_Class==null){
            busStop_Class=new BusStop(this);
        }
        try{
            JSONArray busstopArray = busStop_Class.getBusStopByLocation(mLastLocation);
            if(busstopArray!=null) {
                for (int i = 0; i < busstopArray.length(); i++) {
                    JSONObject busstopObject = busstopArray.getJSONObject(i);
                    MarkerOptions busstopMarker = new MarkerOptions();
                    LatLng latLng = new LatLng(busstopObject.getDouble("Latitude"), busstopObject.getDouble("Longitude"));
                    busstopMarker.position(latLng);
                    busstopMarker.title(busstopObject.getString("Description")+ " ("+ busstopObject.getString("BusStopCode")+ ")");
                    busstopMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    gMap.addMarker(busstopMarker);

                }
            }

        } catch (JSONException ex){
            ex.printStackTrace();

        }
    }

    public void displayBusTiming(JSONObject busTiming, String BusStopCode, String BusStopDescription, double lat, double lng){
        //display bus timing dialog pop up
        String road = getBusStopRdByCode(BusStopCode);
        BusTimingDialog TimingDialog = new BusTimingDialog(this, busTiming, BusStopCode, BusStopDescription, lat, lng, road,  new BusTimingDialog.OnDialogClickListener(){
            @Override
            public void notifyFavoriteDataChange() {
                FavoriteFragment f = (FavoriteFragment) adapter.getItem(1);
                f.refreshData();
            }
        });
        TimingDialog.show();


    }

    @Override
    public void onBookmarkFragmentInteraction(Uri uri) {

    }
    @Override
    public void onNearbyFragmentInteraction() {
        adapter.getItem(1);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
