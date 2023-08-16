package com.example.save_food;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class loginActivity extends AppCompatActivity {
    EditText edtemail, edtpass;
    FirebaseAuth auth;
    Button btnLog;
    LinearLayout OpenForgetPass, backSignup;

    ImageButton mfb;
    ImageButton mgg;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    CallbackManager mCallbackManager;
    ProgressDialog progressDialog;
    public static String SHARED_PREFS = "sharedPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        edtemail   = findViewById(R.id.email);
        edtpass   = findViewById(R.id.password);
        btnLog   = findViewById(R.id.btnlog);
        mgg   = findViewById(R.id.btngg);
        backSignup   = findViewById(R.id.backSignup);
        OpenForgetPass = findViewById(R.id.linerlayoutforgetpass);
//        LoginButton loginButton = findViewById(R.id.btnfb);
        mfb=findViewById(R.id.btnfb);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        checkbox();
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

//        loginButton.setReadPermissions("email", "public_profile");
//        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.d(TAG, "facebook:onSuccess:" + loginResult);
//                handleFacebookAccessToken(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d(TAG, "facebook:onCancel");
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d(TAG, "facebook:onError", error);
//            }
//        });
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(loginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(loginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        mfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(loginActivity.this, Arrays.asList("email", "public_profile"));
            }
        });
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log();
            }
        });
        mgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = gsc.getSignInIntent();
                startActivityForResult(signInIntent, 100);
            }
        });
        OpenForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginActivity.this, forgetPasswordActivity.class));
            }
        });
        backSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginActivity.this, registerActivity.class));
            }
        });

    }
    private void checkbox(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String check = sharedPreferences.getString("email", "");
        if(check.equals("true")){
            Toast.makeText(this, "Login succesfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
    private void log(){
        progressDialog.show();
        String email = edtemail.getText().toString().trim();
        String password = edtpass.getText().toString().trim();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", "true");
                            editor.apply();
                            startActivity(new Intent(loginActivity.this, MainActivity.class));
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(loginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            // When request code is equal to 100 initialize task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            // check condition
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e){
                Toast.makeText(loginActivity.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    FirebaseUser user = auth.getCurrentUser();
                    String email = user.getEmail();
                    String uid = user.getUid();
                    String name = user.getDisplayName();


                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("email", email);
                    hashMap.put("uid", uid);
                    hashMap.put("name", name);
                    hashMap.put("phone", "");
                    hashMap.put("image", "https://firebasestorage.googleapis.com/v0/b/savefood-a697c.appspot.com/o/imagedef%2Fimage.png?alt=media");

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Users");

                    reference.child(uid).setValue(hashMap);
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", "true");
                    editor.apply();
                    Toast.makeText(loginActivity.this, "Login",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(loginActivity.this, MainActivity.class));
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(loginActivity.this, "error",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(loginActivity.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        progressDialog.show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            String email = user.getEmail();
                            String uid = user.getUid();
                            String name = user.getDisplayName();


                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", name);
                            hashMap.put("phone", "");
                            hashMap.put("image", "https://firebasestorage.googleapis.com/v0/b/savefood-a697c.appspot.com/o/imagedef%2Fimage.png?alt=media");

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(uid).setValue(hashMap);
                            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", "true");
                            editor.apply();
                            startActivity(new Intent(loginActivity.this, MainActivity.class));

                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(loginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}