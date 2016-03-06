package com.sunny.forabetterworld;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private BroadcastReceiver broadcastReceiver;
    public static final String NOT_TEMP = "TEMP";
    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.e("Begin", "Started");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // only for gingerbread and newer versions
            if (!canAccessLocation()) {
                requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
            }
        }


        SharedPreference sharedPreference = new SharedPreference();
        sharedPreference.save(this,"0",NOT_TEMP);
        startService(new Intent(this, BackgroundLocationService.class));



         if(isFirstTime()) {
            // What you do when the Application is Opened First time Goes here
            Intent i = new Intent(MainActivity.this, MyPreferencesActivity.class);
            startActivity(i);
        }else{
             Intent i = new Intent(MainActivity.this, Stats_Daily.class);
             startActivity(i);
         }
    }


    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) && hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        int temp = -1;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) temp =checkSelfPermission(perm);
        return(PackageManager.PERMISSION_GRANTED == temp);
    }

    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }

    public boolean showPreference(MenuItem item){
        Intent i = new Intent(MainActivity.this,MyPreferencesActivity.class);
        startActivity(i);
        return true;
    }

    public boolean aboutPage(MenuItem item){
        Intent i = new Intent(MainActivity.this,AboutActivity.class);
        startActivity(i);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_about){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        //unregisterReceiver(broadcastReceiver);
        super.onStop();
    }
}
