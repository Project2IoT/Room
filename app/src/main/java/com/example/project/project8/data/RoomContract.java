package com.example.project.project8.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class RoomContract {

    private RoomContract() {
    }
    public static final String CONTENT_AUTHORITY = "com.example.project.project8";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_STORE = "store";


    public static final class RoomEntry implements BaseColumns {


        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STORE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STORE;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_STORE);
        public final static String TABLE_NAME = "Rooms";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_ARDUINO_NAME= "arduinoName";
        public final static String COLUMN_ROOM_NAME = "name";


    }
}