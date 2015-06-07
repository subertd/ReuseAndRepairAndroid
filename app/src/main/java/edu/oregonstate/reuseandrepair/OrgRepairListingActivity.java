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

import edu.oregonstate.reuseandrepair.database.MySQLiteOpenHelper;


public class OrgRepairListingActivity extends ActionBarActivity {

    private static final String[] FROM = {
            MySQLiteOpenHelper.TABLE_ORGANIZATION_COL_ID,
            MySQLiteOpenHelper.TABLE_ORGANIZATION_COL_NAME
    };

    private static final int[] TO = {
            R.id.orgRepair_id,
            R.id.orgRepair_name
    };

    private static final String TAG = OrgRepairListingActivity.class.getName();
    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_repair_listing);

        populateOrgRepairList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_org_repair_listing, menu);
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

    private void populateOrgRepairList() {
        Log.i(TAG, "entering populateOrgRepairList");

        new OrgRepairListPopulator().execute();
    }

    private class OrgRepairListPopulator extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {

            Intent i = getIntent();
            String itemId = i.getStringExtra("itemId");

            return new MySQLiteOpenHelper(OrgRepairListingActivity.this).getOrganizationsCursorByRepairItem((Long.valueOf(itemId)));
        }

        @Override
        protected void onPostExecute(final Cursor cursor) {

            // populate a list view with the cursor
            listView = (ListView) findViewById(R.id.orgRepair_list);

            // Get a list of organizations
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    OrgRepairListingActivity.this,
                    R.layout.activity_org_repair_listing_entry,
                    cursor,
                    FROM,
                    TO,
                    0);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(final AdapterView<?> listView, final View view, final int position, final long id) {
                    // Set cursor at click position
                    Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                    // Get corresponding org id and name from this row
                    String orgId = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteOpenHelper.TABLE_ORGANIZATION_COL_ID));

                    // Start new activity to show organization contact info
                    Intent i = new Intent(OrgRepairListingActivity.this, OrganizationsActivity.class);
                    i.putExtra("orgId", orgId);
                    startActivity(i);
                }
            });

            // Create a map
     //       GoogleMap googleMap;
      //      googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        }
    }
}
