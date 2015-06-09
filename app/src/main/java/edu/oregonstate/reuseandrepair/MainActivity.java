package edu.oregonstate.reuseandrepair;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import edu.oregonstate.reuseandrepair.database.DatabaseException;
import edu.oregonstate.reuseandrepair.database.MySQLiteOpenHelper;
import edu.oregonstate.reuseandrepair.server.ServerException;
import edu.oregonstate.reuseandrepair.server.ServerProxy;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private static final int REUSE = 0;
    private static final int REPAIR = 1;
    private static final int LINK_1 = 2;
    private static final int LINK_2 = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpOptions();
        syncDatabase();
    }

    private void setUpOptions() {

        final ListView options = (ListView)findViewById(R.id.options);
        options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case REUSE:
                        startReuseCategories();
                        break;
                    case REPAIR:
                        startRepairCategories();
                        break;
                    case LINK_1:
                        linkUrl1();
                        break;
                    case LINK_2:
                        linkUrl2();
                        break;
                }
            }
        });
    }

    private void startReuseCategories() {

        Log.i(TAG, "entering startReuseCategories");

        final Intent categoriesReuseActivity = new Intent(this, CategoriesReuseActivity.class);
        startActivity(categoriesReuseActivity);
    }

    private void startRepairCategories() {

        Log.i(TAG, "entering startRepairCategories");

        final Intent categoriesRepairActivity = new Intent(this, CategoriesRepairActivity.class);
        startActivity(categoriesRepairActivity);
    }

    private void linkUrl1() {
        Intent openBrowser = new Intent(Intent.ACTION_VIEW);
        openBrowser.setData(Uri.parse(getString(R.string.url_republic_curbside)));
        startActivity(openBrowser);
    }

    private void linkUrl2() {
        Intent openBrowser = new Intent(Intent.ACTION_VIEW);
        openBrowser.setData(Uri.parse(getString(R.string.url_republic_accepted_items)));
        startActivity(openBrowser);
    }

    private void syncDatabase() {
        new DatabaseSynchronizer().execute();
    }

    private class DatabaseSynchronizer extends AsyncTask<Void, Void, String> {

        private ProgressDialog progressDialog;

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

        /**
         * @citation: http://www.android-ios-tutorials.com/android/android-asynctask-example-download-progress/
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = ProgressDialog.show(MainActivity.this, "Wait", "downloading...");
        }

        @Override
        protected void onPostExecute(final String result) {
            this.progressDialog.dismiss();
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }
}
