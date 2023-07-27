package com.example.save_food.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.save_food.notification.MapsActivity;
import com.example.save_food.R;
import com.example.save_food.notification.UploadActivity;

import androidx.fragment.app.Fragment;


import com.google.firebase.auth.FirebaseAuth;


public class homeFragment extends Fragment {


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
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
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

}
