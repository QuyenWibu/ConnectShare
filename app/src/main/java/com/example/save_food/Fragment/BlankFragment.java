package com.example.save_food.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.save_food.R;
import com.example.save_food.models.ThongTin_UpLoadClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class BlankFragment extends Fragment {

    public TextView tv_post;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        tv_post = view.findViewById(R.id.tv_post);

        return view;
    }
    public void setTextViewText(String text) {
        tv_post.setText(text);
    }
    //lấy dữ liệu từ firebase về và setText cho TextView trong Fragment
    public void GetDataFireBase(long childCount) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ThongTin_UpLoad/" + uid + "/" + childCount);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ThongTin_UpLoadClass thongTin_upLoadClass = snapshot.getValue(ThongTin_UpLoadClass.class);
                if(tv_post != null){
                    tv_post.setText(thongTin_upLoadClass.toString());
                }
                else{
                    Log.e("AAA", "Lỗi!!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}