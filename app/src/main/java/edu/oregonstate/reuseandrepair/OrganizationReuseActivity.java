package edu.oregonstate.reuseandrepair;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.oregonstate.reuseandrepair.database.MySQLiteOpenHelper;
import edu.oregonstate.reuseandrepair.models.Organization;


public class OrganizationReuseActivity extends AppCompatActivity {

    private static final String TAG = OrganizationReuseActivity.class.getName();

    private static final String UNLISTED = "unlisted";

    private static final int ZOOM = 10;

    private Organization organization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);

        final Intent currentIntent = getIntent();

        // Preserve item selection for reverse navigation
        final Intent backIntent = new Intent();
        backIntent.putExtra("catId", currentIntent.getLongExtra("itemId", 0));
        setResult(RESULT_OK, backIntent);

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
            displayOrgOnFullscreenMap();
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

            final Intent currentIntent = getIntent();
            final long orgId = currentIntent.getLongExtra("orgId", 0);
            final Organization organization = new MySQLiteOpenHelper(OrganizationReuseActivity.this).getOrganizationById(orgId);

            OrganizationReuseActivity.this.organization = organization;

            return organization;
        }

        /**
         * @citation: http://www.android-ios-tutorials.com/android/android-asynctask-example-download-progress/
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = ProgressDialog.show(OrganizationReuseActivity.this, null, "populating...");
        }

        @Override
        protected void onPostExecute(final Organization organization) {

            this.progressDialog.dismiss();

            if (organization == null) {
                Toast.makeText(OrganizationReuseActivity.this, "Unable to get Organization Data", Toast.LENGTH_LONG).show();
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
            
            //displayMiniMap();
        }
    }

    private void displayOrgOnFullscreenMap() {

        if (organization != null) {
            final Intent mapView = new Intent(this, MapsActivity.class);
            final String organizationName = organization.getName();
            final String organizationAddress = organization.getPhysicalAddress();
            mapView.putExtra("name", organizationName);
            mapView.putExtra("address", organizationAddress);
            startActivity(mapView);
        }

    }

    /*
     * @citation this method uses code from:
     * http://www.androidhive.info/2013/08/android-working-with-google-maps-v2/
     * and
     * http://stackoverflow.com/questions/11932453/how-to-get-latitude-longitude-from-address-on-android
     * and
     * https://developers.google.com/maps/documentation/android/map
     *//*
    private void displayMiniMap() {

        final Organization x = organization;
        final String y = x.getPhysicalAddress();

        if (organization != null && organization.getPhysicalAddress() != null) {

            final Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            try {
                List<Address> addresses = geocoder.getFromLocationName(organization.getPhysicalAddress(), 1);

                if (addresses.size() > 0) {

                    final double latitude = addresses.get(0).getLatitude();
                    final double longitude = addresses.get(0).getLongitude();

                    final MapFragment minimap = new MapFragment();
                    final FragmentManager manager = getFragmentManager();
                    final FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.mini_map, minimap);
                    transaction.commit();

                    final CameraPosition target = new CameraPosition.Builder().target(
                            new LatLng(latitude, longitude)).zoom(ZOOM).build();
                    final CameraUpdate update = CameraUpdateFactory.newCameraPosition(target);

                    minimap.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            googleMap.getUiSettings().setScrollGesturesEnabled(false);
                            googleMap.moveCamera(update);
                            googleMap.addMarker(new MarkerOptions().position(
                                    new LatLng(latitude, longitude)).title(organization.getName()));

                            minimap.getView().setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
            catch (final IOException e) {
                // TODO handle
            }
        }
    }*/
}
