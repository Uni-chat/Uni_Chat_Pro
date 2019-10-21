package com.leonard.unichat.Logfiles;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.leonard.unichat.R;
import com.leonard.unichat.Utils;


public class TeacherLogin extends Fragment {

    private View view;
    private String txtPassColor;
    private TextView forgtPass;
    private Button signInValue;
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

        forgtPass = (TextView) view.findViewById(R.id.forgtPass);
        fragmentManager = getActivity().getSupportFragmentManager();
        signInValue = (Button) view.findViewById(R.id.signInValue);


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


}
