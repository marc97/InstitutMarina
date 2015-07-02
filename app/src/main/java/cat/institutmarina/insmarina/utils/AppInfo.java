package cat.institutmarina.insmarina.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppInfo {
	public static boolean isAppUpdated(Context ctx) {
		try {
			// current version
			PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(
					ctx.getPackageName(), 0);
			int versionCode = packageInfo.versionCode;

			SharedPreferences prefs = ctx.getSharedPreferences("APP_VERSION", 0);
			int oldVersion = prefs.getInt("OLDER_VERSION_CODE", 0);

			if (oldVersion < versionCode) {
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt("OLDER_VERSION_CODE", versionCode);
				editor.commit();
				return true;
			} else {
				return false;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String getAppVersionName(Context ctx) {
		try {
			return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int getAppVersionCode(Context ctx) {
		try {
			return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static String getAppName(Context ctx) {
		final PackageManager pm = ctx.getApplicationContext().getPackageManager();
		ApplicationInfo ai;
		
		try {
			ai = pm.getApplicationInfo(ctx.getPackageName(), 0);
		} catch (final NameNotFoundException e) {
			ai = null;
		}
		
		return (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
	}
}