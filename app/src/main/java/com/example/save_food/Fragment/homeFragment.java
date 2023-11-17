package com.example.save_food.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.save_food.MapsActivity;
import com.example.save_food.R;
import com.example.save_food.UploadActivity;
import com.example.save_food.models.KhoangCachLocaitonSort;
import com.example.save_food.models.KhoangCachLocation;
import com.example.save_food.models.UserLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public class homeFragment extends Fragment {
    Location location;
    UserLocation userLocation;
    ArrayList<UserLocation> userLocations = new ArrayList<>();
    Location currentLocation;
    ArrayList<KhoangCachLocation> khoangCachLocationList = new ArrayList<>();
    public List<KhoangCachLocaitonSort> khoangCachLocaitonSorts = new ArrayList<>();
    FusedLocationProviderClient fusedLocationProviderClient;
    double latitude, longitude;
    FirebaseAuth firebaseAuth;
    private final int FINE_PERMISSION_CODE = 1;
    Button showmap, post;
    ProgressDialog progressDialog;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
        boolean k = true;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.hongcogihet) {
                    //openFragment(new BlankFragment());
                    // Tạo ra Bundle để đính kèm vào Fragment
                    k=false;
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("my_list", khoangCachLocationList);
                    BlankFragment fragment = new BlankFragment();
                    // Thiết lập Bundle cho BlankFragment
                    fragment.setArguments(bundle);

                    // Chuyển sang BlankFragment
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
//                    // Ẩn fragment_container
//                    View fragmentContainer = getView().findViewById(R.id.container_home);
//                    fragmentContainer.setVisibility(View.GONE);


                    return true;
                }
                if (itemId == R.id.Users_nav) {
                    openFragment(new UsersFragment());
                    return true;
                }
                else if (itemId == R.id.chat_nav) {
                    openFragment(new ChatListFragment());
                    return true;
                }
                return false;
            }
        });
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));
        showmap = view.findViewById(R.id.showMap);
        post = view.findViewById(R.id.upload);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setBackground(null);
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            // Thêm giá trị trả về ở đây nếu cần
        } else {
            // Code xử lý khi có quyền vị trí
        }




        khoangCachLocationList.clear();
        userLocations.clear();
        GetSumUID();
        for(int i=0;i<khoangCachLocationList.size();i++){
            Log.d("khoangcach", khoangCachLocationList.get(i).getDistance() + " - " + khoangCachLocationList.get(i).getUid() );
        }
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sử dụng biến khoangCachLocationList ở đây
                KetQua();
                Intent intent = new Intent(getActivity(), UploadActivity.class);
                startActivity(intent);
            }
        });

        return view;


    }
    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Quyền vị trí đã được cấp phép
            return true;
        } else {
            // Quyền vị trí chưa được cấp phép, yêu cầu người dùng cấp phép
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return false;
        }
    }
    private void KetQua() {
        for(int i=0;i<khoangCachLocationList.size();i++){
            Log.d("khoangcach", khoangCachLocationList.get(i).getDistance() + " - " + khoangCachLocationList.get(i).getUid() );
        }
    }

    private void GetSumUID() {
        ArrayList<String> uidList = new ArrayList<>();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String uid = userSnapshot.getKey();
                    // do something with uid
                    uidList.add(uid);
                }
                // Log toàn bộ các phần tử trong ArrayList uidList
                for (int i = 0; i < uidList.size(); i++) {
                    Log.d("UID", uidList.get(i));
                }
                KiemtraFireBase(uidList);
            }

            private void KiemtraFireBase(ArrayList<String> uidList) {
                ArrayList<String> uidListupload = new ArrayList<>();
                for (int i = 0; i < uidList.size(); i++) {
                    int userid = i;
                    DatabaseReference databaseReference = FirebaseDatabase
                            .getInstance().getReference("ThongTin_UpLoad");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Kiểm tra xem nút con với ID của người dùng có tồn tại hay không
                            if (dataSnapshot.hasChild(uidList.get(userid))) {
                                uidListupload.add(uidList.get(userid));
                                    GetToaDo(uidListupload);
                                Log.d("EEE", uidList.get(userid));
                                // Thực hiện các thuật toán khác
                                // ...
                            } else {
                                Log.d("CCC", "không có nút " + uidList.get(userid));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Xử lý lỗi
                            Log.d("CCC", "không có nút " + uidList.get(userid));


                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // handle error
            }
        });

    }

    private void GetToaDo(ArrayList<String> uidListupload) {
        ArrayList<UserLocation> userLocationsCopy = new ArrayList<>();
        for (int i = 0; i < uidListupload.size(); i++) {
            int uiduser = i;
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users/" + uidListupload.get(i));
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.hasChild("Latitude") && dataSnapshot.hasChild("Longitude")) {
                        double latitude = dataSnapshot.child("Latitude").getValue(Double.class);
                        double longitude = dataSnapshot.child("Longitude").getValue(Double.class);
                        String url = dataSnapshot.child("image").getValue(String.class);
                        userLocation = new UserLocation(uidListupload.get(uiduser), latitude, longitude, url);
                        userLocations.add(userLocation);
                        userLocationsCopy.add(userLocation);
                        if (uiduser == uidListupload.size() - 1) {
                            showmap.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Kiểm tra quyền vị trí trước khi thực hiện hoạt động
                                    if (checkLocationPermission()) {
                                        // Quyền vị trí đã được cấp phép, thực hiện các hoạt động liên quan đến vị trí
                                        processUserLocations(getActivity(), userLocations);
                                    } else {
                                        // Người dùng chưa cấp phép quyền vị trí
                                        // Có thể hiển thị thông báo hoặc thực hiện các hành động khác tùy thuộc vào yêu cầu của bạn
                                        Toast.makeText(getActivity(), "Vui lòng cấp phép quyền vị trí để sử dụng tính năng này", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            fusedLocationProviderClient.getLastLocation()
                                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            if (location != null) {
                                                // Lấy được vị trí hiện tại của người dùng
                                                currentLocation = location;
                                                TinhKhoangCach(userLocations, currentLocation);

                                            }
                                        }
                                    });

                        }

                        // Di chuyển lệnh Log.d vào bên trong phương thức onDataChange()
                        //Log.d("AAA" + uiduser + " ", userLocations.get(uiduser).getLatitude() + " - " + userLocations.get(uiduser).getLongitude() + " - " + userLocations.get(uiduser).getUid() );
                        Log.d("Size", String.valueOf(userLocations.size()));

                    }
                    else {
                        Log.d("CCC", "Lỗi!!!!");
                        Intent intent = new Intent(getActivity(), MapsActivity.class);
                        startActivity(intent);
                    }

                }

//                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // handle error
                    Log.d("BBB", "Lỗi!!!");
                }
            });
        }
        Log.d("Size", String.valueOf(userLocations.size()));
    }

    private void TinhKhoangCach(ArrayList<UserLocation> userLocations, Location currentLocation) {
            for(int i = 0; i < userLocations.size(); i++){
                    double khoangcach = Math.sqrt(Math.pow(userLocations.get(i).getLatitude() - currentLocation.getLatitude(), 2) + Math.pow(userLocations.get(i).getLongitude() - currentLocation.getLongitude(), 2));
                    khoangCachLocationList.add(new KhoangCachLocation(khoangcach, userLocations.get(i).getUid()));


            }
            Collections.sort(khoangCachLocationList, new Comparator<KhoangCachLocation>() {
                public int compare(KhoangCachLocation o1, KhoangCachLocation o2) {
                    return Double.compare(o1.getDistance(), o2.getDistance());
                }
            });


    }

    private void processUserLocations(Context context, ArrayList<UserLocation> userLocations) {
        Intent intent = new Intent(context, MapsActivity.class);
        Gson gson = new Gson();
        String userLocationsJson = gson.toJson(userLocations);
        intent.putExtra("userLocationsJson", userLocationsJson);
        context.startActivity(intent);
    }

}
