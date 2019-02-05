package com.example.project.project8;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.project.project8.data.RoomDBHelper;
import com.example.project.project8.data.RoomContract;
import com.example.project.project8.data.RoomContract.RoomEntry;
import com.example.project.project8.data.RoomDBHelper;

import static com.example.project.project8.R.id.action_delete;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    int updatedQuantity;

    Button addOne;
    Button deleteOne;
    Button contact;
    private static final int LOADER = 0;
    private Uri mCurrentItemUri;
    private EditText mNameEditText;
    private EditText mArduinoNameEditText;

    String roomNameString;
    String arduinoNameString;

    boolean nameEntered = true;
    boolean priceEntered = true;
    private boolean mItemHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor);
        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();
        //editTexts
        mNameEditText = (EditText) findViewById(R.id.edit_name);
        mArduinoNameEditText = (EditText) findViewById(R.id.edit_arduino);

        //Buttons

        //activity titles
        if (mCurrentItemUri == null) {
            setTitle(getString(R.string.add));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.edit));
            getLoaderManager().initLoader(LOADER, null, this);
        }

        mNameEditText.setOnTouchListener(mTouchListener);
        mArduinoNameEditText.setOnTouchListener(mTouchListener);


        Switch onOffSwitch = (Switch) findViewById(R.id.onOffSwitch);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
            }

        });

    }

    private void insertItem() {
        roomNameString = mNameEditText.getText().toString().trim();
        arduinoNameString = mArduinoNameEditText.getText().toString().trim();

        if (
                TextUtils.isEmpty(arduinoNameString) && TextUtils.isEmpty(roomNameString)){
            Toast.makeText(this, "please fill in the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(roomNameString)) {
            mNameEditText.setHintTextColor(Color.RED);
            nameEntered = false;
            Toast.makeText(this, "please fill in the name ", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(arduinoNameString)) {
            mArduinoNameEditText.setHintTextColor(Color.RED);
            priceEntered = false;
            Toast.makeText(this, "please fill in room number ", Toast.LENGTH_SHORT).show();
        } else {
            nameEntered = true;
            priceEntered = true;
            // Create database helper
            RoomDBHelper mDbHelper = new RoomDBHelper(this);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(RoomEntry.COLUMN_ROOM_NAME, roomNameString);
            values.put(RoomEntry.COLUMN_ARDUINO_NAME, arduinoNameString);

            if (mCurrentItemUri == null) {
                Uri newUri = getContentResolver().insert(RoomEntry.CONTENT_URI, values);
                if (newUri == null) {
                    Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.successful), Toast.LENGTH_SHORT).show();
                }
            } else {
                int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);
                if (rowsAffected == 0) {
                    Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.successful), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public boolean allFieldsEntered() {
        if ((nameEntered == false) || (priceEntered == false)) {
            return false;
        } else
            return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                insertItem();
                if (allFieldsEntered()) {
                    finish();
                } else {
                    Toast.makeText(this, "please fill in all field", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            case action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteItem() {
        if (mCurrentItemUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentItemUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.successful), Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmDelete);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteItem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                RoomEntry._ID,
                RoomEntry.COLUMN_ROOM_NAME,
                RoomEntry.COLUMN_ARDUINO_NAME};


        return new CursorLoader(this,   // Parent activity context
                mCurrentItemUri,
                projection,
                null,                   // No selection clause
                null,                   // No selection arguments
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {

            int nameColumnIndex = cursor.getColumnIndex(RoomEntry.COLUMN_ROOM_NAME);
            int arduinoColumnIndex = cursor.getColumnIndex(RoomEntry.COLUMN_ARDUINO_NAME);
            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            String arduino = cursor.getString(arduinoColumnIndex);
            mNameEditText.setText(name);
            mArduinoNameEditText.setText(arduino);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mArduinoNameEditText.setText("");

    }

    public void addOne() {

    }
}
