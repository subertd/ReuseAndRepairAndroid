package edu.oregonstate.reuseandrepair;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.oregonstate.reuseandrepair.edu.oregonstate.reuseandrepair.database.DatabaseException;
import edu.oregonstate.reuseandrepair.edu.oregonstate.reuseandrepair.database.MySQLiteOpenHelper;
import edu.oregonstate.reuseandrepair.edu.oregonstate.reuseandrepair.server.ServerException;
import edu.oregonstate.reuseandrepair.edu.oregonstate.reuseandrepair.server.ServerProxy;


public class CategoriesActivity extends ActionBarActivity {

    private static final String TAG = CategoriesActivity.class.getName();

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

            final Cursor categories = new MySQLiteOpenHelper(CategoriesActivity.this).getCategoriesCursor();
            // TODO populate a list view with the cursor

            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {

            Toast.makeText(CategoriesActivity.this, "TEMP categories list should now be populated",
                    Toast.LENGTH_LONG).show();
        }
    }
}
