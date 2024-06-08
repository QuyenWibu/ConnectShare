package com.example.save_food;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.save_food.adapter.RecyclerApdapter;
import com.example.save_food.models.HinhAnh_Upload;
import com.example.save_food.models.ThongTin_UpLoadClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity implements RecyclerApdapter.CountOfImagesWhenRemoved, RecyclerApdapter.itemClickListener, AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    Button btn_upload, btn_upload_complete;
    TextView tv_post;
    ArrayList<Uri> uri = new ArrayList<>();
    RecyclerApdapter adapter;
    private long childCount;
    private ArrayList<String> imageUrls = new ArrayList<>();
    private String S="";
    private Spinner spinner, spinner2;
    private static final int Read_Permission = 101;
    private Uri imageuri;
    private int x;
    private String valueFromSpinner, valueFromSpinner2;
    StorageReference  storageReference;
    FragmentManager fragmentManager;
    ActivityResultLauncher<Intent> activityResultLauncher;
    DatabaseReference mData;
    Button btn_mua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        tv_post = findViewById(R.id.tv_posttt);
        EditText TenDonHang_Upload, DonGia_Upload, ThoiGianHetHan_Upload, DiaChi_Upload;
        TenDonHang_Upload = findViewById(R.id.Ten_Don_Hang_Upload);
//        DonGia_Upload = findViewById(R.id.Don_Gia_Upload);
        ThoiGianHetHan_Upload = findViewById(R.id.ThoiGianHetHan_Upload);
        DiaChi_Upload = findViewById(R.id.DiaChi_Upload);

        spinner2= findViewById(R.id.list_item_thoihan);
        spinner = findViewById(R.id.list_item_nganhhang);
        recyclerView = findViewById(R.id.recyclerView_Images);
        btn_upload = findViewById(R.id.btn_upload);
        btn_upload_complete = findViewById(R.id.btn_upload_complete);

        mData = FirebaseDatabase.getInstance().getReference();

        String[] list_nganhhang = getResources().getStringArray(R.array.list_nganhhang);
        ArrayAdapter adapter_list = new ArrayAdapter(this, android.R.layout.simple_spinner_item,list_nganhhang);
        adapter_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter_list);
        spinner.setOnItemSelectedListener(this);

        String[] list_thoigian = getResources().getStringArray(R.array.list_thoigian);
        ArrayAdapter adapter_list2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,list_thoigian);
        adapter_list2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter_list2);
        spinner2.setOnItemSelectedListener(this);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valueFromSpinner2 = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // không có gì được chọn
            }
        });

        adapter = new RecyclerApdapter(uri, getApplicationContext(), this, this);
        childCount=0;
        //Khi người dùng ấn vào nút "đăng tải" -> Dữ liệu sẽ được gửi lên firebase.
        btn_upload_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uri.size()>0){
//                    Intent intent = new Intent(UploadActivity.this, MainActivity.class);
//                    startActivity(intent);
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    // Lấy tham chiếu đến "ThongTin_UpLoad"
                    DatabaseReference thongtin_upload = mData.child("ThongTin_UpLoad").child(uid);
                    // Lấy dữ liệu từ "ThongTin_UpLoad" bằng phương thức get()
                    thongtin_upload.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                //String downloadUrl = task.getResult().getDownloadUrl().toString();
                                // Lấy UID của người dùng hiện tại
                                // Lấy số lượng nút con trong "ThongTin_UpLoad"
                                childCount = task.getResult().getChildrenCount();
                                Log.d("Soluong", String.valueOf(childCount));
                                //đưa dữ liệu lên firebase khi upload
                                if(!valueFromSpinner2.isEmpty() && !valueFromSpinner.isEmpty()
                                        && !TenDonHang_Upload.getText().toString().isEmpty()
//                                        && !TextUtils.isEmpty(DonGia_Upload.getText().toString())
                                        && !DiaChi_Upload.getText().toString().isEmpty()
                                        && !ThoiGianHetHan_Upload.getText().toString().isEmpty() ){
                                    ThongTin_UpLoadClass thongTin_upLoadClass = new ThongTin_UpLoadClass(
                                            TenDonHang_Upload.getText().toString(),
//                                            Integer.parseInt(DonGia_Upload.getText().toString()),
                                            DiaChi_Upload.getText().toString(),
                                            valueFromSpinner,
                                            ThoiGianHetHan_Upload.getText().toString(), valueFromSpinner2);
                                    mData.child("ThongTin_UpLoad").child(uid).child(childCount+1+"").setValue(thongTin_upLoadClass);
                                    for(int i=0;i<uri.size();i++){
                                        uploadToFirebase(uri.get(i));
                                    }
                                    onBackPressed(); finish();
                                    GetDataFireBase();
                                    Toast.makeText(UploadActivity.this, "Đã đăng tải thành công!", Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    Toast.makeText(UploadActivity.this, "Vui lòng thêm 1 ảnh và điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                                }
                                //  tv_post.setText(thongTin_upLoadClass.getTenDonHang());

                                //truyền dữ liệu sang fragment_blank
//                                fragmentManager = getSupportFragmentManager();
//                                BlankFragment blankFragment = (BlankFragment) fragmentManager.findFragmentById(R.id.container);
//                                blankFragment.tv_postt.setText(S);
                            } else {
                                // Xử lý lỗi
                                Toast.makeText(UploadActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
                                // ...
                            }
                        }
                    });

                    //lấy dữ liệu từ firebase về và đăng lên home
//                    BlankFragment blankFragment = new BlankFragment();
//                    blankFragment.GetDataFireBase(childCount+1);
                    //GetDataFireBase();
                    //tv_post.setText(ThongTin_UpLoadClass.getTenDonHang().toString());
                }
                else{
                    Toast.makeText(UploadActivity.this, "Bạn chưa chọn ảnh nào!", Toast.LENGTH_SHORT).show();
                }
//                Intent intent = new Intent(UploadActivity.this, homeFragment.class);
//                startActivity(intent); finish();

            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    if (result.getData().getClipData() != null) {
                        x = result.getData().getClipData().getItemCount();
                        for (int i = 0; i < x; i++) {
                            imageuri = result.getData().getClipData().getItemAt(i).getUri();
                            uri.add(imageuri);
                            //uploadToFirebase();
                        }
                        Toast.makeText(UploadActivity.this, "Bạn đã chọn ảnh thành công!", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();

                    } else {
                        imageuri = result.getData().getData();
                        uri.add(imageuri);
                        // uploadToFirebase();
                        Toast.makeText(UploadActivity.this, "Bạn đã chọn ảnh thành công!", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(UploadActivity.this, "Bạn không chọn ảnh nào!", Toast.LENGTH_SHORT).show();
                }



                recyclerView.setLayoutManager(new GridLayoutManager(UploadActivity.this, 1, GridLayoutManager.HORIZONTAL, false));
                //               recyclerView.setLayoutManager(new LinearLayoutManager(UploadActivity.this));
                recyclerView.setAdapter(adapter);

            }
        });

        if (ContextCompat.checkSelfPermission(UploadActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UploadActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);
        }

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                activityResultLauncher.launch(intent);
            }
        });


    }

    public void GetDataFireBase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ThongTin_UpLoad/" + uid + "/" + String.valueOf(childCount+1));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ThongTin_UpLoadClass thongTin_upLoadClass = snapshot.getValue(ThongTin_UpLoadClass.class);
                    Log.d("AAA", thongTin_upLoadClass.getTenDonHang());
                    Log.d("AAA", thongTin_upLoadClass.getDiaChi());
                    S=thongTin_upLoadClass.toStringg();
                    //tv_post.setText(thongTin_upLoadClass.getTenDonHang() + " - " + thongTin_upLoadClass.getDiaChi() + " + " + thongTin_upLoadClass.getThoiGianHetHan());
                    //Log.e("AAA", "Lỗi!!");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void uploadToFirebase(Uri imageUri) {

        final String randomName = UUID.randomUUID().toString();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Create a reference to "images_upload/"
        storageReference = FirebaseStorage.getInstance().getReference().child("images_upload/" + uid + "/" + randomName);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri downloadUrl) {
                                        HinhAnh_Upload hinhAnh_upload = new HinhAnh_Upload(downloadUrl.toString());

                                        mData.child("ThongTin_UpLoad")
                                                .child(uid)
                                                .child(String.valueOf(childCount + 1))
                                                .child("Ảnh").push().setValue(hinhAnh_upload, new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                        if (error == null) {
                                                            Toast.makeText(UploadActivity.this, "Lưu dữ liệu thành công!", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(UploadActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        Toast.makeText(UploadActivity.this, "Images Uploaded", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadActivity.this, "Uploading failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

//    private void GetUrlImageUpload(String randomName){
//        // Lấy đường dẫn URL của ảnh đã tải lên
//
//        storageReference.child("images_upload/" + randomName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri downloadUrl) {
//                // Lưu trữ thông tin ảnh vào Realtime Database
//                String HinhAnh_UpLoad = downloadUrl.toString();
//                // Tạo tham chiếu đến thư mục "images" trong "ThongTin_UpLoad"
//                DatabaseReference imagesRef = FirebaseDatabase.getInstance().getReference().child("ThongTin_UpLoad").
//                        child("người: " + (childCount + 1))
//                        .child("Ảnh:");
//                // Đẩy một nút con mới dưới "images" và lưu trữ URL hình ảnh
//                imagesRef.push().setValue(HinhAnh_UpLoad);
//            }
//        });
//    }

    @Override
    public void clicked(int getSize) {

    }

    @Override
    public void itemClick(int position) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_image_zoom);
        TextView textView = dialog.findViewById(R.id.text_dialog_img);
        ImageView imageView = dialog.findViewById(R.id.img_view_dialog);
        Button buttonClose = dialog.findViewById(R.id.btn_close_img_dialog);

        textView.setText("Ảnh " + position);

        imageView.setImageURI(uri.get(position));

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        valueFromSpinner = parent.getItemAtPosition(position).toString();
        Toast.makeText(UploadActivity.this, valueFromSpinner, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}