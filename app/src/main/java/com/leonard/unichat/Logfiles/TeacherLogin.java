package com.leonard.unichat.Logfiles;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.leonard.unichat.Messages.MainActivity;
import com.leonard.unichat.R;
import com.leonard.unichat.Utils;


public class TeacherLogin extends Fragment {

    private View view;
    private AutoCompleteTextView edtTcLoginEmail, edtTcLoginPass;
    private String txtPassColor;
    private TextView forgtPass;
    private Button signInValue;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private static FragmentManager fragmentManager;

    public TeacherLogin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_teacher_login, container, false);

        initViews();

        return view;
    }

    private void initViews () {

        edtTcLoginEmail = (AutoCompleteTextView) view.findViewById(R.id.edtTcLoginEmail);
        edtTcLoginPass = (AutoCompleteTextView) view.findViewById(R.id.edtTcLoginPass);
        forgtPass = (TextView) view.findViewById(R.id.forgtPass);
        signInValue = (Button) view.findViewById(R.id.signInValue);

        firebaseAuth = firebaseAuth.getInstance();
        fragmentManager = getActivity().getSupportFragmentManager();

        signInValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tcLoginToApp();
            }
        });





        txtPassColor = "<font color = white > Don't you remember your </font> <font color = yellow > password ? </font>";
        forgtPass.setText(Html.fromHtml(txtPassColor));

        forgtPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager.beginTransaction().replace(R.id.frameContainer,
                        new ForgetPassword(), Utils.ForgetPassword).commit();
            }
        });

    }


    private void tcLoginToApp () {

        String tcUserLoginEmail = edtTcLoginEmail.getText().toString().trim();
        String tcUserLoginPassword = edtTcLoginPass.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(tcUserLoginEmail, tcUserLoginPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);

                            Toast.makeText(getActivity(), "Log in Success.", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        } else {

                            Toast.makeText(getActivity(), "Log in Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
