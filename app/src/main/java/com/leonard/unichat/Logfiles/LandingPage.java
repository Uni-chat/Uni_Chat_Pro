package com.leonard.unichat.Logfiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leonard.unichat.DatabaseOpenHelper;
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

            fragmentManager.beginTransaction().replace(R.id.frameContainer, new SplashToGo(),
                    Utils.SplashToGo).commit();
        }
    }




    protected void replaceFragment () {

        fragmentManager.beginTransaction().replace(R.id.frameContainer, new LandingTwo(),
                Utils.LandingTwo).commit();
    }

    @Override
    public void onBackPressed () {
        Fragment ForgetPassword = fragmentManager.findFragmentByTag(Utils.ForgetPassword);
        Fragment StudentAdminLogin = fragmentManager.findFragmentByTag(Utils.StudentAdminSignin);
        Fragment StudentAdminSignup = fragmentManager.findFragmentByTag(Utils.StudentAdminSignup);
        Fragment StudentLogin = fragmentManager.findFragmentByTag(Utils.StudentSignin);
        Fragment StudentSignup = fragmentManager.findFragmentByTag(Utils.StudentSignup);
        Fragment TeacherLogin = fragmentManager.findFragmentByTag(Utils.TeacherSignin);
        Fragment TeacherSignup = fragmentManager.findFragmentByTag(Utils.TeacherSignup);

        if (ForgetPassword != null)
            replaceFragment();
        else if (StudentAdminLogin != null)
            replaceFragment();
        else if (StudentAdminSignup != null)
            replaceFragment();
        else if (StudentLogin != null)
            replaceFragment();
        else if (StudentSignup != null)
            replaceFragment();
        else if (TeacherLogin != null)
            replaceFragment();
        else if (TeacherSignup != null)
            replaceFragment();
        else
            super.onBackPressed();
    }

    public static void fireBaseInitViews (Context context) {

        FirebaseAuth firebaseAuth;
        FirebaseAuth.AuthStateListener firebaseAuthListener;
        FirebaseDatabase firebaseDatabaseInstance;
        DatabaseReference databaseReference;

        FirebaseApp.initializeApp(context);

        firebaseDatabaseInstance = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabaseInstance.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

    }


}
