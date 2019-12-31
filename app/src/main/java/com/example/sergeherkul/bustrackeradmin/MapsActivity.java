package com.safet.admin.bustrackeradmin;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.safet.admin.bustrackeradmin.Model.Drivers;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import ng.max.slideview.SlideView;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    final int LOCATION_REQUEST_CODE = 1;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private List<Address> address;
    private Geocoder geocoder;
    private LocationRequest mLocationRequest;
    private Accessories mapsAccessor;
    private TextView today_date, busStatus_text;
    private SlideView start_trip;
    private String button_toggle = "0",school_code, driver_code, driver_name;
    private RadioButton securityRbutton, distressRbutton, goodRbutton;
    private DatabaseReference databaseReference;
    private FirebaseAuth currentuser;
    private String message_arrived_title,
            message_arrived_message,message_arrived_location, message_arrived_date, message_arrived_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapsAccessor = new Accessories(MapsActivity.this);
        getSupportActionBar().setTitle("BusTracker Driver");

        school_code = mapsAccessor.getString("school_code");
        driver_code = mapsAccessor.getString("driver_code");

        currentuser = FirebaseAuth.getInstance();
        String dfname = mapsAccessor.getString("driver_fname");
        if(dfname != null){
            driver_name = dfname+" "+mapsAccessor.getString("driver_lname");
        }else{
            driver_name = "Driver";
        }

        getSupportActionBar().setTitle(mapsAccessor.getString("driver_fname")+" | Driver");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
        }else{
            mapFragment.getMapAsync(this);
        }

        Typeface lovelo =Typeface.createFromAsset(getAssets(),  "fonts/lovelo.ttf");

        today_date = findViewById(R.id.today_date);
        start_trip = findViewById(R.id.start_trip);
        busStatus_text = findViewById(R.id.bus_status_text);
        securityRbutton = findViewById(R.id.security_radio);
        distressRbutton = findViewById(R.id.distress_radio);
        goodRbutton = findViewById(R.id.good_radio);

        today_date.setTypeface(lovelo);
//        start_trip.setTypeface(lovelo);
//        busStatus_text.setTypeface(lovelo);
//        securityRbutton.setTypeface(lovelo);
//        distressRbutton.setTypeface(lovelo);
//        goodRbutton.setTypeface(lovelo);

        new Look_for_all().execute();

        securityRbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder security = new AlertDialog.Builder(MapsActivity.this, R.style.Myalert);
                security.setTitle("Security Confirmation?");
                security.setMessage("Confirmation is required for a security alert to be sent to appropriate authorites. " +
                        "Security issues may include but not limited to\n\na. robbery,\nb. kidnapping and so on.\n\nAre you sure you wish to continue?");
                security.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        logout here
                        if(isNetworkAvailable()){
                            _Security_addToAlerts();
                        }else{
                            Toast.makeText(MapsActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
                        }
                    }
                });

                security.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                security.show();
            }
        });


        //setting an onclick for the distress button
        distressRbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder distress = new AlertDialog.Builder(MapsActivity.this, R.style.Myalert);
                distress.setTitle("Distress Confirmation?");
                distress.setMessage("Confirmation is required for a distress alert to be sent to appropriate authorites. " +
                        "Distresses may included but not limited to\n\na. Bus won't start\nb. No fuel and so on.\n\nAre you sure you wish to continue?");
                distress.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        logout here
                        if(isNetworkAvailable()){
                            _Distress_addToAlerts();
                            Toast.makeText(MapsActivity.this,"Distress alert sent", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(MapsActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
                        }
                    }
                });

                distress.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                distress.show();
            }
        });


        Date date = new Date();
        today_date.setText(DateFormat.getDateInstance(DateFormat.FULL).format(date));

//        start_trip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(button_toggle.equals("0")){
//                    button_toggle = "1";
//                    start_trip.setText("IN PROGRESS");
//                }
//                else if(button_toggle.equals("1")){
//                    button_toggle = "0";
//                    start_trip.setText("START TRIP");
//                }
//            }
//        });
        start_trip.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                if(button_toggle.equals("0")){
                    if(isNetworkAvailable()){
                        button_toggle = "1";
                        start_trip.setText("IN PROGRESS | END");
                        Start_Trip();
                    }else{
                        Toast.makeText(MapsActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                    }
                }

                else if(button_toggle.equals("1")){
                    if(isNetworkAvailable()){
                        button_toggle = "0";
                        start_trip.setText("START TRIP");
                        End_Trip();
                    }else{
                        Toast.makeText(MapsActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private class Look_for_all extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            final Handler thehandler;

            thehandler = new Handler(Looper.getMainLooper());
            final int delay = 15000;

            thehandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(isNetworkAvailable()){
                        get_Messages_IDs();
                    }else{
//                        Toast.makeText(Admin_MainActivity.this,"checking", Toast.LENGTH_LONG).show();
                    }
                    thehandler.postDelayed(this,delay);
                }
            },delay);
            return null;
        }
    }

    private void Start_Trip() {
        DatabaseReference start_trip = FirebaseDatabase.getInstance().getReference("trip_status")
                .child(school_code).child(driver_code);
        start_trip.child("status").setValue("progress");
        start_trip.child("time").setValue(new Date()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MapsActivity.this, "Trip started", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void End_Trip() {
        DatabaseReference start_trip = FirebaseDatabase.getInstance().getReference("trip_status")
                .child(school_code).child(driver_code);
        start_trip.child("status").setValue("Arrived");
        start_trip.child("time").setValue(new Date()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MapsActivity.this, "Trip ended", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void _Security_addToAlerts() {
        Random notifyrandom = new Random();
        int notify_no = notifyrandom.nextInt(233434);
        String notify_id = "notification" + notify_no+"";
        databaseReference = FirebaseDatabase.getInstance().getReference("security").child(school_code).child(driver_code);
        databaseReference.child("image").setValue("SN");
        databaseReference.child("message").setValue("Mr. " + driver_name+" has a security issue. Please proceed swiftly to remedy the situation. See Alerts for details.");
        databaseReference.child("time").setValue(new Date());
        databaseReference.child("title").setValue("Security issue Recieved");
        Toast.makeText(MapsActivity.this,"Security issue alert sent", Toast.LENGTH_LONG).show();
    }

    private void _Distress_addToAlerts() {
        Random notifyrandom = new Random();
        int notify_no = notifyrandom.nextInt(233434);
        String notify_id = "notification" + notify_no+"";
        databaseReference = FirebaseDatabase.getInstance().getReference("distress").child(school_code).child(driver_code);
        databaseReference.child("image").setValue("DN");
        databaseReference.child("message").setValue("Mr. "+driver_name+" is in distress. Exact location can be found on the map.");
        databaseReference.child("time").setValue(new Date());
        databaseReference.child("title").setValue("Distress Recieved");
        databaseReference.child("driver_code").setValue(driver_code);
        Toast.makeText(MapsActivity.this,"Distress sent", Toast.LENGTH_LONG).show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API
        ).build();
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.driver_main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.driver_profile:
                startActivity(new Intent(MapsActivity.this, Driver_Profile.class));
                break;

            case R.id.driver_notifications:
                startActivity(new Intent(MapsActivity.this,Notifications.class));
                break;

            case R.id.driver_messages:
                startActivity(new Intent(MapsActivity.this, Driver_Messages_Activity.class));
                break;

            case R.id.assigned_children:
                startActivity(new Intent(MapsActivity.this,Assigned_Children.class));
                break;

            case R.id.driver_logout:
                final AlertDialog.Builder logout = new AlertDialog.Builder(MapsActivity.this, R.style.Myalert);
                logout.setTitle("Logging Out?");
                logout.setMessage("Leaving us? Your child's safety is our number one concern. Please reconsider.");
                logout.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        logout here
                        if(isNetworkAvailable()){
                            FirebaseAuth.getInstance().signOut();
                            mapsAccessor.put("isverified", false);
                            mapsAccessor.clearStore();
                            startActivity(new Intent(MapsActivity.this,Login_Selector.class));
                        }else{
                            Toast.makeText(MapsActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
                        }
                    }
                });

                logout.setPositiveButton("Stay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                logout.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            public static final String TAG = "";

            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            if(mapsAccessor.getBoolean("isverified")){
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            if(mapsAccessor.getBoolean("isverified")){
                displayLocationSettingsRequest(MapsActivity.this);
            }else{
                startActivity(new Intent(MapsActivity.this, Driver_Verification.class));
            }
        }else{
            startActivity(new Intent(MapsActivity.this, Login_Selector.class));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//    initial state of location changed
//
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        try {
            address = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
//            userlocation.setText(address.get(0).getAddressLine(0));

            //        update location of driver
            try{
                String  userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference drivers = FirebaseDatabase.getInstance().getReference("bus_location").child(school_code);
                GeoFire geoFireAvailable = new GeoFire(drivers);

                geoFireAvailable.setLocation(driver_code, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String s, DatabaseError databaseError) {
                    }
                });
            }catch (NullPointerException e){
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onLocationChanged(Location location) {
//        mLastLocation = location;
//        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        try {
//            address = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
////            userlocation.setText(address.get(0).getAddressLine(0));
//
//            //        update location of driver
//            try{
//                String  userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                DatabaseReference drivers = FirebaseDatabase.getInstance().getReference("bus_details").child(school_code);
//                GeoFire geoFireAvailable = new GeoFire(drivers);
//
//                geoFireAvailable.setLocation(driver_code, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
//                    @Override
//                    public void onComplete(String s, DatabaseError databaseError) {
//                    }
//                });
//            }catch (NullPointerException e){
//                e.printStackTrace();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void get_Messages_IDs() {
        try {
            DatabaseReference get_messages_arrived = FirebaseDatabase.getInstance().getReference("temp_messages")
                    .child(school_code).child(driver_code);
            get_messages_arrived.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Fetch_message_details(child.getKey());
                        }
                    }else{
//                    Toast.makeText(getActivity(),"Cannot get ID",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MapsActivity.this,"Cancelled",Toast.LENGTH_LONG).show();
                }
            });
        }catch(NullPointerException e){

        }
    }

    private void Fetch_message_details(final String key) {
        DatabaseReference has_bus_arrived = FirebaseDatabase.getInstance().getReference("temp_messages")
                .child(school_code).child(driver_code).child(key);
        has_bus_arrived.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("subject")){
                            message_arrived_title = child.getValue().toString();
                        }

                        if(child.getKey().equals("message")){
                            message_arrived_message = child.getValue().toString();
                        }

                        if(child.getKey().equals("location")){
                            message_arrived_location = child.getValue().toString();
                            if(!message_arrived_location.equals("")){

                            }else{
                                message_arrived_location = "";
                            }
                        }
                        if(child.getKey().equals("date")){
                            message_arrived_date = child.getValue().toString();
                            if(!message_arrived_date.equals("Select date")){

                            }else{
                                message_arrived_date = "";
                            }
                        }

                        if(child.getKey().equals("time")){
                            message_arrived_time = child.getValue().toString();
                            if(!message_arrived_time.equals("Select time")){

                            }else{
                                message_arrived_time = "";
                            }
                        }

                        else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();

                        }
                    }
                    Show_arrived_notification(R.drawable.message,key, message_arrived_title, message_arrived_message,
                            message_arrived_location,message_arrived_date,message_arrived_time);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this,"Cancelled",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void Show_arrived_notification(int message, String key, String message_arrived_title,
                                           String message_arrived_message, String message_arrived_location,String message_arrived_date, String message_arrived_time) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(MapsActivity.this, Messages_Activity.class);
//        intent.putExtra("alertID","yes");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MapsActivity.this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MapsActivity.this, "1200")
                .setSmallIcon(message)
                .setContentTitle(message_arrived_title)
                .setContentText(message_arrived_message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message_arrived_message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
//                .setFullScreenIntent(fullScreenPendingIntent,true);

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1200", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MapsActivity.this);

            // notificationId is a unique int for each notification that you must define
            notificationManagerCompat.notify(1200, builder.build());
//            builder.setDefaults(Notification.DEFAULT_SOUND);
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//            builder.setDefaults(Notification.DEFAULT_VIBRATE);
            Move_Arrived_From_pending(key,message_arrived_title,message_arrived_message,message_arrived_location,message_arrived_date,message_arrived_time);

        }else {
//        builder.setDefaults(Notification.DEFAULT_SOUND);
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//        builder.setDefaults(Notification.DEFAULT_VIBRATE);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MapsActivity.this);
            // notificationId is a unique int for each notification that you must define
            notificationManagerCompat.notify(1200, builder.build());
            Move_Arrived_From_pending(key,message_arrived_title, message_arrived_message, message_arrived_location,message_arrived_date, message_arrived_time);
        }
    }

    private void Move_Arrived_From_pending(final String message_key, String message_arrived_title, String message_arrived_message, String message_arrived_location,String message_arrived_date, String message_arrived_time) {
        try {
            DatabaseReference move_from_tmpMessage_to_main = FirebaseDatabase.getInstance().getReference("messages")
                    .child(school_code).child(driver_code).child(message_key);

            move_from_tmpMessage_to_main.child("subject").setValue(message_arrived_title);
            move_from_tmpMessage_to_main.child("message").setValue(message_arrived_message);
            move_from_tmpMessage_to_main.child("location").setValue(message_arrived_location);
            move_from_tmpMessage_to_main.child("date").setValue(message_arrived_date);
            move_from_tmpMessage_to_main.child("time").setValue(message_arrived_time)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            DatabaseReference removeRef = FirebaseDatabase.getInstance().getReference("temp_messages").child(school_code).child(driver_code).child(message_key);
                            removeRef.removeValue();
                            Toast.makeText(MapsActivity.this, "Removed", Toast.LENGTH_LONG).show();
                        }
                    });
        }catch (NullPointerException e){

        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}

//use google maps depedency 16.1.0 to remove the supportMapsFragment error
