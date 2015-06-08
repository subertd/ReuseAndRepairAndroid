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

public class OrgReuseListingActivity extends AppCompatActivity {

    private static final String[] FROM = {
            MySQLiteOpenHelper.TABLE_ORGANIZATION_COL_ID,
            MySQLiteOpenHelper.TABLE_ORGANIZATION_COL_NAME
    };

    private static final int[] TO = {
            R.id.org_id,
            R.id.org_name
    };

    private static final String TAG = OrgReuseListingActivity.class.getName();
    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_reuse_listing);

        populateOrgList();
    }

    private void populateOrgList() {
        Log.i(TAG, "entering populateOrgList");

        new OrgListPopulator().execute();
    }

    private class OrgListPopulator extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {

            Intent i = getIntent();
            String itemId = i.getStringExtra("itemId");

            return new MySQLiteOpenHelper(OrgReuseListingActivity.this).getOrganizationsCursorByReuseItem((Long.valueOf(itemId)));
        }

        @Override
        protected void onPostExecute(final Cursor cursor) {

            // populate a list view with the cursor
            listView = (ListView) findViewById(R.id.org_list);

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

                    // Get corresponding org id and name from this row
                    String orgId = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteOpenHelper.TABLE_ORGANIZATION_COL_ID));

                    // Start new activity to show organization contact info
                    Intent i = new Intent(OrgReuseListingActivity.this, OrganizationReuseActivity.class);
                    i.putExtra("orgId", orgId);
                    startActivity(i);
                }
            });
        }
    }
}
