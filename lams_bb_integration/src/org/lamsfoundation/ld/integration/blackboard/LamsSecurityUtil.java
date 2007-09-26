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

import java.lang.IllegalStateException;
import java.net.ConnectException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import blackboard.platform.context.Context;
import org.apache.commons.codec.binary.Hex;
import org.lamsfoundation.ld.integration.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.apache.log4j.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 *  This class creates URLs, servlet calls and webservice calls
 *  for communication with LAMS
 *  
 *  @author <a href="mailto:lfoxton@melcoe.mq.edu.au">Luke Foxton</a>
 */
public class LamsSecurityUtil {
    
    static Logger logger = Logger.getLogger(LamsSecurityUtil.class);
    
    
    /**
     * Generates login requests to LAMS for author, monitor and learner
     * 
     * @param ctx the blackboard contect, contains session data
     * @param method the mehtod to request of LAMS "author", "monitor", "learner" 
     * @return a url pointing to the LAMS lesson, monitor, author session
     * @throws Exception
     */
    public static String generateRequestURL(Context ctx, String method) throws Exception
    {
    	String serverAddr = getServerAddress();
		String serverId = getServerID();
		String reqSrc = getReqSrc();
		
		// If lams.properties could not be read, throw exception
        if(serverAddr == null || serverId == null || reqSrc==null){
            throw new Exception("Configuration Exception " + serverAddr + ", " + serverId);
        }
		
		String timestamp = new Long(System.currentTimeMillis()).toString();
		String username = ctx.getUser().getUserName();
		String hash = generateAuthenticationHash(timestamp, username, method, serverId);
		String courseId = ctx.getCourse().getCourseId();
		
		String locale = ctx.getUser().getLocale();
		String country = getCountry(locale);
		String lang = getLanguage(locale);

		String url;
		try {
			url = serverAddr + "/LoginRequest?" 
				+ "&uid=" 			+ URLEncoder.encode(username, "UTF8") 
				+ "&method="		+ method 
				+ "&ts=" 			+ timestamp  
				+ "&sid=" 			+ serverId  
				+ "&hash=" 			+ hash 
				+ "&courseid=" 		+ URLEncoder.encode(courseId, "UTF8") 
				+ "&country=" 		+ country
				+ "&lang=" 			+ lang
				+ "&requestSrc="	+ URLEncoder.encode(reqSrc, "UTF8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException();
		}
		
		logger.info("LAMS Req: " + url);
		//System.out.println(url);

		return url;
	}
    
    /**
     * Gets a list of learning designs for the current user from LAMS
     * 
     * @param ctx the blackboard contect, contains session data
     * @param mode the mode to call upon learning designes
     * @return a string containing the LAMS workspace tree in tigra format
     * @throws Exception
     */
    public static String getLearningDesigns(Context ctx, Integer mode) throws Exception
    {
		String serverAddr = getServerAddress();
		String serverId = getServerID();
		String serverKey = getServerKey();
		
		// If lams.properties could not be read, throw exception
        if(serverAddr == null || serverId == null || serverKey==null){
            throw new Exception("Configuration Exception " + serverAddr + ", " + serverId);
        }
		
		String timestamp = new Long(System.currentTimeMillis()).toString();
		String username = ctx.getUser().getUserName();
		String hash = generateAuthenticationHash(timestamp, username, serverId);
		String courseId = ctx.getCourse().getCourseId();

		String locale = ctx.getUser().getLocale();
		String country = getCountry(locale);
		String lang = getLanguage(locale);

		// TODO: Make locale settings work
		String learningDesigns = "[]"; // empty javascript array
		try {
			String serviceURL = serverAddr + "/services/xml/LearningDesignRepository?" 
				+ "datetime=" 	+ timestamp
				+ "&username="	+ URLEncoder.encode(username, "utf8")
				+ "&serverId=" 	+ URLEncoder.encode(serverId, "utf8")
				+ "&hashValue=" + hash
				+ "&courseId=" 	+ URLEncoder.encode(courseId, "UTF8")
				+ "&country="	+ country
				+ "&lang=" 		+ lang
				+ "&mode="		+ mode;	
			
			URL url = new URL(serviceURL);
			URLConnection conn = url.openConnection();
			if (!(conn instanceof HttpURLConnection)) {
				logger.error("Unable to open connection to: " + serviceURL); 
			}
			
			HttpURLConnection httpConn = (HttpURLConnection)conn;
			
			
			if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				logger.error("HTTP Response Code: " + httpConn.getResponseCode() 
						+ ", HTTP Response Message: " + httpConn.getResponseMessage());
				return "error";
			}
			
			
			//InputStream is = url.openConnection().getInputStream();
			InputStream is = conn.getInputStream();
			
			
			// parse xml response
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(is);			
			
			learningDesigns = "[" + convertToTigraFormat(document.getDocumentElement()) + "]";
			
			// replace sequence id with javascript method
			String pattern = "'(\\d+)'";
			String replacement = "'javascript:selectSequence($1)'";
			learningDesigns = learningDesigns.replaceAll(pattern, replacement);	
			
			// TODO better error handling
		} catch (MalformedURLException e) {
			logger.error("Unable to get LAMS learning designs, bad URL: '"
					+ serverAddr
					+ "', please check lams.properties", e);
			e.printStackTrace();
			return "error";
		}catch (IllegalStateException e){
			logger.error("LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator", e);
			e.printStackTrace();
			return "error";
		}catch (ConnectException e){
			logger.error("LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator", e);
			e.printStackTrace();
			return "error";
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
			e.printStackTrace();
			return "error";
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
			return "error";
		} catch (ParserConfigurationException e) {
			logger.error(e);
			e.printStackTrace();
			return "error";
		} catch (SAXException e) {
			logger.error(e);
			e.printStackTrace();
			return "error";
		}
		return learningDesigns;
    }
    
    /**
     * Starts lessons in lams through a LAMS webservice
     * 
     * @param ctx the blackboard contect, contains session data
     * @param ldId the learning design id for which you wish to start a lesson
     * @param title the title of the lesson
     * @param desc the description of the lesson
     * @return the learning session id
     */
    public static Long startLesson(Context ctx, long ldId, String title, String desc) 
    {
		Long error = new Long(-1);
    	String serverId = getServerID();
		String serverAddr = getServerAddress();
		String serverKey = getServerKey();
		String courseId = ctx.getCourse().getCourseId();
		String username = ctx.getUser().getUserName();
		String locale = ctx.getUser().getLocale();
		String country = getCountry(locale);
		String lang = getLanguage(locale);
		
		if (serverId == null || serverAddr == null || serverKey == null ) {
			logger.error("Unable to retrieve learning designs from LAMS, one or more lams configuration properties is null");
			return null;
		}
		
		try {
			
			String timestamp = new Long(System.currentTimeMillis()).toString();
			String hash = generateAuthenticationHash(timestamp, username, serverId);
		
			// (serverId, datetime, hashValue, username, ldId, courseId, title, desc, country, lang)

			String serviceURL = serverAddr + "/services/xml/LessonManager?" 
			+ "&serverId=" 	+ URLEncoder.encode(serverId, "utf8")
			+ "&datetime=" 	+ timestamp
			+ "&username="	+ URLEncoder.encode(username, "utf8")
			+ "&hashValue=" + hash
			+ "&courseId=" 	+ URLEncoder.encode(courseId, "utf8")
			+ "&ldId="		+ new Long(ldId).toString()
			+ "&country="	+ country
			+ "&lang=" 		+ lang
			+ "&method="	+ "start"
			+ "&title="		+ URLEncoder.encode(title, "utf8").trim()
			+ "&desc="		+ URLEncoder.encode(desc, "utf8").trim();
			
			
			logger.info("LAMS START LESSON Req: " + serviceURL);
			//System.out.println("START LESSON: " + serviceURL);
			
			URL url = new URL(serviceURL);
			URLConnection conn = url.openConnection();
			if (!(conn instanceof HttpURLConnection)) {
				logger.error("Unable to open connection to: " + serviceURL); 
			}
			
			HttpURLConnection httpConn = (HttpURLConnection)conn;
			
			if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				logger.error("HTTP Response Code: " + httpConn.getResponseCode() 
						+ ", HTTP Response Message: " + httpConn.getResponseMessage());
				return error;
			}
			
			//InputStream is = url.openConnection().getInputStream();
			InputStream is = conn.getInputStream();
			
			
			// parse xml response
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(is);
			
			// get the lesson id from the response
			
			/*
			 * The getTextContext is not a java 1.4 method, so Blackboard 7.1 comes up with errors
			 * using getNodeValue() instead
			 */
			//return Long.parseLong(document.getElementsByTagName("Lesson").item(0).getAttributes().getNamedItem("lessonId").getTextContent());
			return Long.parseLong(document.getElementsByTagName("Lesson").item(0).getAttributes().getNamedItem("lessonId").getNodeValue());
		} catch (MalformedURLException e) {
			logger.error("Unable to start LAMS lesson, bad URL: '"
					+ serverAddr
					+ "', please check lams.properties", e);
			e.printStackTrace();
			return error;
		}catch (IllegalStateException e){
			logger.error("LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator", e);
			e.printStackTrace();
			return error;
		} catch (RemoteException e) {
			logger.error("Unable to start LAMS lesson, RMI Remote Exception",e);
			e.printStackTrace();
			return error;
		} catch (UnsupportedEncodingException e)
		{
			logger.error("Unable to start LAMS lesson, Unsupported Encoding Exception",e);
			e.printStackTrace();
			return error;
		}
		catch (ConnectException e)
		{
			logger.error("LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator", e);
			e.printStackTrace();
			return error;
		}
		catch (Exception e) {
			logger.error("Unable to start LAMS lesson. Please contact your system administrator.",e);
			e.printStackTrace();
			return error;
		} 
		
	}
    
    /**
     * @return gets server address from the lams.properties file
     */
    public static String getServerAddress()
    {
    	return LamsPluginUtil.getProperties().getProperty(LamsPluginUtil.PROP_LAMS_URL);
    }
    
    /**
     * @return gets server id from the lams.properties file
     */
    public static String getServerID()
    {
    	return LamsPluginUtil.getProperties().getProperty(LamsPluginUtil.PROP_LAMS_SERVER_ID);
    }
    
    /**
     * @return gets server key from the lams.properties file
     */
    public static String getServerKey()
    {
    	return LamsPluginUtil.getProperties().getProperty(LamsPluginUtil.PROP_LAMS_SECRET_KEY);
    }
    
    /**
     * @return gets request source from the lams.properties file
     */
    public static String getReqSrc()
    {
    	return LamsPluginUtil.getProperties().getProperty(LamsPluginUtil.PROP_REQ_SRC);
    }
    
    
    
    /**
     * 
     * @param node the node from which to do the recursive conversion
     * @return the string converted to tigra format
     */
    public static String convertToTigraFormat(Node node) {

		StringBuilder sb = new StringBuilder();

		if (node.getNodeName().equals(Constants.ELEM_FOLDER)) {
			sb.append("['");
			
			StringBuilder attribute= new StringBuilder(node.getAttributes().getNamedItem(
					Constants.ATTR_NAME).getNodeValue().replace("'", "\\'"));
			
			sb.append(attribute.append("',").append("null").append(','));
					
			NodeList children = node.getChildNodes();
			if (children.getLength() == 0) {
				sb.append("['',null]");
			} else {
				sb.append(convertToTigraFormat(children.item(0)));
				for (int i = 1; i < children.getLength(); i++) {
					sb.append(',').append(
							convertToTigraFormat(children.item(i)));
				}
			}
			sb.append(']');
		} else if (node.getNodeName().equals(
				Constants.ELEM_LEARNING_DESIGN)) {
			sb.append('[');
			
			StringBuilder attrName = new StringBuilder(node.getAttributes().getNamedItem(
							Constants.ATTR_NAME).getNodeValue().replace("'", "\\'"));
			StringBuilder attrResId = new StringBuilder(node.getAttributes().getNamedItem(
					Constants.ATTR_RESOURCE_ID).getNodeValue().replace("'", "\\'"));			
			
			sb.append('\'')
				.append(attrName
				.append('\'')
				.append(',')
				.append('\'')
				.append(attrResId.append('\'')));

			sb.append(']');
		}
		return sb.toString();
	}

    //generate authentication hash code to validate parameters
    public static String generateAuthenticationHash(String datetime, String login, String method, String serverId) {
        String secretkey = LamsPluginUtil.getSecretKey();
                
        String plaintext = datetime.toLowerCase().trim() +
                           login.toLowerCase().trim() +
                           method.toLowerCase().trim() +
                           serverId.toLowerCase().trim() + 
                           secretkey.toLowerCase().trim();
        
        String hash = sha1(plaintext);    
        return hash;
    }
    
    
    //generate authentication hash code to validate parameters

    public static String generateAuthenticationHash(String datetime, String login, String serverId) {
        String secretkey = getServerKey();
                
        String plaintext = datetime.toLowerCase().trim() +
                           login.toLowerCase().trim() +
                           serverId.toLowerCase().trim() + 
                           secretkey.toLowerCase().trim();
        
        String hash = sha1(plaintext);  
        
        return hash;
    }
    
    //generate authentication hash code to validate parameters
    public static String generateAuthenticationHash(String datetime, String serverId) 
    	throws NoSuchAlgorithmException {
        String secretkey = LamsPluginUtil.getSecretKey();
        
        String plaintext = datetime.toLowerCase().trim() +
                           serverId.toLowerCase().trim() + 
                           secretkey.toLowerCase().trim() ;
        
        return sha1(plaintext);
    }
    
       
    /**
     * The parameters are:
     * uid - the username on the external system
     * method -  either author, monitor or learner
     * ts - timestamp
     * sid - serverID 
     * str is [ts + uid + method + serverID + serverKey]  (Note: all lower case)
     * 
     * @param str The string to be hashed
     * @return The hased string
     */
	private static String sha1(String str){
	    try{
			MessageDigest md = MessageDigest.getInstance("SHA1");
			return new String(Hex.encodeHex(md.digest(str.getBytes())));
	    } catch(NoSuchAlgorithmException e){
	        throw new RuntimeException(e);
	    }
	}
	
	/**
	 * 
	 * @param localeStr the full balckboard locale string
	 * @return the language 
	 */
	public static String getLanguage(String localeStr)
	{
		if (localeStr==null) 
			return "xx";
		String[] split = localeStr.split("_");
		return split[0];
	}
	
	/**
	 * 
	 * @param localeStr the full balckboard locale string
	 * @return the country 
	 */
	public static String getCountry(String localeStr)
	{
		if (localeStr==null) 
			return "XX";
		String[] split = localeStr.split("_");
		return split[1];
	}
	
	
	
}
