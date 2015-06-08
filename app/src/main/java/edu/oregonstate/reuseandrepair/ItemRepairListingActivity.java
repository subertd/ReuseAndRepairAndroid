package edu.oregonstate.reuseandrepair;

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


public class ItemRepairListingActivity extends AppCompatActivity {

    private static final String[] FROM = {
            MySQLiteOpenHelper.TABLE_ITEM_COL_ID,
            MySQLiteOpenHelper.TABLE_ITEM_COL_NAME
    };

    private static final int[] TO = {
            R.id.itemRepair_id,
            R.id.itemRepair_name
    };

    private static final String TAG = ItemRepairListingActivity.class.getName();
    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_repair_listing);

        populateItemRepairList();
    }

    private void populateItemRepairList() {
        Log.i(TAG, "entering populateItemRepairList");

        new ItemsRepairListPopulator().execute();
    }

    private class ItemsRepairListPopulator extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {

            Intent i = getIntent();
            String catId = i.getStringExtra("catId");

            return new MySQLiteOpenHelper(ItemRepairListingActivity.this).getItemsCursorByCategory((Long.valueOf(catId)));
        }

        @Override
        protected void onPostExecute(final Cursor cursor) {

            // populate a list view with the cursor
            listView = (ListView) findViewById(R.id.itemRepair_list);

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    ItemRepairListingActivity.this,
                    R.layout.activity_item_repair_listing_entry,
                    cursor,
                    FROM,
                    TO,
                    0);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(final AdapterView<?> listView,
                                        final View view, final int position, final long id)
                {
                    // Set cursor at click position
                    Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                    // Get corresponding category id and name from this row
                    String itemId = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteOpenHelper.TABLE_ITEM_CATEGORY_COL_ITEM_ID));

                    //        Toast.makeText(CategoriesReuseActivity.this, catId, Toast.LENGTH_SHORT).show();

                    // Start new activity to show list of matching organizations
                    Intent i = new Intent(ItemRepairListingActivity.this, OrgRepairListingActivity.class);
                    i.putExtra("itemId", itemId);
                    startActivity(i);
                }
            });
        }
    }
}
