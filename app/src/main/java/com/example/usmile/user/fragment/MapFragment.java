package com.example.usmile.user.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.usmile.R;

import com.example.usmile.account.AccountFactory;
import com.example.usmile.account.models.Clinic;
import com.example.usmile.account.models.User;
import com.example.usmile.utilities.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback, View.OnClickListener {

    private static final Object PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private  boolean locationPermissionGranted;
    private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private Location defaultLocation = new Location("Vietnam");
    private LocationManager locationManager;
    private List<Clinic> ClinicList = new ArrayList<>();
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.map, container, false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

        SupportMapFragment mapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getBundle();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        loadMapData();

    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
             locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    (int) PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
       locationPermissionGranted = false;
        if (requestCode
                == (int) PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocation();
    }
    private void updateLocation() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    private void getDeviceLocation() {
        int DEFAULT_ZOOM = 16;
        try {
            if (locationPermissionGranted) {
                locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
                Criteria crta = new Criteria();
                crta.setAccuracy(Criteria.ACCURACY_FINE);
                crta.setAltitudeRequired(true);
                crta.setBearingRequired(true);
                crta.setCostAllowed(true);
                crta.setPowerRequirement(Criteria.POWER_LOW);
                locationManager.requestLocationUpdates( locationManager.getBestProvider(crta, false), 0, 0, this);

                Task<Location> locationResult = fusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener((Activity) getContext(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult()!=null) {

                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            showToast("Your location");
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            updateClinicsMaker();
                            LatLng yourLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(yourLocation).title("You are here"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yourLocation,DEFAULT_ZOOM));

                        } else {
                            showToast("default location");
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            updateClinicsMaker();
                            LatLng sydney = new LatLng(-34, 151);
                            mMap.addMarker(new MarkerOptions()
                                    .position(sydney)
                                   .title("Marker in Sydney"));
                             mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void loadMapData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_CLINIC)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> docList = task.getResult().getDocuments();
                        for (DocumentSnapshot doc:docList ){
                            Clinic newClinic = (Clinic) AccountFactory.createAccount(AccountFactory.CLINICSTRING);
                            newClinic.setFullName((String)doc.get(Constants.KEY_CLINIC_FULL_NAME));
                            newClinic.setAvatar((String)doc.get(Constants.KEY_CLINIC_AVATAR));
                            newClinic.setLatitude(Float.parseFloat(String. valueOf(doc.get(Constants.KEY_CLINIC_LATITUDE))));
                            newClinic.setLongitude(Float.parseFloat(String. valueOf(doc.get(Constants.KEY_CLINIC_LONGITUDE))));
                            newClinic.setAddress((String)doc.get(Constants.KEY_CLINIC_ADDRESS));
                            newClinic.setPhone((String)doc.get(Constants.KEY_CLINIC_PHONE));
                            ClinicList.add(newClinic);

                        }
                        updateLocation();
                        getDeviceLocation();
                    }});

    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null)
            user = (User) bundle.getSerializable(AccountFactory.USERSTRING);
    }

    private void updateClinicsMaker(){
       for (int i = 0; i<ClinicList.size(); i++){
            addMarker(ClinicList.get(i), i);

        }

       mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.getTag()!=null) {
                    int pos = (int) (marker.getTag());
                    if (pos >= 0 || pos < ClinicList.size()) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AccountFactory.USERSTRING, user);
                        bundle.putSerializable(AccountFactory.CLINICSTRING, ClinicList.get(pos));
                        Fragment fragment = new ClinicInfoFragment();
                        fragment.setArguments(bundle);
                        openNewFragment(fragment);

                    }
                }
            }
        });
    }


    private void addMarker(Clinic newClinic, int index){
       Marker marker =  mMap.addMarker(new MarkerOptions()
                .position(new LatLng(newClinic.getLatitude(), newClinic.getLongitude()))
                .title(newClinic.getFullName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        );
       marker.setTag(index);
    }

    private void showToast(String msg) {
        Toast.makeText(this.getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {

    }

    @Override
    public void onFlushComplete(int requestCode) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    private void openNewFragment(Fragment nextFragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), nextFragment, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

}
