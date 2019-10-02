package com.leonard.unichat.Logfiles;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leonard.unichat.R;


public class TeacherLogin extends Fragment {


    private String txtPassColor;
    private View view;
    private TextView forgtPass;
//android:text=" Don't you remember your password ? "
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

        txtPassColor = "<font color = white > Don't you remember your </font> <font color = yellow > password ? </font>";
        forgtPass.setText(Html.fromHtml(txtPassColor));
    }


}
