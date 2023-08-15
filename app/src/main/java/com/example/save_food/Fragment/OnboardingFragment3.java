package com.example.save_food.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.save_food.R;
import com.example.save_food.SplashActivity;


public class OnboardingFragment3 extends Fragment {

Button btnGetStart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =  inflater.inflate(R.layout.fragment_onboarding3, container, false);
       btnGetStart = view.findViewById(R.id.btn_get_start);
       btnGetStart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getActivity(), SplashActivity.class);
               getActivity().startActivity(intent);
           }
       });
        return view;
    }
}