package edu.oregonstate.reuseandrepair;

import android.app.ProgressDialog;
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

    private static final String TAG = OrgRepairListingActivity.class.getName();

    private static final String[] FROM = {
            MySQLiteOpenHelper.TABLE_ORGANIZATION_REPAIR_ITEM_COL_ORGANIZATION_ID,
            MySQLiteOpenHelper.TABLE_ORGANIZATION_REPAIR_ITEM_COL_ITEM_ID,
            MySQLiteOpenHelper.TABLE_ORGANIZATION_REPAIR_COL_ADDITIONAL_REPAIR_INFO
    };

    private static final int[] TO = {
            R.id.org_id,
            R.id.org_name,
            R.id.item_additional_info
    };

    private static long itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_repair_listing);

        final Intent i = getIntent();
        final long itemId = i.getLongExtra("itemId", 0);
        if (itemId > 0) {
            OrgRepairListingActivity.itemId = itemId;
        }

        populateOrgRepairList();
    }

    private void populateOrgRepairList() {
        Log.i(TAG, "entering populateOrgRepairList");

        new OrgRepairListPopulator().execute();
    }

    private class OrgRepairListPopulator extends AsyncTask<Void, Void, Cursor> {

        private ProgressDialog progressDialog;

        @Override
        protected Cursor doInBackground(Void... params) {

            return new MySQLiteOpenHelper(OrgRepairListingActivity.this)
                    .getOrganizationsCursorByRepairItem(OrgRepairListingActivity.itemId);
        }

        /**
         * @citation: http://www.android-ios-tutorials.com/android/android-asynctask-example-download-progress/
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = ProgressDialog.show(OrgRepairListingActivity.this, null, "populating...");
        }

        @Override
        protected void onPostExecute(final Cursor cursor) {

            this.progressDialog.dismiss();

            // populate a list view with the cursor
            final ListView listView = (ListView) findViewById(R.id.orgRepair_list);

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
                    final long orgId = cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteOpenHelper.TABLE_ORGANIZATION_COL_ID));

                    // Start new activity to show organization contact info
                    Intent i = new Intent(OrgRepairListingActivity.this, OrganizationRepairActivity.class);
                    i.putExtra("orgId", orgId);
                    startActivity(i);
                }
            });
        }
    }
}
