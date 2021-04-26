package com.seher.shoppingregionlocator.Map.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.seher.shoppingregionlocator.Map.data.api.URLBuilder;
import com.seher.shoppingregionlocator.Map.ui.ImageLoader;
import com.seher.shoppingregionlocator.R;
import com.seher.shoppingregionlocator.Map.data.entity.Places;

public class PlaceDetailsScreen extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Places p;
    ImageView placeImage;
    TextView name, lat,log,open;
    RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details_screen);

        placeImage=findViewById(R.id.placeImage);
        name=findViewById(R.id.name);
        lat=findViewById(R.id.lat);
        log=findViewById(R.id.log);
        open=findViewById(R.id.open);

        ratingBar=findViewById(R.id.ratingBar);

        p= (Places) getIntent().getSerializableExtra("placeDetails");

        name.setText(p.getName());
        lat.setText("Latitude: "+p.getLat().toString());
        log.setText("Longitude"+p.getLng().toString());
        if(p.getRating()!=null)
        ratingBar.setRating(p.getRating());
        if(p.isOpen_now())
        {
            open.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
            open.setText("Open");
        }

        else
        {
            open.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
            open.setText("Closed");
        }
        String url=URLBuilder.thumbURL(p.getPhoto_reference());

        if (!url.isEmpty()) {
            ImageLoader.loadThumbnail(getApplicationContext(), url, placeImage, R.drawable.things, 75, 75);
        }

        System.out.println("kaleem nisar "+p);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(p.getLat(), p.getLng());
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker at "+p.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

    }


    public void navigate(View view) {
        try
        {

                    System.out.println("Location coordinates"+ p.getLat() + "," + p.getLng());
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + p.getLat() + "," + p.getLng() + "&mode=d");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");

                    //if (mapIntent.resolveActivity(getPackageManager()) != null)
                    {
                        startActivity(mapIntent);
                    }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}