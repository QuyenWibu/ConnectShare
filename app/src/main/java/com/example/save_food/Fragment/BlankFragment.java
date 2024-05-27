package com.example.save_food.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.save_food.R;
import com.example.save_food.adapter.VPAdapter;
import com.example.save_food.adapter.ViewPagerItem;
import com.example.save_food.models.KhoangCachLocaitonSort;
import com.example.save_food.models.KhoangCachLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
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





        String[] images = {"https://firebasestorage.googleapis.com/v0/b/savefood-a697c.appspot.com/o/images_upload%2Fdb1a40b7-0fd2-4040-b742-bb495691c652?alt=media&token=c926166d-87e0-45a3-8b99-2c92da893813", "https://firebasestorage.googleapis.com/v0/b/savefood-a697c.appspot.com/o/images_upload%2FviIyekcOkqXWFUxPUlJKL4sSoRf2?alt=media&token=56135fbb-9045-4c6c-84d7-4adcfd552b6a"};
        String[] heading = {"Rau quả củ", "Thức ăn ngon"};
        String[] heading2 = {"Có nhiều loại hoa củ quả khác nhau như hoa cây đào, hoa lotus, hoa lan, hoa dâm bụt, hoa hồng, hoa hibiscus,... Mỗi loại mang theo một gam màu và hình dáng đặc trưng. Chúng nở suốt mùa xuân hè và mang lại vẻ đẹp cho không gian sống.","Hoa củ quả không chỉ đơn thuần để trang trí mà còn mang lại nhiều lợi ích như giảm căng thẳng, giúp hạ huyết áp và cải thiện chất lượng không khí trong nhà. Chưa kể mùi hương dễ chịu từ hoa cũng có tác dụng làm tăng sinh lực và mang niềm vui đến gia đình."};

        //viewPagerItemArrayList = new ArrayList<>();
//        for (int i = 0; i < images.length; i++) {
//            ViewPagerItem viewPagerItem = new ViewPagerItem(images[i], heading[i], heading2[i]);
//            viewPagerItemArrayList.add(viewPagerItem);
//        }


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