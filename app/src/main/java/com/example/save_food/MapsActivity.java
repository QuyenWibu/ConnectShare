package com.example.save_food;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.save_food.models.KhoangCachLocation;
import com.example.save_food.models.UserLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    List<KhoangCachLocation> khoangCachLocationList = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseUser user;
    BitmapDescriptor defaultIcon;
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
        arrayList.clear();
        khoangCachLocationList.clear();



    }

    @Override
    protected void onStart() {
        super.onStart();
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
        this.gMap = googleMap;
        this.mMap = googleMap;
        getUserLocationsFromIntent();
        Log.d("gMapSize", String.valueOf(arrayList.size()));
        LatLng mapVN = new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude());
        this.gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapVN, 16));
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Latitude", Double.valueOf(currentLocation.getLatitude()));
        hashMap.put("Longitude",  Double.valueOf(currentLocation.getLongitude()));
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.child(user.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Cập nhật....", Toast.LENGTH_SHORT).show();
            }
        });


        DatabaseReference imageRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("image");
        imageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String image = snapshot.getValue(String.class);

                    // Sử dụng thư viện Picasso để tải và hiển thị ảnh từ URL
                    Picasso.get().load(image).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            int width = 120;
                            int height = 120;
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

                            Bitmap circularBitmap = Bitmap.createBitmap(scaledBitmap.getWidth(), scaledBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(circularBitmap);
                            Paint paint = new Paint();
                            paint.setAntiAlias(true);
                            Rect rect = new Rect(0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight());
                            RectF rectF = new RectF(rect);
                            canvas.drawOval(rectF, paint);
                            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                            canvas.drawBitmap(scaledBitmap, rect, rect, paint);
                            // Biến đổi Bitmap thành BitmapDescriptor
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(circularBitmap);

                            // Thiết lập biểu tượng của marker
                            gMap.addMarker(new MarkerOptions()
                                    .position(mapVN)
                                    .icon(icon))
                                    .showInfoWindow();


                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            // Xử lý khi không thể tải hình ảnh từ URL
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            // Xử lý khi chuẩn bị tải hình ảnh từ URL
                        }
                    });
                } else {
                    // Nếu không có ảnh trong Realtime Database, sử dụng biểu tượng mặc định
                    gMap.addMarker(new MarkerOptions()
                            .position(mapVN)
                            .icon(defaultIcon))
                            .showInfoWindow();;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra trong quá trình lấy dữ liệu từ Database
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
                        if (gMap != null) {
                            Marker marker = gMap.addMarker(new MarkerOptions().position(uploadmaps).icon(defaultIcon));
                            marker.showInfoWindow();

                            if (marker == null) {
                                Log.d("Marker", "Marker is null");
                            } else {
                                Log.d("Marker", "Marker is not null");
                            }
                        } else {
                            Log.d("gMap", "gMap is null");
                        }
                        double khoangcach = Math.sqrt(Math.pow(location.getLatitude() - currentLocation.getLatitude(), 2) + Math.pow(location.getLongitude() - currentLocation.getLongitude(), 2));
                        arrayList.add(uploadmaps);
                        khoangCachLocationList.add(new KhoangCachLocation(khoangcach, location.getUid()));
                        //Log.d("Khoangcach", khoangcach + " - " + location.getUid());
                        Log.d("AAA" + " ", location.getLatitude() + " - " + location.getLongitude() + " - " + location.getUid());
                    }
                    for(int i=0;i<khoangCachLocationList.size();i++){
                        Log.d("khoangcach", khoangCachLocationList.get(i).getDistance() + " - " + khoangCachLocationList.get(i).getUid() );
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