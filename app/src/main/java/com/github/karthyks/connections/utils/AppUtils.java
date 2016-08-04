package com.github.karthyks.connections.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by karthy07 on 7/23/2016.
 */
public class AppUtils {
    public static final String USER_PREF_FILE = "user_pref_file";
    private Context context;

    private static AppUtils instance;

    public static AppUtils getInstance(Context context) {
        if (instance == null) {
            instance = new AppUtils(context);
        }
        return instance;
    }

    private AppUtils(Context context) {
        this.context = context;
    }

    public boolean putInSharedPrefs(String key, Object value) {
        SharedPreferences userPref = context.getSharedPreferences(USER_PREF_FILE, 0);

        SharedPreferences.Editor editor = userPref.edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        }
        editor.commit();
        return true;
    }

    @SuppressWarnings("unchecked")
    public <Result> Result getFromSharedPref(String key, Result type) {
        Result result = type;
        SharedPreferences userProfile = context.getSharedPreferences(USER_PREF_FILE, 0);
        if (type instanceof Boolean) {
            result = (Result) (Boolean) userProfile.getBoolean(key, (Boolean) type);
        } else if (type instanceof String) {
            result = (Result) userProfile.getString(key, (String) type);
        } else if (type instanceof Integer) {
            result = (Result) (Integer) userProfile.getInt(key, (Integer) type);
        } else if (type instanceof Float) {
            result = (Result) (Float) userProfile.getFloat(key, (Float) type);
        } else if (type instanceof Long) {
            result = (Result) (Long) userProfile.getLong(key, (Long) type);
        }
        return result;
    }
}
