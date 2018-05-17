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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.project8.data.StoreContract;
import com.example.project.project8.data.StoreContract.StoreEntry;
import com.example.project.project8.data.StoreDBHelper;

import static com.example.project.project8.R.id.action_delete;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER = 0;
    private Uri mCurrentItemUri;
    private EditText mNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mSupplierNameEditText;
    private EditText mSupplierNoEditText;
    private Button contact;
    String nameString;
    String priceString;
    String quantityString;
    String supplierNameString = "Unknown";
    String supplierNoString = "Unknown";
    int price = 0;
    int quantity = 0;
    boolean nameEntered = true;
    boolean priceEntered = true;
    private boolean mItemHasChanged = false;
    private Button sale;
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


        mNameEditText = (EditText) findViewById(R.id.edit_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_quantity);
        mSupplierNameEditText = (EditText) findViewById(R.id.edit_supplier_name);
        mSupplierNoEditText = (EditText) findViewById(R.id.edit_supplier_no);
        if (mCurrentItemUri == null) {
            setTitle(getString(R.string.add));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.edit));
            getLoaderManager().initLoader(LOADER, null, this);
        }

        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mSupplierNoEditText.setOnTouchListener(mTouchListener);

    }

    private void insertItem() {

        nameString = mNameEditText.getText().toString().trim();
        priceString = mPriceEditText.getText().toString().trim();
        quantityString = mQuantityEditText.getText().toString().trim();
        supplierNameString = mSupplierNameEditText.getText().toString().trim();
        supplierNoString = mSupplierNoEditText.getText().toString().trim();
        if (mCurrentItemUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(quantityString) && TextUtils.isEmpty(supplierNameString)
                && TextUtils.isEmpty(supplierNoString)) {
            return;
        }
        if (!TextUtils.isEmpty(priceString)) {
            price = Integer.parseInt(priceString);
        }
        if (!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
        }
        if (TextUtils.isEmpty(nameString)) {
            mNameEditText.setHintTextColor(Color.RED);
            nameEntered = false;
        }
        if (TextUtils.isEmpty(priceString)) {
            mPriceEditText.setHintTextColor(Color.RED);
            priceEntered = false;
        } else {
            nameEntered = true;
            priceEntered = true;
        }
        // Create database helper
        StoreDBHelper mDbHelper = new StoreDBHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(StoreEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(StoreEntry.COLUMN_PRODUCT_PRICE, price);
        values.put(StoreEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(StoreEntry.COLUMN_SUPPLIER_NAME, supplierNameString);
        values.put(StoreEntry.COLUMN_SUPPLIER_NUMBER, supplierNoString);

        if (mCurrentItemUri == null) {
            Uri newUri = getContentResolver().insert(StoreEntry.CONTENT_URI, values);
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
        // If this is a new pet, hide the "Delete" menu item.
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
                StoreEntry._ID,
                StoreEntry.COLUMN_PRODUCT_NAME,
                StoreEntry.COLUMN_PRODUCT_PRICE,
                StoreEntry.COLUMN_PRODUCT_QUANTITY,
                StoreEntry.COLUMN_SUPPLIER_NAME,
                StoreEntry.COLUMN_SUPPLIER_NUMBER};


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

            int nameColumnIndex = cursor.getColumnIndex(StoreContract.StoreEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(StoreContract.StoreEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(StoreContract.StoreEntry.COLUMN_PRODUCT_QUANTITY);
            int sNameColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_SUPPLIER_NAME);
            int sNumberColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_SUPPLIER_NUMBER);
            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplierName = cursor.getString(sNameColumnIndex);
            String supplierNumber = cursor.getString(sNumberColumnIndex);
            mNameEditText.setText(name);
            mPriceEditText.setText(Integer.toString(price));
            mQuantityEditText.setText(Integer.toString(quantity));
            mSupplierNameEditText.setText(supplierName);
            mSupplierNoEditText.setText(supplierNumber);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupplierNameEditText.setText("");
        mSupplierNoEditText.setText("");
    }
}
