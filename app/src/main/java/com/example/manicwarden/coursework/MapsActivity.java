package com.example.manicwarden.coursework;
/***
 Frazer J Johnston. Matriculation Number: S1623945
 ***/
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Double _longitude;
    private Double _latitude;
    private GoogleMap _googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        // if data has been passed to the activity
        if (extras != null)
        {
            // set the longitude and latitude to the values sent to the activity
            // as defined by the specified keys
            _longitude = Double.parseDouble(extras.getString("Longitude"));
            _latitude = Double.parseDouble(extras.getString("Latitude"));

        }

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
        _googleMap = googleMap;

        // Add a marker to the incident selected by the user and move the focus to it
        LatLng incident = new LatLng(_latitude, _longitude);
        googleMap.addMarker(new MarkerOptions().position(incident).title("Incident"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(incident));
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.Home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.CurrentIncidents:
                startActivity(new Intent(this, CurrentIncidents.class));
                return true;
            case R.id.PlannedRoadworks:
                startActivity(new Intent(this, PlannedRoadworks.class));
                return true;
            case R.id.findByDate:
                startActivity(new Intent(this, findByDate.class));
                return true;
            case R.id.findByName:
                startActivity(new Intent(this, findByRoadname.class));
                return true;
            case R.id.Exit:
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
