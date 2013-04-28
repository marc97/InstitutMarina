/*******************************************************************************
 * Created by Marc Pacheco Garcia on 28/4/13.            
 * http://marcpacheco.me                                 
 * Copyright (c) 2013, All rights reserved.              
 * App icon and Images are property of Institut Marina
 ******************************************************************************/
package cat.institutmarina.insmarina;

import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.util.APILevelsUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

@SuppressLint("NewApi")
public class AboutActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);
		setTitle(getString(R.string.about));

		//For HOYNECOMB or superior builds.
		if (APILevelsUtils.hasHoneycomb()) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		// Get app Version.
		String version = null;
		try {
			version = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		// Set textview app Version.
		TextView txv_version = (TextView) findViewById(R.id.txv_version);
		txv_version.setText(getString(R.string.current_version) + " " + version);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// Transitions between activities.
		overridePendingTransition(R.anim.activities_transition_in_up_back,
				R.anim.activities_transition_out_up_back);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			// Transitions between activities.
			overridePendingTransition(R.anim.activities_transition_in_up_back,
					R.anim.activities_transition_out_up_back);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
