package com.leonard.unichat.Messages;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.leonard.unichat.DatabaseOpenHelper;
import com.leonard.unichat.Logfiles.LandingPage;
import com.leonard.unichat.Logfiles.MyShare;
import com.leonard.unichat.R;

public class MainActivity extends AppCompatActivity {

    private AppBarLayout mainPageToolBar;

    private ViewPager myViewPager;
    private TabLayout MyTabLayout, tabsContent;
    private TabItem tabMessage, tabNotice, tabChatRequest;
    private TabAccessorAdaptar myTabAccessorAdaptar;
    public static String getUserType, txtSome;
    private Toolbar myToolbar;
    private FirebaseAuth firebaseAuth;
    public static String smText;
    private FirebaseAuth mAuth;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        smText = getUserType;

        if (getUserType != null){

            if (getUserType.equals("Student") || getUserType.equals("Teacher")){

                TabLayout.Tab tab = tabsContent.getTabAt(2);

                if(tab != null) {

                    tabsContent.removeTab(tab);

                }
            }
        } else {

            getUserType = txtSome;

            if (getUserType.equals("Student") || getUserType.equals("Teacher")){

                TabLayout.Tab tab = tabsContent.getTabAt(2);

                if(tab != null) {

                    tabsContent.removeTab(tab);

                }
            }

        }



    }

    private void initViews () {

        getUserType = MyShare.readLogin(MainActivity.this);

        if (getUserType != null) {



        } else {


        }



        mAuth = FirebaseAuth.getInstance();

        tabsContent = (TabLayout) findViewById(R.id.tabsContent);
        tabMessage = (TabItem) findViewById(R.id.tabMessage);
        tabNotice = (TabItem) findViewById(R.id.tabNotice);
        tabChatRequest = (TabItem) findViewById(R.id.tabChatRequest);
        myViewPager = (ViewPager) findViewById(R.id.viewPagerShow);
        myToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.myToolbar);


        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Uni Chat");


        //String nwSrrt = dbOpener.getDataAll().toString();
       // Log.i("TAG", nwSrrt);


        TabChangeMethods();

    }

   /* @Override
    protected void onRestart() {
        super.onRestart();

        getUserType = txtSome;
    }*/

    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        outState.putString("STRING_VALUE", getUserType);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //letsCreateToast("On RestoreSaved Instance Method");

        txtSome = savedInstanceState.getString("STRING_VALUE");

        //txtView.setText(stringValue);
    }*/

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
   /* @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Exit");
        builder.setMessage("Are You Sure?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }*/

    private void TabChangeMethods () {

        myTabAccessorAdaptar = new TabAccessorAdaptar (getSupportFragmentManager(),3);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.optoins_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.menu_profile_options :

                Intent profileIntent = new Intent(MainActivity.this, SettingsActivity.class);
                profileIntent.putExtra("TYPE_USER", getUserType);
                startActivity(profileIntent);
                Toast.makeText(MainActivity.this, "Profile Selected", Toast.LENGTH_SHORT).show();

                break;
            case R.id.manu_settings_options :

                //Toast.makeText(MainActivity.this, "Settings Selected", Toast.LENGTH_SHORT).show();

                Intent intentAbt = new Intent(MainActivity.this, DevInfo.class);
                startActivity(intentAbt);
                finish();

                break;
            case R.id.menu_log_out_options :



                MyShare.ClearData(MainActivity.this);
                signOut();

                Toast.makeText(MainActivity.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LandingPage.class);

                startActivity(intent);
                finish();

        }
        return true;

    }

    private void signOut () {

        firebaseAuth.getInstance().signOut();

        //SharedPreferences preferences =getSharedPreferences("Login", Context.MODE_PRIVATE);
        //MyShare.writeLogin(MainActivity.this, null);
       // editor.clear();
        //editor.commit();
        finish();
    }
}
