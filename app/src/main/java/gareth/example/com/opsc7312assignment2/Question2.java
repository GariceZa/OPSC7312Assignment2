package gareth.example.com.opsc7312assignment2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Gareth on 16/08/2014.
 */
public class Question2 extends Activity implements LocationListener {

    //variables
    private LocationManager locMan;
    private Location location;
    private String provider;
    String lat = "",lon = "",sendNumber = "",sendTo = "";
    TextView tvLocation;
    AutoCompleteTextView ACTVContacts;
    List<String> storedContacts,storedNumbers;
    Button btnSendSMS;
    EditText ETMessage;
    PendingIntent sentPI,deliveredPI;
    BroadcastReceiver sentReceiver,deliveredReceiver;
    String sent = "SMS_SENT",delivered = "SMS_DELIVERED";
    //----------------------

    //Class instantiations
    Q2ContentProvider CP = new Q2ContentProvider();
    //--------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question2);
        setUp();// performing activity initializations

        //check if the devices gps is on
        if(!GPSEnabled())
        {
            GPSDisabledAlert();
        }
        else

        //creating pending intents
        sentPI = PendingIntent.getBroadcast(this,0,new Intent(sent),0);
        deliveredPI = PendingIntent.getBroadcast(this,0,new Intent(delivered),0);
        //------------------------

        /*
            creating an itemClickListener to gather contact information when a contact is selected in
            the RequiredContact auto complete text view
        */
        ACTVContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // retrieving the selected contacts name
                sendTo = (String)parent.getItemAtPosition(position);
                /*
                    searching the storedContacts list to match the selected contact
                    with its stored number in the storedNumbers list
                 */
                for(int ID = 0; ID < storedContacts.size();ID++)
                {
                    if(storedContacts.get(ID).equals(sendTo))
                    {
                        sendNumber = storedNumbers.get(ID);
                    }
                }
                //validating all variables are set so the SMS can be sent
                if (!lat.equals("") && !lon.equals("") && !sendNumber.equals(""))
                {
                    btnSendSMS.setEnabled(true);
                }
            }
        });

        //if the user deletes the contact from the text view then disable the send sms button
        ACTVContacts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 /*
                    if the users has deleted the selected contact from the text view
                    disable the send sms button
                */
                if(s.length() == 0)
                {
                    btnSendSMS.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //do nothing
            }
        });

        //when a lat/long value has been received enable the contact text view
        tvLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            //do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
            // when the lat/lon variables are set enable the auto complete text view so the user can
            // search for a contact to SMS
                ACTVContacts.setEnabled(true);
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {

        //when the location changes set the latitude & longitude and
        //update tvLocation with the new co ordinates
        lat = ""+location.getLatitude();
        lon = ""+location.getLongitude();
        tvLocation.setText("Location: "+ lat + " " + lon );
        //-----------------------------

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

    public void onClickSendSMS (View view) {
        // if the user has left the message box blank then send a default message
        // else use their message
        if (ETMessage.getText().equals(""))
        {
            SendSMS(sendNumber, "My Location is: http://maps.google.com/maps?q=" + lat + "," + lon);
        }
        else
        {
            SendSMS(sendNumber, ETMessage.getText().toString() + " http://maps.google.com/maps?q=" + lat + "," + lon);
        }

    }

    private void setUp() {

        //Setting up the location manager
        locMan = (LocationManager) getSystemService(LOCATION_SERVICE);
        provider = LocationManager.GPS_PROVIDER;
        location = locMan.getLastKnownLocation(provider);
        locMan.requestLocationUpdates(provider, 10000, 50, this);
        //------------------------------

        //linking views
        tvLocation = (TextView)findViewById(R.id.tvLocation);
        ETMessage = (EditText)findViewById(R.id.txtMessage);
        btnSendSMS = (Button)findViewById(R.id.btnSendMessage);
        //--------------

        //Initializing list arrays
        storedContacts  = CP.getContacts(this);
        storedNumbers   = CP.getNumbers(this);
        //------------------------

        //setting up auto complete text view
        ACTVContacts = (AutoCompleteTextView)findViewById(R.id.ACTVContact);
        ACTVContacts.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,storedContacts));
        ACTVContacts.setThreshold(1);
        //---------------------------------

    }

    private void SendSMS(String number,String msg) {
        //creating a broadcast receiver to obtain sent sms responses
        sentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        //creating a broadcast receiver to obtain delivery responses
        deliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        //registering the broadcast receivers
        registerReceiver(sentReceiver,new IntentFilter(sent));
        registerReceiver(deliveredReceiver,new IntentFilter(delivered));

        //sending the sms
        SmsManager smsMan = SmsManager.getDefault();
        smsMan.sendTextMessage(number,null,msg,sentPI,deliveredPI);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.q2menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //when a menu item is selected, perform this decision
        switch(item.getItemId())
        {
            case R.id.q2Help:
                startActivity(new Intent(this,Q2Help.class));
                break;

            case R.id.q2Exit:
                Q2ExitDialog EDF = Q2ExitDialog.newInstance("Exit?");
                EDF.show(getFragmentManager(), "dialog");
                break;
        }
        return super.onOptionsItemSelected(item);
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
        //finish the activity when the user clicks ok
        //in the exit dialog
        finish();
    }

    public void doNegativeClick() {
        //do nothing
    }
}
