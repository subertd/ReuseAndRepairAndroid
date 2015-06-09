package edu.oregonstate.reuseandrepair;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
            try {
                displayOrgOnFullscreenMap();
            }
            catch (final MapException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateOrgInfo() {
        Log.i(TAG, "entering populateOrgInfo");

        new OrgInfoPopulator().execute();
    }

    private class OrgInfoPopulator extends AsyncTask<Void, Void, Organization> {

        private ProgressDialog progressDialog;

        @Override
        protected Organization doInBackground(Void... params) {

            final Intent i = getIntent();
            final long orgId = i.getLongExtra("orgId", 0);
            final Organization organization = new MySQLiteOpenHelper(OrganizationRepairActivity.this).getOrganizationById(orgId);

            OrganizationRepairActivity.this.organization = organization;

            return organization;
        }

        /**
         * @citation: http://www.android-ios-tutorials.com/android/android-asynctask-example-download-progress/
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = ProgressDialog.show(OrganizationRepairActivity.this, "Wait", "downloading...");
        }

        @Override
        protected void onPostExecute(final Organization organization) {

            this.progressDialog.dismiss();

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

    private void displayOrgOnFullscreenMap() throws MapException {

        if (organization != null) {
            final Intent mapView = new Intent(this, MapsActivity.class);
            final String organizationName = organization.getName();
            final String organizationAddress = organization.getPhysicalAddress();


            final Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            try {
                if (organizationAddress == null) {
                    throw new MapException("There is no Address for this Organization");
                }

                List<Address> addresses = geocoder.getFromLocationName(organizationAddress, 1);

                if (addresses.size() > 0) {

                    final double latitude = addresses.get(0).getLatitude();
                    final double longitude = addresses.get(0).getLongitude();

                    mapView.putExtra("name", organizationName);
                    //mapView.putExtra("address", organizationAddress);
                    mapView.putExtra("latitude", latitude);
                    mapView.putExtra("longitude", longitude);
                    startActivity(mapView);

                } else {
                    throw new MapException("The address of this organization is not valid");
                }
            }
            catch (final IOException e) {
                throw new MapException("Unable to get map data at this time");
            }
        }
        else {
            throw new MapException("An unknown error has occurred");
        }
    }
}
