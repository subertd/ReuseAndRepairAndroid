package edu.oregonstate.reuseandrepair;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import edu.oregonstate.reuseandrepair.database.MySQLiteOpenHelper;


public class CategoriesActivity extends ActionBarActivity {

    private static final String[] FROM = {
            MySQLiteOpenHelper.TABLE_CATEGORY_COL_ID,
            MySQLiteOpenHelper.TABLE_CATEGORY_COL_NAME
    };

    private static final int[] TO = {
            R.id.cat_id,
            R.id.cat_name
    };

    private static final String TAG = CategoriesActivity.class.getName();
    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        populateCategoriesList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateCategoriesList() {
        Log.i(TAG, "entering populateCategoriesList");

        new CategoriesListPopulator().execute();
    }

    private class CategoriesListPopulator extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {

            return new MySQLiteOpenHelper(CategoriesActivity.this).getCategoriesCursor();
        }

        @Override
        protected void onPostExecute(final Cursor cursor) {

            // populate a list view with the cursor
            listView = (ListView) findViewById(R.id.cat_list);

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    CategoriesActivity.this,
                    R.layout.activity_categories_entry,
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

                    // Get corresponding category id from this row
                    String catId = cursor.getString(
                            cursor.getColumnIndexOrThrow(MySQLiteOpenHelper.TABLE_CATEGORY_COL_ID));

                    Toast.makeText(CategoriesActivity.this, catId, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
