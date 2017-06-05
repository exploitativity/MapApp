package com.example.zhanga2329.mapappandrewzhang;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.location.LocationListener;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private boolean isGPSenabled = false;
    private boolean isNetworkEnabled = false;
    private boolean trackingEnabled = false;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 5;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 5.0f;
    private Location current;
    private static final float MY_LOC_ZOOM_FACTOR = 17.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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

        // Add a marker in Not Actually Sydney and move the camera
        LatLng brampton = new LatLng(43.73, -79.76);
        mMap.addMarker(new MarkerOptions().position(brampton).title("Born Here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(brampton));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("MapAppAndrewZhang", "Failed coarse permission check");
            Log.d("MapAppAndrewZhang", Integer.toString(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)));
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MapAppAndrewZhang", "Failed fine permission check");
            Log.d("MapAppAndrewZhang", Integer.toString(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)));
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }
        //mMap.setMyLocationEnabled(true);
    }

    public void toggleMapType(View v) {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL)
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        else mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void clearMarkers(View v) {
        mMap.clear();
    }

    public void dropMarker(String provider) {
        LatLng userLocation = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("MapAppAndrewZhang", "Failed coarse permission check");
            Log.d("MapAppAndrewZhang", Integer.toString(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)));
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MapAppAndrewZhang", "Failed fine permission check");
            Log.d("MapAppAndrewZhang", Integer.toString(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)));
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }
        current = null;
        if(locationManager != null) {
            current = locationManager.getLastKnownLocation(provider);
        }
        if(current != null) {
            userLocation = new LatLng(current.getLatitude(),current.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(userLocation, MY_LOC_ZOOM_FACTOR);
            if(provider.equals(LocationManager.GPS_PROVIDER)) {
                mMap.addCircle(new CircleOptions().center(userLocation).radius(2).strokeColor(Color.BLUE).strokeWidth(2).fillColor(Color.BLUE));
                // mMap.addMarker(new MarkerOptions().position(userLocation).title("Last Marked Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }
            else {
                mMap.addCircle(new CircleOptions().center(userLocation).radius(2).strokeColor(Color.GREEN).strokeWidth(2).fillColor(Color.GREEN));
                //mMap.addMarker(new MarkerOptions().position(userLocation).title("Last Marked Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }
            mMap.animateCamera(update);
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
        }
        else {
            Log.d("MapAppAndrewZhang", "NOW YOU FUCKED UP. NOW YOU FUCKED UP. NOW YOU FUCKED UP. YOU HAVE FUCKED UP NOW.");
            Toast.makeText(this, "Not good.", Toast.LENGTH_SHORT).show();
        }
    }

    public void getLocation(View v) {
        try {
            if(trackingEnabled) {
                trackingEnabled = false;
                locationManager.removeUpdates(locationListenerGPS);
                locationManager.removeUpdates(locationListenerNetwork);
                Toast.makeText(this, "Tracking disabled", Toast.LENGTH_SHORT).show();
            }
            else {
                trackingEnabled = true;
                Toast.makeText(this, "Tracking enabled", Toast.LENGTH_SHORT).show();
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                isGPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (isGPSenabled) Log.d("MapAppAndrewZhang", "getLocation: GPS is enabled");

                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (isNetworkEnabled) Log.d("MapAppAndrewZhang", "getLocation: Network is enabled");

                if (!isGPSenabled && !isNetworkEnabled) {
                    Log.d("MapAppAndrewZhang", "getLocation: No Provider is Enabled");
                } else {
                    checkPermission();
                    if (isGPSenabled) {
                        Log.d("MapAppAndrewZhang", "getLocation: GPS ENABLED; REQUESTION LOCATION UPDATES");

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                locationListenerGPS);
                        Log.d("MapAppAndrewZhang","getLocation: Network GPS update request suCCESsFULL");
                        Toast.makeText(this, "Tracking enabled, using GPS", Toast.LENGTH_SHORT).show();
                    }
                    if(isNetworkEnabled) {
                        Log.d("MapAppAndrewZhang","getLocation: NETWORK ENABLED; REQUESTION LOCATION UPDATES");
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                locationListenerNetwork);
                        Log.d("MapAppAndrewZhang","getLocation: Network GPS update request suCCESsFULL");
                        Toast.makeText(this, "Using NETWORK", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
        catch (Exception e) {
            Log.d("MapAppAndrewZhang","GIVE UP THERE IS NO GOD IT IS USELESS");
            e.printStackTrace();
        }
    }
    public void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("MapAppAndrewZhang", "Failed coarse permission check");
            Log.d("MappAppAndrewZhang", Integer.toString(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)));
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MapAppAndrewZhang", "Failed fine permission check");
            Log.d("MappAppAndrewZhang", Integer.toString(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)));
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }
    }
    LocationListener locationListenerGPS = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            //output Log.d and toast messages
            if(trackingEnabled) {
                Log.d("MapAppAndrewZhang","Location changed on GPS, dropping marker & disabling network updates");
                //drop a marker with dropMarker method
                dropMarker(LocationManager.GPS_PROVIDER);
                //disable network updates (see LocationManager to disable updates)
                checkPermission();
                locationManager.removeUpdates(locationListenerNetwork);
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //setup a switch statement on status
            //case: LocationProvider.AVAILABLE into output Log.d + Toast messages
            //case: LocationProvider.OUT_OF_SERVICE into request updates from NETWORK_PROVIDER
            //case: LocationProvider.TEMPORARILY_UNAVAILABLE into request updates from NETWORK_PROVIDER
            //case: default into request updates from N E T W O R K _ P R O V I D E R
            switch(status) {
                case LocationProvider.AVAILABLE:
                    Log.d("MapAppAndrewZhang","GPS provider is available");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    checkPermission();
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            locationListenerNetwork);
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    checkPermission();
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            locationListenerNetwork);
                    break;
                default:
                    checkPermission();
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            locationListenerNetwork);
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    LocationListener locationListenerNetwork = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            if(trackingEnabled) {
                //output Log.d and toast messages
                Log.d("MapAppAndrewZhang","Location changed on NETWORK, dropping marker & enabling network updates");
                //drop a marker with dropMarker method
                dropMarker(LocationManager.NETWORK_PROVIDER);
                //relaunch request for network location updates
                checkPermission();
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        locationListenerNetwork);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //output Log.d and toast messages
            Log.d("MapAppAndrewZhang","Status changed in network");
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
