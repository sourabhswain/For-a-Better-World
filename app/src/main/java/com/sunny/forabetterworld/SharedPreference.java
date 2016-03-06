package com.sunny.forabetterworld;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * Created by Sunny on 25-02-2016.
 */
public class SharedPreference {

    public static final String PREFS_NAME = "APP_PREFS";

    public SharedPreference(){
        super();
    }

    public void save(Context context, String text,String key) {
        SharedPreferences settings;
        Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(key, text); //3

        editor.commit(); //4
    }

    public String getValue(Context context,String key) {
        SharedPreferences settings;
        String text;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(key, null);
        return text;
    }


    public void clearSharedPreference(Context context) {
        SharedPreferences settings;
        Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
    }

    public void removeValue(Context context,String key) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(key);
        editor.commit();
    }
}
