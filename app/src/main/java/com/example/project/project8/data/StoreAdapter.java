package com.example.project.project8.data;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.project.project8.R;


public class StoreAdapter extends CursorAdapter {
    private int quantity;


    public StoreAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.book_name);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.book_quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.book_price);



        int nameColumnIndex = cursor.getColumnIndex(StoreContract.StoreEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(StoreContract.StoreEntry.COLUMN_PRODUCT_PRICE);
        final int quantityColumnIndex = cursor.getColumnIndex(StoreContract.StoreEntry.COLUMN_PRODUCT_QUANTITY);

        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        final String productQuantity = cursor.getString(quantityColumnIndex);
        quantity = cursor.getInt(quantityColumnIndex);


        String id = cursor.getString(cursor.getColumnIndex(StoreContract.StoreEntry._ID));
        final Uri mCurrentItemUri = ContentUris.withAppendedId(StoreContract.StoreEntry.CONTENT_URI,
                Long.parseLong(id));
        /*addOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quantity = Integer.parseInt(quantityTextView.getText().toString().trim());
                int updatedQuantity = quantity + 1;
                ContentValues values = new ContentValues();
                values.put(StoreContract.StoreEntry.COLUMN_PRODUCT_QUANTITY, updatedQuantity);
                context.getContentResolver().update(mCurrentItemUri, values, null, null);
            }
        });
        deleteOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quantity = Integer.parseInt(quantityTextView.getText().toString().trim());
                if (quantity > 0) {
                    int updatedQuantity = quantity - 1;
                    ContentValues values = new ContentValues();
                    values.put(StoreContract.StoreEntry.COLUMN_PRODUCT_QUANTITY, updatedQuantity);
                    context.getContentResolver().update(mCurrentItemUri, values, null, null);
                } else {
                    Toast.makeText(context, context.getString(R.string.invalid), Toast.LENGTH_SHORT).show();
                }
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + supplierNumber));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                view.getContext().startActivity(intent);
            }

        });
*/

        nameTextView.setText(productName);
        priceTextView.setText(productPrice);
        quantityTextView.setText(productQuantity);
    }
}