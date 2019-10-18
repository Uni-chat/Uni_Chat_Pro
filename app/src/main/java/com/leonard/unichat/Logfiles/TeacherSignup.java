package com.leonard.unichat.Logfiles;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.leonard.unichat.R;

public class TeacherSignup extends Fragment {

    private View view;
    private Spinner codeSpinner;
    private AutoCompleteTextView dateOfBirthPicker;

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

        return view;
    }

    private void initViews () {


        dateOfBirthPicker = (AutoCompleteTextView) view.findViewById(R.id.dateOfBirthPicker);
        codeSpinner = (Spinner) view.findViewById(R.id.codeSpinner);

//        String [] departmentName = getResources().getStringArray(R.array.dept_name_arrays);
//        ArrayAdapter <String> deptSpinAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_view,
//                R.id.txtSpinnerCustom, departmentName);
//        codeSpinner.setAdapter(deptSpinAdapter);
//
//        codeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String code = String.valueOf(codeSpinner.getSelectedItem());
//                if (position == 0) {
//                    codeSpinner.setSelection(0);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }


}
