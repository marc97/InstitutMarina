package cat.institutmarina.insmarina.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by marcpacheco on 30/11/14.
 */
public class DiskCacheUtils {
    private Context context;
    public static final String DEBUG_TAG = "CacheDiskUtils";

    public DiskCacheUtils(Context context) {
        this.context = context;
    }

    public File getCacheDir() {
        File cacheDir = context.getCacheDir();
        if (cacheDir == null) {
            Log.w(DEBUG_TAG, "Cache dir doesn't exists");
        }
        return cacheDir;
    }

    public File getExternalCacheDir() {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            Log.w(DEBUG_TAG, "External cache dir doesn't exists");
        }
        return cacheDir;
    }

    public boolean deleteCache() {
        boolean result = deleteFilesFromDirectory(getCacheDir());
        if (!result) {
            Log.w(DEBUG_TAG, "Error deleting cache.");
        }
        return result;
    }

    public boolean deleteExternalCache() {
        boolean result = deleteFilesFromDirectory(getExternalCacheDir());
        if (!result) {
            Log.w(DEBUG_TAG, "Error deleting external cache.");
        }
        return result;
    }

    public long getCacheMemoryUsed(boolean external) {
        if (external) {
            return calculateDirSize(getExternalCacheDir());
        } else {
            return calculateDirSize(getCacheDir());
        }
    }

    public long getCacheMemoryUsed() {
        if (isExternalStorageAvailable()) {
            return getCacheMemoryUsed(true);
        } else {
            return getCacheMemoryUsed(false);
        }
    }

    // Bad METHOD!!!!
    private boolean deleteFilesFromDirectory(File fileDir) {
        File[] files = fileDir.listFiles();
        boolean result = false;
        for (File file : files) {
            result = file.delete();
        }
        return result;
    }

    private long calculateDirSize(File file) {
        long size = 0;
        File[] files = file.listFiles();

        for (File f: files) {
            size += f.length();
        }

        return size;
    }

    private static boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}