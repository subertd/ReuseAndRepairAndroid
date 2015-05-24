package edu.oregonstate.reuseandrepair;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import edu.oregonstate.reuseandrepair.database.MySQLiteOpenHelper;


public class CategoriesActivity extends ActionBarActivity {

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

    private class CategoriesListPopulator extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            // TODO populate a list view with the cursor
            listView = (ListView) findViewById(R.id.cat_list);
            Cursor catCursor = new MySQLiteOpenHelper(CategoriesActivity.this).getCategoriesCursor();
            String[] columns = new String[] {"category_id", "category_name"};
            int[] to = new int[] {R.id.cat_id, R.id.cat_name};

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(CategoriesActivity.this, R.layout.activity_categories, catCursor, columns, to, 0);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                    // Set cursor at click position
                    Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                    // Get corresponding category id from this row
                    String catID = cursor.getString(cursor.getColumnIndexOrThrow("cat_id"));
                    Toast.makeText(getApplicationContext(),catID, Toast.LENGTH_SHORT).show();
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {

            Toast.makeText(CategoriesActivity.this, "TEMP categories list should now be populated",
                    Toast.LENGTH_LONG).show();
        }
    }
}
