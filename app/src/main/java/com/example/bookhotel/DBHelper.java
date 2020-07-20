package com.example.bookhotel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * <p>Класс для работы и настройки базы данных. </p>
 * <p>Содержит методы для работы с БД</p>
 * @author Alex Tereschenko
 *
 * */

public class DBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Auth";
    public static final String TABLE_LOGIN = "users";
    public static final String TABLE_BOOKINGS = "bookings";

    public static final String KEY_ID = "_id";
    public static final String KEY_USER = "user";
    public static final String KEY_EMAIL = "email";

    public static final String BOOK_CITY = "city";
    public static final String BOOK_ARRIVAL_DATE = "arrivalDate";
    public static final String BOOK_ARRIVAL_TIME = "arrivalTime";
    public static final String BOOK_NIGHTS = "nights";
    public static final String BOOK_GUESTS = "guests";
    public static final String BOOK_STATUS = "status";
    public static final String BOOK_ON = "bookedOn";
    public static final String BOOK_PRICE = "price";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_LOGIN + "(" + KEY_ID
                + " integer primary key," + KEY_USER + " text," + KEY_EMAIL + " text" + ")");
        db.execSQL("create table " + TABLE_BOOKINGS + "(" + KEY_ID
                + " integer primary key," + KEY_USER + " text," + BOOK_CITY + " text," + BOOK_ARRIVAL_DATE + " text," +
                BOOK_ARRIVAL_TIME + " text," + BOOK_NIGHTS + " text," + BOOK_GUESTS + " text," + BOOK_STATUS + " text," +
                BOOK_ON + " text," + BOOK_PRICE + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_LOGIN);
        db.execSQL("drop table if exists " + TABLE_BOOKINGS);

        onCreate(db);

    }

    public String getAuth() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOGIN, new String[] { KEY_ID,
                        KEY_USER, KEY_EMAIL }, KEY_USER + "=?",
                new String[] { "auth" }, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()){
            String email = cursor.getString(2);
            return email;
        }
        
        return "no";
    }

    public String getBookings() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BOOKINGS, null, null,
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()){
            while(cursor.moveToNext()){
                Log.i("BOOKINGS", "Task");
            }
        }

        return "ok";
    }

    public void deleteAuth() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOGIN, KEY_USER + " = ?", new String[] { "auth" });
        db.close();
    }

    public int updateAuth(String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER, "auth");
        values.put(KEY_EMAIL, email);

        return db.update(TABLE_LOGIN, values, KEY_USER + " = ?",
                new String[] { "auth" });
    }
}