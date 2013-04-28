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
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

@SuppressLint("NewApi")
public class WebViewActivity extends Activity {
	private WebView webView;
	private String url;
	private String activityTitle;
	private final Activity activity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Request feature progress on Activity to inform
		// the current progress of the webview.
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.webview);
		getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
				Window.PROGRESS_VISIBILITY_ON);
		
		if (APILevelsUtils.hasHoneycomb()) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		// Get intent and extras.
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		url = bundle.getString("URL");
		activityTitle = bundle.getString("ACTIVITY_TITLE");

		// Find WebView and charge that.
		webView = (WebView) findViewById(R.id.main_WebView);
		chargeWebView(webView, url);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// Transitions between activities.
		overridePendingTransition(R.anim.activities_transition_in_back,
				R.anim.activities_transition_out_back);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void chargeWebView(WebView webView, String url) {

		WebSettings webSettings = webView.getSettings();
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);

		// Load Url.
		webView.loadUrl(url);

		webView.setWebChromeClient(new WebChromeClient() {

			// Set progress.
			@Override
			public void onProgressChanged(WebView view, int progress) {
				activity.setProgress(progress * 100);
				activity.setTitle(getString(R.string.webview_loading_title));

				if (progress == 100) {
					activity.setTitle(activityTitle);
				}
			}
		});

		// When ocurred an error on charge WebView.
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(activity, getString(R.string.toast_webview_error_ocurred), Toast.LENGTH_SHORT).show();
			}
		});

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
		});
	}

	// Return to previous Web page.
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.webview, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			startActivity(new Intent(this, MainActivity.class));
			// Transitions between activities.
			overridePendingTransition(R.anim.activities_transition_in_back,
					R.anim.activities_transition_out_back);
			break;
		case R.id.action_open_url_at_browser:
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);
			break;

		default:
			break;
		}
		return true;
	}

}
