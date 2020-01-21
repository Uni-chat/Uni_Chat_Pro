package com.leonard.unichat.Logfiles;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.leonard.unichat.R;



public class ForgetPassword extends Fragment {

    private View view;
    private FirebaseAuth firebaseAuth;
    private AutoCompleteTextView edtForgetEmail;
    private Button btnForgetPassword;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_forget_password, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        edtForgetEmail = (AutoCompleteTextView) view.findViewById(R.id.edtForgetEmail);
        btnForgetPassword = (Button) view.findViewById(R.id.btnForgetPassword);

        final String email = edtForgetEmail.getText().toString();

        btnForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String email = edtForgetEmail.getText().toString();
                forgetPassToGet(email);
            }
        });

        return view;
    }


    // Forget  Password Methods
    private void forgetPassToGet (String emailId) {

        firebaseAuth.sendPasswordResetEmail(emailId)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Check your Email to Reset Password! ",
                                    Toast.LENGTH_SHORT).show();
                        }else {

                            Toast.makeText(getActivity(), "There is an Error! ",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


}
