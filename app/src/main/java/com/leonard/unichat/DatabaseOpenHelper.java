package com.leonard.unichat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseOpenHelper extends SQLiteAssetHelper {


    private static final String DATABASE_NAME = "uni_chat.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME_STUDENT = "student";
    private static final String TABLE_NAME_ADMIN = "admin_student";
    private static final String TABLE_NAME_TEACHER = "teacher";

    private static final String STUDENT_COLUMN_USER_ID = "s_id";
    private static final String STUDENT_COLUMN_USER_REG_ID = "s_std_reg_id";
    private static final String STUDENT_COLUMN_USER_DOB = "s_std_birth_date";

    private static final String ADMIN_COLUMN_USER_ID = "ad_s_id";
    private static final String ADMIN_COLUMN_USER_REG_ID = "ad_s_reg_id";
    private static final String ADMIN_COLUMN_USER_DOB = "ad_s_birth_date";

    private static final String TEACHER_COLUMN_USER_ID = "tc_id";
    private static final String TEACHER_COLUMN_USER_REG_ID = "tc_reg_id";
    private static final String TEACHER_COLUMN_USER_DOB = "tc_birth_date";

    public DatabaseOpenHelper (Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getData (String sql) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery(sql, null);
    }


    public boolean checkUserTeacherDOB (String tcRegID, String tcDOB) {

        String[] columnsSTD = {TEACHER_COLUMN_USER_ID};

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
        }

        return false;
    }
}
