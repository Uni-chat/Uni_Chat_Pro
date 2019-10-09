package com.leonard.unichat.Messages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAccessorAdaptar extends FragmentPagerAdapter {

    public TabAccessorAdaptar(@NonNull FragmentManager fm, int behavior) {

        super(fm, behavior);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch ( position ) {

            case 0:
                Message myMessage = new Message();
                return myMessage;

            case 1:
                Notice myNotice = new Notice();
                return myNotice;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {

        return 2;
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//
//        switch ( position ) {
//
//            case 0:
//                return "Message";
//
//            case 1:
//                return "Notice";
//
//            default:
//                return null;
//        }
//    }
}
