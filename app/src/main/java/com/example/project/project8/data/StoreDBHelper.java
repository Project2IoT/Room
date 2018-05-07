package com.example.project.project8.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.project.project8.data.StoreContract.StoreEntry;

public class StoreDBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = StoreDBHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "store.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    public StoreDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_STORE_TABLE = "CREATE TABLE " + StoreEntry.TABLE_NAME + " ("
                + StoreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + StoreEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + StoreEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, "
                + StoreEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, "
                + StoreEntry.COLUMN_SUPPLIER_NAME + " TEXT ,"
                + StoreEntry.COLUMN_SUPPLIER_NUMBER + " TEXT );";

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