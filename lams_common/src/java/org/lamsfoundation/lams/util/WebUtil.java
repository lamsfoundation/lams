package org.lamsfoundation.lams.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.w3c.dom.Document;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * helper methods useful for servlets
 */
public class WebUtil {

    // ---------------------------------------------------------------------
    // Class level constants - Session attributs
    // ---------------------------------------------------------------------

    private static Logger log = Logger.getLogger(WebUtil.class);
    /**
     * A regular expression pattern that matches HTML tags.
     */
    private static final String HTML_TAG_REGEX = "\\<.*?>";
    /**
     * A regular expression pattern that matches end-of-line and space tags. If needed, BR tags can be extented to
     * <code>(?:<BR>|<br>|<BR />|<br />)</code> . Right now CKeditor creates only the first option.
     */
    private static final String SPACE_TAG_REGEX = "(?:<BR>)|(?:&nbsp;)|(?:</div><div>)";

    private static final String URL_SHORTENING_CYPHER = "jbdnuteywk";

    /**
     * @exception IllegalArgumentException
     *                - if not set
     */
    private static void checkObject(String paramName, Object paramValue) throws IllegalArgumentException {
	boolean isNull = paramValue == null;
	if (!isNull && String.class.isInstance(paramValue)) {
	    String str = (String) paramValue;
	    isNull = str.trim().length() == 0;
	}
	if (isNull) {
	    throw new IllegalArgumentException(paramName + " is required '" + paramValue + "'");
	}
    }

    /**
     * @return integer value of paramValue
     * @exception IllegalArgumentException
     *                - if (a) not set and is not optional or (b) not integer
     */
    public static Integer checkInteger(String paramName, String paramValue, boolean isOptional)
	    throws IllegalArgumentException {
	try {
	    if (!isOptional) {
		WebUtil.checkObject(paramName, paramValue);
	    }
	    String value = paramValue != null ? StringUtils.trimToNull(paramValue) : null;
	    return value != null ? Integer.valueOf(value) : null;

	} catch (NumberFormatException e) {
	    throw new IllegalArgumentException(paramName + " should be an integer '" + paramValue + "'");
	}
    }

    /**
     * @return long value of paramValue
     * @exception IllegalArgumentException
     *                - if (a) not set and is not optional or (b) not long
     */
    public static Long checkLong(String paramName, String paramValue, boolean isOptional)
	    throws IllegalArgumentException {
	try {
	    if (!isOptional) {
		WebUtil.checkObject(paramName, paramValue);
	    }
	    String value = paramValue != null ? StringUtils.trimToNull(paramValue) : null;
	    return value != null ? Long.valueOf(value) : null;

	} catch (NumberFormatException e) {
	    throw new IllegalArgumentException(paramName + " should be a long '" + paramValue + "'");
	}
    }

    /**
     * Get a long version of paramValue, throwing an IllegalArgumentException if isOptional = false and the is value is
     * null
     *
     * @return long value of paramValue
     * @exception IllegalArgumentException
     *                - if not set or not long
     */
    public static long checkLong(String paramName, Long paramValue, boolean isOptional)
	    throws IllegalArgumentException {
	if (!isOptional) {
	    WebUtil.checkObject(paramName, paramValue);
	}
	return paramValue.longValue();
    }

    /**
     * @return boolean value of paramValue
     * @exception IllegalArgumentException
     *                - if not set or not boolean
     */
    public static boolean checkBoolean(String paramName, String paramValue) throws IllegalArgumentException {
	WebUtil.checkObject(paramName, paramValue);
	return Boolean.valueOf(paramValue.trim()).booleanValue();
    }

    /**
     * Read an int parameter, throwing exception if null or not a integer
     *
     * @param req
     *            -
     * @param paramName
     *            -
     * @return parameter value
     */
    public static int readIntParam(HttpServletRequest req, String paramName) {
	return WebUtil.checkInteger(paramName, req.getParameter(paramName), false).intValue();
    }

    /**
     * Read an int parameter, throwing exception if ( not optional and null ) or not a integer
     *
     * @param req
     *            -
     * @param paramName
     *            -
     * @param isOptional
     * @return parameter value
     */
    public static Integer readIntParam(HttpServletRequest req, String paramName, boolean isOptional) {
	return WebUtil.checkInteger(paramName, req.getParameter(paramName), isOptional);
    }

    /**
     * Read an long parameter, throwing exception if null or not a long
     *
     * @param req
     *            -
     * @param paramName
     *            -
     * @return parameter value
     */
    public static long readLongParam(HttpServletRequest req, String paramName) {
	return WebUtil.checkLong(paramName, req.getParameter(paramName), false).longValue();
    }

    /**
     * Read an long parameter, throwing exception if ( not optional and null ) or not a long
     *
     * @param req
     *            -
     * @param paramName
     *            -
     * @param isOptional
     * @return parameter value
     */
    public static Long readLongParam(HttpServletRequest req, String paramName, boolean isOptional) {
	return WebUtil.checkLong(paramName, req.getParameter(paramName), isOptional);
    }

    /**
     * @param req
     *            -
     * @param paramName
     *            -
     * @return parameter value
     */
    public static String readStrParam(HttpServletRequest req, String paramName) {
	return WebUtil.readStrParam(req, paramName, false);
    }

    /**
     * @param req
     *            -
     * @param paramName
     *            -
     * @param isOptional
     * @return parameter value
     */
    public static String readStrParam(HttpServletRequest req, String paramName, boolean isOptional) {
	if (!isOptional) {
	    WebUtil.checkObject(paramName, req.getParameter(paramName));
	}
	return req.getParameter(paramName);
    }

    /**
     * @param req
     *            -
     * @param paramName
     *            -
     * @return parameter value
     * @exception IllegalArgumentException
     *                - if valid boolean parameter value is not found
     */
    public static boolean readBooleanParam(HttpServletRequest req, String paramName) throws IllegalArgumentException {
	return WebUtil.checkBoolean(paramName, req.getParameter(paramName));
    }

    /**
     * @param req
     *            -
     * @param paramName
     *            -
     * @param defaultValue
     *            - if valid boolean parameter value is not found, return this value
     * @return parameter value
     */
    public static boolean readBooleanParam(HttpServletRequest req, String paramName, boolean defaultValue) {
	try {
	    return WebUtil.checkBoolean(paramName, req.getParameter(paramName));
	} catch (IllegalArgumentException e) {
	    return defaultValue;
	}
    }

    public static boolean readBooleanAttr(HttpServletRequest req, String attrName) {
	return WebUtil.checkBoolean(attrName, (String) req.getSession().getAttribute(attrName));
    }

    /**
     * Retrieve the tool access mode from http request. This is a utility used by the tools that share an implementation
     * for the learner screen. They use mode=learner, mode=author and mode=teacher for learning, preview and monitoring
     * respectively. Only used if the tool programmer wants to have one call that supports all three ways of looking at
     * a learner screen.
     *
     * @param request
     * @param param_mode
     * @return the ToolAccessMode object
     */
    public static ToolAccessMode readToolAccessModeParam(HttpServletRequest request, String param_mode,
	    boolean optional) {
	String mode = WebUtil.readStrParam(request, param_mode, optional);
	if (mode == null) {
	    return null;
	} else if (mode.equals(ToolAccessMode.AUTHOR.toString())) {
	    return ToolAccessMode.AUTHOR;
	} else if (mode.equals(ToolAccessMode.LEARNER.toString())) {
	    return ToolAccessMode.LEARNER;
	} else if (mode.equals(ToolAccessMode.TEACHER.toString())) {
	    return ToolAccessMode.TEACHER;
	} else {
	    throw new IllegalArgumentException("[" + mode + "] is not a legal mode" + "in LAMS");
	}
    }

    /**
     * Retrieve the tool access mode from a string value, presumably from a Form. This is a utility used by the tools
     * that share an implementation for the learner screen. They use mode=learner, mode=author and mode=teacher for
     * learning, preview and monitoring respectively. Only used if the tool programmer wants to have one call that
     * supports all three ways of looking at a learner screen.
     *
     * @param request
     * @return the ToolAccessMode object
     */
    public static ToolAccessMode getToolAccessMode(String modeValue) {
	if (modeValue != null) {
	    if (modeValue.equals(ToolAccessMode.AUTHOR.toString())) {
		return ToolAccessMode.AUTHOR;
	    } else if (modeValue.equals(ToolAccessMode.LEARNER.toString())) {
		return ToolAccessMode.LEARNER;
	    } else if (modeValue.equals(ToolAccessMode.TEACHER.toString())) {
		return ToolAccessMode.TEACHER;
	    }
	}
	throw new IllegalArgumentException("[" + modeValue + "] is not a legal mode" + "in LAMS");
    }

    /**
     * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR mode.
     *
     * @param request
     * @return
     */
    public static ToolAccessMode readToolAccessModeAuthorDefaulted(HttpServletRequest request) {
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);

	ToolAccessMode mode;
	if (StringUtils.equalsIgnoreCase(modeStr, ToolAccessMode.TEACHER.toString())) {
	    mode = ToolAccessMode.TEACHER;
	} else {
	    mode = ToolAccessMode.AUTHOR;
	}
	return mode;
    }

    /**
     * Append a parameter to a requested url.
     *
     * @param parameterName
     *            the name of the parameter
     * @param parameterValue
     *            the value of the parameter
     * @param learnerUrl
     *            the target url
     * @return the url with parameter appended.
     */
    public static String appendParameterToURL(String url, String parameterName, String parameterValue) {
	return WebUtil.appendParameterDeliminator(url) + parameterName + "=" + parameterValue;
    }

    /**
     * <p>
     * This helper append the parameter deliminator for a url.
     * </p>
     * It is using a null safe String util method to checkup the url String and append proper deliminator if necessary.
     *
     * @param url
     *            the url needs to append deliminator.
     * @return target url with the deliminator;
     */
    public static String appendParameterDeliminator(String url) {
	if (url == null) {
	    return null;
	} else if (StringUtils.containsNone(url, "?")) {
	    return url + "?";
	} else {
	    return url + "&";
	}
    }

    /**
     * Converts a url (such as one from a tool) to a complete url. If the url starts with "http" then it is assumed to
     * be a complete url and is returned as is. Otherwise it assumes starts with the path of the webapp so it is
     * appended to the server url from the LAMS Configuration.
     *
     * @param url
     *            e.g. tool/lanb11/starter/learner.do
     * @return complete url
     */
    public static String convertToFullURL(String url) {
	if (url == null) {
	    return null;
	} else if (url.startsWith("http")) {
	    return url;
	} else {
	    String serverURL = Configuration.get(ConfigurationKeys.SERVER_URL);
	    if (url.charAt(0) == '/') {
		return serverURL + url;
	    } else {
		return serverURL + '/' + url;
	    }
	}
    }

    /**
     * Convert any newslines in a string to <BR/>
     * . If input = null, returns null.
     */
    public static String convertNewlines(String input) {
	if (input != null) {
	    return input.replaceAll("[\n\r\f]", "<BR/>");
	} else {
	    return null;
	}
    }

    /**
     * Strips HTML tags and leave "pure" text. Useful for CKeditor created text.
     *
     * @param text
     *            string to process
     * @return string after stripping
     */
    public static String removeHTMLtags(String text) {
	return text == null ? null
		: text.replaceAll(WebUtil.SPACE_TAG_REGEX, " ").replaceAll(WebUtil.HTML_TAG_REGEX, "");
    }

    /**
     * Makes a request to the specified url with the specified parameters and returns the response inputstream
     *
     * @param urlStr
     * @param params
     * @return
     * @throws ToolException
     * @throws IOException
     */
    public static InputStream getResponseInputStreamFromExternalServer(String urlStr, HashMap<String, String> params)
	    throws Exception {
	if (!urlStr.contains("?")) {
	    urlStr += "?";
	}

	for (Entry<String, String> entry : params.entrySet()) {
	    urlStr += "&" + entry.getKey() + "=" + entry.getValue();
	}

	WebUtil.log.info("Making request to external servlet: " + urlStr);

	URL url = new URL(urlStr);
	URLConnection conn = url.openConnection();
	if (!(conn instanceof HttpURLConnection)) {
	    WebUtil.log.error("Fail to connect to external server though url:  " + urlStr);
	    throw new Exception("Fail to connect to external server though url:  " + urlStr);
	}

	HttpURLConnection httpConn = (HttpURLConnection) conn;
	if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
	    WebUtil.log.error("Response code from external server:  " + httpConn.getResponseCode() + " Url: " + urlStr);
	}

	InputStream is = url.openConnection().getInputStream();
	if (is == null) {
	    WebUtil.log.error("Fail to fetch data from external server, return InputStream null:  " + urlStr);
	    throw new Exception("Fail to fetch data from external server, return inputStream null:  " + urlStr);
	}

	return is;
    }

    public static String extractParameterValue(String url, String param) {
	if (!StringUtils.isBlank(url) && !StringUtils.isBlank(param)) {
	    int quotationMarkIndex = url.indexOf("?");
	    String queryPart = quotationMarkIndex > -1 ? url.substring(quotationMarkIndex + 1) : url;
	    String[] paramEntries = queryPart.split("&");
	    for (String paramEntry : paramEntries) {
		String[] paramEntrySplitted = paramEntry.split("=");
		if ((paramEntrySplitted.length > 1) && param.equalsIgnoreCase(paramEntrySplitted[0])) {
		    return paramEntrySplitted[1];
		}
	    }
	}
	return null;
    }

    /**
     * Produces JSON object with basic user details.
     */
    public static ObjectNode userToJSON(User user) {
	ObjectNode userJSON = JsonNodeFactory.instance.objectNode();
	userJSON.put("id", user.getUserId());
	userJSON.put("firstName", user.getFirstName());
	userJSON.put("lastName", user.getLastName());
	userJSON.put("login", user.getLogin());
	userJSON.put("portraitId", user.getPortraitUuid());
	return userJSON;
    }

    public static String getBaseServerURL() {
	String serverURL = Configuration.get(ConfigurationKeys.SERVER_URL);
	// "https://" is 8 characters, so next "/" should be context
	return serverURL.substring(0, serverURL.indexOf('/', 9));
    }

    /**
     * Converse lessonId into alphabetic sequence for using it in URL shortening
     *
     * @param lessonId
     * @return
     */
    public static String encodeLessonId(Long lessonId) {
	String encodedLessonId = lessonId.toString();
	encodedLessonId = encodedLessonId.replace('0', URL_SHORTENING_CYPHER.charAt(0));
	encodedLessonId = encodedLessonId.replace('1', URL_SHORTENING_CYPHER.charAt(1));
	encodedLessonId = encodedLessonId.replace('2', URL_SHORTENING_CYPHER.charAt(2));
	encodedLessonId = encodedLessonId.replace('3', URL_SHORTENING_CYPHER.charAt(3));
	encodedLessonId = encodedLessonId.replace('4', URL_SHORTENING_CYPHER.charAt(4));
	encodedLessonId = encodedLessonId.replace('5', URL_SHORTENING_CYPHER.charAt(5));
	encodedLessonId = encodedLessonId.replace('6', URL_SHORTENING_CYPHER.charAt(6));
	encodedLessonId = encodedLessonId.replace('7', URL_SHORTENING_CYPHER.charAt(7));
	encodedLessonId = encodedLessonId.replace('8', URL_SHORTENING_CYPHER.charAt(8));
	encodedLessonId = encodedLessonId.replace('9', URL_SHORTENING_CYPHER.charAt(9));

	return encodedLessonId;
    }

    /**
     * Decodes alphabetic sequence (that is lessonId encoded for URL shortening purposes)
     *
     * @param encodedlessonId
     * @return
     */
    public static String decodeLessonId(String encodedLessonId) throws IllegalArgumentException {

	// it should contain only the characters from URL_SHORTENING_CYPHER
	if (!encodedLessonId.matches("[" + URL_SHORTENING_CYPHER + "]*")) {
	    throw new IllegalArgumentException("LessonId: " + encodedLessonId + " has wrong format.");
	}

	String decodedLessonId = encodedLessonId;
	decodedLessonId = decodedLessonId.replace(URL_SHORTENING_CYPHER.charAt(0), '0');
	decodedLessonId = decodedLessonId.replace(URL_SHORTENING_CYPHER.charAt(1), '1');
	decodedLessonId = decodedLessonId.replace(URL_SHORTENING_CYPHER.charAt(2), '2');
	decodedLessonId = decodedLessonId.replace(URL_SHORTENING_CYPHER.charAt(3), '3');
	decodedLessonId = decodedLessonId.replace(URL_SHORTENING_CYPHER.charAt(4), '4');
	decodedLessonId = decodedLessonId.replace(URL_SHORTENING_CYPHER.charAt(5), '5');
	decodedLessonId = decodedLessonId.replace(URL_SHORTENING_CYPHER.charAt(6), '6');
	decodedLessonId = decodedLessonId.replace(URL_SHORTENING_CYPHER.charAt(7), '7');
	decodedLessonId = decodedLessonId.replace(URL_SHORTENING_CYPHER.charAt(8), '8');
	decodedLessonId = decodedLessonId.replace(URL_SHORTENING_CYPHER.charAt(9), '9');

	return decodedLessonId;
    }

    public static Document getDocument() throws ParserConfigurationException {
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = factory.newDocumentBuilder();
	Document document = builder.newDocument();
	return document;
    }

    public static String getStringFromDocument(Document document) throws TransformerException {
	DOMSource domSource = new DOMSource(document);
	StringWriter writer = new StringWriter();
	StreamResult result = new StreamResult(writer);
	TransformerFactory tf = TransformerFactory.newInstance();
	Transformer transformer = tf.newTransformer();
	transformer.transform(domSource, result);
	return writer.toString();
    }
}