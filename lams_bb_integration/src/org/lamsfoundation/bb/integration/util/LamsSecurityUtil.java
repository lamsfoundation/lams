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
package org.lamsfoundation.bb.integration.util;

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
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.bb.integration.dto.LearnerProgressDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import blackboard.data.course.Course;
import blackboard.data.course.CourseMembership;
import blackboard.data.user.User;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.Id;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.course.CourseMembershipDbLoader;
import blackboard.platform.context.Context;
import blackboard.platform.persistence.PersistenceServiceFactory;
import blackboard.portal.data.ExtraInfo;
import blackboard.portal.data.PortalExtraInfo;
import blackboard.portal.servlet.PortalUtil;

/**
 * This class creates URLs, servlet calls and webservice calls for communication with LAMS
 *
 * @author <a href="mailto:lfoxton@melcoe.mq.edu.au">Luke Foxton</a>
 */
public class LamsSecurityUtil {

    private static Logger logger = LoggerFactory.getLogger(LamsSecurityUtil.class);
    private static final String DUMMY_COURSE = "Previews";
    private static final String EXPORT_FOLDER_LAMS_SERVER = "/tmp/lams/";

    public static final int SHA1_HEX_LENGTH = 40;
    public static final int SHA256_HEX_LENGTH = 64;

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
    public static String generateRequestURL(Context ctx, String method, String lsid)
	    throws PersistenceException, IOException {
	String serverAddr = LamsPluginUtil.getServerUrl();
	String serverId = LamsPluginUtil.getServerId();

	// If lams.properties could not be read, throw exception
	if (serverAddr == null || serverId == null) {
	    throw new RuntimeException("Configuration Exception " + serverAddr + ", " + serverId);
	}

	String timestamp = LamsSecurityUtil.getServerTime();
	String username = ctx.getUser().getUserName();
	String firstName = ctx.getUser().getGivenName();
	String lastName = ctx.getUser().getFamilyName();
	String email = ctx.getUser().getEmailAddress();
	String locale = ctx.getUser().getLocale();
	String country = LamsSecurityUtil.getCountryCode(ctx.getUser().getCountry());

	// Even for authoring calls we still need a 'course' the user, role & organisation are all bound up together
	// do to be authorised to use authoring you must be in an organisation.
	String courseId = LamsSecurityUtil.setupCourseId(ctx, null, true);

	String serverSecretKey = LamsPluginUtil.getServerSecretKey();

	// in case of learnerStrictAuth we should also include lsid value when creating hash: [ts + uid + method + lsid
	// + serverID + serverSecretKey]
	// regular case: [ts + uid + method + serverID + serverSecretKey]
	String plaintext = timestamp.toLowerCase().trim() + username.toLowerCase().trim() + method.toLowerCase().trim()
		+ ("learnerStrictAuth".equals(method) ? lsid.toLowerCase().trim() : "") + serverId.toLowerCase().trim()
		+ serverSecretKey.toLowerCase().trim();
	// generate authentication hash code to validate parameters
	String hash = LamsSecurityUtil.sha1(plaintext);

	String url;
	try {
	    String course = courseId != null ? "&courseid=" + URLEncoder.encode(courseId, "UTF8") : "";
	    url = serverAddr + "/LoginRequest?" + "&uid=" + URLEncoder.encode(username, "UTF8") + "&method=" + method
		    + "&ts=" + timestamp + "&sid=" + serverId + "&hash=" + hash + course + "&country=" + country
		    + "&lang=" + locale + "&firstName=" + URLEncoder.encode(firstName, "UTF-8") + "&lastName="
		    + URLEncoder.encode(lastName, "UTF-8") + "&email=" + URLEncoder.encode(email, "UTF-8");

	    if ("learnerStrictAuth".equals(method) || "monitor".equals(method)) {
		url += "&lsid=" + lsid;
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
     *
     * @throws UnsupportedEncodingException
     */
    public static String generateAuthenticateParameters(String username) throws UnsupportedEncodingException {
	String serverAddr = LamsPluginUtil.getServerUrl();
	String serverId = LamsPluginUtil.getServerId();

	// If lams.properties could not be read, throw exception
	if (serverAddr == null || serverId == null) {
	    throw new RuntimeException("Configuration Exception " + serverAddr + ", " + serverId);
	}

	String timestamp = new Long(System.currentTimeMillis()).toString();
	String hash = LamsSecurityUtil.generateAuthenticationHash(timestamp, username, serverId);

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
    public static String generateRequestLearningDesignImage(String username) throws UnsupportedEncodingException {
	String serverAddr = LamsPluginUtil.getServerUrl();

	//$request = "$CFG->lamslesson_serverurl/services/LearningDesignSVG?serverId=" . $CFG->lamslesson_serverid . "&datetime=" . $datetime_encoded . "&hashValue=" .
	//$hashvalue . "&username=" . $username  . "&courseId=" . $courseid . "&courseName=" . urlencode($coursename) . "&mode=2&country=" . $country . "&lang=" . $lang .
	//"&ldId=" . $ldid;
	String url = serverAddr + "/services/LearningDesignSVG?"
		+ LamsSecurityUtil.generateAuthenticateParameters(username);

	logger.info("LAMS Req: " + url);

	return url;
    }

    /**
     * Gets a list of learning designs & workspace folders for the current user from LAMS.
     *
     * @param ctx
     *            the blackboard context, contains session data
     * @param courseId
     *            blackboard courseid. We pass it as a parameter as ctx.getCourse().getCourseId() is null when called
     *            from LamsLearningDesignServlet.
     * @param folderId
     *            folderId. It can be null and then LAMS returns default workspace folders.
     *
     * @return a string containing the LAMS workspace tree in tigra format
     */
    public static String getLearningDesigns(Context ctx, String courseId, String folderId) {
	return LamsSecurityUtil.getLearningDesigns(ctx, null, courseId, folderId, "getLearningDesignsJSON", null, null,
		null, null, null, null);
    }

    /**
     * Gets a list of learning designs & workspace folders for the current user from LAMS or the user
     * "usernameFromParam"
     *
     * @param ctx
     *            the blackboard context, contains session data
     * @param usernameFromParam
     *            only used if there isn't a user in the context, due to how the servlet is called
     * @param courseId
     *            blackboard course id. We pass it as a parameter as ctx.getCourse().getCourseId() is null when called
     *            from LamsLearningDesignServlet.
     * @param folderId
     *            folderID in LAMS. It can be null and then LAMS returns default workspace folders.
     * @param method
     *            which method to call on the LAMS end
     * @param type
     *            used onlu for method = getLearningDesignsJSON, restricts by type
     * @param page
     *            used only for method = getPagedHomeLearningDesignsJSON
     * @param size
     *            used only for method = getPagedHomeLearningDesignsJSON
     * @return a string containing the LAMS workspace tree in tigra format (method = getLearningDesignsJSON) or
     *         a string containing the learning designs in JSON (method = getPagedHomeLearningDesignsJSON)
     */
    public static String getLearningDesigns(Context ctx, String usernameFromParam, String urlCourseId, String folderId,
	    String method, String type, String search, String page, String size, String sortName, String sortDate) {

	String serverAddr = LamsPluginUtil.getServerUrl();

	String courseId = LamsSecurityUtil.setupCourseId(ctx, urlCourseId, true);
	String serverId = LamsPluginUtil.getServerId();

	// If lams.properties could not be read, throw exception
	if (serverAddr == null || serverId == null) {
	    throw new RuntimeException(
		    "lams.properties file could not be read. serverAddr:" + serverAddr + ", serverId:" + serverId);
	}

	String timestamp = new Long(System.currentTimeMillis()).toString();

	User user = ctx.getUser();
	if (user == null) {
	    user = BlackboardUtil.loadUserFromDB(usernameFromParam);
	}

	String username = user.getUserName();
	String firstName = user.getGivenName();
	String lastName = user.getFamilyName();
	String email = user.getEmailAddress();
	String hash = LamsSecurityUtil.generateAuthenticationHash(timestamp, username, serverId);

	String locale = ctx.getUser().getLocale();
	String country = LamsSecurityUtil.getCountryCode(ctx.getUser().getCountry());

	// the mode to call upon learning designs
	final int MODE = 2;

	// TODO: Make locale settings work
	String learningDesigns = ""; // empty
	try {

	    String serviceURL = serverAddr + "/services/xml/LearningDesignRepository?method=" + method + "&datetime="
		    + timestamp + "&username=" + URLEncoder.encode(username, "utf8") + "&serverId="
		    + URLEncoder.encode(serverId, "utf8") + "&hashValue=" + hash + "&courseId="
		    + URLEncoder.encode(courseId, "UTF8") + "&country=" + country + "&lang=" + locale + "&mode=" + MODE
		    + "&firstName=" + URLEncoder.encode(firstName, "UTF-8") + "&lastName="
		    + URLEncoder.encode(lastName, "UTF-8") + "&email=" + URLEncoder.encode(email, "UTF-8");

	    if (folderId != null) {
		serviceURL += "&folderID=" + (folderId.equalsIgnoreCase("home") ? "-1" : folderId);
	    }

	    // The following parameter is only used for getLearningDesignsJSON
	    if (type != null && type.length() > 0) {
		serviceURL += "&type=" + type;
	    }

	    // The following parameters are only used for getPagedLearningDesignsJSON
	    if (page != null) {
		serviceURL += "&page=" + page;
	    }
	    if (size != null) {
		serviceURL += "&size=" + size;
	    }
	    // sort by name, ascending = 1, descending = 0
	    if (sortName != null) {
		serviceURL += "&sortName=" + sortName;
	    }
	    // sort by date, ascending = 1, descending = 0
	    if (sortDate != null) {
		serviceURL += "&sortDate=" + sortDate;
	    }
	    // get all the designs that contain this string
	    if (search != null) {
		serviceURL += "&search=" + search;
	    }

	    InputStream is = LamsSecurityUtil.callLamsServerPost(serviceURL);

	    // Read/convert response to a String
	    StringWriter writer = new StringWriter();
	    IOUtils.copy(is, writer, "UTF-8");
	    learningDesigns = writer.toString();

	} catch (MalformedURLException e) {
	    throw new RuntimeException(
		    "Unable to get LAMS learning designs, bad URL: '" + serverAddr + "', please check lams.properties",
		    e);
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
     * Gets a list of learning designs & workspace folders for the current user from LAMS.
     *
     * @param ctx
     *            the blackboard context, contains session data
     * @param courseId
     *            blackboard courseid. We pass it as a parameter as ctx.getCourse().getCourseId() is null when called
     *            from LamsLearningDesignServlet.
     * @param ldId
     *            learning design to delete
     * @return JSON response from server
     */
    public static String deleteLearningDesigns(Context ctx, String urlCourseId, Long ldId) {

	String courseId = LamsSecurityUtil.setupCourseId(ctx, urlCourseId, false);

	String serverAddr = LamsPluginUtil.getServerUrl();
	String serverId = LamsPluginUtil.getServerId();

	// If lams.properties could not be read, throw exception
	if (serverAddr == null || serverId == null) {
	    throw new RuntimeException(
		    "lams.properties file could not be read. serverAddr:" + serverAddr + ", serverId:" + serverId);
	}

	String timestamp = new Long(System.currentTimeMillis()).toString();
	String username = ctx.getUser().getUserName();
	String firstName = ctx.getUser().getGivenName();
	String lastName = ctx.getUser().getFamilyName();
	String email = ctx.getUser().getEmailAddress();
	String hash = LamsSecurityUtil.generateAuthenticationHash(timestamp, username, serverId);

	String locale = ctx.getUser().getLocale();
	String country = LamsSecurityUtil.getCountryCode(ctx.getUser().getCountry());

	try {

	    String serviceURL = serverAddr
		    + "/services/xml/LearningDesignRepository?method=deleteLearningDesignJSON&datetime=" + timestamp
		    + "&username=" + URLEncoder.encode(username, "utf8") + "&serverId="
		    + URLEncoder.encode(serverId, "utf8") + "&hashValue=" + hash + "&courseId="
		    + URLEncoder.encode(courseId, "UTF8") + "&country=" + country + "&lang=" + locale + "&firstName="
		    + URLEncoder.encode(firstName, "UTF-8") + "&lastName=" + URLEncoder.encode(lastName, "UTF-8")
		    + "&email=" + URLEncoder.encode(email, "UTF-8") + "&learningDesignID=" + ldId;

	    InputStream is = LamsSecurityUtil.callLamsServerPost(serviceURL);

	    // Read/convert response to a String
	    StringWriter writer = new StringWriter();
	    IOUtils.copy(is, writer, "UTF-8");
	    return writer.toString();

	} catch (MalformedURLException e) {
	    throw new RuntimeException(
		    "Unable to get LAMS learning designs, bad URL: '" + serverAddr + "', please check lams.properties",
		    e);
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
    }

    private static String setupCourseId(Context ctx, String urlCourseId, boolean allowUserDummyCourse) {
	// can we pull the alphanumeric course id from the context, rather than the on passed in from the URL? If neither exist, use the dummy Preview course.
	String courseId = null;
	if (ctx.getCourse() != null) {
	    courseId = ctx.getCourse().getCourseId();
	}
	if (courseId == null && urlCourseId != null && urlCourseId.length() > 0) {
	    courseId = urlCourseId;
	}
	if (courseId == null && allowUserDummyCourse) {
	    courseId = DUMMY_COURSE;
	}
	return courseId;
    }

    /**
     * Starts lessons in lams through a LAMS webservice.
     *
     * @param ctx
     *            the blackboard contect, contains session data
     * @param usernameFromParam
     *            current user's username
     * @param courseIdStr
     *            courseId
     * @param ldId
     *            the learning design id for which you wish to start a lesson
     * @param title
     *            the title of the lesson
     * @param desc
     *            the description of the lesson
     * @param enforceAllowLearnerRestart
     *            whether learners are allowed to restart the lesson. It has a higher priority than LAMS ExtServer's
     *            according default setting
     * @param isPreview
     *            whether LAMS should start it as a preview or not
     *
     * @return the learning session id
     */
    public static Long startLesson(User user, String courseId, long ldId, String title, String desc,
	    boolean enforceAllowLearnerRestart, boolean isPreview) {

	String serverId = LamsPluginUtil.getServerId();
	String serverAddr = LamsPluginUtil.getServerUrl();
	String serverSecretKey = LamsPluginUtil.getServerSecretKey();

	String username = user.getUserName();
	String locale = user.getLocale();
	String country = LamsSecurityUtil.getCountryCode(user.getCountry());
	String method = (isPreview) ? "preview" : "start";

	if (courseId == null || serverId == null || serverAddr == null || serverSecretKey == null) {
	    logger.info("Unable to start lesson, one or more lams configuration properties or the course id is null");
	    throw new RuntimeException(
		    "Unable to start lesson, one or more lams configuration properties or the course id is null. courseId="
			    + courseId);
	}

	try {
	    String timestamp = new Long(System.currentTimeMillis()).toString();
	    String hash = LamsSecurityUtil.generateAuthenticationHash(timestamp, username, serverId);
	    String course = courseId != null ? "&courseId=" + URLEncoder.encode(courseId, "UTF8") : "";

	    String serviceURL = serverAddr + "/services/xml/LessonManager?" + "serverId="
		    + URLEncoder.encode(serverId, "utf8") + "&datetime=" + timestamp + "&username="
		    + URLEncoder.encode(username, "utf8") + "&hashValue=" + hash + course + "&ldId="
		    + new Long(ldId).toString() + "&country=" + country + "&lang=" + locale + "&method=" + method
		    + "&title=" + URLEncoder.encode(title, "utf8").trim() + "&desc="
		    + URLEncoder.encode(desc, "utf8").trim() + "&enableNotifications=true" + "&allowLearnerRestart="
		    + enforceAllowLearnerRestart;

	    logger.info("LAMS START LESSON Req: " + serviceURL);

	    // parse xml response and get the lesson id
	    InputStream is = LamsSecurityUtil.callLamsServerPost(serviceURL);
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document document = db.parse(is);
	    return Long.parseLong(document.getElementsByTagName("Lesson").item(0).getAttributes()
		    .getNamedItem("lessonId").getNodeValue());

	} catch (MalformedURLException e) {
	    throw new RuntimeException(
		    "Unable to start LAMS lesson, bad URL: '" + serverAddr + "', please check lams.properties", e);
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
	} catch (IOException e) {
	    throw new RuntimeException(
		    "Unable to start LAMS lesson. " + e.getMessage() + " Please contact your system administrator.", e);
	} catch (ParserConfigurationException e) {
	    throw new RuntimeException("Unable to start LAMS lesson. Please contact your system administrator.", e);
	} catch (SAXException e) {
	    throw new RuntimeException("Unable to start LAMS lesson. Please contact your system administrator.", e);
	}

    }

    /**
     * Deletes lesson on LAMS server through a LAMS webservice.
     *
     * @param ctx
     *            the blackboard contect, contains session data
     * @param usernameFromParam
     *            current user's username
     * @param lsId
     *            the lesson id to be deleted
     *
     * @return boolean whether lesson was successfully deleted
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static Boolean deleteLesson(String userName, String lsId)
	    throws IOException, ParserConfigurationException, SAXException {

	String serverId = LamsPluginUtil.getServerId();
	String serverAddr = LamsPluginUtil.getServerUrl();
	String serverSecretKey = LamsPluginUtil.getServerSecretKey();

	if (serverId == null || serverAddr == null || serverSecretKey == null) {
	    throw new RuntimeException("Unable to delete lesson. One or more LAMS configuration properties are null");
	}

	String timestamp = new Long(System.currentTimeMillis()).toString();
	String hash = LamsSecurityUtil.generateAuthenticationHash(timestamp, userName, serverId);

	String serviceURL = serverAddr + "/services/xml/LessonManager?" + "serverId="
		+ URLEncoder.encode(serverId, "utf8") + "&datetime=" + timestamp + "&username="
		+ URLEncoder.encode(userName, "utf8") + "&hashValue=" + hash + "&method=removeLesson" + "&lsId=" + lsId;

	logger.info("LAMS DELETE LESSON Req: " + serviceURL);

	// parse xml response and get the lesson id
	InputStream is = LamsSecurityUtil.callLamsServerPost(serviceURL);
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder();
	Document document = db.parse(is);
	return Boolean.parseBoolean(
		document.getElementsByTagName("Lesson").item(0).getAttributes().getNamedItem("deleted").getNodeValue());
    }

    /**
     * Clones lessons in lams through a LAMS webservice using the lsID & courseId parameter.
     *
     * @param courseId
     *            courseId as a request parameter
     * @param ldId
     *            the learning design id for which you wish to start a lesson
     *
     * @return lesson id of a cloned lesson
     */
    public static Long cloneLesson(User teacher, String courseId, String lsId) {

	String serverId = LamsPluginUtil.getServerId();
	String serverAddr = LamsPluginUtil.getServerUrl();
	String serverSecretKey = LamsPluginUtil.getServerSecretKey();
	String username = teacher.getUserName();
	String locale = teacher.getLocale();
	String country = LamsSecurityUtil.getCountryCode(teacher.getCountry());

	if (courseId == null || serverId == null || serverAddr == null || serverSecretKey == null) {
	    logger.info("Unable to clone lesson, one or more lams configuration properties or the course id is null");
	    throw new RuntimeException(
		    "Unable to clone lesson, one or more lams configuration properties or the course id is null. courseId="
			    + courseId);
	}

	try {
	    String method = "clone";
	    String timestamp = new Long(System.currentTimeMillis()).toString();
	    String hash = LamsSecurityUtil.generateAuthenticationHash(timestamp, username, serverId);
	    String serviceURL = serverAddr + "/services/xml/LessonManager?" + "serverId="
		    + URLEncoder.encode(serverId, "utf8") + "&datetime=" + timestamp + "&username="
		    + URLEncoder.encode(username, "utf8") + "&hashValue=" + hash + "&courseId="
		    + URLEncoder.encode(courseId, "UTF8") + "&country=" + country + "&lang=" + locale + "&lsId=" + lsId
		    + "&method=" + method;

	    logger.info("LAMS clone lesson request: " + serviceURL);

	    // parse xml response and get the lesson id
	    InputStream is = LamsSecurityUtil.callLamsServerPost(serviceURL);
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document document = db.parse(is);
	    return Long.parseLong(document.getElementsByTagName("Lesson").item(0).getAttributes()
		    .getNamedItem("lessonId").getNodeValue());

	} catch (MalformedURLException e) {
	    throw new RuntimeException(
		    "Unable to clone LAMS lesson, bad URL: '" + serverAddr + "', please check lams.properties", e);
	} catch (IllegalStateException e) {
	    throw new RuntimeException(
		    "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator",
		    e);
	} catch (RemoteException e) {
	    throw new RuntimeException("Unable to clone LAMS lesson, RMI Remote Exception", e);
	} catch (UnsupportedEncodingException e) {
	    throw new RuntimeException("Unable to clone LAMS lesson, Unsupported Encoding Exception", e);
	} catch (ConnectException e) {
	    throw new RuntimeException(
		    "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator",
		    e);
	} catch (IOException e) {
	    throw new RuntimeException(
		    "Unable to clone LAMS lesson. " + e.getMessage() + " Please contact your system administrator.", e);
	} catch (ParserConfigurationException e) {
	    throw new RuntimeException(
		    "Unable to clone LAMS lesson. " + e.getMessage() + " Can't instantiate DocumentBuilder.", e);
	} catch (SAXException e) {
	    throw new RuntimeException("Unable to clone LAMS lesson. " + e.getMessage() + " Can't parse LAMS results.",
		    e);
	}

    }

    /**
     * Import learning design in LAMS from its temp folder. Then starting a lesson using this learning design.
     *
     * @param courseId
     *            courseId as a request parameter
     * @param ldId
     *            the learning design id for which you wish to start a lesson
     *
     * @return lesson id of a cloned lesson
     * @throws LamsServerException
     */
    public static Long importLearningDesign(User teacher, String courseId, String lsId, String ldId)
	    throws LamsServerException {

	String serverId = LamsPluginUtil.getServerId();
	String serverAddr = LamsPluginUtil.getServerUrl();
	String serverSecretKey = LamsPluginUtil.getServerSecretKey();
	String username = teacher.getUserName();
	String locale = teacher.getLocale();
	String country = LamsSecurityUtil.getCountryCode(teacher.getCountry());

	if (courseId == null || serverId == null || serverAddr == null || serverSecretKey == null) {
	    logger.info("Unable to import lesson, one or more lams configuration properties or the course id is null");
	    throw new RuntimeException(
		    "Unable to import lesson, one or more lams configuration properties or the course id is null. courseId="
			    + courseId);
	}

	//import a learning design
	String filePath = EXPORT_FOLDER_LAMS_SERVER + lsId + "_" + ldId + ".zip";

	try {
	    String filePathParam = URLEncoder.encode(filePath, "UTF-8");
	    String timestamp = new Long(System.currentTimeMillis()).toString();
	    String hash = LamsSecurityUtil.generateAuthenticationHash(timestamp, username, serverId);
	    String serviceURL = serverAddr + "/services/xml/LessonManager?" + "serverId="
		    + URLEncoder.encode(serverId, "utf8") + "&datetime=" + timestamp + "&username="
		    + URLEncoder.encode(username, "utf8") + "&hashValue=" + hash + "&courseId="
		    + URLEncoder.encode(courseId, "UTF8") + "&country=" + country + "&lang=" + locale
		    + "&method=import&customCSV=&filePath=" + filePathParam;

	    logger.info("LAMS import lesson request: " + serviceURL);

	    // parse xml response and get the ldid
	    InputStream is = LamsSecurityUtil.callLamsServerPost(serviceURL);
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document document = db.parse(is);
	    return Long.parseLong(document.getElementsByTagName("Lesson").item(0).getAttributes().getNamedItem("ldId")
		    .getNodeValue());

	} catch (MalformedURLException e) {
	    throw new LamsServerException("Unable to import LAMS lesson, bad URL: '" + serverAddr
		    + "', please check lams.properties. Tried to import file " + filePath, e);
	} catch (IllegalStateException e) {
	    throw new LamsServerException(
		    "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator. Tried to import file "
			    + filePath,
		    e);
	} catch (RemoteException e) {
	    throw new LamsServerException(
		    "Unable to import LAMS lesson, RMI Remote Exception. Tried to import file " + filePath, e);
	} catch (UnsupportedEncodingException e) {
	    throw new LamsServerException(
		    "Unable to import LAMS lesson, Unsupported Encoding Exception. Tried to import file " + filePath,
		    e);
	} catch (ConnectException e) {
	    throw new LamsServerException(
		    "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator. Tried to import file "
			    + filePath,
		    e);
	} catch (IOException e) {
	    throw new LamsServerException("Unable to import LAMS lesson. " + e.getMessage()
		    + " Please contact your system administrator. Tried to import file " + filePath, e);
	} catch (ParserConfigurationException e) {
	    throw new LamsServerException("Unable to import LAMS lesson. " + e.getMessage()
		    + " Can't instantiate DocumentBuilder. Tried to import file " + filePath, e);
	} catch (SAXException e) {
	    throw new LamsServerException("Unable to import LAMS lesson. " + e.getMessage()
		    + " Can't parse LAMS results. Tried to import file " + filePath, e);
	}

    }

    /**
     * Pre-adding students and monitors to a lesson
     *
     * @param ctx
     *            the blackboard contect, contains session data
     * @param lessonId
     *            the lesoon id that was just started
     */
    public static void preaddLearnersMonitorsToLesson(User user, Course course, long lessonId) {
	String serverId = LamsPluginUtil.getServerId();
	String serverAddr = LamsPluginUtil.getServerUrl();
	String serverSecretKey = LamsPluginUtil.getServerSecretKey();
	String username = user.getUserName();
	String locale = user.getLocale();
	String country = LamsSecurityUtil.getCountryCode(user.getCountry());

	if (serverId == null || serverAddr == null || serverSecretKey == null) {
	    throw new RuntimeException("Unable to start lesson, one or more lams configuration properties is null");
	}

	try {
	    /*
	     * Returns a list of learners and monitors in the given course or group.
	     */

	    String learnerIds = "";
	    String firstNames = "";
	    String lastNames = "";
	    String emails = "";
	    String monitorIds = "";
	    final String DUMMY_VALUE = "-";

	    BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
	    CourseMembershipDbLoader courseMemLoader = CourseMembershipDbLoader.Default.getInstance();

	    Id courseId = course.getId();
	    List<CourseMembership> studentCourseMemberships = courseMemLoader.loadByCourseIdAndRole(courseId,
		    CourseMembership.Role.STUDENT, null, true);
	    for (CourseMembership courseMembership : studentCourseMemberships) {
		String learnerId = LamsSecurityUtil.escapeValue(courseMembership.getUser().getUserName());
		learnerIds += learnerId + ",";

		String firstName = LamsSecurityUtil.escapeValue(courseMembership.getUser().getGivenName());
		firstNames += firstName + ",";

		String lastName = LamsSecurityUtil.escapeValue(courseMembership.getUser().getFamilyName());
		lastNames += lastName + ",";

		String email = LamsSecurityUtil.escapeValue(courseMembership.getUser().getEmailAddress());
		emails += email + ",";
	    }

	    List<CourseMembership> monitorCourseMemberships = courseMemLoader.loadByCourseIdAndRole(courseId,
		    CourseMembership.Role.INSTRUCTOR, null, true);
	    List<CourseMembership> teachingAssistantCourseMemberships = courseMemLoader.loadByCourseIdAndRole(courseId,
		    CourseMembership.Role.TEACHING_ASSISTANT, null, true);
	    monitorCourseMemberships.addAll(teachingAssistantCourseMemberships);
	    List<CourseMembership> courseBuilderCourseMemberships = courseMemLoader.loadByCourseIdAndRole(courseId,
		    CourseMembership.Role.COURSE_BUILDER, null, true);
	    monitorCourseMemberships.addAll(courseBuilderCourseMemberships);
	    for (CourseMembership courseMembership : monitorCourseMemberships) {
		String monitorId = LamsSecurityUtil.escapeValue(courseMembership.getUser().getUserName());
		monitorIds += monitorId + ",";

		String firstName = LamsSecurityUtil.escapeValue(courseMembership.getUser().getGivenName());
		firstNames += firstName + ",";

		String lastName = LamsSecurityUtil.escapeValue(courseMembership.getUser().getFamilyName());
		lastNames += lastName + ",";

		String email = LamsSecurityUtil.escapeValue(courseMembership.getUser().getEmailAddress());
		emails += email + ",";
	    }

	    //no learners & no monitors - do nothing
	    if (learnerIds.isEmpty() && monitorIds.isEmpty()) {
		return;
	    }

	    // remove trailing comma
	    learnerIds = learnerIds.isEmpty() ? "" : learnerIds.substring(0, learnerIds.length() - 1);
	    firstNames = firstNames.isEmpty() ? "" : firstNames.substring(0, firstNames.length() - 1);
	    lastNames = lastNames.isEmpty() ? "" : lastNames.substring(0, lastNames.length() - 1);
	    emails = emails.isEmpty() ? "" : emails.substring(0, emails.length() - 1);
	    monitorIds = monitorIds.isEmpty() ? "" : monitorIds.substring(0, monitorIds.length() - 1);

	    String timestamp = new Long(System.currentTimeMillis()).toString();
	    String hash = LamsSecurityUtil.generateAuthenticationHash(timestamp, username, serverId);

	    String serviceURL = serverAddr + "/services/xml/LessonManager?" + "&serverId="
		    + URLEncoder.encode(serverId, "utf8") + "&datetime=" + timestamp + "&username="
		    + URLEncoder.encode(username, "utf8") + "&hashValue=" + hash + "&courseId="
		    + URLEncoder.encode(course.getCourseId(), "utf8") + "&lsId=" + lessonId + "&country=" + country
		    + "&lang=" + locale + "&method=join" + "&firstNames=" + firstNames + "&lastNames=" + lastNames
		    + "&emails=" + emails;
	    if (!monitorIds.isEmpty()) {
		serviceURL += "&monitorIds=" + monitorIds;
	    }
	    if (!learnerIds.isEmpty()) {
		serviceURL += "&learnerIds=" + learnerIds;
	    }

	    logger.info("LAMS Preadd users Req: " + serviceURL);
	    System.out.println("LAMS Preadd users Req: " + serviceURL);

	    InputStream is = LamsSecurityUtil.callLamsServerPost(serviceURL);

	} catch (MalformedURLException e) {
	    throw new RuntimeException(
		    "Unable to preadd users to the lesson, bad URL: '" + serverAddr + "', please check lams.properties",
		    e);
	} catch (IllegalStateException e) {
	    throw new RuntimeException(
		    "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator",
		    e);
	} catch (RemoteException e) {
	    throw new RuntimeException("Unable to preadd users to the lesson, RMI Remote Exception", e);
	} catch (UnsupportedEncodingException e) {
	    throw new RuntimeException("Unable to preadd users to the lesson, Unsupported Encoding Exception", e);
	} catch (ConnectException e) {
	    throw new RuntimeException(
		    "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator",
		    e);
	} catch (IOException e) {
	    throw new RuntimeException("Unable to preadd users to the lesson. " + e.getMessage()
		    + " Please contact your system administrator.", e);
	} catch (KeyNotFoundException e) {
	    throw new RuntimeException("Unable to preadd users to the lesson. " + e.getMessage()
		    + " Please contact your system administrator.", e);
	} catch (PersistenceException e) {
	    throw new RuntimeException("Unable to preadd users to the lesson. " + e.getMessage()
		    + " Please contact your system administrator.", e);
	}

    }

    /**
     * Takes care about blank values. Besides, escapes CSV sensitive symbols (commas, quotes, etc) and then encodes it
     * to be sent as a URL parameter.
     *
     * @param value
     * @param CSV
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String escapeValue(String value) throws UnsupportedEncodingException {
	final String DUMMY_VALUE = "-";

	String notBlankValue = StringUtils.isBlank(value) ? DUMMY_VALUE : value;
	String escapedCsv = StringEscapeUtils.escapeCsv(notBlankValue);
	String encodedValue = URLEncoder.encode(escapedCsv, "utf8");

	return encodedValue;
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
     */
    public static LearnerProgressDTO getLearnerProgress(Context ctx, long lsId) {
	String serverId = LamsPluginUtil.getServerId();
	String serverAddr = LamsPluginUtil.getServerUrl();
	String serverSecretKey = LamsPluginUtil.getServerSecretKey();
	String courseId = ctx.getCourse().getCourseId();

	String username = ctx.getUser().getUserName();
	String firstName = ctx.getUser().getGivenName();
	String lastName = ctx.getUser().getFamilyName();
	String email = ctx.getUser().getEmailAddress();
	String locale = ctx.getUser().getLocale();
	String country = LamsSecurityUtil.getCountryCode(ctx.getUser().getCountry());

	if (serverId == null || serverAddr == null || serverSecretKey == null) {
	    throw new RuntimeException("Unable to start lesson, one or more lams configuration properties is null");
	}

	try {

	    String timestamp = new Long(System.currentTimeMillis()).toString();
	    String hash = LamsSecurityUtil.generateAuthenticationHash(timestamp, username, serverId);

	    String serviceURL = serverAddr + "/services/xml/LessonManager?method=singleStudentProgress" + "&serverId="
		    + URLEncoder.encode(serverId, "utf8") + "&datetime=" + timestamp + "&username="
		    + URLEncoder.encode(username, "utf8") + "&hashValue=" + hash + "&courseId="
		    + URLEncoder.encode(courseId, "utf8") + "&country=" + country + "&lang=" + locale + "&firstName="
		    + URLEncoder.encode(firstName, "UTF-8") + "&lastName=" + URLEncoder.encode(lastName, "UTF-8")
		    + "&email=" + URLEncoder.encode(email, "UTF-8") + "&lsId=" + new Long(lsId).toString();

	    logger.info("Retirieving learner progress: " + serviceURL);

	    // InputStream is = url.openConnection().getInputStream();
	    InputStream is = LamsSecurityUtil.callLamsServerPost(serviceURL);

	    // parse xml response
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document document = db.parse(is);

	    // get the lesson id from the response
	    NamedNodeMap learnerProgress = document.getElementsByTagName("LearnerProgress").item(0).getAttributes();
	    boolean lessonComplete = Boolean
		    .parseBoolean(learnerProgress.getNamedItem("lessonComplete").getNodeValue());
	    int activitiesCompleted = Integer
		    .parseInt(learnerProgress.getNamedItem("activitiesCompleted").getNodeValue());
	    int attemptedActivities = Integer
		    .parseInt(learnerProgress.getNamedItem("attemptedActivities").getNodeValue());
	    int activityCount = Integer.parseInt(learnerProgress.getNamedItem("activityCount").getNodeValue());

	    LearnerProgressDTO learnerProgressDto = new LearnerProgressDTO(activityCount, attemptedActivities,
		    activitiesCompleted, lessonComplete);

	    return learnerProgressDto;

	} catch (MalformedURLException e) {
	    throw new RuntimeException(
		    "Unable to get LearnerProgress, bad URL: '" + serverAddr + "', please check lams.properties", e);
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
	} catch (IOException e) {
	    throw new RuntimeException(
		    "Unable to get LearnerProgress. " + e.getMessage() + " Please contact your system administrator.",
		    e);
	} catch (ParserConfigurationException e) {
	    throw new RuntimeException(
		    "Unable to get LearnerProgress. " + e.getMessage() + " Please contact your system administrator.",
		    e);
	} catch (SAXException e) {
	    throw new RuntimeException(
		    "Unable to get LearnerProgress. " + e.getMessage() + " Please contact your system administrator.",
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
	    throw new IOException("Unable to open connection to: " + serviceURL);
	}

	HttpURLConnection httpConn = (HttpURLConnection) conn;

	if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
	    throw new IOException("LAMS server responded with HTTP response code: " + httpConn.getResponseCode()
		    + ", HTTP response message: " + httpConn.getResponseMessage());
	}

	// InputStream is = url.openConnection().getInputStream();
	InputStream is = conn.getInputStream();

	return is;
    }

    /**
     * Make a call to LAMS server.
     *
     * @param serviceURL
     * @return resulted InputStream
     * @throws IOException
     */
    private static InputStream callLamsServerPost(String serviceURL) throws IOException {

	String path;
	String body;

	int bodyStart = serviceURL.indexOf('?');
	if (bodyStart < 0) {
	    path = serviceURL;
	    body = "";
	} else {
	    path = serviceURL.substring(0, bodyStart);
	    body = serviceURL.substring(bodyStart + 1);
	}

	byte[] postData = body.getBytes("UTF-8");
	int postDataLength = postData.length;

	URL url = new URL(path);
	URLConnection conn = url.openConnection();
	if (!(conn instanceof HttpURLConnection)) {
	    throw new IOException("Unable to open connection to: " + serviceURL);
	}

	HttpURLConnection httpConn = (HttpURLConnection) conn;
	conn.setDoOutput(true);
	httpConn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	conn.setRequestProperty("charset", "utf-8");
	conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
	conn.setUseCaches(false);

	conn.getOutputStream().write(postData);

	if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
	    throw new IOException("LAMS server responded with HTTP response code: " + httpConn.getResponseCode()
		    + ", HTTP response message: " + httpConn.getResponseMessage());
	}

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

	long lamsServerTimeRefreshInterval = LamsSecurityUtil.getLamsServerTimeRefreshInterval() * 60 * 60 * 1000;
	if ((lamsServerTimeDeltaStr == null) || (lastUpdateTime + lamsServerTimeRefreshInterval < now)) {

	    // refresh time from LAMS server
	    String serverAddr = LamsPluginUtil.getServerUrl();
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
     *
     * @return the LAMS server time refresh interval from lams.properties
     */
    private static long getLamsServerTimeRefreshInterval() {
	//set default value
	long lamsServerTimeRefreshInterval = 24;

	try {
	    String lamsServerTimeRefreshIntervalStr = LamsPluginUtil.getLamsServerTimeRefreshInterval();
	    lamsServerTimeRefreshInterval = Long.parseLong(lamsServerTimeRefreshIntervalStr);
	} catch (NumberFormatException e) {
	    logger.warn("Wrong format of PROP_LAMS_SERVER_TIME_REFRESH_INTERVAL from lams.properties");
	}

	return lamsServerTimeRefreshInterval;
    }

    // generate authentication hash code to validate parameters
    private static String generateAuthenticationHash(String datetime, String login, String serverId) {
	String serverSecretKey = LamsPluginUtil.getServerSecretKey();

	String plaintext = datetime.toLowerCase().trim() + login.toLowerCase().trim() + serverId.toLowerCase().trim()
		+ serverSecretKey.toLowerCase().trim();

	String hash = LamsSecurityUtil.sha1(plaintext);

	return hash;
    }

    /**
     * The parameters are: uid - the username on the external system method - either author, monitor or learner ts -
     * timestamp sid - serverID str is [ts + uid + method + serverID + serverSecretKey] (Note: all lower case)
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

    public static String sha256(String plaintext) {
	try {
	    MessageDigest md = MessageDigest.getInstance("SHA-256");
	    return new String(Hex.encodeHex(md.digest(plaintext.getBytes())));
	} catch (NoSuchAlgorithmException e) {
	    throw new RuntimeException(e);
	}
    }

    /**
     * Returns country code based on country name. If it can't find according code - returns "XX".
     */
    public static String getCountryCode(String countryName) {

	if (StringUtils.isNotBlank(countryName)) {
	    Locale[] locales = Locale.getAvailableLocales();
	    for (Locale locale : locales) {
		String code = locale.getCountry();
		String name = locale.getDisplayCountry();

		if (countryName.equals(name)) {
		    return code;
		}
	    }
	}

	return "XX";
    }

}
