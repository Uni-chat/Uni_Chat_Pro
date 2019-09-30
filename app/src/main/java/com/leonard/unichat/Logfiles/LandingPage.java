package com.leonard.unichat.Logfiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.leonard.unichat.R;
import com.leonard.unichat.Utils;

public class LandingPage extends AppCompatActivity {

    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        fragmentManager = getSupportFragmentManager();

        if(savedInstanceState == null) {

            fragmentManager.beginTransaction().replace(R.id.frameContainer, new LandingTwo(),
                    Utils.LandingTwo).commit();
        }


    }
}
