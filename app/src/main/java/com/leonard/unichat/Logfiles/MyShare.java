package com.leonard.unichat.Logfiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class MyShare {
    public static void writeLogin(Context context, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usType", value).apply();
    }

    public static void ClearData (Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usType", null).apply();
        editor.clear();
        editor.commit();
        Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();

    }

    public static String readLogin(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("usType", null);
        return value;
    }
}
