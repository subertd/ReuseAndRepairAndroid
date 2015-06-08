package edu.oregonstate.reuseandrepair;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import edu.oregonstate.reuseandrepair.database.DatabaseException;
import edu.oregonstate.reuseandrepair.database.MySQLiteOpenHelper;
import edu.oregonstate.reuseandrepair.server.ServerException;
import edu.oregonstate.reuseandrepair.server.ServerProxy;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        syncDatabase();
    }

    public void onClickCategoriesButton(final View button) {

        Log.i(TAG, "entering onClickCategoriesButton");

        final Intent categoriesActivity = new Intent(this, CategoriesReuseActivity.class);
        startActivity(categoriesActivity);
    }

    public void onClickRepairCategoriesButton(final View button) {

        Log.i(TAG, "entering onClickRepairCategoriesButton");

        final Intent categoriesRepairActivity = new Intent(this, CategoriesRepairActivity.class);
        startActivity(categoriesRepairActivity);
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
