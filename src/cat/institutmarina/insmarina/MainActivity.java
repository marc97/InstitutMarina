/*******************************************************************************
 * Created by Marc Pacheco Garcia on 28/4/13.            
 * http://marcpacheco.me                                 
 * Copyright (c) 2013, All rights reserved.              
 * App icon and Images are property of Institut Marina
 ******************************************************************************/
package cat.institutmarina.insmarina;

import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.util.APILevelsUtils;
import cat.institutmarina.insmarina.util.LanguageUtils;

import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements android.view.View.OnClickListener {
	private LanguageUtils mLanguage;
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		activity = this;
		
		if (APILevelsUtils.hasHoneycomb()) {
		     //getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));
		}
		
		mLanguage = new LanguageUtils(activity, StaticFields.LOCALES,
				StaticFields.LOCALES_READABLE, StaticFields.LOCALE_CA);
		
		// Show changelog when the application is updated.
		if (isAppUpdated()) {
			showAlertDialogChangeLog();
		}
	
		// Find main view buttons.
		Button btn_marina_rss_webpage = (Button) findViewById(R.id.btn_marina);
		Button btn_marina_rss_revista = (Button) findViewById(R.id.btn_revista_marina);
		Button btn_marina_center_plane = (Button) findViewById(R.id.btn_center_plane);
		btn_marina_rss_webpage.setOnClickListener(this);
		btn_marina_rss_revista.setOnClickListener(this);
		btn_marina_center_plane.setOnClickListener(this);
		
		// Find main tablelayout.
		TableLayout container = (TableLayout) findViewById(R.id.tablelayout_container);
		
		int z = 0;
		for (int i = 0; i < 2; i++) {
			
			// Create new tablerow.
			TableRow tRow = new TableRow(this);
			
			for (int f = 0; f < 2; f++) {
				View child = getLayoutInflater().inflate(R.layout.custom_main_grid_content, null);
				
				// Set the icon of items.
				ImageView imv = (ImageView) child.findViewById(R.id.imv_custom_grid);
				imv.setImageResource(StaticFields.images[z]);
				
				// Set the name of items.
				String[] names = getResources().getStringArray(R.array.main_tablelayout_name_items);
				TextView txv = (TextView) child.findViewById(R.id.txv_custom_grid);
				txv.setText(names[z]);
				
				//SetTag view id.
				child.setTag(z);
				
				// Add listener for table layout.
				child.setOnClickListener(tableLayoutListener());
				
				// Increment z variable.
				z++;
				
				// Get display size to calculate the size of the views into the TableRow.
				Display display = getWindowManager().getDefaultDisplay();
				int dsWidth = display.getWidth();
				int llTrowSize = ((dsWidth / 2) - 15);
				
				// Add Linear layout into row 
				tRow.addView(child, llTrowSize, llTrowSize);
			}
			
			// Add row into the table layout.
			container.addView(tRow);
		}
	}
	
	// Click listener for table layout
	private View.OnClickListener tableLayoutListener() {
		View.OnClickListener ckLis = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int indexView = (Integer) v.getTag();
				switch (indexView) {
				
				case 0:
					Intent intent = new Intent(activity, WebViewActivity.class);
					intent.putExtra("URL", StaticFields.URL_MARINA);
					intent.putExtra("ACTIVITY_TITLE", "Institut Marina");
					startActivity(intent);
					
					//Transitions between activities.
					overridePendingTransition(R.anim.activities_transition_in, R.anim.activities_transition_out);
					break;
				
				case 1:
					Intent intent2 = new Intent(activity, WebViewActivity.class);
					intent2.putExtra("URL", StaticFields.URL_MARINA_MOODLE);
					intent2.putExtra("ACTIVITY_TITLE", "Moodle");
					startActivity(intent2);
					
					//Transitions between activities.
					overridePendingTransition(R.anim.activities_transition_in, R.anim.activities_transition_out);
					break;
				
				case 2:
					startActivity(new Intent(activity, ContactActivity.class));

					// Transitions between activities.
					overridePendingTransition(R.anim.activities_transition_in, R.anim.activities_transition_out);
					break;
				
				case 3:
					startActivity(getOpenFacebookIntent(activity, StaticFields.FB_MARINA_ID, StaticFields.FB_MARINA_NAME));
					break;
				}
			}
		};
		return ckLis;
	}

	// Listener for onclick view RSS buttons.
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
	
		case R.id.btn_marina:
			Intent intent = new Intent(this, RSSViewActivity.class);
			intent.putExtra("URL_RSS", StaticFields.URL_RSS_MARINA);
			intent.putExtra("DIFERENCIATE_ACTIVITY", "marina");
			intent.putExtra("URL_WEB_PAGE", StaticFields.URL_MARINA);
			startActivity(intent);
			
			//Transitions between activities.
			overridePendingTransition(R.anim.activities_transition_in, R.anim.activities_transition_out);
			break;
		
		case R.id.btn_revista_marina:
			Intent intent2 = new Intent(this, RSSViewActivity.class);
			intent2.putExtra("URL_RSS", StaticFields.URL_RSS_MARINA_REVISTA);
			intent2.putExtra("DIFERENCIATE_ACTIVITY", "revista");
			intent2.putExtra("URL_WEB_PAGE", StaticFields.URL_MARINA_REVISTA);
			startActivity(intent2);
			
			//Transitions between activities.
			overridePendingTransition(R.anim.activities_transition_in, R.anim.activities_transition_out);
			break;
			
		case R.id.btn_center_plane:
	        startActivity(new Intent(activity, PlaneActivity.class));
			// Transitions between activities.
			overridePendingTransition(R.anim.activities_transition_in, R.anim.activities_transition_out);
			break;
			
		}
	}
	
	private Intent getOpenFacebookIntent(Context context, String id, String name) {
		try {
		    context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
			return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + id));
		} catch (Exception e) {
			return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + name));
		}
	}
	
	// Finish activity when back button is pressed.
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}

    // Inflate the options menu view.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// Do action when buttons of options are touched.
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_language:
				mLanguage.showAlertDialogChoiceLanguage(getString(R.string.alertdialog_select_language));
				break;
			case R.id.action_about:
				startActivity(new Intent(this, AboutActivity.class));
				
				//Transitions between activities.
				overridePendingTransition(R.anim.activities_transition_in_up, R.anim.activities_transition_out_up);
				break;
			case R.id.action_list_of_changes:
				showAlertDialogChangeLog();
				break;
			}
			return true;
	}
	
	// Check if the app has been updated.
	private boolean isAppUpdated() {
		try {
			// current version
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			int versionCode = packageInfo.versionCode;

			SharedPreferences prefs = getSharedPreferences(StaticFields.PREFS_NAME, 0);
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
	
	// Show alertDialog of the register changes.
	private void showAlertDialogChangeLog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.register_changes_alertdialog));
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		WebView wv_changeLog = new WebView(this);
		wv_changeLog.setWebViewClient(new WebViewClient());
		
		String cLang = LanguageUtils.getCurrentLanguage();
		
		if ("ca_es".equalsIgnoreCase(cLang)) {
			wv_changeLog.loadUrl("file:///android_asset/changelog-ca.html");
		} else if ("en_us".equalsIgnoreCase(cLang)) {
			wv_changeLog.loadUrl("file:///android_asset/changelog-en.html");
		}
		
		builder.setView(wv_changeLog);
		builder.show();
	}
}
