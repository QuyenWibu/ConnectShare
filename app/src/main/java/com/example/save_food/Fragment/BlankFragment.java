package com.example.save_food.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.save_food.R;
import com.example.save_food.adapter.VPAdapter;
import com.example.save_food.adapter.ViewPagerItem;
import com.example.save_food.models.KhoangCachLocaitonSort;
import com.example.save_food.models.KhoangCachLocation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class BlankFragment extends Fragment {
    private long childCount;
    ViewPager2 viewPager2;
    ArrayList<ViewPagerItem> viewPagerItemArrayList;
    ArrayList<KhoangCachLocation> khoangCachLocationList = new ArrayList<>();
    public List<KhoangCachLocaitonSort> khoangCachLocaitonSorts = new ArrayList<>();
    double khoangCach;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        viewPager2 = view.findViewById(R.id.viewPager2);

        Bundle bundle = getArguments();
        if (bundle != null) {
            // Get the list of objects from the Bundle
            ArrayList<KhoangCachLocation> myList = bundle.getParcelableArrayList("my_list");

            for (KhoangCachLocation obj : myList) {
                khoangCachLocationList.add(obj);
                double doubleValue = obj.getDistance();
                String stringValue = obj.getUid();
                // Use the data here
                Log.d("OOO", doubleValue + " - " + stringValue);
            }

        } else {
            // Handle the case when Bundle object is null
            Log.d("bundle", "bundle is null");
        }
        viewPagerItemArrayList = new ArrayList<>();
        VPAdapter vpAdapter = new VPAdapter(viewPagerItemArrayList, getActivity());
        viewPager2.setAdapter(vpAdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(2);
        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        HashSet<String> uniqueImageLinks = new HashSet<>();
        loadData(vpAdapter, uniqueImageLinks);






        return view;
    }
    private void loadData(VPAdapter vpAdapter, HashSet<String> uniqueImageLinks) {
        for (KhoangCachLocation location : khoangCachLocationList) {
            String uid = location.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("ThongTin_UpLoad/" + uid);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        String name = postSnapshot.child("tenDonHang").getValue(String.class);
                        String diaChi = postSnapshot.child("diaChi").getValue(String.class);
                        DataSnapshot imgSnapshot = postSnapshot.child("Ảnh");
                        for (DataSnapshot imgChild : imgSnapshot.getChildren()) {
                            String linkhinh = imgChild.child("linkHinh").getValue(String.class);
                            Log.d("Firebase", "value: " + linkhinh);
                            ViewPagerItem viewPagerItem = new ViewPagerItem(linkhinh, name, diaChi, uid);

                            // Kiểm tra xem ảnh đã tồn tại trong tập hợp chưa
                            if (!uniqueImageLinks.contains(linkhinh)) {
                                uniqueImageLinks.add(linkhinh);
                                viewPagerItemArrayList.add(viewPagerItem);
                            }
                        }
                    }
                    vpAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Error loading data", error.toException());
                }
            });
        }
    }
    private boolean containsViewPagerItem(List<ViewPagerItem> list, ViewPagerItem item) {
        for (ViewPagerItem viewPagerItem : list) {
            if (viewPagerItem.getImgaeId().equals(item.getImgaeId()) &&
                    viewPagerItem.getHeding().equals(item.getHeding()) &&
                    viewPagerItem.getHeding2().equals(item.getHeding2()) &&
                    viewPagerItem.getUid().equals(item.getUid())) {
                return true;
            }
        }
        return false;
    }
}