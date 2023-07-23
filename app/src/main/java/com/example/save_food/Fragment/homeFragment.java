package com.example.save_food.Fragment;

import static android.os.Build.VERSION_CODES.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.save_food.MapsActivity;
import com.example.save_food.UploadActivity;
import com.example.save_food.profileActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


import com.example.save_food.R;
import com.example.save_food.profileActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;


public class homeFragment extends Fragment {


    FirebaseAuth firebaseAuth;
    Button showmap;
    FloatingActionButton post;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.example.save_food.R.layout.fragment_home_admin, container, false);

        showmap = view.findViewById(com.example.save_food.R.id.showMap);
        post = view.findViewById(com.example.save_food.R.id.upload);
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
