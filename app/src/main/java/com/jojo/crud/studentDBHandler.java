package com.jojo.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class studentDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notes_db";
    public static final String TABLE_NAME = "student";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SEM = "sem";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_SEM + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";


    public studentDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertData(String name, String sem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_SEM, sem);
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }
    public int updateData(String id, String name, String sem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_SEM, sem);
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{id});
    }

    public void deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] {id});
        db.close();
    }
    public int getDataCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public studentDetails getData(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_SEM}, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        studentDetails note = new studentDetails(
                cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_SEM)));
        cursor.close();
        return note;
    }

    public List<studentDetails> getAllData() {
        List<studentDetails> notes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_NAME + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                studentDetails details = new studentDetails();
                details.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                details.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                details.setSem(cursor.getInt(cursor.getColumnIndex(COLUMN_SEM)));

                notes.add(details);
            } while (cursor.moveToNext());
        }
        db.close();
        return notes;
    }
}