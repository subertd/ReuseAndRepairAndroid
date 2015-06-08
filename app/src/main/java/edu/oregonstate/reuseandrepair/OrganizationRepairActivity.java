package edu.oregonstate.reuseandrepair;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import edu.oregonstate.reuseandrepair.database.MySQLiteOpenHelper;
import edu.oregonstate.reuseandrepair.models.Organization;


public class OrganizationRepairActivity extends AppCompatActivity {

    private static final String TAG = OrganizationRepairActivity.class.getName();

    private static final String UNLISTED = "unlisted";

    private Organization organization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);
        populateOrgInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_organization, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_map) {
            displayOrgOnMap();
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateOrgInfo() {
        Log.i(TAG, "entering populateOrgInfo");

        new OrgInfoPopulator().execute();
    }

    private class OrgInfoPopulator extends AsyncTask<Void, Void, Organization> {

        @Override
        protected Organization doInBackground(Void... params) {

            Intent i = getIntent();
            String orgId = i.getStringExtra("orgId");
            final Organization organization = new MySQLiteOpenHelper(OrganizationRepairActivity.this).getOrganizationById((Long.valueOf(orgId)));

            OrganizationRepairActivity.this.organization = organization;

            return organization;
        }

        @Override
        protected void onPostExecute(final Organization organization) {

            if (organization == null) {
                Toast.makeText(OrganizationRepairActivity.this, "Unable to get Organization Data", Toast.LENGTH_LONG).show();
                return;
            }

            TextView nameField = (TextView)findViewById(R.id.organization_name);
            TextView phoneNumberField = (TextView)findViewById(R.id.phone_number);
            TextView websiteUrlField = (TextView)findViewById(R.id.website_url);
            TextView physicalAddressField = (TextView)findViewById(R.id.physical_address);

            final String name = organization.getName();
            final String phoneNumber = organization.getPhoneNumber();
            final String websiteUrl = organization.getWebsiteUrl();
            final String physicalAddress = organization.getPhysicalAddress();

            nameField.setText(name);
            phoneNumberField.setText(phoneNumber != null ? phoneNumber : UNLISTED);
            websiteUrlField.setText(websiteUrl != null ? websiteUrl : UNLISTED);
            physicalAddressField.setText(physicalAddress != null ? physicalAddress : UNLISTED);
        }
    }

    private void displayOrgOnMap() {

        if (organization != null) {
            final Intent mapView = new Intent(this, MapsActivity.class);
            final String organizationName = organization.getName();
            final String organizationAddress = organization.getPhysicalAddress();
            mapView.putExtra("name", organizationName);
            mapView.putExtra("address", organizationAddress);
            startActivity(mapView);
        }

    }
}
