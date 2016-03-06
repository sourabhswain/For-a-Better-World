package com.sunny.forabetterworld;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
//import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class BackgroundLocationService extends Service
{

    IntentFilter s_intentFilter;
    private Context context;
    public static final float PETROL = 2.31f;
    public static final float DIESEL = 2.68f;
    public static final String DAILY = "DISTANCE";
    public static final String CUR = "CUR";
    private static final String TAG = "TESTGPS";
    public static final String FLAG = "FLAG";
    public static final String DAILY_KEY = "DAILY_EMISSION";
    public static final String WEEKLY_KEY = "WEEKLY_EMISSION";
    public static final String USER_NAME_KEY = "USER_NAME_PREFS_";
    public static final String VEHICLE1_NAME_KEY = "VEHICLE1_NAME_PREFS";
    public static final String VEHICLE1_MPG_KEY = "VEHICLE1_MPG_PREFS";
    public static final String VEHICLE1_TYPE = "VEHICLE1_TYPE";
    public static final String DAY_1 = "DAY_1";
    public static final String DAY_2 = "DAY_2";
    public static final String DAY_3 = "DAY_3";
    public static final String DAY_4 = "DAY_4";
    public static final String DAY_5 = "DAY_5";
    public static final String DAY_6 = "DAY_6";
    public static final String DAY_7 = "DAY_7";
    public static final String PUBLIC_TRANSPORT = "VEHICLE_1";
    public static final String NOT_TEMP = "TEMP";
    private static final float MIN_CAR_SPEED = 2.0f; // 2m/sec
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 10000;
    private static final float LOCATION_DISTANCE = 15f;
    public static final String MY_ACTION = "Carbon";
    private SharedPreference sharedPreference;


    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;
        public LocationListener(String provider)
        {
            //Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);

        }

        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location + mLastLocation);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");

            String formattedDate = df.format(c.getTime());

         /*   Toast.makeText(context,
                    "Time!\n"
                            + formattedDate,
                    Toast.LENGTH_SHORT).show(); */

            if(formattedDate.equals("00:00")){
                String str = "0";
                sharedPreference.save(context,str,DAILY_KEY);
                for(int i=1;i<8;i++){
                    if(i != 7) sharedPreference.save(context,sharedPreference.getValue(context,"DAY_" + String.valueOf(i+1)),"DAY_"+ String.valueOf(i));
                    else sharedPreference.save(context,"0","DAY_7");
                }
                sharedPreference.save(context,str,DAY_7);
            }


            if (!(mLastLocation.getLongitude()==0.0000 && mLastLocation.getLatitude()==0.000)){
                float speed = location.getSpeed();
                if(speed > MIN_CAR_SPEED){
                    float check = mLastLocation.distanceTo(location);
                    //showNotification("Stats", String.valueOf(check) + "m");
                    String state = sharedPreference.getValue(context,FLAG);
                    if(state == null){
                        String str = sharedPreference.getValue(context,DAILY_KEY);
                        if(str != null) sharedPreference.save(context,sharedPreference.getValue(context,DAILY_KEY),CUR);
                        else sharedPreference.save(context,"0",CUR);
                        sharedPreference.save(context,"1",FLAG);
                    }else if(Integer.valueOf(state) == 0){
                        String str = sharedPreference.getValue(context,DAILY_KEY);
                        if(str != null) sharedPreference.save(context,sharedPreference.getValue(context,DAILY_KEY),CUR);
                        else sharedPreference.save(context,"0",CUR);
                        sharedPreference.save(context,"1",FLAG);
                    }
                 /*   Toast.makeText(context,
                            "Flag value first one!\n"
                                    + sharedPreference.getValue(context,FLAG),
                            Toast.LENGTH_SHORT).show(); */
                    String dis = sharedPreference.getValue(context,DAILY_KEY);
                    float dis_prefs;
                    if(dis == null) dis_prefs = 0.0f;
                    else dis_prefs = Float.valueOf(dis);
                    float emission = calculate(check);
                    emission /= 1000.0f;
                    if(dis == null){
                        sharedPreference.save(context,String.valueOf(emission),DAILY_KEY);
                    }
                    else{
                        sharedPreference.save(context,String.valueOf(dis_prefs+emission),DAILY_KEY);
                    }
                    sharedPreference.save(context,sharedPreference.getValue(context,DAILY_KEY),DAY_7);
                    speed *= 3.6f;
                    //showNotification("Stats", String.valueOf(dis) + "m Speed " + String.valueOf(speed) + "km/hour");
                }else if(sharedPreference.getValue(context,FLAG) != null && Integer.valueOf(sharedPreference.getValue(context,FLAG)) == 1){
                   /* Toast.makeText(context,
                            "Flag value second one!\n"
                                    + sharedPreference.getValue(context,FLAG),
                            Toast.LENGTH_SHORT).show(); */
                    speed *= 3.6f;
                    String str = sharedPreference.getValue(context,DAILY_KEY);
                    String str1 = sharedPreference.getValue(context,CUR);
                    if(str1.equals(null)){
                        sharedPreference.save(context,"0",CUR);
                        sharedPreference.save(context,"0",NOT_TEMP);
                    }
                    if(str != null) {
                        sharedPreference.save(context,String.valueOf(Float.valueOf(str)-Float.valueOf(sharedPreference.getValue(context,CUR))),CUR);
                        sharedPreference.save(context,sharedPreference.getValue(context,CUR),NOT_TEMP);
                    }else{
                        sharedPreference.save(context,sharedPreference.getValue(context,CUR),NOT_TEMP);
                    }
                    showNotification("Journey Detected", String.valueOf(reverseCalculate(Float.valueOf(sharedPreference.getValue(context,CUR)))) + "m");
                    sharedPreference.save(context, "0", FLAG);
                    //dis = 0.0f;
                }else{
                   /* Toast.makeText(context,
                            "Flag value in the third one!\n"
                                    + sharedPreference.getValue(context,FLAG),
                            Toast.LENGTH_SHORT).show(); */
                }
            }            mLastLocation.set(location);
        }

        // Notification code
        private void showNotification(String title,String content){


            Intent intent = new Intent(BackgroundLocationService.this, OptionActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(BackgroundLocationService.this, 0, intent,0);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_launcher_notification)
                            .setContentTitle(title)
                            .setContentText(content);
            mBuilder.setContentIntent(pIntent);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setAutoCancel(true);

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(0, mBuilder.build());

       /*     Intent intent1 = new Intent();
            intent1.setAction(PUBLIC_TRANSPORT);
            intent1.putExtra("DATAPASSED", content);
            sendBroadcast(intent1); */

        }

        public float calculate(float dis){
            //sharedPreference = new SharedPreference();
            float mileage = Float.valueOf(sharedPreference.getValue(context, VEHICLE1_MPG_KEY));
            String str = sharedPreference.getValue(context, VEHICLE1_TYPE);
            str = str.toLowerCase();
            float emission = 0.0f;
            if(str.charAt(0) == 'p'){
                emission = PETROL;
            }else{
                emission = DIESEL;
            }
            float ret = dis/mileage;
            ret *= emission;
            return ret;
        }

        public float reverseCalculate(float res){
            float mileage = Float.valueOf(sharedPreference.getValue(context,VEHICLE1_MPG_KEY));
            String str = sharedPreference.getValue(context, VEHICLE1_TYPE);
            str = str.toLowerCase();
            float emission = 0.0f;
            if(str.charAt(0) == 'p'){
                emission = PETROL;
            }else{
                emission = DIESEL;
            }
            float ret = res*mileage;
            ret /= emission;
            ret *= 1000.0f;
            return ret;
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");
        context = this;
        s_intentFilter = new IntentFilter();
        s_intentFilter.addAction(Intent.ACTION_TIME_TICK);
        s_intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        s_intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        //registerReceiver(m_timeChangedReceiver, s_intentFilter);
        sharedPreference = new SharedPreference();
        //sharedPreference.save(context,"0",CUR);
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
      //  unregisterReceiver(m_timeChangedReceiver);
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}