package com.leonard.unichat.Logfiles;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.leonard.unichat.R;
import com.leonard.unichat.Utils;

import java.util.Timer;
import java.util.TimerTask;


public class SplashToGo extends Fragment {

    private View view, vwImgSplash, vwImgRotation ;
    private Timer timer;
    private int i = 0;
    private TextView txtSplash;
    private Animation fromTop, fromBottom, rotationImage;
    final long period = 100;
    private static FragmentManager fragmentManager;

    public SplashToGo() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_splash_to_go, container, false);

        initViews();
        splashMethod();

        return view;
    }

    private void initViews () {

        vwImgSplash = view.findViewById(R.id.imgSplash);
        vwImgRotation =  view.findViewById(R.id.imgSplashRotation);

        txtSplash = (TextView) view.findViewById(R.id.txtSplash);
        fromTop = AnimationUtils.loadAnimation(getActivity(), R.anim.fromtop);
        fromBottom = AnimationUtils.loadAnimation(getActivity(), R.anim.frombottom);
        rotationImage = AnimationUtils.loadAnimation(getActivity(), R.anim.rotationimage);

        //vwImgRotation.setVisibility(View.GONE);
        vwImgRotation.setAnimation(rotationImage);
        vwImgSplash.setVisibility(View.GONE);
        //vwImgSplash.setAnimation(fromTop);

        txtSplash.setAnimation(fromBottom);

        fragmentManager = getActivity().getSupportFragmentManager();
    }

    private  void splashMethod () {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //this repeats every 100 ms
                if (i<98){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

//                            if ( i > 50) {
//
//                               //vwImgSplash.setVisibility(View.GONE);
//                              // vwImgRotation.setVisibility(View.VISIBLE);
//
//                            }


                        }

                    });
                    i=i+3;
                }else{
                    //closing the timer
                    timer.cancel();
                    fragmentManager.beginTransaction().replace(R.id.frameContainer, new LandingTwo(),
                            Utils.LandingTwo).commit();
                    // close this activity
                }
            }
        }, 0, period);

    }



}
