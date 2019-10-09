package com.leonard.unichat.Messages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.leonard.unichat.R;

public class MainActivity extends AppCompatActivity {

    private AppBarLayout mainPageToolBar;

    private ViewPager myViewPager;
    private TabLayout MyTabLayout, tabsContent;
    private TabItem tabMessage, tabNotice;
    private TabAccessorAdaptar myTabAccessorAdaptar;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

    }

    private void initViews () {

        tabsContent = (TabLayout) findViewById(R.id.tabsContent);
        tabMessage = (TabItem) findViewById(R.id.tabMessage);
        tabNotice = (TabItem) findViewById(R.id.tabNotice);
        myViewPager = (ViewPager) findViewById(R.id.viewPagerShow);
        myToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.myToolbar);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Uni Chat");

        TabChangeMethods();
    }

    private void TabChangeMethods () {

        myTabAccessorAdaptar = new TabAccessorAdaptar (getSupportFragmentManager(),2);
        myViewPager.setAdapter(myTabAccessorAdaptar);
        myViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabsContent));

        tabsContent.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                myViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
