package com.example.nguyenducke.lovenotes.Models;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.nguyenducke.lovenotes.DatabaseHelper.DatabaseHelper;
import com.example.nguyenducke.lovenotes.MainActivity;

/**
 * Created by PHANTHELINH on 12/22/2017.
 */

public class Login {

    private DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    public Login(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void setPassword(String password){
        if(password.equals(null)|| password.isEmpty()||password.equals(" "))
            return;
        db = databaseHelper.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put("matkhau",password);
        db.insert("LOGIN",null,data);
    }

    public String getPassword(){
        db= databaseHelper.getReadableDatabase();
        String sql = "select * from LOGIN";
        Cursor cursor = db.rawQuery(sql,null);
        int count = cursor.getCount();
        if (count>0){
            cursor.moveToFirst();
            return cursor.getString(1);
        }
        else
            return new String();
    }

    public void restoreDatabase(){
        db = databaseHelper.getWritableDatabase();

        try{
            db.delete("LOGIN", null,null);
        }
        catch (Exception e){
            return;
        }

    }
}
