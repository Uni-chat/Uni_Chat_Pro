package com.leonard.unichat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOpenHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "uni_chat.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getData(String sql){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery(sql, null);
    }

    public boolean checkStduentDatabase(String id, String date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM student WHERE s_std_reg_id='"+id+"' AND s_std_birth_date = '"+date+"'", null);
        boolean exists = (cursor.getCount() > 0);
        if (exists) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkStduentAdminDatabase(String id, String date, String key) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery("SELECT * FROM admin_student WHERE ad_s_reg_id='"+id+"' AND ad_s_birth_date = '"+date+"' AND ad_s_key = '"+key+"'", null);
        boolean exists = (cursor.getCount() > 0);
        if (exists) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkTeacherDatabase(String id, String date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM teacher WHERE tc_reg_id='"+id+"' AND tc_birth_date = '"+date+"'", null);
        boolean exists = (cursor.getCount() > 0);
        if (exists) {
            return true;
        } else {
            return false;
        }
    }
}
