package com.leonard.unichat.Logfiles;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.leonard.unichat.Messages.MainActivity;
import com.leonard.unichat.R;
import com.leonard.unichat.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class LandingTwo extends Fragment {

    private View view;
    //private EditText dateOfBirthPicker;
    private Calendar myCalender;
    private Button btnGlobalSignup, btnGlobalSignin ;
    private Switch switchTeacher, switchStudentAdmin, switchStudent;
    private static FragmentManager fragmentManager;
    public static String deptType;
    private FirebaseAuth mAuth;

    public LandingTwo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_landing_two, container, false);



        initViews();
        stdItemEnable();
        return view;

    }

    private void initViews () {

        mAuth = FirebaseAuth.getInstance();

        btnGlobalSignup = (Button) view.findViewById(R.id.btnGlobalSignUp);
        btnGlobalSignin = (Button) view.findViewById(R.id.btnGlobalSignIn);

        switchTeacher = (Switch) view.findViewById(R.id.swTeacher);
        switchStudentAdmin = (Switch) view.findViewById(R.id.swStudentAdmin);
        switchStudent = (Switch) view.findViewById(R.id.swStudent);

        //dateOfBirthPicker = (EditText) view.findViewById(R.id.dateOfBirthPicker);

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

    // Registration Date Picker Public Method For All Fragment
    public void birthDatePicker (final EditText edtDateGlobalSignup, final Activity datePickerFragmentMoving) {

        myCalender = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                myCalender.set(Calendar.YEAR, year);
                myCalender.set(Calendar.MONTH, month);
                myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //updateLabel();
                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat( myFormat, Locale.US );
                edtDateGlobalSignup.setText(simpleDateFormat.format(myCalender.getTime()));
            }
        };

        edtDateGlobalSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog( datePickerFragmentMoving, date, myCalender.get(Calendar.YEAR),
                        myCalender.get(Calendar.MONTH), myCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }




//    private void updateLabel () {
//
//        String myFormat = "dd/MM/yyyy";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( myFormat, Locale.US );
//        edtDateGlobalSignup.setText(simpleDateFormat.format(myCalender.getTime()));
//    }


    // Deptartment Type for Spinner and Department Wise Registration
    public void spinnerDepartmentAdding(final Spinner deptpinner, final Activity spinFrgmntActivty) {

        //String [] departmentName = getResources().getStringArray(R.array.dept_name_arrays);
        String [] departmentName = {"CSE", "EEE", "Civil", "English", "BBA", "Hotel and Tourism", "Architecture" };
        ArrayAdapter <String> deptSpinAdapter = new ArrayAdapter<String>(spinFrgmntActivty, R.layout.spinner_view,
                R.id.txtSpinnerCustom, departmentName);

        deptpinner.setAdapter(deptSpinAdapter);


        deptpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:

                        deptType = "CSE";
                        break;

                    case 1:

                        deptType = "EEE";
                        break;

                    case 2:

                        deptType = "Civil";
                        break;

                    case 3:

                        deptType = "English";
                        break;

                    case 4:

                        deptType = "BBA";
                        break;

                    case 5:

                        deptType = "Hotel and Tourism";
                        break;

                    case 6:

                        deptType = "Architecture";
                        break;

                }

                // String code = String.valueOf(deptpinner.getSelectedItem());
                //Toast.makeText(spinFrgmntActivty, code, Toast.LENGTH_SHORT).show();
//                if (position == 0) {
//                    codeSpinner.setSelection(0);
//                }

//                Object item = parent.getItemAtPosition(position);
//                finalValues[0] = item.toString();
//                if (item != null) {
//                    Toast.makeText(spinFrgmntActivty, finalValues[0],
//                            Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();

        /*if (mAuth.getCurrentUser() != null) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), MainActivity.class));
        }*/

        
        //stdItemEnable();
    }
}





