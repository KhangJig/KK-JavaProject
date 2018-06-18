package com.example.nguyenducke.lovenotes.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.nguyenducke.lovenotes.Adapter.Note;

import java.util.ArrayList;

/**
 * Created by PHANTHELINH on 12/22/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLite data";
    private Context context;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LoveNote";

    //region Bang Login
    private static final String tableLogin = "LOGIN";
    private static final String mkLogin = "matkhau";
    private static final String createLogin = "create table " + tableLogin + " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            mkLogin + " TEXT)";
    //endregion

    //Bang Note
    private static final String TABLE_NOTE = "NOTE";

    private static final String NOTE_ID = "Note_Id";
    private static final String NOTE_TITLE = "Note_Title";
    private static final String NOTE_CONTENT = "Note_Content";
    private static final String NOTE_IMAGE = "Note_Image";
    private static final String NOTE_NGAY_TAO = "Note_Ngay_Tao";
    //private static final String NOTE_MA_THONG_BAO = "Note_Ma_Thong_Bao";

    //private static final String maTepKemNotes = "matep";
    private static final String createNotes = "create table  " + TABLE_NOTE + "("
            + NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NOTE_TITLE + " TEXT,"
            + NOTE_CONTENT + " TEXT,"
            + NOTE_IMAGE + " TEXT,"
            + NOTE_NGAY_TAO + " TEXT)";


    //region Bang Dem ngay yeu
    private String tableCountDays = "COUNTDAYS";
    private String title1 = "title1";
    private String title2 = "title2";
    private String startDay = "startday";
    private String pic1 = "pic1";
    private String pic2 = "pic2";
    private String name1 = "name1";
    private String name2 = "name2";
    private String createCountDays = "create table if not exists " + tableCountDays + " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            title1 + " TEXT," +
            title2 + " TEXT," +
            startDay + " TEXT," +
            pic1 + " TEXT," +
            pic2 + " TEXT," +
            name1 + " TEXT," +
            name2 + " TEXT)";
    //endregion

    public DatabaseHelper(Context context) {
        super(context, "LoveNote", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "DatabaseHelper.onCreate ... ");
        db.execSQL(createLogin);
        db.execSQL(createNotes);
        db.execSQL(createCountDays);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "DatabaseHelper.onUpgrade ... ");
//        db.execSQL("drop table if exists LOGIN");
//        db.execSQL("drop table if exists NOTES");
//        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);

        onCreate(db);
    }


    public void addNote(Note note) {
        Log.i(TAG, "DatabaseHelper.addNote ... " + note.getNoteTitle());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTE_TITLE, note.getNoteTitle());
        values.put(NOTE_CONTENT, note.getNoteContent());
        values.put(NOTE_IMAGE, note.getNotePicture());
        values.put(NOTE_NGAY_TAO, note.getNoteNgayTao());

        db.insert(TABLE_NOTE, null, values);

        db.close();
    }

    public Note getNote(int id) {
        Log.i(TAG, "DatabaseHelper.getNote ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTE, new String[]{NOTE_ID,
                        NOTE_TITLE, NOTE_CONTENT, NOTE_IMAGE}, NOTE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

        return note;
    }

    public ArrayList<Note> getAllNotes() {
        Log.i(TAG, "DatabaseHelper.getAllNotes ... ");

        ArrayList<Note> noteList = new ArrayList<Note>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            if (cursor.getCount() > 0) {
                do {
                    Note note = new Note();
                    note.setNoteId(Integer.parseInt(cursor.getString(0)));
                    note.setNoteTitle(cursor.getString(1));
                    note.setNoteContent(cursor.getString(2));
                    note.setNotePicture(cursor.getString(3));
                    note.setNoteNgayTao(cursor.getString(4));

                    //Them vao danh sach
                    noteList.add(note);
                } while (cursor.moveToNext());
            }
        }
        // return note list
        return noteList;
    }

    public int getNotesCount() {
        Log.i(TAG, "DatabaseHelper.getNotesCount ... ");

        String countQuery = "SELECT  * FROM " + TABLE_NOTE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();
        return count;
    }

    public int updateNote(Note note) {
        Log.i(TAG, "DatabaseHelper.updateNote ... " + note.getNoteTitle());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTE_TITLE, note.getNoteTitle());
        values.put(NOTE_CONTENT, note.getNoteContent());
        values.put(NOTE_IMAGE, note.getNotePicture());
        values.put(NOTE_NGAY_TAO, note.getNoteNgayTao());

        // updating row
        return db.update(TABLE_NOTE, values, NOTE_ID + " = ?",
                new String[]{String.valueOf(note.getNoteId())});
    }

    public void deleteNote(Note note) {
        Log.i(TAG, "MyDatabaseHelper.updateNote ... " + note.getNoteTitle());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTE, NOTE_ID + " = ?",
                new String[]{String.valueOf(note.getNoteId())});
        db.close();
    }


}
