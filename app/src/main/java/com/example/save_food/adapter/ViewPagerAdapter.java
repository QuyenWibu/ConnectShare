package com.example.save_food.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.save_food.Fragment.OnboardingFragment1;
import com.example.save_food.Fragment.OnboardingFragment2;
import com.example.save_food.Fragment.OnboardingFragment3;
import com.example.save_food.databinding.FragmentChangePasswordBinding;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm,  int behavitor){
        super(fm, behavitor);
    }
    @NonNull
    @Override
    public  Fragment getItem(int position){
        switch (position){
            case 0:
                return new OnboardingFragment1();
            case 1:
                return new OnboardingFragment2();
            case 2:
                return new OnboardingFragment3();
            default:
                return new OnboardingFragment1();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
