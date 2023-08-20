package com.example.save_food.Fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.save_food.BeautifulProgressDialog;
import com.example.save_food.R;
import com.example.save_food.profileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

public class ChangePasswordFragment extends Fragment {

    private View mView;
    private TextInputLayout edtNewPass, edtOldPass;
    private Button btnChangePass;
    BeautifulProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_change_password, container, false);
        initUi();
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strNewPassword = edtNewPass.getEditText().getText().toString().trim();
                String strOldPassword = edtOldPass.getEditText().getText().toString().trim();
                if (TextUtils.isEmpty(strOldPassword)){
                    Toast.makeText(getActivity(), "Hãy nhập password cũ!!!!", Toast.LENGTH_SHORT).show();
                    return;
                } if (strNewPassword.length()<6){
                    Toast.makeText(getActivity(), "Mật khẩu mới phải trên 6 kí tự!!!!", Toast.LENGTH_SHORT).show();
                }
                onClickChangePassword(strOldPassword, strNewPassword);
            }
        });
        return mView;
    }

    private void initUi(){
        progressDialog = new BeautifulProgressDialog(getActivity(), BeautifulProgressDialog.withGIF, "Please wait");
        Uri myUri = Uri.fromFile(new File("//android_asset/gif_food_and_smile.gif"));
        progressDialog.setGifLocation(myUri);
        progressDialog.setLayoutColor(getResources().getColor(R.color.BeautifulProgressDialogBg));
        progressDialog.setMessageColor(getResources().getColor(R.color.white));
        edtNewPass = mView.findViewById(R.id.edt_new_pass);
        edtOldPass = mView.findViewById(R.id.edt_old_pass);
        btnChangePass = mView.findViewById(R.id.btnChangePass);
    }

    private void onClickChangePassword(String strOldPassword, final String strNewPassword) {
        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), strOldPassword);
            user.reauthenticate(authCredential)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    user.updatePassword(strNewPassword)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                               progressDialog.dismiss();
                                                    Toast.makeText(getActivity(), "Mật khẩu đã được thay đổi!!!", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Không thể thay đổi mật khẩu", Toast.LENGTH_SHORT).show();
                        }
                    });

    }

//    private void reAuthenticate(){
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//// Get auth credentials from the user for re-authentication. The example below shows
//// email and password credentials but there are multiple possible providers,
//// such as GoogleAuthProvider or FacebookAuthProvider.
//        AuthCredential credential = EmailAuthProvider
//                .getCredential("user@example.com", "password1234");
//
//// Prompt the user to re-provide their sign-in credentials
//        user.reauthenticate(credential)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//                            onClickChangePassword();
//                        } else  {
//
//                        }
//                    }
//                });
//    }

}