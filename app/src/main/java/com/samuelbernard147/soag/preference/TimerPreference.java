package com.samuelbernard147.soag.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class TimerPreference {
    private static final String PREFS_NAME = "settings_pref";

    public static final String CHILI = "cabe";
    public static final String MUSTARD_GREEN = "sawi";
    public static final String KALE = "kangkung";
    public static final String TOMATO = "tomat";

    public static final String CHILI_INTERVAL = "cabe_interval";
    public static final String MUSTARD_GREEN_INTERVAL = "kangkung_interval";
    public static final String KALE_INTERVAL = "sawi_interval";
    public static final String TOMATO_INTERVAL = "tomat_interval";

    private final SharedPreferences preferences;

    public TimerPreference(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void clearPreference(String tag) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(tag);
    }

    public void setStatus(String tagName, Boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(tagName, value);
        editor.apply();
    }

    public void setInterval(String tagInterval, int second) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(tagInterval, second);
        editor.apply();
    }

    public boolean getStatus(String tagName) {
        return preferences.getBoolean(tagName, false);
    }

    public int getInterval(String tagInterval) {
        return preferences.getInt(tagInterval, 0);
    }
}