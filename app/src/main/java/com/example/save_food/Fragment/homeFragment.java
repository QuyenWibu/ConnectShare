package com.example.save_food.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.save_food.MapsActivity;
import com.example.save_food.R;
import com.example.save_food.UploadActivity;
import com.example.save_food.models.UserLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;


public class homeFragment extends Fragment {
    UserLocation userLocation;
    ArrayList<UserLocation> userLocations = new ArrayList<>();
    ;
    FirebaseAuth firebaseAuth;
    Button showmap,post;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        showmap = view.findViewById(R.id.showMap);
        post = view.findViewById(R.id.upload);
        showmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetSumUID();


            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UploadActivity.class);
                startActivity(intent);
            }
        });
        return view;


    }
    private void GetSumUID() {
        ArrayList<String> uidList  = new ArrayList<>();
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
                ArrayList<String> uidListupload  = new ArrayList<>();
                for (int i = 0; i < uidList.size(); i++){
                    int userid = i;
                    DatabaseReference databaseReference = FirebaseDatabase
                            .getInstance().getReference("ThongTin_UpLoad");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Kiểm tra xem nút con với ID của người dùng có tồn tại hay không
                            if (dataSnapshot.hasChild(uidList.get(userid))) {
                                uidListupload.add(uidList.get(userid));
                                if(userid == uidList.size()-1){
                                    GetToaDo(uidListupload);
                                }
                                Log.d("EEE",uidList.get(userid));
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
                        userLocation = new UserLocation(uidListupload.get(uiduser), latitude, longitude);
                        userLocations.add(userLocation);
                        userLocationsCopy.add(userLocation);
                        if(uiduser == uidListupload.size()-1){
                            processUserLocations(getActivity(), userLocations);                        }
                        // Di chuyển lệnh Log.d vào bên trong phương thức onDataChange()
                        //Log.d("AAA" + uiduser + " ", userLocations.get(uiduser).getLatitude() + " - " + userLocations.get(uiduser).getLongitude() + " - " + userLocations.get(uiduser).getUid() );
                        Log.d("Size", String.valueOf(userLocations.size()));

                    }
                    else {
                        Log.d("CCC", "Lỗi!!!!");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // handle error
                    Log.d("BBB", "Lỗi!!!");
                }
            });
        }
        Log.d("Size", String.valueOf(userLocations.size()));
    }

    private void processUserLocations(Context context, ArrayList<UserLocation> userLocations) {
        Intent intent = new Intent(context, MapsActivity.class);
        Gson gson = new Gson();
        String userLocationsJson = gson.toJson(userLocations);
        intent.putExtra("userLocationsJson", userLocationsJson);
        context.startActivity(intent);
    }

}
