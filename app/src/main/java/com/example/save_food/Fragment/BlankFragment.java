package com.example.save_food.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.save_food.R;


public class BlankFragment extends Fragment {

    public TextView tv_postt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        tv_postt = view.findViewById(R.id.tv_postt);

        return view;
    }
    //lấy dữ liệu từ firebase về và setText cho TextView trong Fragment
//    public void GetDataFireBase(long childCount) {
//        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("ThongTin_UpLoad/" + uid + "/" + String.valueOf(childCount));
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    ThongTin_UpLoadClass thongTin_upLoadClass = snapshot.getValue(ThongTin_UpLoadClass.class);
//                   Log.d("AAA", thongTin_upLoadClass.getTenDonHang());
//                    //tv_post.setText(thongTin_upLoadClass.toString());
//                        //Log.e("AAA", "Lỗi!!");
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }
}