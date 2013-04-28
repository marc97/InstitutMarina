/*******************************************************************************
 * Created by Marc Pacheco Garcia on 28/4/13.            
 * http://marcpacheco.me                                 
 * Copyright (c) 2013, All rights reserved.              
 * App icon and Images are property of Institut Marina
 ******************************************************************************/
package cat.institutmarina.insmarina;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParserException;

public class ParseRSS {

	public ParseRSS() {
	}
	

	public ArrayList<HashMap<String, String>> getArrayListHashMap(String url) {
		ArrayList<HashMap<String, String>> list = null;
		try {
			XMLParser xmlParser = new XMLParser();
			String xml = xmlParser.getXMLFileFromURL(url);
			list = xmlParser.parseRSS(xml);
			return list;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
