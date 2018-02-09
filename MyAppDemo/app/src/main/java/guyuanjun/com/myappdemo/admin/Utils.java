package guyuanjun.com.myappdemo.admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

public class Utils {
    public static void saveAdminSession(Context context, String cookieString) {
        SharedPreferences preference = context.getSharedPreferences(Constant.ADMIN_SESSION_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        //然后保存在本地
        editor.putString(Constant.ADMIN_SESSION_ID, cookieString);
        editor.commit();
    }

    public static String getSession(Context context) {
        String cookieString = null;
        SharedPreferences preference = context.getSharedPreferences(Constant.ADMIN_SESSION_FILE, MODE_PRIVATE);
        cookieString = preference.getString(Constant.ADMIN_SESSION_ID, null);
        return cookieString;
    }

    public static boolean isLogin(Context context) {
        String sessionId = getSession(context);
        if (sessionId == null) {
            return false;
        }
        return true;
    }

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

    public static String encodeMD5(String str) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(str.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(str.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
