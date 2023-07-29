package com.example.save_food;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.save_food.models.UserLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {



    ArrayList<LatLng> arrayList = new ArrayList<LatLng>();
    GoogleMap gMap, mMap;
    FrameLayout map;
    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        map = findViewById(R.id.map);


//        Intent intent = getIntent();
//        if (intent != null) {
//            Bundle bundle = intent.getExtras();
//            if (bundle != null) {
//                ArrayList<? extends Parcelable> parcelableList = bundle.getParcelableArrayList("userLocations");
//                if (parcelableList != null) {
//                    ArrayList<UserLocation> userLocations = new ArrayList<>();
//                    for (Parcelable parcelable : parcelableList) {
//                        if (parcelable instanceof UserLocation) {
//                            userLocations.add((UserLocation) parcelable);
//                        }
//                    }
//
//                    // Sử dụng danh sách userLocations ở đây
//                    for (int i = 0; i < userLocations.size(); i++) {
//                        UserLocation location = userLocations.get(i);
//                        // Do something with each user location
//
//                        Log.d("AAA" + i + " ", location.getLatitude() + " - " + location.getLongitude() + " - " + location.getUid());
//                    }
//                }
//            }
//        }



    }


    private void getLastLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
//        // clear all elements in uidList
//        uidList.clear();
//        // clear all elements in userLocations
//        userLocations.clear();
//        // clear all elements in latLngs
//        latLngs.clear();
        //lấy tất cả UID trong firebase

        //lấy giá trị kinh độ và vĩ độ của từng UID

        this.gMap = googleMap;
        this.mMap = googleMap;
        //thêm marker vào những user đã upload
//        arrayList.add(mapVN);
        //this.gMap.addMarker(new MarkerOptions().position(mapVN).title("vietnam"));
//        LatLng mapp = new LatLng(37.4219983, -122.084);
//        this.mMap.addMarker(new MarkerOptions().position(mapp));
        getUserLocationsFromIntent();
//        for (int i = 0; i < arrayList.size(); i++) {
//            LatLng latLng = arrayList.get(i);
//                this.gMap.addMarker(new MarkerOptions().position(latLng));
//
//        }
        Log.d("gMapSize", String.valueOf(arrayList.size()));
        LatLng mapVN = new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude());
        this.gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapVN, 16));
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Latitude", Double.valueOf(currentLocation.getLatitude()));
        hashMap.put("Longitude",  Double.valueOf(currentLocation.getLongitude()));
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");;
        databaseReference.child(user.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Cập nhật....", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserLocationsFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String userLocationsJson = intent.getStringExtra("userLocationsJson");
            if (userLocationsJson != null) {
                Gson gson = new Gson();
                Type userListType = new TypeToken<ArrayList<UserLocation>>() {}.getType();
                ArrayList<UserLocation> userLocations = gson.fromJson(userLocationsJson, userListType);
                if (userLocations != null) {
                    // Use the userLocations list here
                    for (UserLocation location : userLocations) {
                        LatLng uploadmaps = new LatLng(Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()));
                        gMap.addMarker(new MarkerOptions().position(uploadmaps));
                        arrayList.add(uploadmaps);
                        Log.d("AAA" + " ", location.getLatitude() + " - " + location.getLongitude() + " - " + location.getUid());
                    }
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            } else {
                Toast.makeText(this, "hãy cấp quyền Map", Toast.LENGTH_SHORT).show();
            }
        }
    }
}