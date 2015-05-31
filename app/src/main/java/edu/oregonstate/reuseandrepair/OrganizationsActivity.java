package edu.oregonstate.reuseandrepair;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import edu.oregonstate.reuseandrepair.database.MySQLiteOpenHelper;


public class OrganizationsActivity extends ActionBarActivity {

    ListView listView;
    private static final String TAG = OrganizationsActivity.class.getName();

    private static final String[] FROM = {
            MySQLiteOpenHelper.TABLE_ORGANIZATION_COL_ID,
            MySQLiteOpenHelper.TABLE_ORGANIZATION_COL_NAME,
            MySQLiteOpenHelper.TABLE_ORGANIZATION_PHONE_NUMBER,
            MySQLiteOpenHelper.TABLE_ORGANIZATION_PHYSICAL_ADDRESS,
            MySQLiteOpenHelper.TABLE_ORGANIZATION_WEBSITE_URL
    };

    private static final int[] TO = {
            R.id.org_id,
            R.id.org_name,
            R.id.org_phone,
            R.id.org_address,
            R.id.org_url
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizations);
        populateOrgInfoList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_organizations, menu);
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

    private void populateOrgInfoList() {
        Log.i(TAG, "entering populateOrgInfoList");

        new OrgInfoListPopulator().execute();
    }

    private class OrgInfoListPopulator extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {

            Intent i = getIntent();
            String orgId = i.getStringExtra("orgId");
            return new MySQLiteOpenHelper(OrganizationsActivity.this).getOrgInfoCursor((Long.valueOf(orgId)));
        }

        @Override
        protected void onPostExecute(final Cursor cursor) {

            // populate a list view with the cursor
            listView = (ListView) findViewById(R.id.orgInfo_list);

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    OrganizationsActivity.this,
                    R.layout.activity_organizations_entry,
                    cursor,
                    FROM,
                    TO,
                    0);

            listView.setAdapter(adapter);
        }
    }
}
