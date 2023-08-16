package com.example.save_food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.save_food.adapter.ViewPagerAdapter;

import me.relex.circleindicator.CircleIndicator;

public class OnboardingActivity extends AppCompatActivity {
private TextView tvskip;
private ViewPager viewPager;
private RelativeLayout layoutBottom;
private CircleIndicator circleIndicator;
private LinearLayout layoutNext;
private ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        String FirstTime = sharedPreferences.getString("FirstTimeInstall", "");
        if(FirstTime.equals("Yes")){
            startActivity(new Intent(OnboardingActivity.this, SplashActivity.class));

        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("FirstTimeInstall","Yes");
            editor.apply();
        }
        tvskip = findViewById(R.id.tv_skip);
        viewPager = findViewById(R.id.view_paper);
        layoutBottom = findViewById(R.id.layout_bottom);
        circleIndicator = findViewById(R.id.circle_indicator);
        layoutNext = findViewById(R.id.layout_next);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    viewPager.setAdapter(viewPagerAdapter);
    circleIndicator.setViewPager(viewPager);
    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
                if(position == 2){
                    tvskip.setVisibility(View.GONE);
                    layoutBottom.setVisibility(View.GONE);
                } else {
                    tvskip.setVisibility(View.VISIBLE);
                    layoutBottom.setVisibility(View.VISIBLE);
                }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    });
    tvskip.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(2);
        }
    });
    layoutNext.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(viewPager.getCurrentItem() < 2){
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        }
    });
    }
}