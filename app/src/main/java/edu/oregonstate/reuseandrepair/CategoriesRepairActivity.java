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


public class CategoriesRepairActivity extends AppCompatActivity {

    private static final String[] FROM = {
            MySQLiteOpenHelper.TABLE_CATEGORY_COL_ID,
            MySQLiteOpenHelper.TABLE_CATEGORY_COL_NAME
    };

    private static final int[] TO = {
            R.id.cat_id,
            R.id.cat_name
    };

    private static final String TAG = CategoriesRepairActivity.class.getName();
    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_repair);

        populateCategoriesRepairList();
    }

    private void populateCategoriesRepairList() {
        Log.i(TAG, "entering populateCategoriesRepairList");

        new CategoriesRepairListPopulator().execute();
    }

    private class CategoriesRepairListPopulator extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {

            return new MySQLiteOpenHelper(CategoriesRepairActivity.this).getCategoriesCursor();
        }

        @Override
        protected void onPostExecute(final Cursor cursor) {

            // populate a list view with the cursor
            listView = (ListView) findViewById(R.id.catRepair_list);

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    CategoriesRepairActivity.this,
                    R.layout.categories_entry,
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
                    String catId = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteOpenHelper.TABLE_CATEGORY_COL_ID));
                    String catName = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteOpenHelper.TABLE_CATEGORY_COL_NAME));

                    // Start new activity to show list of matching organizations
                    Intent i = new Intent(CategoriesRepairActivity.this, ItemRepairListingActivity.class);
                    i.putExtra("catId", catId);
                    i.putExtra("catName", catName);
                    startActivity(i);
                }
            });
        }
    }
}
