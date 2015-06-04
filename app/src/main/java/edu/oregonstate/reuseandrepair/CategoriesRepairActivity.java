package edu.oregonstate.reuseandrepair;

import android.content.Intent;
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


public class CategoriesRepairActivity extends ActionBarActivity {

    private static final String[] FROM = {
            MySQLiteOpenHelper.TABLE_CATEGORY_COL_ID,
            MySQLiteOpenHelper.TABLE_CATEGORY_COL_NAME
    };

    private static final int[] TO = {
            R.id.catRepair_id,
            R.id.catRepair_name
    };

    private static final String TAG = CategoriesRepairActivity.class.getName();
    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_repair);

        populateCategoriesRepairList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categories_repair, menu);
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
                    R.layout.activity_categories_repair_entry,
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
