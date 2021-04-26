package com.seher.shoppingregionlocator.Map.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.BubbleThumbSeekbar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.snackbar.Snackbar;
import com.seher.shoppingregionlocator.Map.data.DatabaseInitializer;
import com.seher.shoppingregionlocator.Map.data.api.HttpClient;
import com.seher.shoppingregionlocator.Map.data.api.JsonParser;
import com.seher.shoppingregionlocator.Map.data.api.URLBuilder;
import com.seher.shoppingregionlocator.Map.data.db.AppDatabase;
import com.seher.shoppingregionlocator.Map.data.entity.Places;
import com.seher.shoppingregionlocator.Map.network.InternetConnectionDetector;
import com.seher.shoppingregionlocator.Adapters.PlacesRecyclerAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import com.seher.shoppingregionlocator.R;


public class MainActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener
{

    private static final int HTTP_REQUEST_CODE = 1;
    private static final int ACTIVITY_REQUEST_CODE = 100;

    private static int RADIUS = 500;
    private static double LATITUDE;
    private static double LONGITUDE;

    private CoordinatorLayout main_layout;
    private RecyclerView mRecyclerView;
    private BubbleThumbSeekbar seekbar;
    private ProgressBar pbLoading;
    private TextView tvRadius;
    private LinearLayout seekbarLayout;

    private PlacesRecyclerAdapter mAdapter;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private int placesCount;
    private int count;



    private List<Places> placesList = new ArrayList<>();

    private ArrayList<String> type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placesCount=0;
        main_layout = findViewById(R.id.main_layout);
        mRecyclerView = findViewById(R.id.recycler_view);
        seekbar = findViewById(R.id.seekbar);
        pbLoading = findViewById(R.id.pb_loading);
        tvRadius = findViewById(R.id.tv_radius);
        seekbarLayout=findViewById(R.id.seekbarLayout);
        type=getIntent().getStringArrayListExtra("SELECTED_PLACES");

        System.out.println("Selected places are "+ type);

        buildConnectionWithGoogleAPI();

        /**
         * If offline mode selected then load from locally
         */
        if(getIntent().getBooleanExtra("IS_OFFLINE", false))
        {
            placesList.addAll(DatabaseInitializer.getAllRestaurant(AppDatabase.getAppDatabase(MainActivity.this)));

            if(placesList.size() == 0)
            {
                Toast.makeText(getApplicationContext(), "No Favourite. Find other places", Toast.LENGTH_LONG).show();
            }
        }

        if(getIntent().getBooleanExtra("fav", false))
        {
            placesList.addAll(DatabaseInitializer.getAllRestaurant(AppDatabase.getAppDatabase(MainActivity.this)));

            if(placesList.size() == 0)
            {
                Toast.makeText(getApplicationContext(), "No Favourite. Find other places", Toast.LENGTH_LONG).show();
            }
            seekbarLayout.setVisibility(View.INVISIBLE);
        }



        addSeekbarListener();
        initAutoComplete();
        initAdapter();
    }

    /**
     * Initialize Place Auto Complete Adapter
     */
    private void initAutoComplete()
    {
        // Initialize the SDK
        com.google.android.libraries.places.api.Places.initialize(getApplicationContext(), "AIzaSyDS5JMA46n9vDeLf8mZv67lTUheSKTApWY");


// Create a new Places client instance.
        PlacesClient placesClient = com.google.android.libraries.places.api.Places.createClient(this);

        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
// Create a RectangularBounds object.
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596));
// Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
// Call either setLocationBias() OR setLocationRestriction().
                .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
                .setCountry("au")
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery("atm")
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                Log.i(TAG, prediction.getPlaceId());
                Log.i(TAG, prediction.getPrimaryText(null).toString());
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
            }
        });
//
//        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//
//        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
//                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
//                .setTypeFilter(Place.TYPE_COUNTRY).setCountry("PK")
//                .build();
//
//        autocompleteFragment.setFilter(typeFilter);
//
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//
//            @Override
//            public void onPlaceSelected(Place place)
//            {
//                LATITUDE = place.getLatLng().latitude;
//                LONGITUDE = place.getLatLng().longitude;
//
//                /**
//                 * Rest Api Call
//                 */
//                restApiCall();
//            }
//
//            @Override
//            public void onError(Status status)
//            {
//                Toast.makeText(getApplicationContext(), "" + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    /**
     * Initialize recycler adapter
     */
    private void initAdapter()
    {
        if(mAdapter != null)
        {
            return;
        }

        mRecyclerView.setHasFixedSize(false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new PlacesRecyclerAdapter(this, placesList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new PlacesRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int i)
            {
                Places places = placesList.get(i);

                /**
                 * Redirect to Device Google Map on Click
                 */
                try
                {

//                    System.out.println("Location coordinates"+ places.getLat() + "," + places.getLng());
//                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + places.getLat() + "," + places.getLng() + "&mode=d");
//                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                    mapIntent.setPackage("com.google.android.apps.maps");
//
//                    //if (mapIntent.resolveActivity(getPackageManager()) != null)
//                    {
//                        startActivity(mapIntent);
//                    }
                            Intent intent=new Intent(getApplicationContext(), PlaceDetailsScreen.class);
                            intent.putExtra("placeDetails",places);
//                            intent.putExtra("user",userEnteredUsername);

                    startActivity(intent);
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFavoriteClick(View view, int position) {

                Places places = placesList.get(position);

                if(DatabaseInitializer.count(AppDatabase.getAppDatabase(MainActivity.this), places.getPlace_id()) <= 0)
                {
                    /**
                     * If place not added to favourite then add
                     */
                    places.setIs_favourite(true);
                    DatabaseInitializer.insertRestaurant(AppDatabase.getAppDatabase(MainActivity.this), places);
                    placesList.get(position).setIs_favourite(true);

                    Toast.makeText(getApplicationContext(), "Added Favorite", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    /**
                     * If place already added to favourite then remove
                     */
                    DatabaseInitializer.delete(AppDatabase.getAppDatabase(MainActivity.this), places);
                    placesList.get(position).setIs_favourite(false);

                    Toast.makeText(getApplicationContext(), "Removed Favorite", Toast.LENGTH_SHORT).show();
                }

                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume()
    {
        super.onResume();
        checkServicesEnable();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        stopLocationUpdates();
    }

    /**
     * Check GPS and Network enabled or not
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkServicesEnable()
    {   LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        if(getIntent().getBooleanExtra("fav", false)) {

            try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            network_enabled = cm.getActiveNetwork() != null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (!gps_enabled) {
            final Snackbar snackbar = Snackbar.make(main_layout, "Please Enable GPS", Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("Enable GPS", new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    snackbar.dismiss();
                    startActivityForResult(myIntent, ACTIVITY_REQUEST_CODE);
                }
            }).show();
        }

        if (gps_enabled && !network_enabled) {
            final Snackbar snackbar1 = Snackbar.make(main_layout, "Please Enable Internet", Snackbar.LENGTH_LONG);
            snackbar1.setAction("Enable Internet", new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    snackbar1.dismiss();
                    startActivityForResult(myIntent, ACTIVITY_REQUEST_CODE);
                }
            }).show();
        }
    }
        return gps_enabled && network_enabled;

    }

    /**
     * Initialize Google Api Client
     */
    private void buildConnectionWithGoogleAPI()
    {
        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(com.google.android.gms.location.places.Places.GEO_DATA_API)
                    .addApi(com.google.android.gms.location.places.Places.PLACE_DETECTION_API)
                    .build();
        }

        mGoogleApiClient.connect();
    }

    /**
     * Initialize location request object
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void createLocationRequest()
    {
        mLocationRequest = LocationRequest.create();

        mLocationRequest.setInterval(500000);
        mLocationRequest.setFastestInterval(100000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        startLocationUpdates();
    }

    /**
     * Start location updating
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void startLocationUpdates()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        if(mGoogleApiClient!=null && mGoogleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    protected  void stopLocationUpdates()
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(mLocationRequest != null)
                    {
                        startLocationUpdates();
                    }

                    else
                    {
                        createLocationRequest();
                    }
                }

                break;

            default:

                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        createLocationRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onLocationChanged(Location location) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        LATITUDE = location.getLatitude();
        LONGITUDE = location.getLongitude();

        if(getIntent().getBooleanExtra("IS_OFFLINE", false))
        {
            return;
        }

        /**
         * Rest API Call
         */
        restApiCall();
    }


    @Override
    public void onPreExecute()
    {
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(int requestCode, int responseCode, String response)
    {
        if(isFinishing())
        {
            return;
        }

        try
        {
            /**
             * Check status returned by Google Place API
             */
            JSONObject json = new JSONObject(response);
            String status = json.getString("status");

            /**
             * If status is OK
             */
            if(requestCode == HTTP_REQUEST_CODE && responseCode == HttpClient.OK && status.equalsIgnoreCase("OK"))
            {
                /**
                 * Parse response JSON and add all place in a list
                 */
                List<Places> places = JsonParser.parseJson(response);
               // Toast.makeText(getApplicationContext(), " All selected places are not found in this radius. Increase radius", Toast.LENGTH_SHORT).show();


                if(places.size() != 0)
                {
                    //placesList.clear();
                    placesList.addAll(places);
                    placesCount++;
                }

                else
                {
                    //Toast.makeText(getApplicationContext(), "No Place Found", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), " All selected places are not found in this radius. Increase radius", Toast.LENGTH_SHORT).show();


                }


                if(placesCount==type.size())
                {

                    Toast.makeText(getApplicationContext(), " All selected places are found in selected radius.", Toast.LENGTH_LONG).show();

                }
                else if(placesCount==type.size() && count==type.size())
                {
                    Toast.makeText(getApplicationContext(), placesCount+" All selected places are not found in this radius. Increase radius", Toast.LENGTH_LONG).show();

                }

                try
                {
                    /**
                     * Check if place added to favourite
                     */
                    for(int pos = 0; pos< placesList.size(); pos++)
                    {
                        if(DatabaseInitializer.count(AppDatabase.getAppDatabase(MainActivity.this), placesList.get(pos).getPlace_id()) > 0)
                        {
                            placesList.get(pos).setIs_favourite(true);
                        }
                    }
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }

                finally
                {
                    mAdapter.notifyDataSetChanged();
                }
            }

            else if(status.equals("ZERO_RESULTS"))
            {
                Toast.makeText(getApplicationContext(), "All selected places are not found in this radius. Increase radius", Toast.LENGTH_LONG).show();

            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        finally
        {
            pbLoading.setVisibility(View.GONE);
        }
    }

    /**
     * Added listener to radius seekbar
     */
    private void addSeekbarListener()
    {
        seekbar.setMinStartValue(RADIUS);

        seekbar.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {

            @Override
            public void finalValue(Number value)
            {
                RADIUS = Integer.parseInt(String.valueOf(value));

                double distance = RADIUS / 1000;
                tvRadius.setText(String.valueOf("Places with in "+ distance + " km"));

                //new added
                placesList.clear();
                /**
                 * Rest API Call
                 */
                restApiCall();
            }
        });
    }


    /**
     * Rest API Call
     */
    private void restApiCall()
    {
        placesCount=0;
        placesList.clear();
        count=0;
        if (new InternetConnectionDetector(MainActivity.this).isConnected())
        {
            if(!(getIntent().getBooleanExtra("fav", false))) {
                for (int i = 0; i < type.size(); i++) {
                    String search = type.get(i);
                    if (search.equals("Electronics Store"))
                        search = "electronics_store";
                    else if (search.equals("Apparels"))
                        search = "clothing_store";
                    else if (search.equals("Jewelry Store"))
                        search = "jewelry_store";
                    else if (search.equals("Department Store"))
                        search = "department_store";
                    else if (search.equals("Shopping Mall"))
                        search = "shopping_mall";

                    System.out.println("Going to search " + search + "  size: " + type.size());

                    new HttpClient(HTTP_REQUEST_CODE, MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, URLBuilder.placesURL(search.toLowerCase(), LATITUDE, LONGITUDE, RADIUS));
                  count++;
                }
//
//                if(placesCount==type.size())
//                {
//
//                    Toast.makeText(getApplicationContext(), placesCount+" All selected places are found in selected radius.", Toast.LENGTH_SHORT).show();
//
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(), placesCount+" All selected places are not found in this radius. Increase radius", Toast.LENGTH_SHORT).show();
//
//                }
            }
        }

        else
        {
            Toast.makeText(getApplicationContext(), "Internet Not Connected", Toast.LENGTH_SHORT).show();
        }
    }
}