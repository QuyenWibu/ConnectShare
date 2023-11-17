package com.example.save_food;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.HashMap;

public class registerActivity extends AppCompatActivity {

    EditText edtemail, edtpass;
    FirebaseAuth auth;
    Button btnLog;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private final int FINE_PERMISSION_CODE = 1;
    TextView acc;
    BeautifulProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        acc = findViewById(R.id.acc);
        edtemail   = findViewById(R.id.email);
        edtpass   = findViewById(R.id.password);
        btnLog   = findViewById(R.id.btnlog);
        dialog = new BeautifulProgressDialog(registerActivity.this, BeautifulProgressDialog.withGIF, "Please wait");
        Uri myUri = Uri.fromFile(new File("//android_asset/gif_food_and_smile.gif"));
        dialog.setGifLocation(myUri);
        dialog.setLayoutColor(getResources().getColor(R.color.BeautifulProgressDialogBg));
        dialog.setMessageColor(getResources().getColor(R.color.white));
        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registerActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reg();
            }
        });
    }

    private void Reg(){
        dialog.show();
        String email = edtemail.getText().toString().trim();
        String password = edtpass.getText().toString().trim();
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null;
                            String email = user.getEmail();
                            String uid = user.getUid();

                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", "user_name");
                            hashMap.put("onlineStatus", "online");
                            hashMap.put("typingTo", "noOne");
                            hashMap.put("phone", "");
                            hashMap.put("image", "https://firebasestorage.googleapis.com/v0/b/savefood-a697c.appspot.com/o/imagedef%2Fimage.png?alt=media");

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");

                            reference.child(uid).setValue(hashMap);

                            startActivity(new Intent(registerActivity.this, loginActivity.class));
                        } else {
                            dialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(registerActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}