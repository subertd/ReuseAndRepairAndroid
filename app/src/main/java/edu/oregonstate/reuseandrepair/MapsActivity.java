package edu.oregonstate.reuseandrepair;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //setUpMap();
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            Toast.makeText(MapsActivity.this, "mMap is null", Toast.LENGTH_LONG).show();
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                Toast.makeText(MapsActivity.this, "mMap is now valid", Toast.LENGTH_LONG).show();
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     *
     * @citation this method uses code from:
     * http://www.androidhive.info/2013/08/android-working-with-google-maps-v2/
     * and
     * http://stackoverflow.com/questions/11932453/how-to-get-latitude-longitude-from-address-on-android
     */
    private void setUpMap() {

        final String name = getIntent().getStringExtra("name");
        final String address = getIntent().getStringExtra("address");

        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            if (address == null) {
                throw new IllegalArgumentException("There is no Address for this Organization");
            }

            List<Address> addresses = geocoder.getFromLocationName(address, 1);

            if (addresses.size() > 0) {

                final double latitude = addresses.get(0).getLatitude();
                final double longitude = addresses.get(0).getLongitude();


                final CameraPosition target = new CameraPosition.Builder().target(
                        new LatLng(latitude, longitude)).zoom(12).build();
                final CameraUpdate update = CameraUpdateFactory.newCameraPosition(target);

                mMap.moveCamera(update);
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(name));
            } else {
                throw new MapException("No results");
            }
        }
        catch (final MapException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch (final IOException e) {
            Toast.makeText(this, "There was a problem interfacing with Google Maps", Toast.LENGTH_LONG).show();
        }
    }
}
