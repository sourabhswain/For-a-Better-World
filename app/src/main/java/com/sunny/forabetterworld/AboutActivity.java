package com.sunny.forabetterworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Sunny on 06-03-2016.
 */
public class AboutActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_about);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(AboutActivity.this,Stats_Daily.class);
        startActivity(intent);
    }
}
