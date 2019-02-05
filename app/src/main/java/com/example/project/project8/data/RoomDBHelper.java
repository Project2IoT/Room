package com.example.project.project8.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.project.project8.data.RoomContract.RoomEntry;

public class RoomDBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = RoomDBHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "rooms.db";
    private static final int DATABASE_VERSION = 1;
    public RoomDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_STORE_TABLE = "CREATE TABLE " + RoomContract.RoomEntry.TABLE_NAME + " ("
                + RoomContract.RoomEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RoomContract.RoomEntry.COLUMN_ROOM_NAME + " TEXT NOT NULL , "
                + RoomContract.RoomEntry.COLUMN_ARDUINO_NAME + " TEXT NOT NULL  );";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_STORE_TABLE);

    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}