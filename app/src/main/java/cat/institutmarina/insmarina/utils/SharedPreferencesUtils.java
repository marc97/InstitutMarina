package cat.institutmarina.insmarina.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marcpacheco on 29/11/14.
 */
public class SharedPreferencesUtils {

    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferences.Editor getSharedPreferencesEditor(Context context) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.edit();
    }

    public static void putJSON(Context context, String key, JSONObject jsonObject) {
        putString(context, key, jsonObject.toString());
    }

    public static JSONObject getJSON(Context context, String key, JSONObject jsonObject) throws JSONException {
        return new JSONObject(getSharedPreferences(context).getString(key, jsonObject.toString()));
    }

    public static void putInt(Context context, String key, int value) {
        getSharedPreferencesEditor(context).putInt(key, value).commit();
    }

    public static int getInt(Context context, String key, int defValue) {
        return getSharedPreferences(context).getInt(key, defValue);
    }

    public static void putString(Context context, String key, String value) {
        getSharedPreferencesEditor(context).putString(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        return getSharedPreferences(context).getString(key, defValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        getSharedPreferencesEditor(context).putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getSharedPreferences(context).getBoolean(key, defValue);
    }

    public static void putFloat(Context context, String key, float value) {
        getSharedPreferencesEditor(context).putFloat(key, value).commit();
    }

    public static float getFloat(Context context, String key, float defValue) {
        return getSharedPreferences(context).getFloat(key, defValue);
    }

    public static void putLong(Context context, String key, long value) {
        getSharedPreferencesEditor(context).putLong(key, value).commit();
    }

    public static long getLong(Context context, String key, long defValue) {
        return getSharedPreferences(context).getLong(key, defValue);
    }
}