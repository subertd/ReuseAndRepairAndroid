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


public class CategoriesReuseActivity extends AppCompatActivity {

    private static final String TAG = CategoriesReuseActivity.class.getName();

    private static final String[] FROM = {
            MySQLiteOpenHelper.TABLE_CATEGORY_COL_ID,
            MySQLiteOpenHelper.TABLE_CATEGORY_COL_NAME
    };

    private static final int[] TO = {
            R.id.cat_id,
            R.id.cat_name
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_reuse);

        populateCategoriesList();
    }

    private void populateCategoriesList() {
        Log.i(TAG, "entering populateCategoriesList");

        new CategoriesListPopulator().execute();
    }

    private class CategoriesListPopulator extends AsyncTask<Void, Void, Cursor> {

        private ProgressDialog progressDialog;

        @Override
        protected Cursor doInBackground(Void... params) {

            return new MySQLiteOpenHelper(CategoriesReuseActivity.this).getCategoriesCursor();
        }

        /**
         * @citation: http://www.android-ios-tutorials.com/android/android-asynctask-example-download-progress/
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = ProgressDialog.show(CategoriesReuseActivity.this, "Wait", "downloading...");
        }

        @Override
        protected void onPostExecute(final Cursor cursor) {

            this.progressDialog.dismiss();

            // populate a list view with the cursor
            final ListView listView = (ListView) findViewById(R.id.cat_list);

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    CategoriesReuseActivity.this,
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
                    final Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                    // Get corresponding category id and name from this row
                    final long catId = cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteOpenHelper.TABLE_CATEGORY_COL_ID));
                    final String catName = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteOpenHelper.TABLE_CATEGORY_COL_NAME));

                    // Start new activity to show list of matching organizations
                    final Intent i = new Intent(CategoriesReuseActivity.this, ItemReuseListingActivity.class);
                    i.putExtra("catId", catId);
                    i.putExtra("catName", catName);
                    startActivity(i);
                }
            });
        }
    }
}
