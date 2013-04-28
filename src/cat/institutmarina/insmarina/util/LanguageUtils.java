/*******************************************************************************
 * Created by Marc Pacheco Garcia on 28/4/13.            
 * http://marcpacheco.me                                 
 * Copyright (c) 2013, All rights reserved.              
 * App icon and Images are property of Institut Marina
 ******************************************************************************/
package cat.institutmarina.insmarina.util;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;

public class LanguageUtils {
	private static final String LANGUAGE_PREFS_NAME = "LanguageUtilsSharedPrefs";
	private Activity activity;
	private String[] languages;
	private String[][] languagesReadable;
	private String currentLanguage;
	private String defaultLanguage;
	private int langPos;

	public LanguageUtils(Activity activity, String[] languages,
			String[][] languagesReadable, String defaultLanguage) {
		this.activity = activity;
		this.languages = languages;
		this.languagesReadable = languagesReadable;
		this.defaultLanguage = defaultLanguage;
		getSharedPreferencesLanguage();
		setLanguage(currentLanguage);
	}
	
	private void restartActivity() {
		Intent intent = new Intent(activity, activity.getClass());
		activity.finish();
		activity.startActivity(intent);
	}
	
	private void setLanguage(String language) {
		Locale locale = new Locale(language);
		Locale.setDefault(locale);
		
		Configuration config = new Configuration();
		config.locale = locale;
		activity.getApplicationContext().getResources().updateConfiguration(config, null);
	}
	
	private void putLanguageIntoSharedPreferences(String language) {
		// Save language on SharedPreferences.
		SharedPreferences.Editor editor = activity.getSharedPreferences(LANGUAGE_PREFS_NAME, 0).edit();
		int pos = getPositionInArray(language);
		editor.putString("APP_LANGUAGE", language);
		editor.putInt("ITEM_POSITION", pos);
		editor.commit();
	}
	
	private int getPositionInArray(String language) {
		int pos = 0;
		for (int i = 0; i < languages.length; i++) {
			if (languages[i] == language) {
				pos = i;
				break;
			}
		}
		return pos;
	}
	
	private void getSharedPreferencesLanguage() {
		try {
		   SharedPreferences prefs = activity.getSharedPreferences(LANGUAGE_PREFS_NAME, 0);
		       currentLanguage = prefs.getString("APP_LANGUAGE", defaultLanguage);
		       langPos = prefs.getInt("ITEM_POSITION", getPositionInArray(defaultLanguage));
		       
		       if (currentLanguage == null) {
		    	   currentLanguage = defaultLanguage;
		       } 	   
		} catch(NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	public void setAndRefreshLanguage(String language) {
		putLanguageIntoSharedPreferences(language);
		restartActivity();
	}

	public static String getCurrentLanguage() {
		return Locale.getDefault().toString();
	}
	
	public void showAlertDialogChoiceLanguage(String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setSingleChoiceItems(languagesReadable[langPos], langPos, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				putLanguageIntoSharedPreferences(languages[which]);
				dialog.dismiss();
				
				// Restart activity.
				restartActivity();
			}
		});
		builder.show();
	}

}
