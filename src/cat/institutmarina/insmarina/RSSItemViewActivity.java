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
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class RSSItemViewActivity extends Activity {
	private String link;
	private String urlFeed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rss_item);
		
		// Set title.
		setTitle(getString(R.string.rss_activity_item_detail_title));
		
		
		if (APILevelsUtils.hasHoneycomb()) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		// Get bundle of intent.
		Bundle extras = getIntent().getExtras();
		
		urlFeed = extras.getString("URL_RSS");
		String title = extras.getString("TITLE");
		link = extras.getString("LINK");
		String description = extras.getString("DESCRIPTION");
		String author = extras.getString("AUTHOR");
		String pubdate = extras.getString("PUBDATE");
		
		TextView txv_detail = (TextView) findViewById(R.id.txv_rss_detail);
		txv_detail.setText(title);
		
		TextView txv_author = (TextView) findViewById(R.id.txv_rss_detail_author);
		txv_author.setText(author);
		
		TextView txv_date = (TextView) findViewById(R.id.txv_rss_detail_date);
		txv_date.setText(pubdate);
		
		WebView webview = (WebView) findViewById(R.id.ww_rss_detail_description);
		webview.loadData(description, "text/html; charset=UTF-8", null);
		
	}
	
	// Create new view intent when the title of new is clicked.
	public void onTitleClick(View v) {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, RSSViewActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("URL_RSS", urlFeed);
			startActivity(intent);
			
			//Transitions between activities.
		    overridePendingTransition(R.anim.activities_transition_in_back, R.anim.activities_transition_out_back);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/*// Handler the problem when Intent.FLAG_ACTIVITY_REORDER_TO_FRONT is called and the
	// overridependingtransition isn't working.
	@Override
	protected void onResume() {
		super.onResume();
		//Transitions between activities.
	    overridePendingTransition(R.anim.activities_transition_in_back, R.anim.activities_transition_out_back);
	}*/
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		//Transitions between activities.
		overridePendingTransition(R.anim.activities_transition_in_back, R.anim.activities_transition_out_back);
	}
}
