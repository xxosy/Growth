package com.growth.domain.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SSL-D on 2016-08-31.
 */

public class DBManager extends SQLiteOpenHelper {
    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user( _id integer primary key autoincrement, " +
                "usercode TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int getCount(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user;",null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public String getUserCode(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user;",null);
        cursor.moveToNext();
        String userCode = cursor.getString(1);
        cursor.close();
        db.close();
        return userCode;
    }

    public void createUserCode(String userCode){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("insert into user (`usercode`) values ('"+userCode+"')");
        db.close();
    }
}
