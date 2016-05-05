/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.wookie.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.wookie.dto.WidgetData;
import org.lamsfoundation.lams.tool.wookie.dto.WidgetDefinition;
import org.lamsfoundation.lams.tool.wookie.web.actions.AuthoringAction;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class WookieUtil {

    private static Logger logger = Logger.getLogger(AuthoringAction.class);

    public static List<WidgetDefinition> getWidgetDefinitions(String url)
	    throws DOMException, IOException, ParserConfigurationException, SAXException, Exception {
	List<WidgetDefinition> widgetDefinitions = new ArrayList<WidgetDefinition>();

	// add the relative url for the widget list
	url += WookieConstants.RELATIVE_URL_WIDGET_LIST;

	// add required parameters
	HashMap<String, String> params = new HashMap<String, String>();
	params.put(WookieConstants.PARAM_ALL, WookieConstants.PARAM_TRUE);

	// Reading the response from the wookie server
	String xml = WookieUtil.getResponseStringFromExternalServer(url, params);
	if (xml == null) {
	    logger.error("Got null xml from url: " + url);
	    return null;
	}

	// Parsing the xml document containing the widget definitions
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder();
	Document document = db.parse(new InputSource(new StringReader(xml)));
	NodeList widgetList = document.getElementsByTagName(WookieConstants.XML_WIDGET);
	for (int i = 0; i < widgetList.getLength(); i++) {
	    // Creating a new widget object
	    WidgetDefinition widgetDefinition = new WidgetDefinition();

	    // Get the attributes
	    NamedNodeMap widgetAttributes = (widgetList.item(i)).getAttributes();
	    if (widgetAttributes.getNamedItem(WookieConstants.XML_IDENTIFIER) != null) {
		String identifier = widgetAttributes.getNamedItem(WookieConstants.XML_IDENTIFIER).getNodeValue();
		widgetDefinition.setIdentifier(identifier);
	    }

	    // Get the properties for this widget
	    NodeList widgetPropertyList = (widgetList.item(i)).getChildNodes();
	    for (int j = 0; j < widgetPropertyList.getLength(); j++) {
		Node widgetProperty = widgetPropertyList.item(j);

		String propertyTitle = widgetProperty.getNodeName();

		// Add the properties
		if (propertyTitle.equals(WookieConstants.XML_TITLE)) {
		    widgetDefinition.setTitle(widgetProperty.getTextContent());
		} else if (propertyTitle.equals(WookieConstants.XML_DESCRIPTION)) {
		    widgetDefinition.setDescription(widgetProperty.getTextContent());
		} else if (propertyTitle.equals(WookieConstants.XML_ICON)) {
		    widgetDefinition.setIcon(widgetProperty.getTextContent());
		}
	    }
	    widgetDefinitions.add(widgetDefinition);
	}
	return widgetDefinitions;
    }

    public static int getWidgetCount(String url)
	    throws DOMException, IOException, ParserConfigurationException, SAXException, Exception {

	// add the relative url for the widget list
	url += WookieConstants.RELATIVE_URL_WIDGET_LIST;

	// add required parameters
	HashMap<String, String> params = new HashMap<String, String>();
	params.put(WookieConstants.PARAM_ALL, WookieConstants.PARAM_TRUE);

	// Make the request to the wookie server
	String xml = WookieUtil.getResponseStringFromExternalServer(url, params);
	if (xml == null) {
	    logger.error("Got null xml from url: " + url);
	    return 0;
	}

	// Parsing the xml document containing the widget definitions
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder();
	Document document = db.parse(new InputSource(new StringReader(xml)));
	NodeList widgetList = document.getElementsByTagName(WookieConstants.XML_WIDGET);

	if (widgetList != null) {
	    return widgetList.getLength();
	}
	return 0;
    }

    public static String getWidget(String url, String apiKey, String widgetIdentifier, UserDTO user,
	    String sharedDataKey, boolean isModerator) throws Exception {

	if (url == null || apiKey == null || widgetIdentifier == null || user == null || sharedDataKey == null) {
	    return null;
	}

	HashMap<String, String> params = new HashMap<String, String>();
	params.put(WookieConstants.PARAM_KEY_WIDGET_ID, URLEncoder.encode(widgetIdentifier, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_USER_ID, URLEncoder.encode(user.getUserID().toString(), "UTF8"));
	params.put(WookieConstants.PARAM_KEY_SHARED_DATA_KEY, URLEncoder.encode(sharedDataKey, "UTF8"));

	params.put(WookieConstants.PARAM_KEY_API_KEY, URLEncoder.encode(apiKey, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_REQUEST_ID,
		URLEncoder.encode(WookieConstants.PARAM_VALUE_GET_WIDGET, "UTF8"));

	String widgetXML = WookieUtil.getResponseStringFromExternalServer(url, params);
	String displayName = user.getFirstName() + " " + user.getLastName();

	// Add the participant
	boolean participantAdded = WookieUtil.addParticipant(url, apiKey, widgetIdentifier, user.getUserID().toString(),
		sharedDataKey, user.getUserID().toString(), displayName, null);
	if (!participantAdded) {
	    throw new WookieException("Attempt to add participant failed, check response code in logs");
	}

	// If required, set the moderator property
	if (isModerator) {
	    boolean propertyAdded = WookieUtil.addProperty(url, apiKey, widgetIdentifier, user.getUserID().toString(),
		    sharedDataKey, user.getUserID().toString(), displayName, null,
		    WookieConstants.PARAM_VALUE_PROPERTY_NAME_MODERATOR,
		    WookieConstants.PARAM_VALUE_PROPERTY_VALUE_TRUE);
	    if (!propertyAdded) {
		throw new WookieException("Attempt to add property failed, check response code in logs");
	    }

	}

	return widgetXML;

    }

    /**
     *
     * @param url
     * @param apiKey
     * @param widgetIdentifier
     * @param userId
     * @param sharedDataKey
     * @param participantId
     * @param participantDisplayName
     * @param participantThumbnailURL
     * @return
     * @throws Exception
     * @throws Exception
     */
    public static boolean addParticipant(String url, String apiKey, String widgetIdentifier, String userId,
	    String sharedDataKey, String participantId, String participantDisplayName, String participantThumbnailURL)
	    throws Exception {
	if (url == null || apiKey == null || widgetIdentifier == null || userId == null || sharedDataKey == null) {
	    throw new WookieException("Parameters missing in addParticipant call");
	}

	HashMap<String, String> params = new HashMap<String, String>();
	params.put(WookieConstants.PARAM_KEY_WIDGET_ID, URLEncoder.encode(widgetIdentifier, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_USER_ID, URLEncoder.encode(userId, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_SHARED_DATA_KEY, URLEncoder.encode(sharedDataKey, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_PARTICIPANT_ID, URLEncoder.encode(participantId, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_PARTICIPANT_DISPLAY_NAME,
		URLEncoder.encode(participantDisplayName, "UTF8"));
	if (participantThumbnailURL != null) {
	    params.put(WookieConstants.PARAM_KEY_PARTICIPANT_THUMBNAIL_URL,
		    URLEncoder.encode(participantThumbnailURL, "UTF8"));
	} else {
	    // LDEV-2742 add default avatar
	    String defaultAvatar = Configuration.get(ConfigurationKeys.SERVER_URL) + "images/lamb_big.png";
	    params.put(WookieConstants.PARAM_KEY_PARTICIPANT_THUMBNAIL_URL, URLEncoder.encode(defaultAvatar, "UTF8"));
	}
	params.put(WookieConstants.PARAM_KEY_API_KEY, URLEncoder.encode(apiKey, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_REQUEST_ID,
		URLEncoder.encode(WookieConstants.PARAM_VALUE_ADD_PARTICIPANT, "UTF8"));

	// Making the request and getting the response code
	int responseCode = WookieUtil.getResponseCodeExternalServer(url, params);

	// Checking the response code
	if (responseCode == HttpServletResponse.SC_OK || responseCode == HttpServletResponse.SC_CREATED) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Add a property to the existing initiated widget
     *
     * @param url
     * @param apiKey
     * @param widgetIdentifier
     * @param userId
     * @param sharedDataKey
     * @param participantId
     * @param participantDisplayName
     * @param participantThumbnailURL
     * @return
     * @throws Exception
     * @throws Exception
     */
    public static boolean addProperty(String url, String apiKey, String widgetIdentifier, String userId,
	    String sharedDataKey, String participantId, String participantDisplayName, String participantThumbnailURL,
	    String propertyName, String propertyValue) throws Exception {
	if (url == null || apiKey == null || widgetIdentifier == null || userId == null || sharedDataKey == null) {
	    throw new WookieException("Parameters missing in addProperty call");
	}

	HashMap<String, String> params = new HashMap<String, String>();
	params.put(WookieConstants.PARAM_KEY_WIDGET_ID, URLEncoder.encode(widgetIdentifier, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_USER_ID, URLEncoder.encode(userId, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_SHARED_DATA_KEY, URLEncoder.encode(sharedDataKey, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_PARTICIPANT_ID, URLEncoder.encode(participantId, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_PARTICIPANT_DISPLAY_NAME,
		URLEncoder.encode(participantDisplayName, "UTF8"));
	if (participantThumbnailURL != null) {
	    params.put(WookieConstants.PARAM_KEY_PARTICIPANT_THUMBNAIL_URL,
		    URLEncoder.encode(participantThumbnailURL, "UTF8"));
	}
	params.put(WookieConstants.PARAM_KEY_PROPERTY_NAME, URLEncoder.encode(propertyName, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_PROPERTY_VALUE, URLEncoder.encode(propertyValue, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_API_KEY, URLEncoder.encode(apiKey, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_REQUEST_ID,
		URLEncoder.encode(WookieConstants.PARAM_VALUE_SET_PERSONAL_PROPERTY, "UTF8"));

	// Making the request and getting the response code
	int responseCode = WookieUtil.getResponseCodeExternalServer(url, params);

	// Checking the response code
	if (responseCode == HttpServletResponse.SC_OK || responseCode == HttpServletResponse.SC_CREATED) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Make a clone of the widget identified by the shared data key
     *
     * @param url
     * @param apiKey
     * @param widgetIdentifier
     * @param userId
     * @param sharedDataKey
     * @param participantId
     * @param participantDisplayName
     * @param participantThumbnailURL
     * @return
     * @throws Exception
     * @throws Exception
     */
    public static boolean cloneWidget(String url, String apiKey, String widgetIdentifier, String sharedDataKey,
	    String newSharedDataKey, String userID) throws Exception {

	HashMap<String, String> params = new HashMap<String, String>();
	params.put(WookieConstants.PARAM_KEY_WIDGET_ID, URLEncoder.encode(widgetIdentifier, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_SHARED_DATA_KEY, URLEncoder.encode(sharedDataKey, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_API_KEY, URLEncoder.encode(apiKey, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_REQUEST_ID,
		URLEncoder.encode(WookieConstants.PARAM_VALUE_PROPERTY_VALUE_CLONE, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_SHARED_DATA_KEY, URLEncoder.encode(sharedDataKey, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_PROPERTY_CLONED_SHARED_KEY, URLEncoder.encode(newSharedDataKey, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_USER_ID, URLEncoder.encode(userID, "UTF8"));

	// Making the request and getting the response code
	int responseCode = WookieUtil.getResponseCodeExternalServer(url, params);

	// Checking the response code
	if (responseCode == HttpServletResponse.SC_OK) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Makes a request to the specified url with the specified parameters and
     * returns the response string
     *
     * @param urlStr
     * @param params
     * @return
     * @throws ToolException
     * @throws IOException
     */
    public static String getResponseStringFromExternalServer(String urlStr, HashMap<String, String> params)
	    throws Exception {
	InputStream is = WookieUtil.getResponseInputStreamFromExternalServer(urlStr, params);
	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	StringBuilder sb = new StringBuilder();
	String line = null;
	while ((line = reader.readLine()) != null) {
	    sb.append(line + "\n");
	}
	is.close();
	return sb.toString();
    }

    /**
     * Makes a request to the specified url. Returns true if the expected status
     * response code is returned
     *
     * @param urlStr
     * @param params
     * @return
     * @throws ToolException
     * @throws IOException
     */
    public static int getResponseCodeExternalServer(String urlStr, HashMap<String, String> params) throws Exception {
	URLConnection conn = WookieUtil.getResponseUrlConnectionFromExternalServer(urlStr, params);
	HttpURLConnection httpConn = (HttpURLConnection) conn;

	return httpConn.getResponseCode();
    }

    /**
     * Makes a request to the specified url with the specified parameters and
     * returns the response input stream
     *
     * @param urlStr
     * @param params
     * @return
     * @throws ToolException
     * @throws IOException
     */
    public static InputStream getResponseInputStreamFromExternalServer(String urlStr, HashMap<String, String> params)
	    throws Exception {
	URLConnection conn = WookieUtil.getResponseUrlConnectionFromExternalServer(urlStr, params);
	InputStream is = conn.getInputStream();
	return is;
    }

    /**
     * Makes a request to the specified url with the specified parameters and
     * returns the response URLConnection
     *
     * @param urlStr
     * @param params
     * @return
     * @throws ToolException
     * @throws IOException
     */
    public static URLConnection getResponseUrlConnectionFromExternalServer(String urlStr,
	    HashMap<String, String> params) throws Exception {
	if (!urlStr.contains("?")) {
	    urlStr += "?";
	}

	for (Entry<String, String> entry : params.entrySet()) {
	    urlStr += "&" + entry.getKey() + "=" + entry.getValue();
	}

	URL url = new URL(urlStr);
	URLConnection conn = url.openConnection();
	if (!(conn instanceof HttpURLConnection)) {
	    logger.error("Fail to connect to external server though url:  " + urlStr);
	    throw new Exception("Fail to connect to external server though url:  " + urlStr);
	}

	HttpURLConnection httpConn = (HttpURLConnection) conn;
	logger.info("Response code was " + new Integer(httpConn.getResponseCode()).toString() + " for URL: " + urlStr);

	return conn;
    }

    /**
     * Gets the url response from the getWidget xml
     *
     * @param xml
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static String getWidgetUrlFromXML(String xml)
	    throws ParserConfigurationException, SAXException, IOException {
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder();
	Document document = db.parse(new InputSource(new StringReader(xml)));
	NodeList widgetList = document.getElementsByTagName(WookieConstants.XML_URL);

	if (widgetList != null && widgetList.item(0) != null) {
	    String url = widgetList.item(0).getTextContent();
	    return url;
	} else {
	    return null;
	}
    }

    /**
     * Gets the widget data response from the getWidget xml
     *
     * @param xml
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static WidgetData getWidgetDataFromXML(String xml)
	    throws ParserConfigurationException, SAXException, IOException {
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder();
	Document document = db.parse(new InputSource(new StringReader(xml)));
	NodeList widgetList = document.getElementsByTagName(WookieConstants.XML_WIDGET_DATA);

	NodeList widgetPropertyList = (widgetList.item(0)).getChildNodes();
	WidgetData widgetData = new WidgetData();

	for (int i = 0; i < widgetPropertyList.getLength(); i++) {
	    Node widgetProperty = widgetPropertyList.item(i);

	    String propertyTitle = widgetProperty.getNodeName();

	    // Add the properties
	    if (propertyTitle.equals(WookieConstants.XML_URL)) {
		widgetData.setUrl(widgetProperty.getTextContent());
	    } else if (propertyTitle.equals(WookieConstants.XML_IDENTIFIER)) {
		widgetData.setIdentifier(widgetProperty.getTextContent());
	    } else if (propertyTitle.equals(WookieConstants.XML_TITLE)) {
		widgetData.setTitle(widgetProperty.getTextContent());
	    } else if (propertyTitle.equals(WookieConstants.XML_HEIGHT)) {
		String heightStr = widgetProperty.getTextContent();
		if (heightStr != null) {
		    widgetData.setHeight(Integer.parseInt(heightStr));
		}
	    } else if (propertyTitle.equals(WookieConstants.XML_WIDTH)) {
		String widthStr = widgetProperty.getTextContent();
		if (widthStr != null) {
		    widgetData.setWidth(Integer.parseInt(widthStr));
		}
	    } else if (propertyTitle.equals(WookieConstants.XML_MAXIMISE)) {
		String maximiseStr = widgetProperty.getTextContent();
		if (maximiseStr != null) {
		    widgetData.setMaximize(Boolean.parseBoolean(maximiseStr));
		}
	    }
	}

	return widgetData;
    }

}
