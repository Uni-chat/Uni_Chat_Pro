package com.leonard.unichat.Logfiles;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import com.leonard.unichat.R;

public class TeacherSignup extends Fragment {

    private View view;
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

        return view;
    }

    private void initViews () {


        dateOfBirthPicker = (AutoCompleteTextView) view.findViewById(R.id.dateOfBirthPicker);

    }


}
