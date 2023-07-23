package com.example.save_food;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.save_food.adapter.RecyclerApdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity implements RecyclerApdapter.CountOfImagesWhenRemoved, RecyclerApdapter.itemClickListener {

    RecyclerView recyclerView;
    Button btn_upload, btn_upload_complete;

    ArrayList<Uri> uri = new ArrayList<>();
    RecyclerApdapter adapter;

    private static final int Read_Permission = 101;
    private Uri imageuri;
    private int x;

    StorageReference  storageReference;

    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        recyclerView = findViewById(R.id.recyclerView_Images);
        btn_upload = findViewById(R.id.btn_upload);
        btn_upload_complete = findViewById(R.id.btn_upload_complete);

        adapter = new RecyclerApdapter(uri, getApplicationContext(), this, this);

        btn_upload_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uri.size()>0){
                    Intent intent = new Intent(UploadActivity.this, MainActivity.class);
                    startActivity(intent);
                    for(int i=0;i<uri.size();i++){
                        uploadToFirebase(i);
                    }
                }
                else{
                    Toast.makeText(UploadActivity.this, "Bạn chưa chọn ảnh nào!", Toast.LENGTH_SHORT).show();
                }
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

        if (ContextCompat.checkSelfPermission(UploadActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UploadActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);
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

    private void uploadToFirebase(int i) {
        final String randomName = UUID.randomUUID().toString();
        // Create a reference to "images_upload/"
        storageReference = FirebaseStorage.getInstance().getReference().child("images_upload/" + randomName);
        storageReference.putFile(uri.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UploadActivity.this, "Images Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, "Uploading failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

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
}