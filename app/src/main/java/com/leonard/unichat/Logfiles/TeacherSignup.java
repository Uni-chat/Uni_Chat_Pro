package com.leonard.unichat.Logfiles;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leonard.unichat.R;

import java.util.HashMap;

public class TeacherSignup extends Fragment {

    private View view;
    private Spinner codeSpinner;
    private AutoCompleteTextView edtTcName, edtTcID, edtTcEmail, edtTcPass, edtTcPassConfirm, dateOfBirthPicker;
    private Button signUpValue;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseDatabase firebaseDatabaseInstance;
    private DatabaseReference databaseReference;

    //private String deptType;

    public TeacherSignup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_teacher_signup, container, false);

        initViews();

        LandingTwo myLandingPageTwo = new LandingTwo();
        myLandingPageTwo.birthDatePicker( dateOfBirthPicker, getActivity());

        myLandingPageTwo.spinnerDepartmentAdding(codeSpinner, getActivity());

        //Toast.makeText(getActivity(), deptType, Toast.LENGTH_LONG).show();


        return view;
    }

    private void initViews () {


        edtTcName = (AutoCompleteTextView) view.findViewById(R.id.edtTcName);
        edtTcID = (AutoCompleteTextView) view.findViewById(R.id.edtTcID);
        edtTcEmail = (AutoCompleteTextView) view.findViewById(R.id.edtTcEmail);
        edtTcPass = (AutoCompleteTextView) view.findViewById(R.id.edtTcPass);
        edtTcPassConfirm = (AutoCompleteTextView) view.findViewById(R.id.edtTcPassConfirm);
        dateOfBirthPicker = (AutoCompleteTextView) view.findViewById(R.id.dateOfBirthPicker);
        codeSpinner = (Spinner) view.findViewById(R.id.codeSpinner);

        signUpValue = (Button) view.findViewById(R.id.signUpValue);


        FirebaseApp.initializeApp(getActivity());
        firebaseDatabaseInstance = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabaseInstance.getReference();
        firebaseAuth = FirebaseAuth.getInstance();


       // LandingPage.fireBaseInitViews(getActivity());

        signUpValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUSer();
            }
        });
    }




    // Firebase User Regisatration
    private void registerUSer () {

        String name = edtTcName.getText().toString().trim();
        String ids = edtTcID.getText().toString().trim();
        String email = edtTcEmail.getText().toString().trim();
        String password = edtTcPass.getText().toString().trim();
        String confirmPass = edtTcPassConfirm.getText().toString().trim();
        String dob = dateOfBirthPicker.getText().toString().trim();
        //String spinValue = codeSpinner.setSelection();
//
//        if (TextUtils.isEmpty(name)) {
//            Toast.makeText(getActivity(), "Please Enter Your Name", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (TextUtils.isEmpty(ids)) {
//            Toast.makeText(getActivity(), "Please Enter Your ID", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (TextUtils.isEmpty(email)) {
//            Toast.makeText(getActivity(), "Please Enter Your Email", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (TextUtils.isEmpty(password)) {
//            Toast.makeText(getActivity(), "Please Enter Your Password", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (TextUtils.isEmpty(confirmPass)) {
//            Toast.makeText(getActivity(), "Please Enter Your Confirm Password", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!confirmPass.equals(password)) {
//            Toast.makeText(getActivity(), "Password do not match", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//
//        if (TextUtils.isEmpty(dob)) {
//            Toast.makeText(getActivity(), "Please Enter Your Date of Birth", Toast.LENGTH_SHORT).show();
//            return;
//        }



        // Main Inner Methods of Creating Account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                saveUserData();
                Toast.makeText(getActivity()," Registered ", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed to Register",Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Firebase Database of Storing user Data
    private void saveUserData () {


        String name = edtTcName.getText().toString().trim();
        String ids = edtTcID.getText().toString().trim();
        String email = edtTcEmail.getText().toString().trim();
        String password = edtTcPass.getText().toString().trim();
        String dob = dateOfBirthPicker.getText().toString().trim();

        HashMap < String , String> dataMap = new HashMap<String, String>();
        dataMap.put("Name : ", name);
        dataMap.put("ID : ", ids);
        dataMap.put("Email : ", email);
        dataMap.put("Password :", password);
        dataMap.put("Date Of Birth :", dob);

        final String userID = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference currentUserInfo = FirebaseDatabase.getInstance()
                .getReference("Users/Teachers/" + LandingTwo.deptType).child(userID);

        currentUserInfo.setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), " Saved ", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), userID , Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}

