package com.example.sergeherkul.bustrackeradmin;

import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Admin_MainActivity extends AppCompatActivity {

    FirebaseAuth mauth;
    Accessories mainaccessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mauth = FirebaseAuth.getInstance();
        mainaccessor = new Accessories(Admin_MainActivity.this);

        getSupportActionBar().setTitle("Admin | Safet");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.admin_profile:
                startActivity(new Intent(Admin_MainActivity.this, Admin_Profile.class));
                break;

            case R.id.admin_notifications:
                startActivity(new Intent(Admin_MainActivity.this, Notifications.class));
                break;

            case R.id.admin_the_drivers:
                startActivity(new Intent(Admin_MainActivity.this,Registered_Drivers.class));
                break;

            case R.id.admin_logout:
                final AlertDialog.Builder logout = new AlertDialog.Builder(Admin_MainActivity.this, R.style.Myalert);
                logout.setTitle("Logging Out?");
                logout.setMessage("Leaving us? Please reconsider.");
                logout.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        logout here
                        if(isNetworkAvailable()){
                            FirebaseAuth.getInstance().signOut();
                            mainaccessor.put("isverified", false);
                            mainaccessor.clearStore();
                            startActivity(new Intent(Admin_MainActivity.this,Login_Selector.class));
                        }else{
                            Toast.makeText(Admin_MainActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
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

    @Override
    protected void onStart() {
        super.onStart();
        if(mauth.getCurrentUser() != null){
            if(mainaccessor.getBoolean("isverified")){
                if(mainaccessor.getString("user_type").equals("Admin")){

                }else{
                    Intent verify_school = new Intent(Admin_MainActivity.this, MapsActivity.class);
                    verify_school.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(verify_school);
                }
            }else{
                if(mainaccessor.getString("user_type").equals("Admin")){
                    Intent verify_school = new Intent(Admin_MainActivity.this, Verify_School.class);
                    verify_school.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(verify_school);
                }else{
                    Intent verify_school = new Intent(Admin_MainActivity.this, Driver_Verification.class);
                    verify_school.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(verify_school);
                }
            }
        }else{
            Intent gotoLogin = new Intent(Admin_MainActivity.this, Login_Selector.class);
            gotoLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(gotoLogin);
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
