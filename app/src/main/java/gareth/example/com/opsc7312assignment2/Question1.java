package gareth.example.com.opsc7312assignment2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


import java.util.List;
import java.util.Locale;

public class Question1 extends FragmentActivity implements LocationListener {

    //variables
    private GoogleMap mMap;
    private String provider;
    private Location location;
    private LocationManager locMan;
    TextView tvAddress,tvSuburb,tvCountry,tvPostalCode;
    //----------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.q1menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // when a menu item is selected, perform the following decision
        switch(item.getItemId())
        {
            case R.id.help:
                startActivity(new Intent(this,Q1Help.class));
                break;

            case R.id.exit:
                Q1ExitDialog EDF = Q1ExitDialog.newInstance("Exit?");
                EDF.show(getFragmentManager(), "dialog");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            // if google play services are installed
            if (googlePlayServiceAvailable())
            {
                setContentView(R.layout.activity_question1);
                setUpMapIfNeeded();//set up the map
                setUpFragment();//set up the address fragment

                if(!GPSEnabled())//check if the gps is enabled
                {
                    GPSDisabledAlert();
                }
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //on resume set up the map again
        setUpMapIfNeeded();
    }

    @Override
    public void onLocationChanged(Location location) {
        //get the lat/long, zoom into the location & update the geocoder
        LatLng LL = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LL, 16));
        updateWithNewLocation(LL);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //do nothing
    }

    @Override
    public void onProviderEnabled(String provider) {
        //do nothing
    }

    @Override
    public void onProviderDisabled(String provider) {
        //do nothing
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        mMap.setMyLocationEnabled(true);//enabling the my location button
        locMan = (LocationManager) getSystemService(LOCATION_SERVICE);//linking locMan to the location system service
        provider = LocationManager.GPS_PROVIDER;//setting provider to the gps provider
        location = locMan.getLastKnownLocation(provider);//getting the last known location using the set provider
        locMan.requestLocationUpdates(provider, 30000, 100, this);//performing location updates with the provider every 30 secs or 100m
    }

    private void setUpFragment() {
         //linking local textviews to fragement_address_info textviews
         tvAddress      = (TextView)findViewById(R.id.tvAddress);
         tvSuburb       = (TextView)findViewById(R.id.tvSuburb);
         tvCountry      = (TextView)findViewById(R.id.tvCountry);
         tvPostalCode   = (TextView)findViewById(R.id.tvPostalCode);
    }

    private boolean googlePlayServiceAvailable() {

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if(status == ConnectionResult.SUCCESS)
        {
            return true;
        }
        else
        {
            //alerts the user that google play services is not installed and
            //provides the user with a link to the play store to get the service
            ((Dialog) GooglePlayServicesUtil.getErrorDialog(status, this, 10)).show();
        }
        return false;
    }

    private void updateAddress(String address,String suburb,String country,String PostalCode) {

       // setting textviews to the parameters passed in
       tvAddress.setText("Address: " + address);
       tvSuburb.setText("Suburb: " + suburb);
       tvCountry.setText("Country: " + country);
       tvPostalCode.setText(" Code: " + PostalCode);

       setVisibility();
    }

    private void setVisibility() {

        //setting the fragment_address_info textviews visibility to visible
        tvAddress.setVisibility(View.VISIBLE);
        tvSuburb.setVisibility(View.VISIBLE);
        tvCountry.setVisibility(View.VISIBLE);
        tvPostalCode.setVisibility(View.VISIBLE);
    }

    private void updateWithNewLocation(LatLng LL) {

        //instantiating new Geocoder object
        Geocoder gCode = new Geocoder(getBaseContext(), Locale.getDefault());

        try
        {
            //Address variables
            String streetAdd = "",suburb = "",postCode = "",country = "";
            //----------------

            //adding address information to the addresses arraylist
            List<Address> addresses = gCode.getFromLocation(LL.latitude,LL.longitude,1);

            //if the addresses arraylist is not empty then set variables
            if(addresses.size() > 0)
            {
                streetAdd   = addresses.get(0).getAddressLine(0);
                suburb      = addresses.get(0).getLocality();
                postCode    = addresses.get(0).getPostalCode();
                country     = addresses.get(0).getCountryName();
            }
            //update textviews in fragement_address_info
            updateAddress(streetAdd,suburb,country,postCode);
        }
        catch(Exception Err)
        {
            //display any errors in this toast
            Toast.makeText(getApplicationContext(),"Error: " + Err,Toast.LENGTH_LONG).show();
        }
    }

    private boolean GPSEnabled() {
        //checks if gps is enabled in the device settings
        LocationManager GPSEnabled = (LocationManager)getSystemService(LOCATION_SERVICE);
        return  GPSEnabled.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    private void GPSDisabledAlert(){

        /*
            create an alert dialog which can direct the user to the settings to turn
            gps on so that the app can perform tracking
         */
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Enable GPS").setMessage("GPS is disabled.Would you like to enable it?").setCancelable(false).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent EnableGPSIntent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(EnableGPSIntent);
                        finish();
                    }
                }
        );
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void doPositiveClick() {
        //close the activity
        finish();
    }

    public void doNegativeClick() {
        //Do nothing
    }
}
