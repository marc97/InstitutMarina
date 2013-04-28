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
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.*;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class ContactActivity extends FragmentActivity {
	static final LatLng LA_LLAGOSTA = new LatLng(41.51386, 2.19309);
	static final LatLng INSTITUT_MARINA = new LatLng(41.51109, 2.19784);
	private GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_activity);
		setTitle(getString(R.string.contact_activity_title));
		
		// For HOYNECOMB or superior builds.
		if (APILevelsUtils.hasHoneycomb()) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		/*ScrollView scr_contact = (ScrollView) findViewById(R.id.scr_contact);
		
		View googleMapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
		googleMapView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 int action = event.getAction();
				    switch (action) {
				    case MotionEvent.ACTION_DOWN:
				        System.out.println("touched");
				    	// Disallow ScrollView to intercept touch events.
				        //scr_contact.requestDisallowInterceptTouchEvent(true);
				        System.out.println("t");
				        break;

				    case MotionEvent.ACTION_UP:
				        
				    	// Allow ScrollView to intercept touch events.
				    	//scr_contact.requestDisallowInterceptTouchEvent(true);
				        break;
				    }
				return true;
			}
		});*/
		
		// For contact TextView info.
		TextView txv_contact_info = (TextView) findViewById(R.id.txv_contact_info);
		final String contactInfo = "<b>" + getString(R.string.address) + "</b> C/ Estació s/n  08120 La Llagosta (Barcelona)" +
				                   "<br></br> <b>" + getString(R.string.telephone) + "</b> 93 560 73 31 <br></br> <b>Fax: </b>" +
				                   "93 574 25 46 <br></br> <b>" + getString(R.string.email) + 
				                   "</b> <a href=\"mailto:iesmarina@xtec.cat\">iesmarina@xtec.cat</a>";
		txv_contact_info.setText(Html.fromHtml(contactInfo));
		txv_contact_info.setMovementMethod(LinkMovementMethod.getInstance());
	
		 map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
			        .getMap();
			   // Marker llagosta = map.addMarker(new MarkerOptions().position(LA_LLAGOSTA).title("La Llagosta"));
			   
			    Marker marina = map.addMarker(new MarkerOptions()
			        .position(INSTITUT_MARINA)
			        .title("Ins Marina")
			        .snippet("Institut d'Educació Secundaria, Batxillerat i Mòduls")
			        .icon(BitmapDescriptorFactory
			            .fromResource(R.drawable.ic_launcher)));
			    marina.showInfoWindow();

			    // Move the camera instantly to INS MARINA with a zoom of 15.
			    map.moveCamera(CameraUpdateFactory.newLatLngZoom(INSTITUT_MARINA, 15));

			    // Zoom in, animating the camera.
			    map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
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
