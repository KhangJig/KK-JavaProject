package com.example.nguyenducke.lovenotes.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nguyenducke.lovenotes.DatabaseHelper.DatabaseHelper;

import java.util.Date;

/**
 * Created by PHANTHELINH on 12/29/2017.
 */

public class CountDays {
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    boolean isNew;
    String title1, title2;
    String startDay;
    String pic1, pic2;
    String name1, name2;

    public CountDays(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
        loadDatabase();
    }

    public void loadDatabase(){
        db = databaseHelper.getReadableDatabase();
        String sql = "select * from COUNTDAYS";
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        if(cursor.getCount()>0){
            setTitle1(cursor.getString(1));
            setTitle2(cursor.getString(2));
            setStartDay(cursor.getString(3));
            setPic1(cursor.getString(4));
            setPic2(cursor.getString(5));
            setName1(cursor.getString(6));
            setName2(cursor.getString(7));
            isNew = false;
        }
        else {
            isNew =true;
            title1="";
            title2="";
            startDay ="";
            pic1="";
            pic2="";
            name1="";
            name2="";
        }
    }

    //region Commit
    public void commit(String title1, String title2, String startDay, String pic1, String pic2, String name1, String name2){
        db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title1",title1);
        contentValues.put("title2", title2);
        contentValues.put("startday", startDay);
        contentValues.put("pic1", pic1);
        contentValues.put("pic2",pic2);
        contentValues.put("name1",name1);
        contentValues.put("name2", name2);

        if(isNew){
            db.insert("COUNTDAYS",null,contentValues);
        }
        else
            db.update("COUNTDAYS",contentValues,null,null);
    }

    public void commit(String title1, String title2, String startDay, String pic1, String pic2, String name1){
        commit(title1,title2,startDay,pic1,pic2,name1,getName2());
    }

    public void commit(String title1, String title2, String startDay, String pic1, String pic2){
        commit(title1,title2,startDay,pic1,pic2,getName1(),getName2());
    }

    public void commit(String title1, String title2, String startDay, String pic1){
        commit(title1,title2,startDay,pic1,getPic2(),getName1(),getName2());
    }

    public void commit(String title1, String title2, String startDay){
        commit(title1,title2,startDay,getPic1(),getPic2(),getName1(),getName2());
    }

    public void commit(String title1, String title2){
        commit(title1,title2,getStartDay(),getPic1(),getPic2(),getName1(),getName2());
    }

    public void commit(String title1){
        commit(title1,getTitle2(),getStartDay(),getPic1(),getPic2(),getName1(),getName2());
    }

    public void commit(){
        commit(getTitle1(),getTitle2(),getStartDay(),getPic1(),getPic2(),getName1(),getName2());
    }
    //endregion

    //region Get Set functions

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }
    //endregion

}
