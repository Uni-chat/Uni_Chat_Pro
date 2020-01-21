package com.leonard.unichat.Messages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.leonard.unichat.R;

public class DevInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_info);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(DevInfo.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
