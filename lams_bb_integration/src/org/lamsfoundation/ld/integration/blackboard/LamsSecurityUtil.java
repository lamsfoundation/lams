/****************************************************************
 * Copyright (C) 2007 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.ld.integration.blackboard;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.ld.integration.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import blackboard.platform.context.Context;

/**
 * This class creates URLs, servlet calls and webservice calls for communication with LAMS
 * 
 * @author <a href="mailto:lfoxton@melcoe.mq.edu.au">Luke Foxton</a>
 */
public class LamsSecurityUtil {

    private static Logger logger = Logger.getLogger(LamsSecurityUtil.class);

    /**
     * Generates login requests to LAMS for author, monitor and learner
     * 
     * @param ctx
     *            the blackboard contect, contains session data
     * @param method
     *            the mehtod to request of LAMS "author", "monitor", "learner"
     * @return a url pointing to the LAMS lesson, monitor, author session
     * @throws Exception
     */
    public static String generateRequestURL(Context ctx, String method) throws Exception {
	
	String serverAddr = getServerAddress();
	String serverId = getServerID();
	String reqSrc = getReqSrc();

	// If lams.properties could not be read, throw exception
	if (serverAddr == null || serverId == null || reqSrc == null) {
	    throw new Exception("Configuration Exception " + serverAddr + ", " + serverId);
	}

	String timestamp = new Long(System.currentTimeMillis()).toString();
	String username = ctx.getUser().getUserName();
	String firstName = ctx.getUser().getGivenName();
	String lastName  = ctx.getUser().getFamilyName();
	String email = ctx.getUser().getEmailAddress();
	String hash = generateAuthenticationHash(timestamp, username, method, serverId);
	String courseId = ctx.getCourse().getCourseId();

	String locale = ctx.getUser().getLocale();
	String country = getCountry(locale);
	String lang = getLanguage(locale);

	String url;
	try {
	    url = serverAddr + "/LoginRequest?" + "&uid=" + URLEncoder.encode(username, "UTF8") + "&method=" + method
		    + "&ts=" + timestamp + "&sid=" + serverId + "&hash=" + hash + "&courseid="
		    + URLEncoder.encode(courseId, "UTF8") + "&country=" + country + "&lang=" + lang + "&requestSrc="
		    + URLEncoder.encode(reqSrc, "UTF8") + "&firstName=" + URLEncoder.encode(firstName, "UTF-8")
		    + "&lastName=" + URLEncoder.encode(lastName, "UTF-8")
		    + "&email=" + email;
		    
	} catch (UnsupportedEncodingException e) {
	    throw new RuntimeException();
	}

	logger.info("LAMS Req: " + url);
	// System.out.println(url);

	return url;
    }
    
    /**
     * Generates default
     */
    public static String generateAuthenticateParameters(Context ctx) throws Exception {
	String serverAddr = getServerAddress();
	String serverId = getServerID();
	String reqSrc = getReqSrc();

	// If lams.properties could not be read, throw exception
	if (serverAddr == null || serverId == null || reqSrc == null) {
	    throw new Exception("Configuration Exception " + serverAddr + ", " + serverId);
	}

	String timestamp = new Long(System.currentTimeMillis()).toString();
	String username = ctx.getUser().getUserName();
	String hash = generateAuthenticationHash(timestamp, username, serverId);

	String authenticateParameters = "&serverId=" + serverId + "&datetime=" + timestamp + "&hashValue=" + hash
		+ "&username=" + URLEncoder.encode(username, "UTF8");

	return authenticateParameters;
    }
    
    /**
     * Generates url request to LAMS for LearningDesignImage.
     * 
     * @param ctx
     *            the blackboard contect, contains session data
     * @return a url pointing to the LAMS lesson, monitor, author session
     * @throws Exception
     */
    public static String generateRequestLearningDesignImage(Context ctx, boolean isSvgImage) throws Exception {
	String serverAddr = getServerAddress();
	int svgFormat = (isSvgImage) ? 1 : 2;
	
        //$request = "$CFG->lamslesson_serverurl/services/LearningDesignSVG?serverId=" . $CFG->lamslesson_serverid . "&datetime=" . $datetime_encoded . "&hashValue=" . 
        //$hashvalue . "&username=" . $username  . "&courseId=" . $courseid . "&courseName=" . urlencode($coursename) . "&mode=2&country=" . $country . "&lang=" . $lang . 
        //"&ldId=" . $ldid . "&svgFormat=" . $format;
	String url = serverAddr + "/services/LearningDesignSVG?" + generateAuthenticateParameters(ctx) + "&svgFormat=" + svgFormat;

	logger.info("LAMS Req: " + url);

	return url;
    }

    /**
     * Gets a list of learning designs for the current user from LAMS
     * 
     * @param ctx
     *            the blackboard contect, contains session data
     * @param mode
     *            the mode to call upon learning designes
     * @return a string containing the LAMS workspace tree in tigra format
     */
    public static String getLearningDesigns(Context ctx, Integer mode) {
	String serverAddr = getServerAddress();
	String serverId = getServerID();
	String serverKey = getServerKey();

	// If lams.properties could not be read, throw exception
	if (serverAddr == null || serverId == null || serverKey == null) {
	    throw new RuntimeException("lams.properties file could not be read. serverAddr:" + serverAddr + ", serverId:" + serverId);
	}

	String timestamp = new Long(System.currentTimeMillis()).toString();
	String username = ctx.getUser().getUserName();
	String firstName = ctx.getUser().getGivenName();
	String lastName  = ctx.getUser().getFamilyName();
	String email = ctx.getUser().getEmailAddress();
	String hash = generateAuthenticationHash(timestamp, username, serverId);
	String courseId = ctx.getCourse().getCourseId();

	String locale = ctx.getUser().getLocale();
	String country = getCountry(locale);
	String lang = getLanguage(locale);

	// TODO: Make locale settings work
	String learningDesigns = "[]"; // empty javascript array
	try {
	    String serviceURL = serverAddr + "/services/xml/LearningDesignRepository?" + "datetime=" + timestamp
		    + "&username=" + URLEncoder.encode(username, "utf8") + "&serverId="
		    + URLEncoder.encode(serverId, "utf8") + "&hashValue=" + hash + "&courseId="
		    + URLEncoder.encode(courseId, "UTF8") + "&country=" + country + "&lang=" + lang + "&mode=" + mode
		    + "&firstName=" + URLEncoder.encode(firstName, "UTF-8")
		    + "&lastName=" + URLEncoder.encode(lastName, "UTF-8")
		    + "&email=" + email;

	    URL url = new URL(serviceURL);
	    URLConnection conn = url.openConnection();
	    if (!(conn instanceof HttpURLConnection)) {
		logger.error("Unable to open connection to: " + serviceURL);
	    }

	    HttpURLConnection httpConn = (HttpURLConnection) conn;

	    if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
		logger.error("HTTP Response Code: " + httpConn.getResponseCode() + ", HTTP Response Message: "
			+ httpConn.getResponseMessage());
		return "error";
	    }

	    // InputStream is = url.openConnection().getInputStream();
	    InputStream is = conn.getInputStream();

	    // parse xml response
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document document = db.parse(is);

	    learningDesigns = "[" + convertToTigraFormat(document.getDocumentElement()) + "]";

	    // replace sequence id with javascript method
	    //String pattern = "'(\\d+)'";
	    //String replacement = "'javascript:selectSequence($1)'";
	    //learningDesigns = learningDesigns.replaceAll(pattern, replacement);

	} catch (MalformedURLException e) {
	    throw new RuntimeException("Unable to get LAMS learning designs, bad URL: '" + serverAddr
		    + "', please check lams.properties", e);
	} catch (IllegalStateException e) {
	    throw new RuntimeException(
		    "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator",
		    e);
	} catch (ConnectException e) {
	    throw new RuntimeException(
		    "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator",
		    e);
	} catch (UnsupportedEncodingException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	} catch (ParserConfigurationException e) {
	    throw new RuntimeException(e);
	} catch (SAXException e) {
	    throw new RuntimeException(e);
	}
	
	return learningDesigns;
    }

    /**
     * Starts lessons in lams through a LAMS webservice
     * 
     * @param ctx
     *            the blackboard contect, contains session data
     * @param ldId
     *            the learning design id for which you wish to start a lesson
     * @param title
     *            the title of the lesson
     * @param desc
     *            the description of the lesson 
     *            
     * @return the learning session id
     */
    public static Long startLesson(Context ctx, long ldId, String title, String desc, boolean isPreview) {
	Long error = new Long(-1);
	String serverId = getServerID();
	String serverAddr = getServerAddress();
	String serverKey = getServerKey();
	String courseId = ctx.getCourse().getCourseId();
	String username = ctx.getUser().getUserName();
	String locale = ctx.getUser().getLocale();
	String country = getCountry(locale);
	String lang = getLanguage(locale);
	String method = (isPreview) ? "preview" : "start";

	if (serverId == null || serverAddr == null || serverKey == null) {
	    throw new RuntimeException("Unable to start lesson, one or more lams configuration properties is null");
	}

	try {

	    String timestamp = new Long(System.currentTimeMillis()).toString();
	    String hash = generateAuthenticationHash(timestamp, username, serverId);

	    // (serverId, datetime, hashValue, username, ldId, courseId, title, desc, country, lang)

	    String serviceURL = serverAddr + "/services/xml/LessonManager?" + "&serverId="
		    + URLEncoder.encode(serverId, "utf8") + "&datetime=" + timestamp + "&username="
		    + URLEncoder.encode(username, "utf8") + "&hashValue=" + hash + "&courseId="
		    + URLEncoder.encode(courseId, "utf8") + "&ldId=" + new Long(ldId).toString() + "&country="
		    + country + "&lang=" + lang + "&method=" + method + "&title="
		    + URLEncoder.encode(title, "utf8").trim() + "&desc=" + URLEncoder.encode(desc, "utf8").trim();

	    logger.info("LAMS START LESSON Req: " + serviceURL);
	    // System.out.println("START LESSON: " + serviceURL);

	    URL url = new URL(serviceURL);
	    URLConnection conn = url.openConnection();
	    if (!(conn instanceof HttpURLConnection)) {
		throw new RuntimeException("Unable to open connection to: " + serviceURL);
	    }

	    HttpURLConnection httpConn = (HttpURLConnection) conn;

	    if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
		throw new RuntimeException("HTTP Response Code: " + httpConn.getResponseCode()
			+ ", HTTP Response Message: " + httpConn.getResponseMessage());
	    }

	    // InputStream is = url.openConnection().getInputStream();
	    InputStream is = conn.getInputStream();

	    // parse xml response
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document document = db.parse(is);

	    // get the lesson id from the response

	    /*
	     * The getTextContext is not a java 1.4 method, so Blackboard 7.1 comes up with errors using getNodeValue()
	     * instead
	     */
	    // return Long.parseLong(document.getElementsByTagName("Lesson").item(0).getAttributes().getNamedItem("lessonId").getTextContent());
	    return Long.parseLong(document.getElementsByTagName("Lesson").item(0).getAttributes()
		    .getNamedItem("lessonId").getNodeValue());
	} catch (MalformedURLException e) {
	    throw new RuntimeException("Unable to start LAMS lesson, bad URL: '" + serverAddr
		    + "', please check lams.properties", e);
	} catch (IllegalStateException e) {
	    throw new RuntimeException(
		    "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator",
		    e);
	} catch (RemoteException e) {
	    throw new RuntimeException("Unable to start LAMS lesson, RMI Remote Exception", e);
	} catch (UnsupportedEncodingException e) {
	    throw new RuntimeException("Unable to start LAMS lesson, Unsupported Encoding Exception", e);
	} catch (ConnectException e) {
	    throw new RuntimeException(
		    "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator",
		    e);
	} catch (Exception e) {
	    throw new RuntimeException("Unable to start LAMS lesson. Please contact your system administrator.", e);
	}

    }

    /**
     * @return gets server address from the lams.properties file
     */
    public static String getServerAddress() {
	return LamsPluginUtil.getProperties().getProperty(LamsPluginUtil.PROP_LAMS_URL);
    }

    /**
     * @return gets server id from the lams.properties file
     */
    public static String getServerID() {
	return LamsPluginUtil.getProperties().getProperty(LamsPluginUtil.PROP_LAMS_SERVER_ID);
    }

    /**
     * @return gets server key from the lams.properties file
     */
    public static String getServerKey() {
	return LamsPluginUtil.getProperties().getProperty(LamsPluginUtil.PROP_LAMS_SECRET_KEY);
    }

    /**
     * @return gets request source from the lams.properties file
     */
    public static String getReqSrc() {
	return LamsPluginUtil.getProperties().getProperty(LamsPluginUtil.PROP_REQ_SRC);
    }

    /**
     * 
     * @param node
     *            the node from which to do the recursive conversion
     * @return the string converted to tigra format
     */
    public static String convertToTigraFormat(Node node) {

	StringBuilder sb = new StringBuilder();

	if (node.getNodeName().equals(Constants.ELEM_FOLDER)) {

	    StringBuilder attribute = new StringBuilder(node.getAttributes().getNamedItem(Constants.ATTR_NAME)
		    .getNodeValue().replace("'", "\\'"));

	    sb.append("{type:'Text', label:'" + attribute + "',id:0");

	    NodeList children = node.getChildNodes();
	    if (children.getLength() == 0) {
		sb.append(",expanded:0,children:[{type:'HTML',html:'<i>-empty-</i>', id:0}]}");
		return sb.toString();
	    } else {
		sb.append(",children:[");
		
		
		sb.append(convertToTigraFormat(children.item(0)));
		for (int i = 1; i < children.getLength(); i++) {
		    sb.append(',').append(convertToTigraFormat(children.item(i)));
		}
		
		sb.append("]}");
	    }
	    
	} else if (node.getNodeName().equals(Constants.ELEM_LEARNING_DESIGN)) {
	    
	    
//		  $ld_name = preg_replace("/'/", "$1\'", $xml_node['@']['name']);
//		  $output .= "{type:'Text',label:'" . $ld_name . "',id:'" . $xml_node['@']['resourceId'] . "'}";

	    StringBuilder attrName = new StringBuilder(node.getAttributes().getNamedItem(Constants.ATTR_NAME)
		    .getNodeValue().replace("'", "\\'"));
	    StringBuilder attrResId = new StringBuilder(node.getAttributes().getNamedItem(Constants.ATTR_RESOURCE_ID)
		    .getNodeValue().replace("'", "\\'"));

	    sb.append("{type:'Text',label:'");
	    sb.append(attrName);
	    sb.append("',id:'");
	    sb.append(attrResId);
	    sb.append("'}");
	}
	return sb.toString();
    }

    // generate authentication hash code to validate parameters
    public static String generateAuthenticationHash(String datetime, String login, String method, String serverId) {
	String secretkey = LamsPluginUtil.getSecretKey();

	String plaintext = datetime.toLowerCase().trim() + login.toLowerCase().trim() + method.toLowerCase().trim()
		+ serverId.toLowerCase().trim() + secretkey.toLowerCase().trim();

	String hash = sha1(plaintext);
	return hash;
    }

    // generate authentication hash code to validate parameters

    public static String generateAuthenticationHash(String datetime, String login, String serverId) {
	String secretkey = getServerKey();

	String plaintext = datetime.toLowerCase().trim() + login.toLowerCase().trim() + serverId.toLowerCase().trim()
		+ secretkey.toLowerCase().trim();

	String hash = sha1(plaintext);

	return hash;
    }

    // generate authentication hash code to validate parameters
    public static String generateAuthenticationHash(String datetime, String serverId) throws NoSuchAlgorithmException {
	String secretkey = LamsPluginUtil.getSecretKey();

	String plaintext = datetime.toLowerCase().trim() + serverId.toLowerCase().trim()
		+ secretkey.toLowerCase().trim();

	return sha1(plaintext);
    }

    /**
     * The parameters are: uid - the username on the external system method - either author, monitor or learner ts -
     * timestamp sid - serverID str is [ts + uid + method + serverID + serverKey] (Note: all lower case)
     * 
     * @param str
     *            The string to be hashed
     * @return The hased string
     */
    private static String sha1(String str) {
	try {
	    MessageDigest md = MessageDigest.getInstance("SHA1");
	    return new String(Hex.encodeHex(md.digest(str.getBytes())));
	} catch (NoSuchAlgorithmException e) {
	    throw new RuntimeException(e);
	}
    }

    /**
     * 
     * @param localeStr
     *            the full balckboard locale string
     * @return the language
     */
    public static String getLanguage(String localeStr) {
	if (localeStr == null)
	    return "xx";
	String[] split = localeStr.split("_");
	return split[0];
    }

    /**
     * 
     * @param localeStr
     *            the full balckboard locale string
     * @return the country
     */
    public static String getCountry(String localeStr) {
	if (localeStr == null)
	    return "XX";
	String[] split = localeStr.split("_");
	return split[1];
    }

}
