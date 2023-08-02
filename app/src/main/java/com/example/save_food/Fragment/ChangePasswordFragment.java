package com.example.save_food.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.save_food.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {

    private View mView;
    private TextInputLayout edtNewPass;
    private Button btnChangePass;
    ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_change_password, container, false);
        initUi();
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChangePassword();
            }
        });
        return mView;
    }

    private void initUi(){
        progressDialog = new ProgressDialog(getActivity());
        edtNewPass = mView.findViewById(R.id.edt_new_pass);
        btnChangePass = mView.findViewById(R.id.btnChangePass);
    }

    private void onClickChangePassword() {
            String strNewPassword = edtNewPass.getEditText().getText().toString().trim();
            progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(strNewPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Mật khẩu của bạn đã được thay đổi!!!!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
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