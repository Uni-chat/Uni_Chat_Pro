package com.leonard.unichat.Logfiles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leonard.unichat.Messages.MainActivity;
import com.leonard.unichat.R;
import com.leonard.unichat.Utils;


public class TeacherLogin extends Fragment {

    private View view;
    private AutoCompleteTextView edtTcLoginEmail, edtTcLoginPass;
    private String txtPassColor;
    private TextView forgtPass;
    private Button signInValue;
    private String usType = "Teacher";
    private String currentUserUID, userType;
    private CheckBox rememberChechBox;
    private SharedPreferences loginPreferencesTc;
    private SharedPreferences.Editor loginPreferenceEditorTc;
    private boolean saveLoginStateTc;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference checkUserRef;
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

        // CheckBox implementation
        rememberChechBox = (CheckBox) view.findViewById(R.id.rememberChechBox);
        loginPreferencesTc = getActivity().getSharedPreferences("loginPrefsDataTc", Context.MODE_PRIVATE);
        loginPreferenceEditorTc = loginPreferencesTc.edit();

        // CheckBox Preferences
        saveLoginStateTc = loginPreferencesTc.getBoolean("saveLoginStateTc", true);
        if (saveLoginStateTc == true) {
            edtTcLoginEmail.setText(loginPreferencesTc.getString("UsernameTc",null ));
            edtTcLoginPass.setText(loginPreferencesTc.getString("PasswordTc", null));
        }

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

        if (TextUtils.isEmpty(tcUserLoginEmail)) {

            Toast.makeText( getActivity(), " Enter Email Address !",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(tcUserLoginPassword)) {
            Toast.makeText(getActivity(), " Enter Your Password !", Toast.LENGTH_SHORT).show();
            return;
        }

        // Remember Checked
        if (rememberChechBox.isChecked()){
            loginPreferenceEditorTc.putBoolean("saveLoginStateTc", true);
            loginPreferenceEditorTc.putString("UsernameTc", tcUserLoginEmail);
            loginPreferenceEditorTc.putString("PasswordTc", tcUserLoginPassword);
            loginPreferenceEditorTc.commit();
        }

        firebaseAuth.signInWithEmailAndPassword(tcUserLoginEmail, tcUserLoginPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {

                        firebaseDatabase = FirebaseDatabase.getInstance();
                        checkUserRef = firebaseDatabase.getReference("Users/Teachers");
                        currentUserUID = firebaseAuth.getCurrentUser().getUid();

                        checkUserRef.child(currentUserUID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    userType = dataSnapshot.child("USER_TYPE").getValue().toString();

                                    if (userType.equals(usType)) {

                                        if (task.isSuccessful()) {


                                            try {

                                                Intent intent = new Intent(getContext(), MainActivity.class);
                                                intent.putExtra("US_REF", usType);
                                                startActivity(intent);

                                                Toast.makeText(getActivity(), "Log in Success.", Toast.LENGTH_SHORT).show();
                                                getActivity().finish();


                                            } catch (Exception e) {

                                                e.printStackTrace();
                                            }

                                        } else {
                                            Toast.makeText(getActivity(), "Log in Failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {

                                        FirebaseAuth.getInstance().signOut();
                                        Toast.makeText(getActivity(), "Your are not Registered as Teacher", Toast.LENGTH_SHORT).show();

                                    }
                                } else {

                                    FirebaseAuth.getInstance().signOut();
                                    Toast.makeText(getActivity(), "Your are not Registered as Teacher", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
    }


}
