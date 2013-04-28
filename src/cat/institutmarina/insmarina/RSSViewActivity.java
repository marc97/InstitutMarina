/*******************************************************************************
 * Created by Marc Pacheco Garcia on 28/4/13.            
 * http://marcpacheco.me                                 
 * Copyright (c) 2013, All rights reserved.              
 * App icon and Images are property of Institut Marina
 ******************************************************************************/
package cat.institutmarina.insmarina;

import java.util.ArrayList;
import java.util.HashMap;

import cat.institutmarina.insmarina.R;
import cat.institutmarina.insmarina.util.APILevelsUtils;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class RSSViewActivity extends Activity {
	private static final String MARINA_REVISTA_EMAIL = "aniram.revista@gmail.com";
	private Activity activity;
	private ListView listView;
	private TextView textView; 
	private ArrayList<HashMap<String, String>> list;
	private AsyncTask<Void, Void, Void> progressTask;
	private String url;
	private String urlWebpage;
	private String diferenciateActivity;
	private Typeface tf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rss);
		setTitle(getString(R.string.RSSViewActivity_title));
		
		tf = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
		
		textView = (TextView) findViewById(R.id.update_for_view);
		textView.setText(getString(R.string.refresh_to_see_the_content));
		
		// Get intent url.
		Bundle extras = getIntent().getExtras();
		url = extras.getString("URL_RSS");
		urlWebpage = extras.getString("URL_WEB_PAGE");
		diferenciateActivity = extras.getString("DIFERENCIATE_ACTIVITY");
		
		// Get listview.
	    listView = (ListView) findViewById(R.id.lv_rss);
		
		// Set variable activity.
		activity = this;
		
		// Display the button to go to before activity.
		if (!APILevelsUtils.hasHoneycomb()) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		// Charge RSS ListView.
		if (savedInstanceState == null) {
			
			//Check internet connection.
			if (comproveNetworkState()) {
				progressTask = new ProgressTask().execute();
			} else {
				Toast.makeText(this, getString(R.string.toast_check_your_internet_connection), Toast.LENGTH_LONG).show();
				textView.setVisibility(View.VISIBLE);
			}
		} else {
			list = (ArrayList<HashMap<String, String>>) savedInstanceState.getSerializable("LIST");
			setAdapterView();
		}
		
		addListViewScrollListenerAnim();
		addListViewListener();
	}
	
	private int mLastFirstVisibleItem;
	private boolean mIsScrollingUp = false;
	private void addListViewScrollListenerAnim() {
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			    if (view.getId() == listView.getId()) {
			        final int currentFirstVisibleItem = listView.getFirstVisiblePosition();
			        
                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
			            mIsScrollingUp = false;
			        } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
			            mIsScrollingUp = true;
			        }
			        
			        mLastFirstVisibleItem = currentFirstVisibleItem;
			    } 
			}
		});
	}
	
	public boolean comproveNetworkState() {
		final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected() && activeNetwork.isAvailable() 
				&& activeNetwork.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}
	
	// Save data of arraylist.
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable("LIST", list);
		super.onSaveInstanceState(outState);
	}
	
	private void setAdapterView() {
		listView.setAdapter(new RSSAdapter(list));
		
		// Hide info update.
		textView.setVisibility(View.INVISIBLE);
	}
	
	// Add Listener on ListView for capture the item and 
	// start the RSSItemActivity with the corresponent bundle data.
	private void addListViewListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(position);
				Intent intent = new Intent(activity, RSSItemViewActivity.class);
				intent.putExtra("TITLE", map.get("title"));
				intent.putExtra("LINK", map.get("link"));
				intent.putExtra("DESCRIPTION", map.get("description"));
				intent.putExtra("AUTHOR", map.get("author"));
				intent.putExtra("PUBDATE", map.get("pubDate"));
				intent.putExtra("URL_RSS", url);
				startActivity(intent);
				
				//Transitions between activities.
				overridePendingTransition(R.anim.activities_transition_in, R.anim.activities_transition_out);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
	    menuInflater.inflate(R.menu.rss_listview_activity, menu);
		return true;
	}
	
	// Listener for options item.
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			
			//Transitions between activities.
		    overridePendingTransition(R.anim.activities_transition_in_back, R.anim.activities_transition_out_back);
			break;
			
		// Update listView.	
		case R.id.update_rss_listview:
			progressTask = new ProgressTask().execute();
			break;
			
		// Open navigator app.
		case R.id.btn_email_contact_revista:
			Intent intent2 = new Intent(Intent.ACTION_SENDTO);
			intent2.setData(Uri.parse("mailto: " + MARINA_REVISTA_EMAIL));
		    startActivity(intent2);
			break;
		
		// Open email app. 
		case R.id.btn_revista_open_into_a_browser:
		    Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(urlWebpage));
		    startActivity(intent3);
			break;
		}
		
		return true;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		//Transitions between activities.
	    overridePendingTransition(R.anim.activities_transition_in_back, R.anim.activities_transition_out_back);
	}
	
	// Asynctask for handler the view while the data are downloading.
	private class ProgressTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progressDialog;
		private ParseRSS rss;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			// Create ProgressDialog.
			progressDialog = new ProgressDialog(activity);
			progressDialog.setTitle(getString(R.string.progressdialog_xml_rss_downloading_title));
			progressDialog.setMessage(getString(R.string.progressdialog_xml_rss_downloading_message));
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					progressTask.cancel(true);
					
					if (list == null)
					    textView.setVisibility(View.VISIBLE);
					else 
						textView.setVisibility(View.INVISIBLE);
				}
			});
			progressDialog.show();
		}
		
		
		@Override
		protected Void doInBackground(Void... params) {
			// Get RSS in Arraylist<HashMap<String, String>> type.
			rss = new ParseRSS();
			list = rss.getArrayListHashMap(url);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Set adapter to listview.
			setAdapterView();
			// Dimiss the progress dialog.
			progressDialog.dismiss();
		}
	}
	
	//Custom listview adapter.
	private class RSSAdapter extends BaseAdapter {
		private LayoutInflater inflater = null;
		private ArrayList<HashMap<String, String>> list;
		private int positionBefore = 0;
		
		public RSSAdapter(ArrayList<HashMap<String, String>> list) {
			this.inflater = (LayoutInflater) getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			this.list = list;
		}
		
        class ViewHolder {
			TextView txv_title;
			TextView txv_author;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int index) {
			return list.get(index);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			
			// Animation on the items.
			TranslateAnimation tAnim = new TranslateAnimation(150, 0, 0, 0);
			tAnim.setDuration(300);
			tAnim.setFillAfter(true);
			
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.custom_listview_layout_items, null);
				
				holder = new ViewHolder();
				holder.txv_title = (TextView) convertView.findViewById(R.id.txv_rss_item_title);
				holder.txv_author = (TextView) convertView.findViewById(R.id.txv_rss_item_author);
				convertView.setTag(holder);
			
			} else
				
				holder = (ViewHolder) convertView.getTag();
				holder.txv_title.setText(list.get(position).get("title"));
				holder.txv_author.setText(list.get(position).get("author"));
				
				holder.txv_title.setTypeface(tf);
				holder.txv_author.setTypeface(tf);
				
				// For two colors.
				if (position % 2 == 0) {
					convertView.setBackgroundResource(R.drawable.items_listview_background_shape);
				} else {
					convertView.setBackgroundResource(R.drawable.items_listview_background_shape_second_color);
				}
				
				if (mIsScrollingUp == false && positionBefore < position) {
					// Start translate animation.
					convertView.startAnimation(tAnim);
				}
				positionBefore = position;
				
			return convertView;
		}
		
	}
}
