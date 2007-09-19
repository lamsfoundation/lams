/******************************************************************************
 * LamstwoLogicImpl.java - created by Sakai App Builder -AZ
 * 
 * Copyright (c) 2007 LAMS Foundation
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

package org.lamsfoundation.lams.integrations.sakai.logic.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lamsfoundation.lams.integrations.sakai.dao.LamstwoDao;
import org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic;
import org.lamsfoundation.lams.integrations.sakai.model.LamstwoItem;
import org.lamsfoundation.lams.webservice.LessonManager;
import org.lamsfoundation.lams.webservice.LessonManagerService;
import org.lamsfoundation.lams.webservice.LessonManagerServiceLocator;
import org.lamsfoundation.ld.integration.Constants;
import org.sakaiproject.authz.api.AuthzGroup;
import org.sakaiproject.authz.api.AuthzGroupService;
import org.sakaiproject.authz.api.AuthzPermissionException;
import org.sakaiproject.authz.api.FunctionManager;
import org.sakaiproject.authz.api.GroupNotDefinedException;
import org.sakaiproject.authz.api.Role;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.thread_local.cover.ThreadLocalManager;
import org.sakaiproject.tool.api.Session;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.util.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This is the implementation of the business logic interface
 * @author Sakai App Builder -AZ
 */
public class LamstwoLogicImpl implements LamstwoLogic {

	private static Log log = LogFactory.getLog(LamstwoLogicImpl.class);
	
	public final static String ITEM_CREATE = "lamstwo.create";
	public final static String ITEM_WRITE_ANY = "lamstwo.write.any";
	public final static String ITEM_READ_HIDDEN = "lamstwo.read.hidden";

	private final static String SITE_TEMPLATE = "!site.template";
	private final static String COURSE_TEMPLATE = "!site.template.course";

	private LamstwoDao dao;
	public void setDao(LamstwoDao dao) {
		this.dao = dao;
	}

	private AuthzGroupService authzGroupService;
	public void setAuthzGroupService(AuthzGroupService authzGroupService) {
		this.authzGroupService = authzGroupService;
	}

	private FunctionManager functionManager;
	public void setFunctionManager(FunctionManager functionManager) {
		this.functionManager = functionManager;
	}

	private SecurityService securityService;
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	private SessionManager sessionManager;
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	private SiteService siteService;
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	private ToolManager toolManager;
	public void setToolManager(ToolManager toolManager) {
		this.toolManager = toolManager;
	}

	private UserDirectoryService userDirectoryService;
	public void setUserDirectoryService(UserDirectoryService userDirectoryService) {
		this.userDirectoryService = userDirectoryService;
	}

	/**
	 * Place any code that should run when this class is initialized by spring here
	 */
	public void init() {
		log.debug("init");
		// register Sakai permissions for this tool
		functionManager.registerFunction(ITEM_WRITE_ANY);
		functionManager.registerFunction(ITEM_READ_HIDDEN);
		functionManager.registerFunction(ITEM_CREATE);

		// add the permissions to the maintain role of new sites by default
		try {
			// setup the admin user so we can do the stuff below
			Session s = sessionManager.getCurrentSession();
			if (s != null) {
				s.setUserId("admin");
			} else {
				log.warn("no CurrentSession, cannot set to admin user");
			}
			AuthzGroup ag = authzGroupService.getAuthzGroup(SITE_TEMPLATE);
			if (authzGroupService.allowUpdate(ag.getId())) {
				Role r = ag.getRole(ag.getMaintainRole());
				r.allowFunction(ITEM_READ_HIDDEN);
				r.allowFunction(ITEM_WRITE_ANY);
				r.allowFunction(ITEM_CREATE);
				authzGroupService.save(ag);
				log.info("Added Permissions to group:" + SITE_TEMPLATE);
			} else {
				log.warn("Cannot update authz group: " + SITE_TEMPLATE);
			}
			ag = authzGroupService.getAuthzGroup(COURSE_TEMPLATE);
			if (authzGroupService.allowUpdate(ag.getId())) {
				Role r = ag.getRole(ag.getMaintainRole());
				r.allowFunction(ITEM_READ_HIDDEN);
				r.allowFunction(ITEM_WRITE_ANY);
				r.allowFunction(ITEM_CREATE);
				authzGroupService.save(ag);
				log.info("Added Permissions to group:" + COURSE_TEMPLATE);
			} else {
				log.warn("Cannot update authz group: " + COURSE_TEMPLATE);
			}
		} catch (GroupNotDefinedException e) {
			log.error("Could not find group: " + SITE_TEMPLATE + ", default perms will not be assigned");
		} catch (AuthzPermissionException e) {
			log.error("Could not save group: " + SITE_TEMPLATE);
		} finally {
			// wipe out the admin session
			ThreadLocalManager.clear();
		}
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#getItemById(java.lang.Long)
	 */
	public LamstwoItem getItemById(Long id) {
		log.debug("Getting item by id: " + id);
		return (LamstwoItem) dao.findById(LamstwoItem.class, id);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#canWriteItem(org.lamsfoundation.lams.integrations.sakai.model.LamstwoItem)
	 */
	public boolean canWriteItem(LamstwoItem item) {
		return canWriteItem(item, getCurrentContext(), getCurrentUserId() );
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#canWriteItem(org.lamsfoundation.lams.integrations.sakai.model.LamstwoItem, java.lang.String, java.lang.String)
	 */
	public boolean canWriteItem(LamstwoItem item, String siteId, String userId) {
		log.debug("checking if can write for: " + userId + ", " + siteId + ": and item=" + item.getTitle() );
		String siteRef = siteService.siteReference(siteId);
		if (item.getOwnerId().equals( userId ) ) {
			// owner can always modify an item
			return true;
		} else if ( securityService.isSuperUser(userId) ) {
			// the system super user can modify any item
			return true;
		} else if ( siteId.equals(item.getSiteId()) &&
				securityService.unlock(userId, ITEM_WRITE_ANY, siteRef) ) {
			// users with permission in the specified site can modify items from that site
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#getAllVisibleItems()
	 */
	public List getAllVisibleItems() {
		return getAllVisibleItems( getCurrentContext(), getCurrentUserId() );
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#getAllVisibleItems(java.lang.String, java.lang.String)
	 */
	public List getAllVisibleItems(String siteId, String userId) {
		log.debug("Fetching visible items for " + userId + " in site: " + siteId);
		String siteRef = siteService.siteReference( siteId );
		List l = dao.findByProperties(LamstwoItem.class, 
				new String[] {"siteId"}, new Object[] {siteId});
		// check if the current user can see all items (or is super user)
		if ( securityService.isSuperUser(userId) || 
				securityService.unlock(userId, ITEM_READ_HIDDEN, siteRef) ) {
			log.debug("Security override: " + userId + " able to view all items");
		} else {
			// go backwards through the loop to avoid hitting the "end" early
			for (int i=l.size()-1; i >= 0; i--) {
				LamstwoItem item = (LamstwoItem) l.get(i);
				if ( item.getHidden().booleanValue() &&
						!item.getOwnerId().equals(userId) ) {
					l.remove(item);
				}
			}
		}
		return l;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#removeItem(org.lamsfoundation.lams.integrations.sakai.model.LamstwoItem)
	 */
	public void removeItem(LamstwoItem item) {
		log.debug("In removeItem with item:" + item.getId() + ":" + item.getTitle());
		// check if current user can remove this item
		if ( canWriteItem(item) ) {
			dao.delete(item);
			log.info("Removing item: " + item.getId() + ":" + item.getTitle());
		} else {
			throw new SecurityException("Current user cannot remove item " + 
					item.getId() + " because they do not have permission");
		}
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#saveItem(org.lamsfoundation.lams.integrations.sakai.model.LamstwoItem)
	 */
	public void saveItem(LamstwoItem item) {
		log.debug("In saveItem with item:" + item.getTitle());
		// set the owner and site to current if they are not set
		if (item.getOwnerId() == null) {
			item.setOwnerId( getCurrentUserId() );
		}
		if (item.getSiteId() == null) {
			item.setSiteId( getCurrentContext() );
		}
		if (item.getDateCreated() == null) {
			item.setDateCreated( new Date() );
		}
		// save item if new OR check if the current user can update the existing item
		if ( (item.getId() == null) || canWriteItem(item) ) {
			dao.save(item);
			log.info("Saving item: " + item.getId() + ":" + item.getTitle());
		} else {
			throw new SecurityException("Current user cannot update item " + 
					item.getId() + " because they do not have permission");
		}
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#getCurrentUserDisplayName()
	 */
	public String getCurrentUserDisplayName() {
		return userDirectoryService.getCurrentUser().getDisplayName();
	}

	/**
	 * Get the context of the location the current user is in
	 * <br/>Note: This is only public so it can be tested and should not be used outside the impl
	 * @return a context
	 */
	public String getCurrentContext() {
		return toolManager.getCurrentPlacement().getContext();
	}

	/**
	 * Get the internal Sakai userId of the current user
	 * <br/>Note: This is only public so it can be tested and should not be used outside the impl
	 * @return a userId (not necessarily username)
	 */
	public String getCurrentUserId() {
		return userDirectoryService.getCurrentUser().getId();
	}
	
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#deleteLesson()
	 */
	public boolean deleteLesson(long lsId) {
		String serverID = getServerID();
		String serverAddress = getServerAddress();
		String serverKey = getServerKey();
		
		String username = getCurrentUserId();
		
		if (serverID == null || serverAddress == null || serverKey == null ) {
			log.error("Unable to delete LAMS lesson: lsid = " + lsId + ", one or more lams configuration properties is null");
			return false;
		}
		
		try {
			String datetime = new Date().toString();
			String hashValue = LamstwoUtils.generateAuthenticationHash(serverID, serverKey, username, datetime);
			LessonManagerService service = new LessonManagerServiceLocator();
			LessonManager lessonManager = service.getLessonManagerService(new URL(serverAddress + "/services/LessonManagerService"));
			lessonManager.deleteLesson(serverID, datetime, hashValue, username, lsId);
						
			return true;
			
		} catch (MalformedURLException e) {
			log.error("Unable to delete LAMS lesson, lsid = " + lsId + ": ', bad URL: '"
					+ serverAddress
					+ "', please check sakai.properties");
		} catch (ServiceException e) {
			log.error("Unable to start LAMS lesson, RPC Service Exception");
			e.printStackTrace();
		} catch (RemoteException e) {
			log.error("Unable to start LAMS lesson, RMI Remote Exception");
			e.printStackTrace();
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#getLearningDesigns()
	 */
	public String getLearningDesigns(Integer mode) {
		String serverAddress = getServerAddress();
		String serverID = getServerID();
		String serverKey = getServerKey();
		String datetime = new Long(System.currentTimeMillis()).toString();
		String username = getCurrentUserId();
		String hashValue = LamstwoUtils.generateAuthenticationHash(serverID, serverKey, username, datetime);
		String courseId = getCurrentContext();
		String country = getCountryCode();
		String lang = getLanguageCode();

		String learningDesigns = "[]"; // empty javascript array
		try {
			String serviceURL = serverAddress + "/services/xml/timestamp?" 
				+ "datetime=" 	+ datetime
				+ "&username="	+ URLEncoder.encode(username, "utf8")
				+ "&serverId=" 	+ URLEncoder.encode(serverID, "utf8")
				+ "&serverKey="	+ URLEncoder.encode(serverKey, "utf8")
				+ "&hashValue=" + hashValue
				+ "&courseId=" 	+ courseId
				+ "&country="	+ country
				+ "&lang=" 		+ lang
				+ "&mode="		+ mode;	
			
			URL url = new URL(serviceURL);
			URLConnection conn = url.openConnection();
			if (!(conn instanceof HttpURLConnection)) {
				log.error("Unable to open connection to: " + serviceURL); 
			}
			
			HttpURLConnection httpConn = (HttpURLConnection)conn;
			
			if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				log.error("HTTP Response Code: " + httpConn.getResponseCode() 
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

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#scheduleLesson()
	 */
	public Long scheduleLesson(long ldId, String title, String desc, String startDate) {
		
		
		String serverID = getServerID();
		String serverAddress = getServerAddress();
		String serverKey = getServerKey();
		
		String username = getCurrentUserId();
		String siteId = getCurrentContext();
		
		if (serverID == null || serverAddress == null || serverKey == null ) {
			log.error("Unable to retrieve learning designs from LAMS, one or more lams configuration properties is null");
			return null;
		}
		
		try {
			
			String datetime = new Date().toString();
			String hashValue = LamstwoUtils.generateAuthenticationHash(serverID, serverKey, username, datetime);
	
			LessonManagerService service = new LessonManagerServiceLocator();
			LessonManager lessonManager = service.getLessonManagerService(new URL(serverAddress + "/services/LessonManagerService"));
			Long lessonId = lessonManager.scheduleLesson(serverID, datetime, hashValue, username, ldId, siteId, title, desc, startDate, getCountryCode(), getLanguageCode()); 
						
			return lessonId;
			
		} catch (MalformedURLException e) {
			log.error("Unable to schedule LAMS lesson, bad URL: '"
					+ serverAddress
					+ "', please check sakai.properties");
		} catch (ServiceException e) {
			log.error("Unable to schedule LAMS lesson, RPC Service Exception");
			e.printStackTrace();
		} catch (RemoteException e) {
			log.error("Unable to schedule LAMS lesson, RMI Remote Exception");
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#startLesson()
	 */
	public Long startLesson(long ldId, String title, String desc) {

		String serverID = getServerID();
		String serverAddress = getServerAddress();
		String serverKey = getServerKey();
		
		String username = getCurrentUserId();
		String siteId = getCurrentContext();
		
		if (serverID == null || serverAddress == null || serverKey == null ) {
			log.error("Unable to retrieve learning designs from LAMS, one or more lams configuration properties is null");
			return null;
		}
		
		try {
			
			String datetime = new Date().toString();
			String hashValue = LamstwoUtils.generateAuthenticationHash(serverID, serverKey, username, datetime);
		
			LessonManagerService service = new LessonManagerServiceLocator();
			LessonManager lessonManager = service.getLessonManagerService(new URL(serverAddress + "/services/LessonManagerService"));
			Long lessonId = lessonManager.startLesson(serverID, datetime, hashValue, username, ldId, siteId, title, desc, getCountryCode(), getLanguageCode()); 
						
			return lessonId;
		
		} catch (MalformedURLException e) {
			log.error("Unable to start LAMS lesson, bad URL: '"
					+ serverAddress
					+ "', please check sakai.properties");
		} catch (ServiceException e) {
			log.error("Unable to start LAMS lesson, RPC Service Exception");
			e.printStackTrace();
		} catch (RemoteException e) {
			log.error("Unable to start LAMS lesson, RMI Remote Exception");
			e.printStackTrace();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#getServerAddress()
	 */
	public String getServerAddress() {
		return ServerConfigurationService.getString(LamstwoConstants.CONF_SERVER_ADDRRESS);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#getServerID()
	 */
	public String getServerID() {
		return ServerConfigurationService.getString(LamstwoConstants.CONF_SERVER_ID);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#getServerKey()
	 */
	public String getServerKey() {
		return ServerConfigurationService.getString(LamstwoConstants.CONF_SERVER_KEY);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#getRequestSource()
	 */
	public String getRequestSource() {
		return ServerConfigurationService.getString(LamstwoConstants.CONF_REQUEST_SOURCE);
	}
	
	public boolean isHashValid(String[] elements, String hash) {
		String generatedHash = generateHash(elements);
		log.debug("hash=" + hash + " , generated hash="+ generatedHash);
		return generatedHash.equals(hash);
	}
	
	public String generateHash(String[] elements) {
		String plaintext = new String();
		for (String str : elements) {
			plaintext += str.trim().toLowerCase();
		}
		return LamstwoUtils.sha1(plaintext);
	}

	public String getUserInfo(String username) {
		User user = null;
	       
		try {
			user = userDirectoryService.getUser(username);
		} catch (UserNotDefinedException e) {
			log.error("Could not find user with id: " + username);
			return null;
		}
		
		String[] valList = {"",
				user.getFirstName(),
				user.getLastName(),
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				user.getEmail(),
				"",
				""
		};
      
		return CSVUtil.write(valList);
	}

	private String createURL(String method) {
		String timestamp = new Long(java.lang.System.currentTimeMillis()).toString();
		String uid = getCurrentUserId();
		String [] plaintext = {timestamp, uid, method, getServerID(), getServerKey()};
		String hash = generateHash(plaintext);
		
		ResourceLoader rl = new ResourceLoader();
		Locale locale = rl.getLocale();

		String siteID = getCurrentContext();

		String url;
		try {
			url = getServerAddress() + "/LoginRequest?" 
				+ "uid=" 			+ URLEncoder.encode(uid, "UTF8") 
				+ "&method="		+ method 
				+ "&ts=" 			+ timestamp  
				+ "&sid=" 			+ getServerID()  
				+ "&hash=" 			+ hash 
				+ "&courseid=" 		+ URLEncoder.encode(siteID, "UTF8") 
				+ "&country=" 		+ locale.getCountry() 
				+ "&lang=" 			+ locale.getLanguage()
				+ "&requestSrc="	+ URLEncoder.encode(getRequestSource(), "UTF8");
		} catch (UnsupportedEncodingException e) {
			log.error("Unable to encode URL");
			throw new RuntimeException();
		}
		return url;
	}
	
	private String getCountryCode() {
		return new ResourceLoader().getLocale().getCountry();
		
	}
	
	private String getLanguageCode() {
		return new ResourceLoader().getLocale().getLanguage();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#getAuthorURL()
	 */
	public String getAuthorURL() {
		return createURL("author");
	}

	/*
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#getLearnerURL()
	 */
	public String getLearnerURL() {
		return createURL("learner");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#getMonitorURL()
	 */
	public String getMonitorURL() {
		return createURL("monitor");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic#canCreateItem()
	 */
	public Boolean canCreateItem() {
		String siteId = getCurrentContext();
		String userId = getCurrentUserId();
		log.debug("checking if can create new LAMS item for: " + userId + ", " + siteId );
		String siteRef = siteService.siteReference(siteId);
		if ( securityService.isSuperUser(userId) ) {
			// the system super user can modify any item
			return true;
		} else if (securityService.unlock(userId, ITEM_CREATE, siteRef) ) {
			// users with permission in the specified site can modify items from that site
			return true;
		}
		return false;
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
}
