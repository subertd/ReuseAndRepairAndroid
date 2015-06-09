package edu.oregonstate.reuseandrepair;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import edu.oregonstate.reuseandrepair.database.MySQLiteOpenHelper;


public class ItemReuseListingActivity extends AppCompatActivity {

    private static final String TAG = ItemReuseListingActivity.class.getName();

    private static final String[] FROM = {
            MySQLiteOpenHelper.TABLE_ITEM_COL_ID,
            MySQLiteOpenHelper.TABLE_ITEM_COL_NAME
    };

    private static final int[] TO = {
            R.id.item_id,
            R.id.item_name
    };

    private static long catId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_listing);

        // Set catId Parameter
        final Intent currentIntent = getIntent();
        long catId = currentIntent.getLongExtra("catId", 0);
        if (catId > 0) {
            ItemReuseListingActivity.catId = catId;
        }

        populateItemList();
    }

    private void populateItemList() {
        Log.i(TAG, "entering populateItemList");

        new ItemsListPopulator().execute();
    }

    private class ItemsListPopulator extends AsyncTask<Void, Void, Cursor> {

        private ProgressDialog progressDialog;

        @Override
        protected Cursor doInBackground(Void... params) {

            return new MySQLiteOpenHelper(ItemReuseListingActivity.this).getItemsCursorByCategory((Long.valueOf(catId)));
        }

        /**
         * @citation: http://www.android-ios-tutorials.com/android/android-asynctask-example-download-progress/
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = ProgressDialog.show(ItemReuseListingActivity.this, null, "populating...");
        }

        @Override
        protected void onPostExecute(final Cursor cursor) {

            this.progressDialog.dismiss();

            // populate a list view with the cursor
            final ListView listView = (ListView) findViewById(R.id.item_list);

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    ItemReuseListingActivity.this,
                    R.layout.item_reuse_listing_entry,
                    cursor,
                    FROM,
                    TO,
                    0);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(final AdapterView<?> listView,
                                        final View view, final int position, final long id) {

                    // Set cursor at click position
                    final Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                    // Get category id and name from this row
                    final long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteOpenHelper.TABLE_ITEM_CATEGORY_COL_ITEM_ID));
                    final String itemName = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteOpenHelper.TABLE_CATEGORY_COL_NAME));

                    // Start new activity to show list of matching organizations
                    final Intent currentIntent = getIntent();
                    final Intent i = new Intent(ItemReuseListingActivity.this, OrgReuseListingActivity.class);
                    i.putExtra("catId", currentIntent.getLongExtra("catId", 0));
                    i.putExtra("itemId", itemId);
                    i.putExtra("itemName", itemName);
                    startActivityForResult(i, MyActivityResults.REUSE_ITEMS_FROM_ORGS);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == MyActivityResults.REUSE_ITEMS_FROM_ORGS) {
            ItemReuseListingActivity.catId = intent.getLongExtra("catId", 0);
        }
    }
}
