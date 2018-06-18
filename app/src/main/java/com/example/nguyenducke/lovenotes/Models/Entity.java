package com.example.nguyenducke.lovenotes.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.nguyenducke.lovenotes.DatabaseHelper.DatabaseHelper;
import com.example.nguyenducke.lovenotes.MainActivity;

/**
 * Created by PHANTHELINH on 12/22/2017.
 */

public abstract class Entity {
    Context context;
     void insert(Context context,ContentValues contentValues){
         DatabaseHelper helper = new DatabaseHelper(context);
         SQLiteDatabase db = helper.getWritableDatabase();

    }
}
