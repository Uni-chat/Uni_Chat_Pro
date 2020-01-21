package com.leonard.unichat.Logfiles;

import android.app.ProgressDialog;
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


public class StudentAdminLogin extends Fragment {


    private View view;
    private AutoCompleteTextView edtStdAdminLoginEmail, edtStdAdminLoginPass;
    private String txtPassColor;
    private TextView forgtPass;
    private Button signInValue;
    private String usType = "Admin";
    private String currentUserUID, userType;
    private CheckBox rememberChechBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPreferenceEditor;
    private boolean saveLoginState;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference checkUserRef;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private static FragmentManager fragmentManager;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ProgressDialog pgDailog;



    public StudentAdminLogin() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_student_admin_login, container, false);
        initViews();
        return view;
    }


    private void initViews () {

        edtStdAdminLoginEmail = (AutoCompleteTextView) view.findViewById(R.id.edtStdAdminLoginEmail);
        edtStdAdminLoginPass = (AutoCompleteTextView) view.findViewById(R.id.edtStdAdminLoginPass);
        forgtPass = (TextView) view.findViewById(R.id.forgtPass);
        signInValue = (Button) view.findViewById(R.id.signInValue);

        pgDailog = new ProgressDialog(getContext());
        pgDailog.setTitle("Processing...");
        pgDailog.setMessage("Please wait...");
        pgDailog.setCancelable(false);
        pgDailog.setIndeterminate(true);

        // CheckBox implementation
        rememberChechBox = (CheckBox) view.findViewById(R.id.rememberChechBox);
        loginPreferences = getActivity().getSharedPreferences("loginPrefsData", Context.MODE_PRIVATE);
        loginPreferenceEditor = loginPreferences.edit();

        // CheckBox Preferences
        saveLoginState = loginPreferences.getBoolean("saveLoginState", true);
        if (saveLoginState == true) {
            edtStdAdminLoginEmail.setText(loginPreferences.getString("Username",null ));
            edtStdAdminLoginPass.setText(loginPreferences.getString("Password", null));
        }


      fragmentManager = getActivity().getSupportFragmentManager();

        firebaseAuth = firebaseAuth.getInstance();

       /* mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null) {

                    try {

                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.putExtra("US_REF", usType);
                        startActivity(intent);

                        Toast.makeText(getActivity(), "Log in Success.", Toast.LENGTH_SHORT).show();
                        getActivity().finish();

                    } catch (NullPointerException e) {


                    }



                }
            }
        };*/

        fragmentManager = getActivity().getSupportFragmentManager();

      txtPassColor = "<font color = white > Don't you remember your </font> <font color = yellow > password ? </font>";
      forgtPass.setText(Html.fromHtml(txtPassColor));

        signInValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                adminLoginToApp();
                pgDailog.show();
            }
        });

        forgtPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager.beginTransaction().replace(R.id.frameContainer,
                        new ForgetPassword(), Utils.ForgetPassword).commit();
            }
        });
    }

   /* @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }*/

    private void adminLoginToApp () {

        String adminUserLoginEmail = edtStdAdminLoginEmail.getText().toString().trim();
        String adminUserLoginPassword = edtStdAdminLoginPass.getText().toString().trim();

        if (TextUtils.isEmpty(adminUserLoginEmail)) {

            Toast.makeText( getActivity(), " Enter Email Address !",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(adminUserLoginPassword)) {
            Toast.makeText(getActivity(), " Enter Your Password !", Toast.LENGTH_SHORT).show();
            return;
        }

        // Remember Checked
        if (rememberChechBox.isChecked()){
            loginPreferenceEditor.putBoolean("saveLoginState", true);
            loginPreferenceEditor.putString("Username", adminUserLoginEmail);
            loginPreferenceEditor.putString("Password", adminUserLoginPassword);
            loginPreferenceEditor.commit();
        }

        firebaseAuth.signInWithEmailAndPassword(adminUserLoginEmail, adminUserLoginPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {

                        firebaseDatabase = FirebaseDatabase.getInstance();
                        checkUserRef = firebaseDatabase.getReference("Users/Admin");

                        if (task.isSuccessful())  {

                        currentUserUID = firebaseAuth.getCurrentUser().getUid();

                        checkUserRef.child(currentUserUID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    userType = dataSnapshot.child("USER_TYPE").getValue().toString();

                                    if (userType.equals(usType)) {

                                        if (task.isSuccessful()) {



                                            try {

                                                MyShare.writeLogin(getContext(), usType);
                                                Intent intent = new Intent(getContext(), MainActivity.class);
                                                intent.putExtra("US_REF", usType);
                                                startActivity(intent);

                                                Toast.makeText(getActivity(), "Log in Success.", Toast.LENGTH_SHORT).show();
                                                getActivity().finish();


                                            } catch (Exception e) {

                                                e.printStackTrace();
                                            }

                                            pgDailog.dismiss();

                                        } else {

                                            pgDailog.dismiss();
                                            Toast.makeText(getActivity(), "Log in Failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {

                                        pgDailog.dismiss();
                                        FirebaseAuth.getInstance().signOut();
                                        Toast.makeText(getActivity(), "Your are not Registered as Admin", Toast.LENGTH_SHORT).show();

                                    }
                                } else {

                                    pgDailog.dismiss();
                                    FirebaseAuth.getInstance().signOut();
                                    Toast.makeText(getActivity(), "Your are not Registered as Admin", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                            pgDailog.dismiss();
                        }

                    }
                });
    }
}
