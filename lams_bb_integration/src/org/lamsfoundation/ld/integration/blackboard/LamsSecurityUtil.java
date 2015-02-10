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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
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
import java.util.Date;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.ld.integration.dto.LearnerProgressDTO;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import blackboard.persist.PersistenceException;
import blackboard.platform.context.Context;
import blackboard.portal.data.ExtraInfo;
import blackboard.portal.data.PortalExtraInfo;
import blackboard.portal.servlet.PortalUtil;

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
     *            the mehtod to request of LAMS "author", "monitor", "learnerStrictAuth"
     * @param lsid
     *            lesson id. It is expected to be present in case of "monitor" and "learnerStrictAuth"
     * @return a url pointing to the LAMS lesson, monitor, author session
     * @throws IOException 
     * @throws PersistenceException 
     * @throws Exception
     */
    public static String generateRequestURL(Context ctx, String method, String lsid) throws PersistenceException, IOException {
	
	String serverAddr = getServerAddress();
	String serverId = getServerID();
	String reqSrc = getReqSrc();

	// If lams.properties could not be read, throw exception
	if (serverAddr == null || serverId == null || reqSrc == null) {
	    throw new RuntimeException("Configuration Exception " + serverAddr + ", " + serverId);
	}

	String timestamp = getServerTime();
	String username = ctx.getUser().getUserName();
	String firstName = ctx.getUser().getGivenName();
	String lastName  = ctx.getUser().getFamilyName();
	String email = ctx.getUser().getEmailAddress();
	String courseId = ctx.getCourse().getCourseId();
	String locale = ctx.getUser().getLocale();
	String country = getCountry(locale);
	String lang = getLanguage(locale);

	String secretkey = LamsPluginUtil.getSecretKey();

	// in case of learnerStrictAuth we should also include lsid value when creating hash: [ts + uid + method + lsid
	// + serverID + serverKey]
	// regular case: [ts + uid + method + serverID + serverKey]
	String plaintext = "learnerStrictAuth".equals(method) ? timestamp.toLowerCase().trim()
		+ username.toLowerCase().trim() + method.toLowerCase().trim() + lsid.toLowerCase().trim()
		+ serverId.toLowerCase().trim() + secretkey.toLowerCase().trim() : timestamp.toLowerCase().trim()
		+ username.toLowerCase().trim() + method.toLowerCase().trim() + serverId.toLowerCase().trim()
		+ secretkey.toLowerCase().trim();
	// generate authentication hash code to validate parameters
	String hash = sha1(plaintext);

	String url;
	try {
	    url = serverAddr + "/LoginRequest?" + "&uid=" + URLEncoder.encode(username, "UTF8") + "&method=" + method
		    + "&ts=" + timestamp + "&sid=" + serverId + "&hash=" + hash + "&courseid="
		    + URLEncoder.encode(courseId, "UTF8") + "&country=" + country + "&lang=" + lang + "&requestSrc="
		    + URLEncoder.encode(reqSrc, "UTF8") + "&firstName=" + URLEncoder.encode(firstName, "UTF-8")
		    + "&lastName=" + URLEncoder.encode(lastName, "UTF-8")
		    + "&email=" + URLEncoder.encode(email, "UTF-8");
	    
	    if ("learnerStrictAuth".equals(method) || "monitor".equals(method)) {
		url +=  "&lsid=" + lsid;
	    }
		    
	} catch (UnsupportedEncodingException e) {
	    throw new RuntimeException(e);
	}

	logger.info("LAMS Req: " + url);
	// System.out.println(url);

	return url;
    }
    
    /**
     * Generates default
     * @throws UnsupportedEncodingException 
     */
    public static String generateAuthenticateParameters(String username) throws UnsupportedEncodingException {
	String serverAddr = getServerAddress();
	String serverId = getServerID();
	String reqSrc = getReqSrc();

	// If lams.properties could not be read, throw exception
	if (serverAddr == null || serverId == null || reqSrc == null) {
	    throw new RuntimeException("Configuration Exception " + serverAddr + ", " + serverId);
	}

	String timestamp = new Long(System.currentTimeMillis()).toString();
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
     * @throws UnsupportedEncodingException 
     */
    public static String generateRequestLearningDesignImage(Context ctx, boolean isSvgImage) throws UnsupportedEncodingException {
	String serverAddr = getServerAddress();
	int svgFormat = (isSvgImage) ? 1 : 2;
	
        //$request = "$CFG->lamslesson_serverurl/services/LearningDesignSVG?serverId=" . $CFG->lamslesson_serverid . "&datetime=" . $datetime_encoded . "&hashValue=" . 
        //$hashvalue . "&username=" . $username  . "&courseId=" . $courseid . "&courseName=" . urlencode($coursename) . "&mode=2&country=" . $country . "&lang=" . $lang . 
        //"&ldId=" . $ldid . "&svgFormat=" . $format;
	String username = ctx.getUser().getUserName();
	String url = serverAddr + "/services/LearningDesignSVG?" + generateAuthenticateParameters(username)
		+ "&svgFormat=" + svgFormat;

	logger.info("LAMS Req: " + url);

	return url;
    }

    /**
     * Gets a list of learning designs for the current user from LAMS
     * 
     * @param ctx
     *            the blackboard context, contains session data
     * @param courseId
     *            blackboard courseid. We pass it as a parameter as ctx.getCourse().getCourseId() is null when called
     *            from LamsLearningDesignServlet.
     * @param folderId folderId. It can be null and then LAMS returns default workspace folders.
     * 
     * @return a string containing the LAMS workspace tree in tigra format
     */
    public static String getLearningDesigns(Context ctx, String courseId, String folderId) {
	String serverAddr = getServerAddress();
	String serverId = getServerID();

	// If lams.properties could not be read, throw exception
	if (serverAddr == null || serverId == null) {
	    throw new RuntimeException("lams.properties file could not be read. serverAddr:" + serverAddr + ", serverId:" + serverId);
	}

	String timestamp = new Long(System.currentTimeMillis()).toString();
	String username = ctx.getUser().getUserName();
	String firstName = ctx.getUser().getGivenName();
	String lastName = ctx.getUser().getFamilyName();
	String email = ctx.getUser().getEmailAddress();
	String hash = generateAuthenticationHash(timestamp, username, serverId);

	String locale = ctx.getUser().getLocale();
	String country = getCountry(locale);
	String lang = getLanguage(locale);

	// LamsSecurityUtil.getLearningDesigns(null, userDTO.getUserID(), false);
	// the mode to call upon learning designs
	final Integer MODE = 2;

	// TODO: Make locale settings work
	String learningDesigns = ""; // empty 
	try {

	    String serviceURL = serverAddr
		    + "/services/xml/LearningDesignRepository?method=getLearningDesignsJSON" + "&datetime="
		    + timestamp + "&username=" + URLEncoder.encode(username, "utf8") + "&serverId="
		    + URLEncoder.encode(serverId, "utf8") + "&hashValue=" + hash + "&courseId="
		    + URLEncoder.encode(courseId, "UTF8") + "&country=" + country + "&lang=" + lang + "&mode=" + MODE
		    + "&firstName=" + URLEncoder.encode(firstName, "UTF-8") + "&lastName="
		    + URLEncoder.encode(lastName, "UTF-8") + "&email=" + URLEncoder.encode(email, "UTF-8");
	    if (folderId != null) {
		serviceURL += "&folderID=" + folderId;
	    }

	    InputStream is = LamsSecurityUtil.callLamsServer(serviceURL);

	    // Read/convert response to a String 
	    StringWriter writer = new StringWriter();
	    IOUtils.copy(is, writer, "UTF-8");
	    learningDesigns = writer.toString();

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

	    String serviceURL = serverAddr + "/services/xml/LessonManager?" + "&serverId="
		    + URLEncoder.encode(serverId, "utf8") + "&datetime=" + timestamp + "&username="
		    + URLEncoder.encode(username, "utf8") + "&hashValue=" + hash + "&courseId="
		    + URLEncoder.encode(courseId, "utf8") + "&ldId=" + new Long(ldId).toString() + "&country="
		    + country + "&lang=" + lang + "&method=" + method + "&title="
		    + URLEncoder.encode(title, "utf8").trim() + "&desc=" + URLEncoder.encode(desc, "utf8").trim();

	    logger.info("LAMS START LESSON Req: " + serviceURL);

	    // InputStream is = url.openConnection().getInputStream();
	    InputStream is = LamsSecurityUtil.callLamsServer(serviceURL);

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
     * getLearnerProgress in current lesson through a LAMS webservice
     * 
     * @param ctx
     *            the blackboard contect, contains session data
     * @param lsId
     *            the lesson id for which you wish to retrieve progress
     * 
     * @return the learning session id
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static LearnerProgressDTO getLearnerProgress(Context ctx, long lsId) throws ParserConfigurationException,
	    IOException, SAXException {
	String serverId = getServerID();
	String serverAddr = getServerAddress();
	String serverKey = getServerKey();
	String courseId = ctx.getCourse().getCourseId();
	String username = ctx.getUser().getUserName();

	if (serverId == null || serverAddr == null || serverKey == null) {
	    throw new RuntimeException("Unable to start lesson, one or more lams configuration properties is null");
	}

	try {

	    String timestamp = new Long(System.currentTimeMillis()).toString();
	    String hash = generateAuthenticationHash(timestamp, username, serverId);

	    String serviceURL = serverAddr + "/services/xml/LessonManager?method=singleStudentProgress" + "&serverId="
		    + URLEncoder.encode(serverId, "utf8") + "&datetime=" + timestamp + "&username="
		    + URLEncoder.encode(username, "utf8") + "&hashValue=" + hash + "&courseId="
		    + URLEncoder.encode(courseId, "utf8") + "&lsId=" + new Long(lsId).toString() + "&progressUser="
		    + URLEncoder.encode(username, "utf8");

	    logger.info("Retirieving learner progress: " + serviceURL);

	    // InputStream is = url.openConnection().getInputStream();
	    InputStream is = LamsSecurityUtil.callLamsServer(serviceURL);

	    // parse xml response
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document document = db.parse(is);

	    // get the lesson id from the response
	    NamedNodeMap learnerProgress = document.getElementsByTagName("LearnerProgress").item(0).getAttributes();
	    boolean lessonComplete = Boolean.parseBoolean(learnerProgress.getNamedItem("lessonComplete").getNodeValue());
	    int activitiesCompleted = Integer.parseInt(learnerProgress.getNamedItem("activitiesCompleted").getNodeValue());
	    int attemptedActivities = Integer.parseInt(learnerProgress.getNamedItem("attemptedActivities").getNodeValue());
	    int activityCount = Integer.parseInt(learnerProgress.getNamedItem("activityCount").getNodeValue());

	    LearnerProgressDTO learnerProgressDto = new LearnerProgressDTO(activityCount, attemptedActivities,
		    activitiesCompleted, lessonComplete);
	    
	    return learnerProgressDto;

	} catch (MalformedURLException e) {
	    throw new RuntimeException("Unable to get LearnerProgress, bad URL: '" + serverAddr
		    + "', please check lams.properties", e);
	} catch (IllegalStateException e) {
	    throw new RuntimeException(
		    "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator",
		    e);
	} catch (RemoteException e) {
	    throw new RuntimeException("Unable to get LearnerProgress, RMI Remote Exception", e);
	} catch (UnsupportedEncodingException e) {
	    throw new RuntimeException("Unable to get LearnerProgress, Unsupported Encoding Exception", e);
	} catch (ConnectException e) {
	    throw new RuntimeException(
		    "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator",
		    e);
	}

    }
    
    /**
     * Make a call to LAMS server.
     * 
     * @param serviceURL
     * @return resulted InputStream
     * @throws IOException
     */
    private static InputStream callLamsServer(String serviceURL) throws IOException {
	URL url = new URL(serviceURL);
	URLConnection conn = url.openConnection();
	if (!(conn instanceof HttpURLConnection)) {
	    throw new RuntimeException("Unable to open connection to: " + serviceURL);
	}

	HttpURLConnection httpConn = (HttpURLConnection) conn;

	if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
	    throw new RuntimeException("LAMS server responded with HTTP response code: " + httpConn.getResponseCode()
		    + ", HTTP response message: " + httpConn.getResponseMessage());
	}

	// InputStream is = url.openConnection().getInputStream();
	InputStream is = conn.getInputStream();

	return is;
    }
    
    public static String getServerTime() throws IOException, PersistenceException {
	long now = (new Date()).getTime();
	
	// get LamsServerTime from the storage
	PortalExtraInfo pei = PortalUtil.loadPortalExtraInfo(null, null, "LamsServerTimeStorage");
	ExtraInfo ei = pei.getExtraInfo();
	
	String lamsServerTimeDeltaStr = ei.getValue("LAMSServerTimeDelta");
	long lamsServerTimeDelta = (lamsServerTimeDeltaStr == null) ? -1 : Long.parseLong(lamsServerTimeDeltaStr);
	
	//check if it's time to update
	String lastUpdateTimeStr = ei.getValue("lastUpdateTime");
	long lastUpdateTime = (lastUpdateTimeStr == null) ? -1 : Long.parseLong(lastUpdateTimeStr);
	long lamsServerTime;

	long lamsServerTimeRefreshInterval = getLamsServerTimeRefreshInterval() * 60 * 60 * 1000;
	if ((lamsServerTimeDeltaStr == null) || (lastUpdateTime + lamsServerTimeRefreshInterval < now)) {
	    
	    // refresh time from LAMS server
	    String serverAddr = getServerAddress();
	    String serviceURL = serverAddr + "/services/getServerTime";
	    logger.info("LAMS Get Server Time request: " + serviceURL);
	    InputStream is = LamsSecurityUtil.callLamsServer(serviceURL);

	    StringWriter writer = new StringWriter();
	    IOUtils.copy(is, writer, "UTF-8");
	    
	    try {
		lamsServerTime = Long.parseLong(writer.toString().trim());
	    } catch (NumberFormatException e) {
		throw new RuntimeException("LAMS server returned wrong time format on call to /service/getServerTime",
			e);
	    }
	    lamsServerTimeDelta = now - lamsServerTime;

	    // Store LAMSServerTime and lastUpdateTime to the storage
	    ei.setValue("LAMSServerTimeDelta", "" + lamsServerTimeDelta);
	    ei.setValue("lastUpdateTime", "" + now);
	    PortalUtil.savePortalExtraInfo(pei);
	} else {
	    
	    //no need to refresh - use stored value
	    lamsServerTime = now - lamsServerTimeDelta;
	}
	
	return "" + lamsServerTime;
    }
    
    /**
     * Gets the app.version property value from
     * the ./main.properties file of the base folder
     *
     * @return app.version string
     * @throws IOException
     */
    public static String getAppVersion() throws IOException{

        String versionString = null;

        //to load application's properties, we use this class
        Properties mainProperties = new Properties();

        FileInputStream file;

        //the base folder is ./, the root of the main.properties file  
        String path = "./main.properties";

        //load the file handle for main.properties
        file = new FileInputStream(path);

        //load all the properties from this file
        mainProperties.load(file);

        //we have loaded the properties, so close the file handle
        file.close();

        //retrieve the property we are intrested, the app.version
        versionString = mainProperties.getProperty("app.version");

        return versionString;
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
     * @return the LAMS server time refresh interval from lams.properties
     */
    public static long getLamsServerTimeRefreshInterval() {
	//set default value
	long lamsServerTimeRefreshInterval = 24;
	
	try {
	    String lamsServerTimeRefreshIntervalStr = LamsPluginUtil.getProperties().getProperty(
		    LamsPluginUtil.PROP_LAMS_SERVER_TIME_REFRESH_INTERVAL);
	    lamsServerTimeRefreshInterval = Long.parseLong(lamsServerTimeRefreshIntervalStr);
	} catch (NumberFormatException e) {
	    logger.warn("Wrong format of PROP_LAMS_SERVER_TIME_REFRESH_INTERVAL from lams.properties");
	}
	
	return lamsServerTimeRefreshInterval;
    }

    // generate authentication hash code to validate parameters
    public static String generateAuthenticationHash(String datetime, String login, String serverId) {
	String secretkey = getServerKey();

	String plaintext = datetime.toLowerCase().trim() + login.toLowerCase().trim() + serverId.toLowerCase().trim()
		+ secretkey.toLowerCase().trim();

	String hash = sha1(plaintext);

	return hash;
    }

    /**
     * The parameters are: uid - the username on the external system method - either author, monitor or learner ts -
     * timestamp sid - serverID str is [ts + uid + method + serverID + serverKey] (Note: all lower case)
     * 
     * @param str
     *            The string to be hashed
     * @return The hased string
     */
    public static String sha1(String str) {
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
	
	//default country set to AU
	String country = split.length > 1 ? split[1] : "AU";
	return country;
    }

}
