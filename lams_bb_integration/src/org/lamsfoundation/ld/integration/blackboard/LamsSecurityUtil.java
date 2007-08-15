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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Locale;
import javax.xml.rpc.ServiceException;
import blackboard.platform.context.Context;
import org.apache.commons.codec.binary.Hex;
import javax.servlet.ServletException;

//import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.ld.integration.Constants;
import org.lamsfoundation.ld.webservice.LessonManager;
import org.lamsfoundation.ld.webservice.LessonManagerService;
import org.lamsfoundation.ld.webservice.LessonManagerServiceLocator;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.apache.log4j.Logger;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 *  @author <a href="mailto:lfoxton@melcoe.mq.edu.au">Luke Foxton</a>
 */
public class LamsSecurityUtil {
    
    //private static Connection _conn = null;
    static Logger logger = Logger.getLogger(LamsSecurityUtil.class);
    
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
		String country = "US";
		String lang = "en";
		
		
		//ResourceLoader rl = new ResourceLoader();
		//String[] locale = ctx.getGuestSessionLocale().split("-");

		//String siteID = getCurrentContext();
		
		// TODO: Make locale settings work

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
		
		System.out.println(url);

		return url;
	}
    
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
		String country = "US";
		String lang = "en";

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
			}
			
			InputStream is = url.openConnection().getInputStream();
			
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
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		return learningDesigns;
	}
    
    
    
    
    
    
    /* Scheduling is handled by blackboard
    public static Long scheduleLesson(Context ctx, long ldId, String title, String desc, String startDate) {
		
		
		String serverID = getServerID();
		String serverAddress = getServerAddress();
		String serverKey = getServerKey();
		String courseId = ctx.getCourse().getCourseId();
		String username = ctx.getUser().getUserName();
		String siteId = getReqSrc();
		
		if (serverID == null || serverAddress == null || serverKey == null ) {
			logger.error("Unable to retrieve learning designs from LAMS, one or more lams configuration properties is null");
			return null;
		}
		
		try {

			String datetime = new Date().toString();
			String hashValue = generateAuthenticationHash(serverID, serverKey, username, datetime);
	
			LessonManagerService service = new LessonManagerServiceLocator();
			LessonManager lessonManager = service.getLessonManagerService(new URL(serverAddress + "/services/LessonManagerService"));
			Long lessonId = lessonManager.scheduleLesson(serverID, datetime, hashValue, username, ldId, siteId, title, desc, startDate, "US", "en"); 
						
			return lessonId;
			
		} catch (MalformedURLException e) {
			logger.error("Unable to schedule LAMS lesson, bad URL: '"
					+ serverAddress
					+ "', please check sakai.properties");
		} catch (ServiceException e) {
			logger.error("Unable to schedule LAMS lesson, RPC Service Exception");
			e.printStackTrace();
		} catch (RemoteException e) {
			logger.error("Unable to schedule LAMS lesson, RMI Remote Exception");
			e.printStackTrace();
		}
		return null;
	}
	*/
    
    
    /*
	String serverId 	= request.getParameter(CentralConstants.PARAM_SERVER_ID);
	String datetime 	= request.getParameter(CentralConstants.PARAM_DATE_TIME);
	String hashValue 	= request.getParameter(CentralConstants.PARAM_HASH_VALUE);
	String username  	= request.getParameter(CentralConstants.PARAM_USERNAME);
	String courseId 	= request.getParameter(CentralConstants.PARAM_COURSE_ID);
	String ldIdStr 		= request.getParameter(CentralConstants.PARAM_LEARNING_DESIGN_ID);
	String lsIdStr 		= request.getParameter(CentralConstants.PARAM_LESSON_ID);
	String country 		= request.getParameter(CentralConstants.PARAM_COUNTRY);
	String title 		= request.getParameter(CentralConstants.PARAM_TITLE);
	String desc 		= request.getParameter(CentralConstants.PARAM_DESC);
	String startDate 	= request.getParameter(CentralConstants.PARAM_STARTDATE);
	String lang 		= request.getParameter(CentralConstants.PARAM_LANG);
	String method 		= request.getParameter(CentralConstants.PARAM_METHOD);
 
 										serverId, 
 										datetime, 
 										hashValue,
										username, 
										ldId, 
										courseId, 
										title, 
										desc, 
										country, 
										lang
 	
 	/lams//services/xml/LessonManager?serverId=lamsbb
 	 									&datetime=1186617857718
 	 									&username=administrator
 	 									&hashValue=696e870fde732c1e14a6f79a2872f6eaff9be349
 	 									&courseId=1
 	 									&ldId=5
 	 									&country=US
 	 									&lang=en
 	 									&method=start
 	 									&title=test
 	 									&desc=+%0D%0A
 */
    
    public static Long startLesson(Context ctx, long ldId, String title, String desc) 
    {
		String serverId = getServerID();
		String serverAddr = getServerAddress();
		String serverKey = getServerKey();
		String courseId = ctx.getCourse().getCourseId();
		String username = ctx.getUser().getUserName();
		
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
			+ "&country="	+ "US"
			+ "&lang=" 		+ "en"
			+ "&method="	+ "start"
			+ "&title="		+ URLEncoder.encode(title, "utf8").trim()
			+ "&desc="		+ URLEncoder.encode(desc, "utf8").trim();
			
			System.out.println("START LESSON: " + serviceURL);
			
			URL url = new URL(serviceURL);
			URLConnection conn = url.openConnection();
			if (!(conn instanceof HttpURLConnection)) {
				logger.error("Unable to open connection to: " + serviceURL); 
			}
			
			HttpURLConnection httpConn = (HttpURLConnection)conn;
			
			if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				logger.error("HTTP Response Code: " + httpConn.getResponseCode() 
						+ ", HTTP Response Message: " + httpConn.getResponseMessage());
			}
			
			InputStream is = url.openConnection().getInputStream();
			
			// parse xml response
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(is);
			
			// get the lesson id from the response
			return Long.parseLong(document.getElementsByTagName("Lesson").item(0).getAttributes().getNamedItem("lessonId").getTextContent());
		
			

		} catch (MalformedURLException e) {
			logger.error("Unable to start LAMS lesson, bad URL: '"
					+ serverAddr
					+ "', please check lams.properties");
			e.printStackTrace();
		} catch (RemoteException e) {
			logger.error("Unable to start LAMS lesson, RMI Remote Exception");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e)
		{
			logger.error("Unable to start LAMS lesson, Unsupported Encoding Exception");
			e.printStackTrace();
		}
		catch (Exception e)
		{
			logger.error("Unable to start LAMS lesson");
			e.printStackTrace();
		}
		
		return null;
	}
    
    
    /*
     * TODO: 
     */
    public static boolean deleteLesson(Context ctx, long lsId) {
    	String serverId = getServerID();
		String serverAddr = getServerAddress();
		String serverKey = getServerKey();
		String username = ctx.getUser().getUserName();
		if (serverId == null || serverAddr == null || serverKey == null ) {
			logger.error("Unable to retrieve learning designs from LAMS, one or more lams configuration properties is null");
			return false;
		}
		
		try {
			
			String timestamp = new Long(System.currentTimeMillis()).toString();
			String hash = generateAuthenticationHash(serverId, serverKey, username, timestamp);
		
			// Boolean deleted = deleteLesson(serverId, datetime, hashValue, username, lsId);
			
			
			String serviceURL = serverAddr + "/services/xml/LessonManager?" 
			+ "datetime=" 	+ timestamp
			+ "&username="	+ URLEncoder.encode(username, "utf8")
			+ "&serverId=" 	+ URLEncoder.encode(serverId, "utf8")
			+ "&serverKey="	+ URLEncoder.encode(serverKey, "utf8")
			+ "&hashValue=" + hash
			+ "&lsId="		+ new Long(lsId).toString()
			+ "&method="	+ "delete";
			
			URL url = new URL(serviceURL);
			URLConnection conn = url.openConnection();
			if (!(conn instanceof HttpURLConnection)) {
				logger.error("Unable to open connection to: " + serviceURL); 
			}
			
			HttpURLConnection httpConn = (HttpURLConnection)conn;
			
			if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				logger.error("HTTP Response Code: " + httpConn.getResponseCode() 
						+ ", HTTP Response Message: " + httpConn.getResponseMessage());
			}
			
			InputStream is = url.openConnection().getInputStream();
			
			// parse xml response
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(is);
			
			return Boolean.parseBoolean(document.getElementById("lesson").getAttribute("deleted"));
		
		} catch (MalformedURLException e) {
			logger.error("Unable to delete LAMS lesson, bad URL: '"
					+ serverAddr
					+ "', please check lams.properties");
		} catch (RemoteException e) {
			logger.error("Unable to delete LAMS lesson, RMI Remote Exception");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e)
		{
			logger.error("Unable to delet LAMS lesson, Unsupported Encoding Exception");
			e.printStackTrace();
		} catch (Exception e)
		{
			logger.error("Unable to delete LAMS lesson");
			e.printStackTrace();
		}
		return false;
	}


    public static String getServerAddress()
    {
    	return LamsPluginUtil.getProperties().getProperty(LamsPluginUtil.PROP_LAMS_URL);
    }
    
    public static String getServerID()
    {
    	return LamsPluginUtil.getProperties().getProperty(LamsPluginUtil.PROP_LAMS_SERVER_ID);
    }
    
    public static String getServerKey()
    {
    	return LamsPluginUtil.getProperties().getProperty(LamsPluginUtil.PROP_LAMS_SECRET_KEY);
    }
    
    public static String getReqSrc()
    {
    	return LamsPluginUtil.getProperties().getProperty(LamsPluginUtil.PROP_REQ_SRC);
    }
    
    
    
    
    public static String convertToTigraFormat(Node node) {

		StringBuilder sb = new StringBuilder();

		if (node.getNodeName().equals(Constants.ELEM_FOLDER)) {
			sb.append("['");
			sb.append(
					node.getAttributes().getNamedItem(
							Constants.ATTR_NAME).getNodeValue()).append(
					"',").append("null").append(',');

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
			sb.append('\'').append(
					node.getAttributes().getNamedItem(
							Constants.ATTR_NAME).getNodeValue()).append(
					'\'').append(',').append('\'').append(
					node.getAttributes().getNamedItem(
							Constants.ATTR_RESOURCE_ID).getNodeValue())
					.append('\'');
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
        
        System.out.println(plaintext);
        String hash = sha1(plaintext);  
        
        return hash;
    }
    
    
    //generate authentication hash code to validate parameters
    public static String generateAuthenticationHash(String datetime, String login, String serverId) {
        String secretkey = getServerKey();
                
        String plaintext = datetime.toLowerCase().trim() +
                           login.toLowerCase().trim() +
                           serverId.toLowerCase().trim() + 
                           secretkey;
        
        System.out.println(plaintext);
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
	
}
