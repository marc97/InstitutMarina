package cat.institutmarina.insmarina.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import cat.institutmarina.insmarina.AppPreferences;
import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.utils.DiskCacheUtils;

public class SettingsActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Holo_Light);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) getActionBarToolbar().findViewById(R.id.toolbar_title))
                .setText(getString(R.string.nav_settings));


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container_settings,
                    new PrefFragment()).commit();
        }
    }

    public static class PrefFragment extends PreferenceFragment
            implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

        private static final String PREF_APP_VERSION = "pref_app_version";
        private static final String PREF_APP_LIBRARIES = "pref_app_libraries";
        private static final String PREF_APP_CHANGELOG = "pref_app_changelog";
        private static final String PREF_APP_ENABLE_PHOTOS_CACHE = "pref_app_enable_photos_cache";
        private static final String PREF_APP_DELETE_APP_CACHE = "pref_delete_app_cache";
        private static final String PREF_APP_CACHE_USED = "pref_app_cache_used";

        private static final String ASSETS_FILE_URI_CHANGELOG = "";
        private static final String ASSETS_FILE_URI_LIBRARIES = "marina_app_open_source_libraries.html";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);


            Preference pref_version = (Preference) findPreference(PREF_APP_VERSION);
            String version = null;
            try {
                version = getActivity().getApplicationContext().getPackageManager()
                        .getPackageInfo(getActivity().getApplicationContext()
                                .getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            pref_version.setTitle(this.getString(R.string.app_name) + " " + version);

           // findPreference(PREF_APP_CHANGELOG).setOnPreferenceClickListener(this);
            findPreference(PREF_APP_LIBRARIES).setOnPreferenceClickListener(this);
            findPreference(PREF_APP_DELETE_APP_CACHE).setOnPreferenceClickListener(this);

            CheckBoxPreference pref_enable_photos_cache =
                    (CheckBoxPreference) findPreference(PREF_APP_ENABLE_PHOTOS_CACHE);
            pref_enable_photos_cache.setChecked(AppPreferences.isPhotosDiskCacheEnabled(getActivity()));
            pref_enable_photos_cache.setOnPreferenceChangeListener(this);

            Preference pref_cache_used = findPreference(PREF_APP_CACHE_USED);
            pref_cache_used.setSummary(
                    new DiskCacheUtils(getActivity()).getCacheMemoryUsed() / 1024 / 1024 + "MB " +
                            getString(R.string.settings_cache_memory_used_summary));
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (preference.getKey().equals(PREF_APP_LIBRARIES)) {
                showWebViewAlertDialog(ASSETS_FILE_URI_LIBRARIES, true);
            } else if (preference.getKey().equals(PREF_APP_CHANGELOG)) {
                showWebViewAlertDialog(ASSETS_FILE_URI_CHANGELOG, true);
            } else {
                if (preference.getKey().equals(PREF_APP_DELETE_APP_CACHE)) {
                    DiskCacheUtils cacheDiskUtils = new DiskCacheUtils(getActivity());

                    if (cacheDiskUtils.deleteCache() || cacheDiskUtils.deleteExternalCache()) {
                        Toast.makeText(getActivity(),
                                getString(R.string.toast_cache_deleted), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            return true;
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference.getKey().equals(PREF_APP_ENABLE_PHOTOS_CACHE)) {
                AppPreferences.setPhotosDiskCacheEnabled(getActivity(), (Boolean) newValue);
                System.out.println(newValue);
            }
            return true;
        }

        private void showWebViewAlertDialog(String url, boolean fromAssets) {
            if (fromAssets) {
                url = "file:///android_asset/" + url;
            }
            WebView wb = new WebView(getActivity());
            wb.getSettings().setUseWideViewPort(true);
            wb.loadUrl(url);
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setView(wb)
                    .setPositiveButton(getString(R.string.alert_dialog_accept), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }
    }
}