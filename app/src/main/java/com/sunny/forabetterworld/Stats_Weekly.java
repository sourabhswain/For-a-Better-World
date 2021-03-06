package com.sunny.forabetterworld;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import java.math.BigDecimal;


/**
 * Created by Sunny on 04-03-2016.
 */
public class Stats_Weekly extends AppCompatActivity {
    Context context;
    View view;
    public static final String DAILY_KEY = "DAILY_EMISSION";
    public static final String WEEKLY_KEY = "WEEKLY_EMISSION";
    public static final String DAY_1 = "DAY_1";
    public static  final String DAY_2 = "DAY_2";
    public static final String DAY_3 = "DAY_3";
    public static final String DAY_4 = "DAY_4";
    public static final String DAY_5 = "DAY_5";
    public static final String DAY_6 = "DAY_6";
    public static final String DAY_7 = "DAY_7";
    private static float data;
    ProgressCircle progressCircle;
    Button daily,weekly;
    //MyReceiver myReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_demo);
        //  ProgressCircle.flag = 0;
        android.support.v7.app.ActionBar bar = getSupportActionBar();
//for color
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6F4E37")));
        // bar.setLogo(R.drawable.ic_launcher);
        context = this;
        final SharedPreference sharedPreference = new SharedPreference();
        progressCircle = (ProgressCircle) findViewById(R.id.progress_circle);
        progressCircle.startAnimation();
        animate(view);
        daily = (Button) findViewById(R.id.button_daily);
        weekly = (Button) findViewById(R.id.button_weekly);
        daily.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sharedPreference.save(context,"0","Flag");
                Intent i = new Intent(Stats_Weekly.this,Stats_Daily.class);
                startActivity(i);
            }
        });

        weekly.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sharedPreference.save(context,"1","Flag");
                Intent i = new Intent(Stats_Weekly.this,Stats_Weekly.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub

        //Start our own service
        Intent intent = new Intent(Stats_Weekly.this,
                BackgroundLocationService.class);
        startService(intent);

        super.onStart();
    }



    public void animate(View view) {
        SharedPreference sharedPreference = new SharedPreference();
        //List<String temp = sharedPreference.getValueArray(this,WEEKLY_KEY);
        data = 0.0f;
        for(int i=0;i<7;i++){
            String temp = sharedPreference.getValue(context,"DAY_" + String.valueOf(i+1));
            if(temp == null) data += 0.0f;
            else data += Float.valueOf(temp);
        }

       /* Toast.makeText(Stats_Weekly.this,
                "Triggered by Service!\n"
                        + "Data passed: " + String.valueOf(sharedPreference.getValue(this,"Flag")),
                Toast.LENGTH_SHORT).show(); */

        BigDecimal bd = new BigDecimal(Float.toString(data));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        data = bd.floatValue();

        float val = data;
        if(val > 70.0f){
            val = 1.0f;
        }else{
            val = (data)/ 70.0f;
        }
        progressCircle.setProgress(val);
        progressCircle.startAnimation();
    }


    public boolean showPreference(MenuItem item){
        Intent i = new Intent(Stats_Weekly.this,MyPreferencesActivity.class);
        startActivity(i);
        return true;
    }

    public boolean aboutPage(MenuItem item){
        Intent i = new Intent(Stats_Weekly.this,AboutActivity.class);
        startActivity(i);
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
