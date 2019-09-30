package com.leonard.unichat.Logfiles;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.leonard.unichat.R;
import com.leonard.unichat.Utils;


public class LandingTwo extends Fragment {

   private View view;
   private Button btnGlobalSignup, btnGlobalSignin ;
   private Switch switchTeacher, switchStudentAdmin, switchStudent;
   private static FragmentManager fragmentManager;

    public LandingTwo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_landing_two, container, false);

        initViews();
        return view;

    }

    private void initViews () {

        btnGlobalSignup = (Button) view.findViewById(R.id.btnGlobalSignUp);
        btnGlobalSignin = (Button) view.findViewById(R.id.btnGlobalSignIn);

        switchTeacher = (Switch) view.findViewById(R.id.swTeacher);
        switchStudentAdmin = (Switch) view.findViewById(R.id.swStudentAdmin);
        switchStudent = (Switch) view.findViewById(R.id.swStudent);

        fragmentManager = getActivity().getSupportFragmentManager();


        switchStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stdItemEnable();
            }
        });

        switchStudentAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stdAdminItemEnable();
            }
        });

        switchTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tcItemEnable();
            }
        });

    }

    private void stdItemEnable () {

        switchStudent.setChecked(true);
        switchStudentAdmin.setChecked(false);
        switchTeacher.setChecked(false);

        btnGlobalSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager.beginTransaction().replace(R.id.frameContainer, new StudentSignup(),
                        Utils.StudentSignup).commit();
            }
        });

        btnGlobalSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frameContainer, new StudentLogin(),
                        Utils.StudentSignin).commit();
            }
        });
    }

    private void stdAdminItemEnable () {

        switchStudent.setChecked(false);
        switchStudentAdmin.setChecked(true);
        switchTeacher.setChecked(false);

        btnGlobalSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager.beginTransaction().replace(R.id.frameContainer, new StudentAdminSignup(),
                        Utils.StudentAdminSignup).commit();
            }
        });

        btnGlobalSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frameContainer, new StudentAdminLogin(),
                        Utils.StudentAdminSignin).commit();
            }
        });
    }

    private void tcItemEnable () {

        switchStudent.setChecked(false);
        switchStudentAdmin.setChecked(false);
        switchTeacher.setChecked(true);

        btnGlobalSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager.beginTransaction().replace(R.id.frameContainer, new TeacherSignup(),
                        Utils.TeacherSignup).commit();
            }
        });

        btnGlobalSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frameContainer, new TeacherLogin(),
                        Utils.TeacherSignin).commit();
            }
        });
    }
}





