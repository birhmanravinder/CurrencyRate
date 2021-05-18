package com.birhman.currencyrate.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class SavedPrefManager {
    private static SharedPreferences myPrefs;
    private static SharedPreferences.Editor prefsEditor;

    public static void openDataBase(Context context) {
        try {
            context=context.getApplicationContext();
            myPrefs = context.getSharedPreferences("Reload", context.MODE_PRIVATE);
            prefsEditor = myPrefs.edit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static String getId() {
        String i = myPrefs.getString("KEY_ID"," ");
        Log.e("dfg", i);
        return i;
    }

    public static void setId(String m) {
        Log.e("dfgdfd", m);
        prefsEditor.putString("KEY_ID", m);
        prefsEditor.commit();
    }

    public static String getUserId() {
        String i = myPrefs.getString("KEY_USER_ID"," ");
        Log.e("dfg", i);
        return i;
    }

    public static void setUserId(String m) {
        Log.e("dfgdfd", m);
        prefsEditor.putString("KEY_USER_ID", m);
        prefsEditor.commit();
    }

    public static String getName(){
        String name = myPrefs.getString("KEY_NAME", "");
        return name;
    }

    public static void setName(String name){
        prefsEditor.putString("KEY_NAME", name);
        prefsEditor.commit();
    }

    public static String saveStringPreferences(Context context, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
        return key;
    }

    public static String getStringPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, "");
    }
}
