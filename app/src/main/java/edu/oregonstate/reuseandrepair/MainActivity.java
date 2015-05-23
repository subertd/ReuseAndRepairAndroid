package edu.oregonstate.reuseandrepair;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.oregonstate.reuseandrepair.edu.oregonstate.reuseandrepair.database.DatabaseException;
import edu.oregonstate.reuseandrepair.edu.oregonstate.reuseandrepair.database.MySQLiteOpenHelper;
import edu.oregonstate.reuseandrepair.edu.oregonstate.reuseandrepair.server.ServerException;
import edu.oregonstate.reuseandrepair.edu.oregonstate.reuseandrepair.server.ServerProxy;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        syncDatabase();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onClickCategoriesButton(final View button) {

        Log.i(TAG, "entering onClickCategoriesButton");

        final Intent categoriesActivity = new Intent(this, CategoriesActivity.class);
        startActivity(categoriesActivity);
    }

    private void syncDatabase() {
        new DatabaseSynchronizer().execute();
    }

    private class DatabaseSynchronizer extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                final JSONObject database = new ServerProxy().syncDatabase();
                new MySQLiteOpenHelper(MainActivity.this).syncDatabase(database);
                return "Database Sync'd with Remote Server";
            }
            catch (final ServerException | DatabaseException e) {
                Log.e(TAG, e.getMessage());
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(final String result) {
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }
}
