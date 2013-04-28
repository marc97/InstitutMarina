/*******************************************************************************
 * Created by Marc Pacheco Garcia on 28/4/13.            
 * http://marcpacheco.me                                 
 * Copyright (c) 2013, All rights reserved.              
 * App icon and Images are property of Institut Marina
 ******************************************************************************/
package cat.institutmarina.insmarina;

import cat.institutmarina.insmarina.util.APILevelsUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("NewApi")
public class PlaneActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plane_activity);
		setTitle(getString(R.string.plane_activity_title));
		
		if (APILevelsUtils.hasHoneycomb()) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		//ImageViewZoom imvz = new ImageViewZoom(this);
		//LinearLayout ll_plane_activity = (LinearLayout) findViewById(R.id.ll_plane_activity);
		//ll_plane_activity.addView(imvz);
		WebView wv_plane = (WebView) findViewById(R.id.wv_pdf_plane_center);
		wv_plane.setWebViewClient(new WebViewClient());
		
		WebSettings webSettings = wv_plane.getSettings();
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		
		webSettings.setBuiltInZoomControls(true);
		wv_plane.loadUrl("file:///android_asset/marina-center-map.html");
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.activities_transition_in_back, R.anim.activities_transition_out_back);
		}
		return true;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		//Transitions between activities.
		overridePendingTransition(R.anim.activities_transition_in_back, R.anim.activities_transition_out_back);
	}
}
