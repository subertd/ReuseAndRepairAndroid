package edu.oregonstate.reuseandrepair;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.oregonstate.reuseandrepair.edu.oregonstate.reuseandrepair.server.ServerException;
import edu.oregonstate.reuseandrepair.edu.oregonstate.reuseandrepair.server.ServerProxy;


public class CategoriesActivity extends ActionBarActivity {

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

        new CategoriesListPopulator().execute();
    }

    private class CategoriesListPopulator extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                final JSONObject jsonObject = new ServerProxy().syncDatabase();
                return jsonObject;
            }
            catch (final ServerException e) {
                // TODO handle
                return null;
            }
        }

        @Override
        protected void onPostExecute(final JSONObject result) {
            Toast.makeText(CategoriesActivity.this, "This is a test message", Toast.LENGTH_LONG).show();
            // TODO implement
        }
    }
}
