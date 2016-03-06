package com.sunny.forabetterworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Sunny on 25-02-2016.
 */
public class SplashScreen extends Activity{

    //Splash Screen Timer
    private static int SPLASH_SCREEN_TIMER = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //Sets the layout of the activity.
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
            }, SPLASH_SCREEN_TIMER);
        }
    }


