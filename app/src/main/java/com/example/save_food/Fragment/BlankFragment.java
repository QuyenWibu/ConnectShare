package com.example.save_food.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.save_food.R;
import com.example.save_food.adapter.VPAdapter;
import com.example.save_food.adapter.ViewPagerItem;

import java.util.ArrayList;


public class BlankFragment extends Fragment {

    ViewPager2 viewPager2;
    ArrayList<ViewPagerItem> viewPagerItemArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);


        viewPager2  = view.findViewById(R.id.viewPager2);
        int[] images = {R.drawable.a,R.drawable.b};
        String[] heading = {"Rau quả củ", "Thức ăn ngon"};
        String[] heading2 = {"Có nhiều loại hoa củ quả khác nhau như hoa cây đào, hoa lotus, hoa lan, hoa dâm bụt, hoa hồng, hoa hibiscus,... Mỗi loại mang theo một gam màu và hình dáng đặc trưng. Chúng nở suốt mùa xuân hè và mang lại vẻ đẹp cho không gian sống.","Hoa củ quả không chỉ đơn thuần để trang trí mà còn mang lại nhiều lợi ích như giảm căng thẳng, giúp hạ huyết áp và cải thiện chất lượng không khí trong nhà. Chưa kể mùi hương dễ chịu từ hoa cũng có tác dụng làm tăng sinh lực và mang niềm vui đến gia đình."};

        viewPagerItemArrayList = new ArrayList<>();
        for (int i=0;i<images.length;i++){
            ViewPagerItem viewPagerItem = new ViewPagerItem(images[i],heading[i],heading2[i] );
            viewPagerItemArrayList.add(viewPagerItem);
        }
        VPAdapter vpAdapter = new VPAdapter(viewPagerItemArrayList);

        viewPager2.setAdapter(vpAdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(2);
        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        return view;
    }
}