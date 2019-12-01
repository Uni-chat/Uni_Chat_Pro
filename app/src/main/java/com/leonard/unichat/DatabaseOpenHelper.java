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

    public boolean check(String id, String date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM teacher WHERE tc_reg_id='"+id+"' AND tc_birth_date = '"+date+"'", null);
        boolean exists = (cursor.getCount() > 0);
        if (exists) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkDataBase() {
        /*File dbFile = myContext.getDatabasePath(DATABASE_NAME);
        if (dbFile.exists()) return true;
        if (!dbFile.getParentFile().exists()) {
            dbFile.getParentFile().mkdirs();
        }*/
        return false;
    }

    public ArrayList getDataAll() {

        //SQLiteDatabase db = this.getReadableDatabase();

        /*if (checkDataBase()== true) {
            SQLiteDatabase db = this.getReadableDatabase();

            ArrayList <String> arrayList = new ArrayList<>();

            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_TEACHER, null );
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                arrayList.add(cursor.getString(cursor.getColumnIndex(TEACHER_COLUMN_USER_REG_ID)));
                cursor.moveToNext();
            }



        return arrayList;
    } else {

            return null;
        }*/
        return null;
    }

    public boolean checkUserTeacherDOB(String tcRegID, String tcDOB) {

        /*String[] columnsSTD = {TEACHER_COLUMN_USER_ID};

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = TEACHER_COLUMN_USER_REG_ID + " =?" + " AND " + TEACHER_COLUMN_USER_DOB + " =?";
        //String selection = TEACHER_COLUMN_USER_REG_ID  + " AND " + TEACHER_COLUMN_USER_DOB;

        String[] selectionArgs = {tcRegID, tcDOB};

//        Cursor cursor = db.query(TABLE_NAME_TEACHER,
//                columnsSTD,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                null);

        Cursor cursor = db.rawQuery("SELECT tc_id FROM teacher where tc_reg_id AND tc_birth_date", selectionArgs);

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {

            return true;
        }*/

        return false;
    }

}
