/*******************************************************************************
 * Created by Marc Pacheco Garcia on 28/4/13.            
 * http://marcpacheco.me                                 
 * Copyright (c) 2013, All rights reserved.              
 * App icon and Images are property of Institut Marina
 ******************************************************************************/
package cat.institutmarina.insmarina;

import cat.institutmarina.insmarina.R;

public class StaticFields {

	// For sharedpreferences.
	public static final String PREFS_NAME = "InsMarinaSharedPrefs";

	// Url fields.
	public static final String URL_MARINA = "http://institutmarina.cat";
	public static final String URL_MARINA_MOODLE = "http://agora.educat1x1.cat/iesmarina/moodle/";
	public static final String URL_MARINA_REVISTA = "http://revistadelmarina.blogspot.com/";
	public static final String URL_RSS_MARINA = "http://institutmarina.cat/?option=com_content&view=category&id=19&format=feed&type=rss";
	public static final String URL_RSS_MARINA_REVISTA = "http://revistadelmarina.blogspot.com/feeds/posts/default?alt=rss";
	public static final String FB_MARINA_ID = "100003366520360";
	public static final String FB_MARINA_NAME = "iesmarina";

	// For language.
	public static final String LOCALE_CA = "ca_ES";
	public static final String LOCALE_EN = "en_US";
	public static final String[] LOCALES = { LOCALE_CA, LOCALE_EN };
	public static final String[][] LOCALES_READABLE = { { "Català", "Anglès" },
			{ "Catalan", "English" } };

	// For main tablelayout icons.
	protected static int[] images = { R.drawable.marina_logo_218_194,
			                          R.drawable.moodle_icon_180x, 
			                          R.drawable.ic_phone_180x,
			                          R.drawable.facebook_icon_180x,
			                          android.R.color.transparent};
}
