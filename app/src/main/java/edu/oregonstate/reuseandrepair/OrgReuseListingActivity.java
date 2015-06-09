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

public class OrgReuseListingActivity extends AppCompatActivity {

    private static final String TAG = OrgReuseListingActivity.class.getName();

    private static final String[] FROM = {
            MySQLiteOpenHelper.TABLE_ORGANIZATION_COL_ID,
            MySQLiteOpenHelper.TABLE_ORGANIZATION_COL_NAME
    };

    private static final int[] TO = {
            R.id.org_id,
            R.id.org_name
    };

    private static long itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_reuse_listing);

        final Intent current = getIntent();

        // Preserve category selection for reverse navigation
        final Intent backIntent = new Intent();
        backIntent.putExtra("catId", current.getLongExtra("catId", 0));
        setResult(RESULT_OK, backIntent);

        // Set itemId Parameter
        final long itemId = current.getLongExtra("itemId", 0);
        if (itemId > 0) {
            OrgReuseListingActivity.itemId = itemId;
        }

        populateOrgList();
    }

    private void populateOrgList() {
        Log.i(TAG, "entering populateOrgList");

        new OrgListPopulator().execute();
    }

    private class OrgListPopulator extends AsyncTask<Void, Void, Cursor> {

        private ProgressDialog progressDialog;

        @Override
        protected Cursor doInBackground(Void... params) {

            return new MySQLiteOpenHelper(OrgReuseListingActivity.this).getOrganizationsCursorByReuseItem(itemId);
        }

        /**
         * @citation: http://www.android-ios-tutorials.com/android/android-asynctask-example-download-progress/
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = ProgressDialog.show(OrgReuseListingActivity.this, null, "populating...");
        }

        @Override
        protected void onPostExecute(final Cursor cursor) {

            this.progressDialog.dismiss();

            // populate a list view with the cursor
            final ListView listView = (ListView) findViewById(R.id.org_list);

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    OrgReuseListingActivity.this,
                    R.layout.org_reuse_listing_entry,
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

                    // Get org id from this row
                    final long orgId = cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteOpenHelper.TABLE_ORGANIZATION_COL_ID));

                    // Start new activity to show organization contact info
                    final Intent organizationReuseActivity = new Intent(OrgReuseListingActivity.this, OrganizationReuseActivity.class);
                    organizationReuseActivity.putExtra("itemId", itemId);
                    organizationReuseActivity.putExtra("orgId", orgId);
                    startActivityForResult(organizationReuseActivity, MyActivityResults.REPAIR_ORGS_FROM_ORG);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == MyActivityResults.REUSE_ORGS_FROM_ORG) {
            OrgReuseListingActivity.itemId = intent.getLongExtra("itemId", 0);
        }
    }
}
