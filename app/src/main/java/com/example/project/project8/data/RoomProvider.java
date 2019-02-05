package com.example.project.project8.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class RoomProvider extends ContentProvider {

    public static final String LOG_TAG = RoomProvider.class.getSimpleName();
    private RoomDBHelper dbHelper;
    private static final int STORE = 100;
    private static final int ITEM_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(RoomContract.CONTENT_AUTHORITY, RoomContract.PATH_STORE, STORE);
        sUriMatcher.addURI(RoomContract.CONTENT_AUTHORITY, RoomContract.PATH_STORE + "/#", ITEM_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new RoomDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case STORE:
                cursor = database.query(RoomContract.RoomEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ITEM_ID:
                selection = RoomContract.RoomEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(RoomContract.RoomEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        String roomName = values.getAsString(RoomContract.RoomEntry.COLUMN_ROOM_NAME);
        if (roomName == null) {
            throw new IllegalArgumentException("Please fill in the room name");
        }
        String arduinoName = values.getAsString(RoomContract.RoomEntry.COLUMN_ARDUINO_NAME);
        if (arduinoName == null) {
            throw new IllegalArgumentException("Please fill in the arduino name");
        }


        SQLiteDatabase database = dbHelper.getWritableDatabase();

        long id = database.insert(RoomContract.RoomEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case STORE:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case ITEM_ID:
                selection = RoomContract.RoomEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(RoomContract.RoomEntry.COLUMN_ROOM_NAME)) {
            String roomName = values.getAsString(RoomContract.RoomEntry.COLUMN_ROOM_NAME);
            if (roomName == null) {
                throw new IllegalArgumentException("Please fill in room name");
            }
        }

        if (values.containsKey(RoomContract.RoomEntry.COLUMN_ARDUINO_NAME)) {
            String arduinoName = values.getAsString(RoomContract.RoomEntry.COLUMN_ROOM_NAME);
            if (arduinoName == null) {
                throw new IllegalArgumentException("Please fill in arduino name");
            }
        }


        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        int rowsUpdated = database.update(RoomContract.RoomEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows updated
        return rowsUpdated;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case STORE:
                rowsDeleted = database.delete(RoomContract.RoomEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEM_ID:
                selection = RoomContract.RoomEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted= database.delete(RoomContract.RoomEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);

        }
        return rowsDeleted;
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case STORE:
                return RoomContract.RoomEntry.CONTENT_LIST_TYPE;
            case ITEM_ID:
                return RoomContract.RoomEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}