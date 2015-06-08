package edu.oregonstate.reuseandrepair;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import edu.oregonstate.reuseandrepair.database.MySQLiteOpenHelper;

public class OrgRepairListingActivity extends AppCompatActivity {

    private static final String[] FROM = {
            MySQLiteOpenHelper.TABLE_ORGANIZATION_COL_ID,
            MySQLiteOpenHelper.TABLE_ORGANIZATION_COL_NAME
    };

    private static final int[] TO = {
            R.id.org_id,
            R.id.org_name
    };

    private static final String TAG = OrgRepairListingActivity.class.getName();
    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_repair_listing);

        populateOrgRepairList();
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
                    R.layout.org_repair_listing_entry,
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
                    Intent i = new Intent(OrgRepairListingActivity.this, OrganizationReuseActivity.class);
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
