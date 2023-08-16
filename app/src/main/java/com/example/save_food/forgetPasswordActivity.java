package com.example.save_food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

public class forgetPasswordActivity extends AppCompatActivity {

    private Button btnForgetpass,Opensignup;
    private String email;
    private EditText Foremail;
    private FirebaseAuth mAuth;
    BeautifulProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mAuth = FirebaseAuth.getInstance();

        Foremail = findViewById(R.id.Foremail);
        btnForgetpass = findViewById(R.id.btnForgetpass);
        Opensignup = findViewById(R.id.Opensignup);
        progressDialog = new BeautifulProgressDialog(forgetPasswordActivity.this, BeautifulProgressDialog.withGIF, "Please wait");
        Uri myUri = Uri.fromFile(new File("//android_asset/gif_food_and_smile.gif"));
        progressDialog.setGifLocation(myUri);
        progressDialog.setLayoutColor(getResources().getColor(R.color.BeautifulProgressDialogBg));
        progressDialog.setMessageColor(getResources().getColor(R.color.white));
        Opensignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(forgetPasswordActivity.this ,loginActivity.class));
            }
        });
        btnForgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }


        });

    }



    private void validateData() {
        email = Foremail.getText().toString();
        if(email.isEmpty()){
            Foremail.setError("Hãy nhập email của bạn!!!!!");
        } else {
            forgetpass();
        }
    }

    private void forgetpass() {
        progressDialog.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(forgetPasswordActivity.this, "Hãy kiểm tra Email của bạn", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(forgetPasswordActivity.this, loginActivity.class));
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(forgetPasswordActivity.this, "Lỗi:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}