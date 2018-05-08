package com.example.project.project8;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.project8.data.StoreContract.StoreEntry;
import com.example.project.project8.data.StoreDBHelper;

import static com.example.project.project8.R.id.action_delete;

public class EditorActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mSupplierNameEditText;
    private EditText mSupplierNoEditText;
    String nameString;
    String priceString;
    String quantityString;
    String supplierNameString = "Unknown";
    String supplierNoString = "Unknown";
    int price = 0;
    int quantity = 0;
    boolean nameEntered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor);

        mNameEditText = (EditText) findViewById(R.id.edit_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_quantity);
        mSupplierNameEditText = (EditText) findViewById(R.id.edit_supplier_name);
        mSupplierNoEditText = (EditText) findViewById(R.id.edit_supplier_no);

    }


    private void insertItem() {

        nameString = mNameEditText.getText().toString().trim();
        priceString = mPriceEditText.getText().toString().trim();
        quantityString = mQuantityEditText.getText().toString().trim();
        supplierNameString = mSupplierNameEditText.getText().toString().trim();
        supplierNoString = mSupplierNoEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(priceString)) {
            price = Integer.parseInt(priceString);
        }
        if (!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
        }
        if (TextUtils.isEmpty(nameString)) {
            mNameEditText.setHintTextColor(Color.RED);
        }
        if (TextUtils.isEmpty(priceString)) {
            mPriceEditText.setHintTextColor(Color.RED);
        }
        if (TextUtils.isEmpty(nameString) || TextUtils.isEmpty(priceString)) {
            nameEntered = false;
            return;
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

        long newRowId = db.insert(StoreEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Error with saving item details", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "item saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean allFieldsEntered() {
        if (nameEntered==false) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                if (allFieldsEntered()){
                    insertItem();
                    finish();
                } else {
                    Toast.makeText(this, "please fill in all field", Toast.LENGTH_SHORT).show();
                }

            }
            case action_delete:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
