package com.leonard.unichat.Logfiles;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.leonard.unichat.R;


public class StudentSignup extends Fragment {

    private View view;
    private Spinner codeSpinner;
    private AutoCompleteTextView dateOfBirthPicker;

    public StudentSignup() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_student_signup, container, false);

        initViews();

        LandingTwo myLandingPageTwo = new LandingTwo();
        myLandingPageTwo.birthDatePicker( dateOfBirthPicker, getActivity());
        myLandingPageTwo.spinnerDepartmentAdding(codeSpinner, getActivity());
        return view;
    }

    private void initViews () {

        dateOfBirthPicker = (AutoCompleteTextView) view.findViewById(R.id.dateOfBirthPicker);

        codeSpinner = (Spinner) view.findViewById(R.id.codeSpinner);
    }


}
