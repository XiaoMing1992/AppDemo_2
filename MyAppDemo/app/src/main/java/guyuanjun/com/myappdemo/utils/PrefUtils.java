package guyuanjun.com.myappdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * SharedPreferences工具类
 * 保存 夜间模式
 */
public class PrefUtils {
    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public static void set(String filename, String key, String value, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void set(String filename, String key, boolean value, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void set(String filename, String key, long value, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static void set(String filename, String key, int value, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void set(String filename, String key, float value, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static String getString(String filename, String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return prefs.getString(key, null);
    }

    public static boolean getBoolean(String filename, String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    public static float getFloat(String filename, String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return prefs.getFloat(key, 0);
    }

    public static long getLong(String filename, String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return prefs.getLong(key, 0);
    }

    public static int getInt(String filename, String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return prefs.getInt(key, 0);
    }

    public static void remove(String filename, String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(filename, Context.MODE_PRIVATE);

        try {
            if (getString(filename, key, context) != null) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove(key);
                editor.commit();
            }
        } catch (Exception e) {
            try {
                if (getLong(filename, key, context) != 0) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove(key);
                    editor.commit();
                }
            } catch (Exception e1) {
                try {
                    if (getInt(filename, key, context) != 0) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.remove(key);
                        editor.commit();
                    }
                } catch (Exception e2) {
                    try {
                        if (getFloat(filename, key, context) != 0) {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.remove(key);
                            editor.commit();
                        }
                    } catch (Exception e3) {
                        //布尔值
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.remove(key);
                        editor.commit();
                    }
                }
            }
        }
    }

}