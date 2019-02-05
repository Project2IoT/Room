package com.example.project.project8;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.project.project8.data.RoomDBHelper;
import com.example.project.project8.data.RoomAdapter;
import com.example.project.project8.data.RoomContract.RoomEntry;
import com.example.project.project8.data.RoomDBHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private RoomDBHelper mDbHelper;
    Cursor cursor;
    private static final int LOADER = 1;
    RoomAdapter storeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });


        ListView listView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);


        storeAdapter = new RoomAdapter(this, null);
        listView.setAdapter(storeAdapter);

        mDbHelper = new RoomDBHelper(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri currentitemUri = ContentUris.withAppendedId(RoomEntry.CONTENT_URI, id);
                intent.setData(currentitemUri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(LOADER, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void refresh() {
        getLoaderManager().restartLoader(LOADER, null, this);

    }

    public void deleteAllData() {
        int rowsDeleted = getContentResolver().delete(RoomEntry.CONTENT_URI, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_data:
                refresh();
                return true;
            case R.id.action_delete_all_entries:
                deleteAllData();
                refresh();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                RoomEntry._ID,
                RoomEntry.COLUMN_ROOM_NAME,
                RoomEntry.COLUMN_ARDUINO_NAME};

        return new CursorLoader(this,   // Parent activity context
                RoomEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        storeAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        storeAdapter.swapCursor(null);
    }
}
