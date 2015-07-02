package cat.institutmarina.insmarina;

import android.content.Context;

import cat.institutmarina.insmarina.utils.SharedPreferencesUtils;

/**
 * Created by marcpacheco on 29/11/14.
 */
public class AppPreferences {
    public static final String PREF_PHOTOS_DISK_CACHE_ENABLED = "pref_photos_disk_cache_enabled";

    public static void setPhotosDiskCacheEnabled(Context context, boolean enabled) {
        SharedPreferencesUtils.putBoolean(context, PREF_PHOTOS_DISK_CACHE_ENABLED, enabled);
    }

    public static boolean isPhotosDiskCacheEnabled(Context context) {
        return SharedPreferencesUtils.getBoolean(
                context, PREF_PHOTOS_DISK_CACHE_ENABLED, true);
    }
}