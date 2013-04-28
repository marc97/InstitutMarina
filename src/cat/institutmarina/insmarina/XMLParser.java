/*******************************************************************************
 * Created by Marc Pacheco Garcia on 28/4/13.            
 * http://marcpacheco.me                                 
 * Copyright (c) 2013, All rights reserved.              
 * App icon and Images are property of Institut Marina
 ******************************************************************************/
package cat.institutmarina.insmarina;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.StrictMode;
import android.util.Xml;
/*
 *   Created by Marc Pacheco Garcia on 28/4/13.            *
 *   http://marcpacheco.me                                 *
 *   Copyright (c) 2013, All rights reserved.              *
 *   App icon and Images are property of Institut Marina   *
                                                           */
public class XMLParser {

	public XMLParser() {
	}

	public String getXMLFileFromURL(String url) {
		String responseBody = null;

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		try {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = httpClient.execute(request, responseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		httpClient.getConnectionManager().shutdown();
		return responseBody;
	}

	public ArrayList<HashMap<String, String>> parseRSS(String xml)
			throws XmlPullParserException, IOException {

		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> hash = null;
		hash = new HashMap<String, String>();

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(xml));

		boolean isInItemTag = false;

		while (parser.next() != XmlPullParser.END_DOCUMENT) {
			String tag = null;
			int eventType = parser.getEventType();

			switch (eventType) {

			case XmlPullParser.START_DOCUMENT:
				break;

			case XmlPullParser.START_TAG:
				tag = parser.getName();

				if (tag.equals("item")) {
					isInItemTag = true;
					hash = new HashMap<String, String>();

				} else if (list != null) {

					if (tag.equals("title")) {
						hash.put("title", parser.nextText().toString());
					} else if (tag.equals("link")) {
						hash.put("link", parser.nextText());
					} else if (tag.equals("description")) {
						hash.put("description", parser.nextText());
					} else if (tag.equals("author")) {
						hash.put("author", parser.nextText());
					} else if (tag.equals("category")) {
						hash.put("category", parser.nextText());
					} else if (tag.equals("pubDate")) {
						hash.put("pubDate", parser.nextText());
					}
				}
				break;

			case XmlPullParser.END_TAG:
				tag = parser.getName();

				if (tag.equals("item") && list != null) {
					list.add(hash);
				}
				break;

			case XmlPullParser.END_DOCUMENT:
				isInItemTag = false;
				break;
			}
		}

		return list;
	}
}
