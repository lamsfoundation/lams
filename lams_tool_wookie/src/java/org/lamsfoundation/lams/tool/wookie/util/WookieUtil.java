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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.wookie.dto.WidgetDefinition;
import org.lamsfoundation.lams.tool.wookie.web.actions.AuthoringAction;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class WookieUtil {

    private static Logger logger = Logger.getLogger(AuthoringAction.class);

    public static List<WidgetDefinition> getWidgetDefinitions(String url) throws DOMException, IOException,
	    ParserConfigurationException, SAXException, Exception {
	List<WidgetDefinition> widgetDefinitions = new ArrayList<WidgetDefinition>();

	// add the relative url for the widget list
	url += WookieConstants.RELATIVE_URL_WIDGET_LIST;

	// add required parameters
	HashMap<String, String> params = new HashMap<String, String>();
	params.put(WookieConstants.PARAM_ALL, WookieConstants.PARAM_TRUE);

	// Make the request to the wookie server
	InputStream is = WebUtil.getResponseInputStreamFromExternalServer(url, params);

	// Reading the response from the wookie server
	String xml = convertStreamToString(is);
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

    public static int getWidgetCount(String url) throws DOMException, IOException, ParserConfigurationException,
	    SAXException, Exception {

	// add the relative url for the widget list
	url += WookieConstants.RELATIVE_URL_WIDGET_LIST;

	// add required parameters
	HashMap<String, String> params = new HashMap<String, String>();
	params.put(WookieConstants.PARAM_ALL, WookieConstants.PARAM_TRUE);

	// Make the request to the wookie server
	InputStream is = WebUtil.getResponseInputStreamFromExternalServer(url, params);

	// Reading the response from the wookie server
	String xml = convertStreamToString(is);
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

    public static String getWidget(String url, String apiKey, String widgetIdentifier, UserDTO user, String sharedDataKey, boolean isModerator)
	    throws Exception {

	if (url == null || apiKey == null || widgetIdentifier == null || user == null || sharedDataKey == null) {
	    return null;
	}

	HashMap<String, String> params = new HashMap<String, String>();
	params.put(WookieConstants.PARAM_KEY_WIDGIT_ID, URLEncoder.encode(widgetIdentifier, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_USER_ID, URLEncoder.encode(user.getUserID().toString(), "UTF8"));
	params.put(WookieConstants.PARAM_KEY_SHARED_DATA_KEY, URLEncoder.encode(sharedDataKey, "UTF8"));

	InputStream is = requestWookieServlet(url, apiKey, WookieConstants.PARAM_VALUE_GET_WIDGET, params);
	String widgetXML = convertStreamToString(is);
	String displayName = user.getFirstName() + " " + user.getLastName();
	
	
	// Add the participant
	String participantAddedXML = addParticipant(url, apiKey, widgetIdentifier, user.getUserID().toString(), sharedDataKey, user.getUserID().toString(), displayName, null);
	if (!checkCompleted(participantAddedXML)) {
	    throw new WookieException("Attempt to add participant failed: " + participantAddedXML);
	}
	

	// If required, set the moderator property
	if (isModerator) {
	    String propertyAddedXML = addProperty(url, apiKey, widgetIdentifier, user.getUserID().toString(), sharedDataKey, user.getUserID().toString(), displayName, null, WookieConstants.PARAM_VALUE_PROPERTY_NAME_MODERATOR, WookieConstants.PARAM_VALUE_PROPERTY_VALUE_TRUE);
	    if (!checkCompleted(propertyAddedXML)) {
		throw new WookieException("Attempt to add property failed: " + propertyAddedXML);
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
    public static String addParticipant(String url, String apiKey, String widgetIdentifier, String userId,
	    String sharedDataKey, String participantId, String participantDisplayName, String participantThumbnailURL) throws Exception
	     {
	if (url == null || apiKey == null || widgetIdentifier == null || userId == null || sharedDataKey == null) {
	    throw new WookieException("Parameters missing in addParticipant call");
	}

	HashMap<String, String> params = new HashMap<String, String>();
	params.put(WookieConstants.PARAM_KEY_WIDGIT_ID, URLEncoder.encode(widgetIdentifier, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_USER_ID, URLEncoder.encode(userId, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_SHARED_DATA_KEY, URLEncoder.encode(sharedDataKey, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_PARTICIPANT_ID, URLEncoder.encode(participantId, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_PARTICIPANT_DISPLAY_NAME, URLEncoder
		.encode(participantDisplayName, "UTF8"));
	if (participantThumbnailURL != null) {
	    params.put(WookieConstants.PARAM_KEY_PARTICIPANT_THUMBNAIL_URL, URLEncoder.encode(participantThumbnailURL,
		"UTF8"));
	}
	

	InputStream is = requestWookieServlet(url, apiKey, WookieConstants.PARAM_VALUE_ADD_PARTICIPANT, params);
	String xml = convertStreamToString(is);
	return xml;
    }

    /**
     * 
     * 
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
    public static String addProperty(String url, String apiKey, String widgetIdentifier, String userId,
	    String sharedDataKey, String participantId, String participantDisplayName, String participantThumbnailURL,
	    String propertyName, String propertyValue) throws Exception {
	if (url == null || apiKey == null || widgetIdentifier == null || userId == null || sharedDataKey == null) {
	    throw new WookieException("Parameters missing in addProperty call");
	}

	HashMap<String, String> params = new HashMap<String, String>();
	params.put(WookieConstants.PARAM_KEY_WIDGIT_ID, URLEncoder.encode(widgetIdentifier, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_USER_ID, URLEncoder.encode(userId, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_SHARED_DATA_KEY, URLEncoder.encode(sharedDataKey, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_PARTICIPANT_ID, URLEncoder.encode(participantId, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_PARTICIPANT_DISPLAY_NAME, URLEncoder
		.encode(participantDisplayName, "UTF8"));
	if (participantThumbnailURL != null) {
	    params.put(WookieConstants.PARAM_KEY_PARTICIPANT_THUMBNAIL_URL, URLEncoder.encode(participantThumbnailURL,
		"UTF8"));
	}
	params.put(WookieConstants.PARAM_KEY_PROPERTY_NAME, URLEncoder.encode(propertyName, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_PROPERTY_VALUE, URLEncoder.encode(propertyValue, "UTF8"));

	InputStream is = requestWookieServlet(url, apiKey, WookieConstants.PARAM_VALUE_SET_PERSONAL_PROPERTY, params);
	String xml = convertStreamToString(is);
	return xml;
    }

    /**
     * All requests to the wooke servlet go through this method
     * 
     * @param url
     * @param apiKey
     * @param requestId
     * @param params
     * @return
     * @throws Exception
     */
    public static InputStream requestWookieServlet(String url, String apiKey, String requestId,
	    HashMap<String, String> params) throws Exception {
	// Adding required params to hashmap
	params.put(WookieConstants.PARAM_KEY_API_KEY, URLEncoder.encode(apiKey, "UTF8"));
	params.put(WookieConstants.PARAM_KEY_REQUEST_ID, URLEncoder.encode(requestId, "UTF8"));

	return WebUtil.getResponseInputStreamFromExternalServer(url, params);
    }

    /**
     * Checks the resulting xml for "<message>completed</message>"
     * 
     * @param xml
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws Exception
     */
    private static boolean checkCompleted(String xml) throws ParserConfigurationException, SAXException, IOException {

	if (xml != null) {

	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document document = db.parse(new InputSource(new StringReader(xml)));
	    NodeList result = document.getElementsByTagName(WookieConstants.XML_MESSAGE);

	    if (result != null) {
		Node resultNode = result.item(0);
		if (resultNode != null && resultNode.getTextContent().equals(WookieConstants.XML_COMPLETED)) {
		    return true;
		}
	    }
	    logger.error("Recieved bad result when calling wookie server: " + xml);
	}

	return false;
    }

    /**
     * Get a string response from an input stream
     * 
     * @param is
     * @return
     * @throws IOException
     */
    private static String convertStreamToString(InputStream is) throws IOException {
	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	StringBuilder sb = new StringBuilder();
	String line = null;
	while ((line = reader.readLine()) != null) {
	    sb.append(line + "\n");
	}
	is.close();
	return sb.toString();
    }

}
