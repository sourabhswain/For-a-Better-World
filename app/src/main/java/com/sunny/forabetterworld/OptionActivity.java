package com.sunny.forabetterworld;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Sunny on 06-03-2016.
 */
public class OptionActivity extends Activity {

    private Button yes,no;
    private String temp = null;
    private SharedPreference sharedPreference;
    Context context;
    BackgroundLocationService bg;
    public static final String DAILY_KEY = "DAILY_EMISSION";
    public static final String WEEKLY_KEY = "WEEKLY_EMISSION";
    public static final String USER_NAME_KEY = "USER_NAME_PREFS_";
    public static final String VEHICLE1_NAME_KEY = "VEHICLE1_NAME_PREFS";
    public static final String VEHICLE1_MPG_KEY = "VEHICLE1_MPG_PREFS";
    public static final String VEHICLE1_TYPE = "VEHICLE1_TYPE";
    public static final float PETROL = 2.31f;
    public static final float DIESEL = 2.68f;
    public static final String DAY_1 = "DAY_1";
    public static final String DAY_2 = "DAY_2";
    public static final String DAY_3 = "DAY_3";
    public static final String DAY_4 = "DAY_4";
    public static final String DAY_5 = "DAY_5";
    public static final String DAY_6 = "DAY_6";
    public static final String DAY_7 = "DAY_7";
    public static final String NOT_TEMP = "TEMP";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        findViewById();
       // bg = new BackgroundLocationService();
        context = this;
        //distance.setText(temp);
         /* Toast.makeText(context,
                            "To reduce!\n"
                                    + sharedPreference.getValue(context,NOT_TEMP),
                            Toast.LENGTH_SHORT).show(); */
        sharedPreference = new SharedPreference();
        yes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(sharedPreference.getValue(context,NOT_TEMP) != null){
                    float reduce = Float.valueOf(sharedPreference.getValue(context,NOT_TEMP));
                    float cur = Float.valueOf(sharedPreference.getValue(context, DAILY_KEY));
                    sharedPreference.save(context,String.valueOf(cur - reduce),DAILY_KEY);
                    sharedPreference.save(context,sharedPreference.getValue(context, DAILY_KEY),DAY_7);
                    sharedPreference.save(context, "0", NOT_TEMP);
                }
                Intent i = new Intent(OptionActivity.this,Stats_Daily.class);
                startActivity(i);
            }
        });
        no.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(OptionActivity.this,Stats_Daily.class);
                startActivity(i);
            }
        });
    }

  /*  @Override
    protected void onStart() {
        // TODO Auto-generated method stub

        //Register BroadcastReceiver
        //to receive event from our service
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BackgroundLocationService.PUBLIC_TRANSPORT);
        registerReceiver(myReceiver, intentFilter);

        //Start our own service
        Intent intent = new Intent(OptionActivity.this,
                BackgroundLocationService.class);
        startService(intent);
        super.onStart();
    } */


    private void findViewById(){
        //distance = (TextView) findViewById(R.id.body_distance);
        yes = (Button) findViewById(R.id.button1);
        no = (Button) findViewById(R.id.button2);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
       // unregisterReceiver(myReceiver);
        super.onStop();
    }


}
