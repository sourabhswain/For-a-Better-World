package com.sunny.forabetterworld;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Sunny on 20-02-2016.
 */
public class MyPreferencesActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "APP_PREFS";
    public static final String USER_NAME_KEY = "USER_NAME_PREFS_";
    public static final String VEHICLE1_NAME_KEY = "VEHICLE1_NAME_PREFS";
    public static final String VEHICLE1_MPG_KEY = "VEHICLE1_MPG_PREFS";
    public static final String VEHICLE1_TYPE = "VEHICLE1_TYPE";

    //UI interface objects
    private EditText vehicle1Name,vehicle1MPG,vehicle1Type,userName;
    private Button saveButton;
   // private Button fbButton;


    private String text;
    private SharedPreference sharedPreference;
    Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
//for color
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6F4E37")));
        sharedPreference = new SharedPreference();
        findViewsById();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = userName.getText().toString();
                if(text.equals(null)) text = "User";
                //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(userName.getWindowToken(), 0);
                sharedPreference.save(context, text, USER_NAME_KEY);
                text = vehicle1Name.getText().toString();
                if(text.equals(null)) text = "Default Vehicle";
                sharedPreference.save(context, text, VEHICLE1_NAME_KEY);
                text = vehicle1MPG.getText().toString();
                if(text.equals(null)) text = "15";
                sharedPreference.save(context,text, VEHICLE1_MPG_KEY);
                text = vehicle1Type.getText().toString();
                if(text.equals(null)) text = "Petrol";
                sharedPreference.save(context,text, VEHICLE1_TYPE);
                //text = vehicle2MPG.getText().toString();
                //sharedPreference.save(context,text, VEHICLE2_MPG_KEY);

                Toast.makeText(context,
                        getResources().getString(R.string.saved),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void findViewsById() {
        userName= (EditText) findViewById(R.id.input_name);
        String temp = sharedPreference.getValue(context, USER_NAME_KEY);
        if(temp != null) userName.setText(sharedPreference.getValue(context,USER_NAME_KEY));
        vehicle1Name = (EditText) findViewById(R.id.input_vehicle1_name);
        temp = sharedPreference.getValue(context,VEHICLE1_NAME_KEY);
        if(temp != null) vehicle1Name.setText(temp);
        vehicle1MPG = (EditText) findViewById(R.id.input_vehicle1_mpg);
        temp = sharedPreference.getValue(context,VEHICLE1_MPG_KEY);
        if(temp != null) vehicle1MPG.setText(temp);
        vehicle1Type = (EditText) findViewById(R.id.vehicle_type);
        temp = sharedPreference.getValue(context,VEHICLE1_TYPE);
        if(temp != null) vehicle1Type.setText(temp);
  //      vehicle2MPG = (EditText) findViewById(R.id.input_vehicle2_mpg);
        saveButton = (Button) findViewById(R.id.btn_save);
    //    fbButton = (Button) findViewById(R.id.btn_fb);
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(MyPreferencesActivity.this, MainActivity.class);
        startActivity(i);
    }
}