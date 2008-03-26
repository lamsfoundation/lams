/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
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

/* $$Id$$ */
package org.lams.lams.tool.wiki.service;

import java.io.InputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lams.lams.tool.wiki.WikiApplicationException;
import org.lams.lams.tool.wiki.WikiAttachment;
import org.lams.lams.tool.wiki.WikiConstants;
import org.lams.lams.tool.wiki.WikiContent;
import org.lams.lams.tool.wiki.WikiSession;
import org.lams.lams.tool.wiki.WikiUser;
import org.lams.lams.tool.wiki.dao.IWikiAttachmentDAO;
import org.lams.lams.tool.wiki.dao.IWikiContentDAO;
import org.lams.lams.tool.wiki.dao.IWikiSessionDAO;
import org.lams.lams.tool.wiki.dao.IWikiUserDAO;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.dao.DataAccessException;



/**
 * An implementation of the WikiService interface.
 * 
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 * @author mtruong
 *
 */
public class WikiServicePOJO implements IWikiService, ToolContentManager, ToolSessionManager, ToolContentImport102Manager {

	private WikiContent wikiContent;
	private IWikiContentDAO wikiContentDAO=null;
	
	private WikiSession wikiSession;
	private IWikiSessionDAO wikiSessionDAO = null;
	
	private ILearnerService learnerService;
	private ILamsToolService toolService;
	
	private WikiUser wikiUser;
	private IWikiUserDAO wikiUserDAO=null;
	
	private IWikiAttachmentDAO wikiAttachmentDAO = null;
	private IToolContentHandler wikiToolContentHandler = null;
	
	private IExportToolContentService exportContentService;
	private static Logger log = Logger.getLogger(WikiServicePOJO.class);
	
	private ICoreNotebookService coreNotebookService;

	
	/* ==============================================================================
	 * Methods for access to WikiContent objects
	 * ==============================================================================
	 */
	
	
	
	/**
	 * @see org.lams.lams.tool.wiki.service.IWikiService#retrieveWiki(Long)
	 */
	public WikiContent retrieveWiki(Long wikiContentId) throws WikiApplicationException
	{
	    if (wikiContentId == null)
	    {
	        String error = "Unable to continue. The tool content id is missing";
	        log.error(error);
	        throw new WikiApplicationException(error);
	    }
	       
		try
		{
			wikiContent = wikiContentDAO.findWikiContentById(wikiContentId);
						
		}
		catch (DataAccessException e)
		{
			throw new WikiApplicationException("An exception has occured when trying to retrieve wiki content: "
                                                         + e.getMessage(),
														   e);
		}
		
		return wikiContent;
	}
	
	/**
	 * @see org.lams.lams.tool.wiki.service.IWikiService#retrieveWikiBySessionID(Long)
	 */
	public WikiContent retrieveWikiBySessionID(Long wikiSessionId)
	{
	    if (wikiSessionId == null)
	    {
	        String error = "Unable to continue. The tool session id is missing";
	        log.error(error);
	        throw new WikiApplicationException(error);
	    }
	    
	    try
		{
			wikiContent = wikiContentDAO.getWikiContentBySession(wikiSessionId);
		}
		catch (DataAccessException e)
		{
			throw new WikiApplicationException("An exception has occured when trying to retrieve wiki content: "
                                                         + e.getMessage(),
														   e);
		}
		
		return wikiContent;
	}
	
	
	/**
	 * @see org.lams.lams.tool.wiki.service.IWikiService#saveWiki(org.lams.lams.tool.wiki.WikiContent)
	 */
	public void saveWiki(WikiContent wikiContent)
	{
		try
		{
			if ( wikiContent.getUid() == null ) {
				wikiContentDAO.saveWikiContent(wikiContent);
			} else {
				wikiContentDAO.updateWikiContent(wikiContent);
			}
		}
		catch (DataAccessException e)
		{
			throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to save the wiki content object: "
														+ e.getMessage(), e);
		}
	}
	
	/**
	 * @see org.lams.lams.tool.wiki.service.IWikiService#removeWikiSessions(org.lams.lams.tool.wiki.WikiContent)
	 */
	public void removeWikiSessionsFromContent(WikiContent wikiContent)
	{
		try
		{
		    wikiContent.getWikiSessions().clear();
		    //updateWiki(wikiContent);
		    
			wikiContentDAO.removeWikiSessions(wikiContent);
		}
		catch (DataAccessException e)
		{
			throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to remove the sessions associated with this wiki content object: "
														+ e.getMessage(), e);
		}
		
	}
	 
	/**
     * @see org.lams.lams.tool.wiki.service.IWikiService#removeWiki(org.lams.lams.tool.wiki.WikiContent)
     */
	public void removeWiki(Long wikiContentId)
	{
	    if (wikiContentId == null)
	    {
	        String error = "Unable to continue. The tool content id is missing";
	        log.error(error);
	        throw new WikiApplicationException(error);
	    }
	    
		try
		{
			wikiContentDAO.removeWiki(wikiContentId);
		}
		catch (DataAccessException e)
		{
			throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to remove this wiki content object: "
					+ e.getMessage(), e);
		}
	}
	
	/**
	 * @see org.lams.lams.tool.wiki.service.IWikiService#removeWiki(org.lams.lams.tool.wiki.WikiContent)
	 */
	public void removeWiki(WikiContent wikiContent)
	{
	    try
		{
			wikiContentDAO.removeWiki(wikiContent);
		}
		catch (DataAccessException e)
		{
			throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to remove this wiki content object: "
					+ e.getMessage(), e);
		}
	}
	
		
	/* ==============================================================================
	 * Methods for access to WikiSession objects
	 * ==============================================================================
	 */
	
	/**
	 * @see org.lams.lams.tool.wiki.service.IWikiService#retrieveWikiSession(Long)
	 */
	public WikiSession retrieveWikiSession(Long wikiSessionId)
	{
	    if (wikiSessionId == null)
	    {
	        String error = "Unable to continue. The tool session id is missing";
	        log.error(error);
	        throw new WikiApplicationException(error);
	    }
	    
	    try
	    {
	        wikiSession = wikiSessionDAO.findWikiSessionById(wikiSessionId);
	    }
	    catch (DataAccessException e)
		{
			throw new WikiApplicationException("An exception has occured when trying to retrieve wiki session object: "
                                                         + e.getMessage(),
														   e);
		}
		
		return wikiSession;
	}
	
	
	
	/**
	 * @see org.lams.lams.tool.wiki.service.IWikiService#saveWikiSession(org.lams.lams.tool.wiki.WikiSession)
	 */
	public void saveWikiSession(WikiSession wikiSession)
	{
	    try
	    {
	        WikiContent content = wikiSession.getWikiContent();
	     //   content.getWikiSessions().add(wikiSession);
	       // content.
	        
	     /*   updateWiki(content); */
	        wikiSessionDAO.saveWikiSession(wikiSession);
	    }
	    catch(DataAccessException e)
	    {
	        throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to save this wiki session: "
	                	+e.getMessage(), e);
	    }
	}
	
	/**
	 * @see org.lams.lams.tool.wiki.service.IWikiService#updateWikiSession(org.lams.lams.tool.wiki.WikiSession)
	 */
	public void updateWikiSession(WikiSession wikiSession)
	{
	    try
	    {
	        wikiSessionDAO.updateWikiSession(wikiSession);
	    }
	    catch (DataAccessException e)
	    {
	        throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to update this wiki session: "
                	+e.getMessage(), e);
	    }
	}
	
	/**
	 * @see org.lams.lams.tool.wiki.service.IWikiService#removeSession(Long)
	 */
	public void removeSession(Long wikiSessionId)
	{
	    if (wikiSessionId == null)
	    {
	        String error = "Unable to continue. The tool session id is missing";
	        log.error(error);
	        throw new WikiApplicationException(error);
	    }
	    
	    try
		{
	        WikiSession sessionToDelete = retrieveWikiSession(wikiSessionId);
	        WikiContent contentReferredBySession = sessionToDelete.getWikiContent();
	        //un-associate the session from content
	        contentReferredBySession.getWikiSessions().remove(sessionToDelete);
	        wikiSessionDAO.removeWikiSession(wikiSessionId);
	      //  updateWiki(contentReferredBySession);
		}
		catch (DataAccessException e)
		{
			throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to remove this wiki session object: "
					+ e.getMessage(), e);
		}
	}
	
	/**
	 * @see org.lams.lams.tool.wiki.service.IWikiService#removeSession(org.lams.lams.tool.wiki.WikiSession)
	 */
	public void removeSession(WikiSession wikiSession)
	{
	    try
		{
	        WikiContent contentReferredBySession = wikiSession.getWikiContent();
	        //un-associate the session from content
	        contentReferredBySession.getWikiSessions().remove(wikiSession);
	              
			wikiSessionDAO.removeWikiSession(wikiSession);
		//	updateWiki(contentReferredBySession);
		}
		catch (DataAccessException e)
		{
			throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to remove this wiki session object: "
					+ e.getMessage(), e);
		}
	}
	
	
	/**
	 * @see org.lams.lams.tool.wiki.service.IWikiService#removeWikiUsersFromSession(org.lams.lams.tool.wiki.WikiSession)
	 */
	public void removeWikiUsersFromSession(WikiSession wikiSession)
	{
		try
		{
		    wikiSession.getWikiUsers().clear();
		//    updateWikiSession(wikiSession);
		    
			wikiSessionDAO.removeWikiUsers(wikiSession);
		}
		catch (DataAccessException e)
		{
			throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to remove the users associated with this wiki session instance: "
														+ e.getMessage(), e);
		}
		
	}
	
	/**
	 * @see org.lams.lams.tool.wiki.service.IWikiService#retrieveWikiSessionByUserID(java.lang.Long)
	 */
	public WikiSession retrieveWikiSessionByUserID(Long userId)
	{
	    if (userId == null)
	    {
	        String error = "Unable to continue. The tool session id is missing";
	        log.error(error);
	        throw new WikiApplicationException(error);
	    }
	    
		try
		{
			wikiSession = wikiSessionDAO.getWikiSessionByUser(userId);
		}
		catch (DataAccessException e)
		{
			throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to retrieve wiki session instance "
														+ e.getMessage(), e);
		}
		return wikiSession;
		
	}
	
	/** @see org.lams.lams.tool.wiki.service.IWikiService#getSessionIdsFromContent(org.lams.lams.tool.wiki.WikiContent) */
	public List getSessionIdsFromContent(WikiContent content)
	{
	    List list = null;
	    try
	    {
	        list = wikiSessionDAO.getSessionsFromContent(content);
	    }
	    catch(DataAccessException e)
	    {
	        throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to the list of session ids from content "
					+ e.getMessage(), e);
	    }
	    return list;
	}
	
	/* ==============================================================================
	 * Methods for access to WikiUser objects
	 * ==============================================================================
	 */
	
	/**
	 * @see org.lams.lams.tool.wiki.service.IWikiService#retrieveWikiUser(java.lang.Long)
	 */
	public WikiUser retrieveWikiUser(Long wikiUserId, Long wikiSessionId)
	{
	    if (wikiUserId == null)
	    {
	        String error = "Unable to continue. The user id is missing";
	        log.error(error);
	        throw new WikiApplicationException(error);
	    }
	    
	    try
		{
			wikiUser = wikiUserDAO.getWikiUser(wikiUserId, wikiSessionId);
		}
		catch (DataAccessException e)
		{
			throw new WikiApplicationException("An exception has occured when trying to retrieve wiki user: "
                                                         + e.getMessage(),
														   e);
		}
		
		return wikiUser;
	}
	
	
	
	/**
	 * @see org.lams.lams.tool.wiki.service.IWikiService#saveWikiUser(org.lams.lams.tool.wiki.WikiUser)
	 */
	public void saveWikiUser(WikiUser wikiUser)
	{
		try
		{
		    WikiSession session = wikiUser.getWikiSession();
		    session.getWikiUsers().add(wikiUser);
		  //  updateWikiSession(session);
		    
			wikiUserDAO.saveWikiUser(wikiUser);
		}
		catch (DataAccessException e)
		{
			throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to save the wiki user object: "
														+ e.getMessage(), e);
		}
	}
	
	/** org.lams.lams.tool.wiki.service.IWikiService#retrieveWikiUserBySession(java.lang.Long, java.lang.Long) */
	public WikiUser retrieveWikiUserBySession(Long userId, Long sessionId)
	{
		try
		{
		  wikiUser = wikiUserDAO.getWikiUserBySession(userId, sessionId);
		}
		catch (DataAccessException e)
		{
			throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to retrive the wiki user object: "
					+ e.getMessage(), e);
		}
		
		return wikiUser;
	}
	
	/**
	 * @see org.lams.lams.tool.wiki.service.IWikiService#updateWikiUser(org.lams.lams.tool.wiki.WikiUser)
	 */
	public void updateWikiUser(WikiUser wikiUser)
	{
		try
		{
			wikiUserDAO.updateWikiUser(wikiUser);
		}
		catch (DataAccessException e)
		{
			throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to update the wiki user object: "
														+ e.getMessage(), e);
		}
	}
	
		
	/**
	 * @see org.lams.lams.tool.wiki.service.IWikiService#removeUser(org.lams.lams.tool.wiki.WikiUser)
	 */
	public void removeUser(WikiUser wikiUser)
	{
		try
		{
		    WikiSession session = wikiUser.getWikiSession();
		    session.getWikiUsers().remove(wikiUser);
		    
			wikiUserDAO.removeWikiUser(wikiUser);
			
		//	updateWikiSession(session);
		}
		catch (DataAccessException e)
		{
			throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to remove the wiki user object: "
														+ e.getMessage(), e);
		}
	}
	
	/**
	 * @see org.lams.lams.tool.wiki.service.IWikiService#removeUser(java.lang.Long)
	 */
	public void removeUser(Long wikiUserId, Long toolSessionId)
	{
	    if (wikiUserId == null)
	    {
	        String error = "Unable to continue. The user id is missing";
	        log.error(error);
	        throw new WikiApplicationException(error);
	    }
		try
		{
		    WikiUser user = retrieveWikiUser(wikiUserId, toolSessionId);
		    WikiSession session = user.getWikiSession();
		    session.getWikiUsers().remove(user);
			wikiUserDAO.removeWikiUser(wikiUserId);
			
		//	updateWikiSession(session);
		}
		catch (DataAccessException e)
		{
			throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to remove the wiki user object: "
														+ e.getMessage(), e);
		}
	}
	
	/** @see org.lams.lams.tool.wiki.service.IWikiService#addSession(java.lang.Long, org.lams.lams.tool.wiki.WikiSession) */
	public void addSession(Long wikiContentId, WikiSession session)
    {

	    if (wikiContentId == null || session == null)
	    {
	        String error = "Unable to continue. The tool content id is missing";
	        log.error(error);
	        throw new WikiApplicationException(error);
	    }
	    
        try
		{
		    wikiContentDAO.addWikiSession(wikiContentId, session);
		}
		catch (DataAccessException e)
		{
			throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to create session: "
														+ e.getMessage(), e);
		}
    }	
	
	/** @see org.lams.lams.tool.wiki.service.IWikiService#addUser(java.lang.Long, org.lams.lams.tool.wiki.WikiSession) */
	public void addUser(Long wikiSessionId, WikiUser user)
	{

	    if (wikiSessionId == null)
	    {
	        String error = "Unable to continue. The tool session id is missing";
	        log.error(error);
	        throw new WikiApplicationException(error);
	    } 
	    try
	    {
	        wikiSessionDAO.addWikiUsers(wikiSessionId, user);
	    }
	    catch (DataAccessException e)
	    {
	        throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to create user: "
					+ e.getMessage(), e);
	    }
	}
	
	/** @see org.lams.lams.tool.wiki.service.IWikiService#getNumberOfUsersInSession(org.lams.lams.tool.wiki.oticeboardSession) */
	public int getNumberOfUsersInSession(WikiSession session)
	{
	    int numberOfUsers;
	    try
	    {
	        numberOfUsers = wikiUserDAO.getNumberOfUsers(session);
	    }
	    catch (DataAccessException e)
	    {
	        throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to get the number of users in the session: "
					+ e.getMessage(), e);
	    }
	    return numberOfUsers;
	}
	
	/** @see org.lams.lams.tool.wiki.service.IWikiService#calculateTotalNumberOfUsers(java.lang.Long) */
	public int calculateTotalNumberOfUsers(Long toolContentId)
	{

	    if (toolContentId == null)
	    {
	        String error = "Unable to continue. The tool content id is missing";
	        log.error(error);
	        throw new WikiApplicationException(error);
	    }
	    
	    int totalNumberOfUsers = 0;
	    try
	    {
	        wikiContent = retrieveWiki(toolContentId);
	        List listOfSessionIds = getSessionIdsFromContent(wikiContent);
	        
	        Iterator i = listOfSessionIds.iterator();
	        
	        while(i.hasNext())
	        {
	            Long sessionId = (Long)i.next();
	            int usersInThisSession = getNumberOfUsersInSession(retrieveWikiSession(sessionId));
	            totalNumberOfUsers = totalNumberOfUsers + usersInThisSession;
	        }
	    }
	    catch (DataAccessException e)
	    {
	        throw new WikiApplicationException("EXCEPTION: An exception has occurred while calculating the total number of users in tool activity "
					+ e.getMessage(), e);
	    }
	    return totalNumberOfUsers;
	}
	
	public List getUsersBySession(Long sessionId) {
		
		if (sessionId!=null) {
			try {
				return wikiUserDAO.getWikiUsersBySession(sessionId);
			} catch (DataAccessException e) {
		        throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to get the list of users in the session: "
						+ e.getMessage(), e);
		    }
		} else {
			log.error("Unable to continue. Session id is missing");
		}
		return null;
	}
	
	/* ==============================================================================
	 * Methods for access to WikiUser objects
	 * ==============================================================================
	 */
	
	/** @see org.lams.lams.tool.wiki.service.IWikiService#retrieveAttachment(java.lang.Long) */
	public WikiAttachment retrieveAttachment(Long attachmentId)
	{
	    if (attachmentId == null)
	    {
	        String error = "Unable to continue. The attachment id is missing";
	        log.error(error);
	        throw new WikiApplicationException(error);
	    }
	    
	    try
	    {
	        return wikiAttachmentDAO.retrieveAttachment(attachmentId);
	    }
	    catch (DataAccessException e)
	    {
	        throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to retrieve the attachment "
	                + e.getMessage(), e);
	    }
	}
	
	/** @see org.lams.lams.tool.wiki.service.IWikiService#retrieveAttachmentByUuid(java.lang.Long) */
	public WikiAttachment retrieveAttachmentByUuid(Long uuid)
	{
	    if (uuid == null)
	    {
	        String error = "Unable to continue. The uuid is missing";
	        log.error(error);
	        throw new WikiApplicationException(error);
	    }
	    try
	    {
	        return wikiAttachmentDAO.retrieveAttachmentByUuid(uuid);
	    }
	    catch (DataAccessException e)
	    {
	        throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to retrieve the attachment "
	                + e.getMessage(), e);
	    }
	}
	/** @see org.lams.lams.tool.wiki.service.IWikiService#retrieveAttachment(java.lang.String) */
	public WikiAttachment retrieveAttachmentByFilename(String filename)
	{
	    if (filename == null || filename.trim().length() == 0)
	    {
	        String error = "Unable to continue. The filename is missing";
	        log.error(error);
	        throw new WikiApplicationException(error);
	    }
	    try
	    {
	        return wikiAttachmentDAO.retrieveAttachmentByFilename(filename);
	    }
	    catch (DataAccessException e)
	    {
	        throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to retrieve the attachment with filename " + filename + " "
	                + e.getMessage(), e);
	    }
	}
	
	
	/** @see org.lams.lams.tool.wiki.service.IWikiService#getAttachmentIdsFromContent(org.lams.lams.tool.wiki.WikiContent) */
	public List getAttachmentIdsFromContent(WikiContent wikiContent)
	{
	    try
	    {
	        return wikiAttachmentDAO.getAttachmentIdsFromContent(wikiContent);
	    }
	    catch (DataAccessException e)
	    {
	        throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to retrieve the list of attachment ids "
	                + e.getMessage(), e);
	    }
	}
	
	/** @see org.lams.lams.tool.wiki.service.IWikiService#saveAttachment(org.lams.lams.tool.wiki.WikiAttachment) */
	public void saveAttachment(WikiContent content, WikiAttachment attachment)
	{
	    try
	    {
	    	content.getWikiAttachments().add(attachment);
			attachment.setWikiContent(content);
			saveWiki(content);
	    }
	    catch (DataAccessException e)
	    {
	        throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to save the attachment "
	                + e.getMessage(), e);
	    }
	}
	
	/** @throws RepositoryCheckedException 
	 * @throws  
	 * @see org.lams.lams.tool.wiki.service.IWikiService#removeAttachment(org.lams.lams.tool.wiki.WikiAttachment) */
	public void removeAttachment(WikiContent content, WikiAttachment attachment) throws RepositoryCheckedException
	{
	    try
	    {
			attachment.setWikiContent(null);
			content.getWikiAttachments().remove(attachment);
			saveWiki(content);
	    }
	    catch (DataAccessException e)
	    {
	        throw new WikiApplicationException("EXCEPTION: An exception has occurred while trying to remove this attachment"
	                + e.getMessage(), e);
	    }
	}
	
	/** @throws RepositoryCheckedException 
	 * @see org.lams.lams.tool.wiki.service.IWikiService#uploadFile(java.io.InputStream, java.lang.String, java.lang.String, java.lang.String) */
	public NodeKey uploadFile(InputStream istream, String filename, String contentType, String fileType) throws RepositoryCheckedException
	{
	    return wikiToolContentHandler.uploadFile(istream, filename, contentType, fileType); 
	}
	
	/* ===============Methods implemented from ToolContentManager =============== */
	
	/** @see org.lamsfoundation.lams.tool.ToolContentManager#copyToolContent(java.lang.Long, java.lang.Long)*/
	public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	
	    if (toContentId == null)
		    throw new ToolException("Failed to copy Wiki tool content. Missing parameter: toContentId");
		if (fromContentId == null)
		{
		    //use the default content Id
		    //fromContentId = WikiConstants.DEFAULT_CONTENT_ID;
		    fromContentId = getToolDefaultContentIdBySignature(WikiConstants.TOOL_SIGNATURE);
		}
		
		//fromContentId might not have any content, in this case use default content
		//default content id might not have any contnet, throw exception
		WikiContent originalWiki = null;
		
		try {
			if ((originalWiki = retrieveWiki(fromContentId))== null) //the id given does not have content, use default content
			{
			    //use default content id to grab contents
			    WikiContent defaultContent = retrieveWiki(getToolDefaultContentIdBySignature(WikiConstants.TOOL_SIGNATURE));
			    
			    if (defaultContent != null)
			    {
			        WikiContent newContent = WikiContent.newInstance(defaultContent, toContentId, wikiToolContentHandler);
			        saveWiki(newContent);
			    }
			    else
			    {
			        throw new ToolException("Default content is missing. Unable to copy tool content");
			    }
			}
			else
			{
			    WikiContent newWikiContent = WikiContent.newInstance(originalWiki, toContentId, wikiToolContentHandler);
				saveWiki(newWikiContent);
			}
		} catch (RepositoryCheckedException e) {
			log.error("Unable to copy the tool content due to a content repository error. fromContentId "+fromContentId+" toContentId "+toContentId);
			throw new ToolException(e);
		}
			
		
	}
	
	/** @see org.lamsfoundation.lams.tool.ToolContentManager#setAsDefineLater(java.lang.Long)*/
	public void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException
	{
	    WikiContent wikiContent = getAndCheckIDandObject(toolContentId);
		
	    wikiContent.setDefineLater(value);
	    //wikiContent.setContentInUse(false); //if define later is set to true, then contentInUse flag should be false
	    saveWiki(wikiContent);
		
	}
	
	/** @see org.lamsfoundation.lams.tool.ToolContentManager#setAsRunOffline(java.lang.Long)*/
	public void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException
	{
	    WikiContent wikiContent = getAndCheckIDandObject(toolContentId);
	    
		wikiContent.setForceOffline(value);
		saveWiki(wikiContent);
	}
	   
	/** @see org.lamsfoundation.lams.tool.ToolContentManager#removeToolContent(java.lang.Long)*/
	public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException, ToolException
	{	
	    WikiContent wikiContent =  getAndCheckIDandObject(toolContentId);
	   //if session data exist and removeSessionData=false, throw an exception
	    if ((!wikiContent.getWikiSessions().isEmpty()) && !removeSessionData)
	        throw new SessionDataExistsException("Delete failed: There is session data that belongs to this tool content id");
	    
	   //remove any attachments that belong to this tool entry
	   Set attachments = wikiContent.getWikiAttachments();
	   Iterator i = attachments.iterator();
	   while(i.hasNext())
	   {
		   try
		   {
			   removeAttachment(wikiContent, (WikiAttachment)i.next());
		   }
		   catch(RepositoryCheckedException e)
		   {
			   //TODO: not sure if suppose to throw another type of exception or not
		   }
	   }
	    
	   removeWiki(toolContentId);
	}

	private WikiContent getAndCheckIDandObject(Long toolContentId) throws ToolException, DataMissingException
	{
	    if (toolContentId == null)
		    throw new ToolException("Tool content ID is missing. Unable to continue");
	   
	    WikiContent wikiContent = retrieveWiki(toolContentId);
	    if (wikiContent == null)
	        throw new DataMissingException("No tool content matches this tool content id");
	    
	    return wikiContent;
	}
	
	private WikiSession getAndCheckSessionIDandObject(Long toolSessionId) throws ToolException, DataMissingException
	{
	    if (toolSessionId == null)
		    throw new ToolException("Tool session ID is missing. Unable to continue");
	   
	    WikiSession wikiSession = retrieveWikiSession(toolSessionId);
	    if (wikiSession == null)
	        throw new DataMissingException("No tool session matches this tool session id");
	    
	    return wikiSession;
	} 
	
	/*private void checkSessionIDandObject(Long toolSessionId) throws ToolException, DataMissingException
	{
	    if (toolSessionId == null)
		    throw new ToolException("Tool session ID is missing. Unable to continue");
	   
	    WikiSession wikiSession = retrieveWikiSession(toolSessionId);
	    if (wikiSession == null)
	        throw new DataMissingException("No tool session matches this tool session id");
	} */

	/**
     * Export the XML fragment for the tool's content, along with any files needed
     * for the content.
     * @throws DataMissingException if no tool content matches the toolSessionId 
     * @throws ToolException if any other error occurs
     */

	public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
		WikiContent toolContentObj = wikiContentDAO.findWikiContentById(toolContentId);
 		if(toolContentObj == null) {
		    Long defaultContentId = getToolDefaultContentIdBySignature(WikiConstants.TOOL_SIGNATURE);
		    toolContentObj = retrieveWiki(defaultContentId);
 		}
 		if(toolContentObj == null)
 			throw new DataMissingException("Unable to find default content for the wiki tool");
 		
		try {
			//set ResourceToolContentHandler as null to avoid copy file node in repository again.
			toolContentObj = WikiContent.newInstance(toolContentObj,toolContentId,null);
			toolContentObj.setWikiSessions(null);
			exportContentService.registerFileClassForExport(WikiAttachment.class.getName(),"uuid","versionId");
			exportContentService.exportToolContent( toolContentId, toolContentObj,wikiToolContentHandler, rootPath);
		} catch (ExportToolContentException e) {
			throw new ToolException(e);
		} catch (ItemNotFoundException e) {
			throw new ToolException(e);
		} catch (RepositoryCheckedException e) {
			throw new ToolException(e);
		}
	}

    /**
     * Import the XML fragment for the tool's content, along with any files needed
     * for the content.
     * @throws ToolException if any other error occurs
     */
	 public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath,String fromVersion,String toVersion) throws ToolException {
		 try {
				exportContentService.registerFileClassForImport(WikiAttachment.class.getName()
						,"uuid","versionId","filename","fileProperty",null,null);
				
				Object toolPOJO =  exportContentService.importToolContent(toolContentPath,wikiToolContentHandler,fromVersion,toVersion);
				if(!(toolPOJO instanceof WikiContent))
					throw new ImportToolContentException("Import Noteice board tool content failed. Deserialized object is " + toolPOJO);
				WikiContent toolContentObj = (WikiContent) toolPOJO;
				
//				reset it to new toolContentId
				toolContentObj.setWikiContentId(toolContentId);
				wikiContentDAO.saveWikiContent(toolContentObj);
			} catch (ImportToolContentException e) {
				throw new ToolException(e);
			}
	}
	
	/** Get the definitions for possible output for an activity, based on the toolContentId. These may be definitions that are always
	 * available for the tool (e.g. number of marks for Multiple Choice) or a custom definition created for a particular activity
	 * such as the answer to the third question contains the word Koala and hence the need for the toolContentId
	 * @return SortedMap of ToolOutputDefinitions with the key being the name of each definition
	 */
	public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId) throws ToolException {
		return new TreeMap<String, ToolOutputDefinition>();
	}
	 

	 
	/* ===============Methods implemented from ToolSessionManager =============== */
	
	/** @see org.lamsfoundation.lams.tool.ToolSessionManager#createToolSession(java.lang.Long, java.lang.String, java.lang.Long) */
	public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException
	{
	    if (toolSessionId == null || toolContentId == null)
	    {
	        String error = "Failed to create tool session. The tool session id or tool content id is invalid";
	        throw new ToolException(error);
	    }
	    

	    if ((wikiContent = retrieveWiki(toolContentId)) == null)
	    {
	        //use default content
		    WikiContent defaultContent = retrieveWiki(getToolDefaultContentIdBySignature(WikiConstants.TOOL_SIGNATURE));
		   
		    if (defaultContent != null)
		    {
		        WikiSession newSession = new WikiSession(toolSessionId, 
		        														toolSessionName,
		                												defaultContent,
		                												new Date(System.currentTimeMillis()),
		                												WikiSession.NOT_ATTEMPTED);
		        //saveWikiSession(newSession);
		        defaultContent.getWikiSessions().add(newSession);
		        saveWiki(defaultContent);
		       
		    }
		    else
		    {
		        throw new ToolException("Default content is missing. Unable to create tool session");
		    }
	    }
	    else
	    {
	        WikiSession wikiSession = new WikiSession(toolSessionId,
	        									  toolSessionName,
												  wikiContent,
												  new Date(System.currentTimeMillis()),
												  WikiSession.NOT_ATTEMPTED);
	        
	        wikiContent.getWikiSessions().add(wikiSession);
	        saveWiki(wikiContent);
	        //saveWikiSession(wikiSession);
	    }
	    
	    
	    
	}
	
	/** @see org.lamsfoundation.lams.tool.ToolSessionManager#leaveToolSession(java.lang.Long, org.lamsfoundation.lams.usermanagement.User)*/
	public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException
	{
	    getAndCheckSessionIDandObject(toolSessionId);
	    
	   return learnerService.completeToolSession(toolSessionId, learnerId);
	}
   
	/** @see org.lamsfoundation.lams.tool.ToolSessionManager#exportToolSession(java.lang.Long)*/ 
	 public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws ToolException, DataMissingException
	{
	     getAndCheckSessionIDandObject(toolSessionId);
	    throw new UnsupportedOperationException("not yet implemented");
	}

	/** @see org.lamsfoundation.lams.tool.ToolSessionManager#exportToolSession(java.util.List) */
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws ToolException, DataMissingException
	{
        Iterator i = toolSessionIds.iterator();
        if (i.hasNext())
        {
            Long id = (Long)i.next();
            getAndCheckSessionIDandObject(id);
        }
        
        
	    throw new UnsupportedOperationException("not yet implemented");
	}
    
    /** @see org.lamsfoundation.lams.tool.ToolSessionManager#removeToolSession(java.lang.Long)*/
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException
    {
        WikiSession session = getAndCheckSessionIDandObject(toolSessionId);
        removeSession(session);
    }
	
	/** 
	 * Get the tool output for the given tool output names.
	 * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long, java.lang.Long)
	 */
	public SortedMap<String, ToolOutput> getToolOutput(List<String> names,
			Long toolSessionId, Long learnerId) {
		return new TreeMap<String,ToolOutput>();
	}

	/** 
	 * Get the tool output for the given tool output name.
	 * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long, java.lang.Long)
	 */
	public ToolOutput getToolOutput(String name, Long toolSessionId,
			Long learnerId) {
		return null;
	}

	/* ===============Methods implemented from ToolContentImport102Manager =============== */
	

    /**
     * Import the data for a 1.0.2 Wiki or HTMLWiki
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues)
    {
    	Date now = new Date();
    	WikiContent toolContentObj = new WikiContent();
    	String content = WebUtil.convertNewlines((String)importValues.get(ToolContentImport102Manager.CONTENT_BODY));
    	toolContentObj.setContent(content);
    	toolContentObj.setContentInUse(false);
    	toolContentObj.setCreatorUserId(user.getUserID().longValue());
    	toolContentObj.setDateCreated(now);
    	toolContentObj.setDateUpdated(now);
    	toolContentObj.setDefineLater(false);
    	toolContentObj.setForceOffline(false);
    	toolContentObj.setWikiContentId(toolContentId);
    	toolContentObj.setOfflineInstructions(null);
    	toolContentObj.setOnlineInstructions(null);
    	toolContentObj.setTitle((String)importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
    	toolContentObj.setReflectOnActivity(false);
    	// leave as empty, no need to set them to anything.
    	//toolContentObj.setWikiSessions(wikiSessions);
    	//toolContentObj.setWikiAttachments(wikiAttachments);
    	wikiContentDAO.saveWikiContent(toolContentObj);
    }

    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) 
    		throws ToolException, DataMissingException {
    	
    	WikiContent toolContentObj = retrieveWiki(toolContentId);
    	if ( toolContentObj == null ) {
    		throw new DataMissingException("Unable to set reflective data titled "+title
	       			+" on activity toolContentId "+toolContentId
	       			+" as the tool content does not exist.");
    	}

    	toolContentObj.setReflectOnActivity(Boolean.TRUE);
    	toolContentObj.setReflectInstructions(description);
    }
    
    //=========================================================================================
    
    public Long getToolDefaultContentIdBySignature(String toolSignature)
    {
        Long contentId = null;
    	contentId=new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));    
    	if (contentId == null)
    	{
    	    String error="Could not retrieve default content id for this tool";
    	    log.error(error);
    	    throw new WikiApplicationException(error);
    	}
	    return contentId;
    }

    /* =============== Used by Spring to "inject" the linked objects =============== */
    
	/*public IWikiContentDAO getWikiContentDAO()
	{
		return wikiContentDAO;
	}
	*/
	public void setWikiContentDAO(IWikiContentDAO wikiContentDAO)
	{
		this.wikiContentDAO = wikiContentDAO;
	}
	
	/*public IWikiSessionDAO getWikiSessionDAO()
	{
	    return wikiSessionDAO;
	}
	*/
	public void setWikiSessionDAO(IWikiSessionDAO wikiSessionDAO)
	{
	    this.wikiSessionDAO = wikiSessionDAO;
	}
	
	/*public IWikiUserDAO getWikiUserDAO()
	{
	    return wikiUserDAO;
	}
	*/
	public void setWikiUserDAO(IWikiUserDAO wikiUserDAO)
	{
	    this.wikiUserDAO = wikiUserDAO;
	}
	
    /**
     * @return Returns the learnerService.
     */
  /*  public ILearnerService getLearnerService() {
        return learnerService;
    } */
    /**
     * @param learnerService The learnerService to set.
     */
	 public void setLearnerService(ILearnerService learnerService) {
	        this.learnerService = learnerService;
	    } 
 /*   public IWikiAttachmentDAO getWikiAttachmentDAO() {
        return wikiAttachmentDAO;
    } */
   
    public void setWikiAttachmentDAO(IWikiAttachmentDAO wikiAttachmentDAO) {
        this.wikiAttachmentDAO = wikiAttachmentDAO;
    }
    /**
     * @param toolService The toolService to set.
     */
    public void setToolService(ILamsToolService toolService) {
        this.toolService = toolService;
    }

	public IToolContentHandler getWikiToolContentHandler() {
		return wikiToolContentHandler;
	}

	public void setWikiToolContentHandler(IToolContentHandler wikiToolContentHandler) {
		this.wikiToolContentHandler = wikiToolContentHandler;
	}
	
	public IExportToolContentService getExportContentService() {
		return exportContentService;
	}

	public void setExportContentService(IExportToolContentService exportContentService) {
		this.exportContentService = exportContentService;
	}
	
	public ICoreNotebookService getCoreNotebookService() {
		return coreNotebookService;
	}

	public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
		this.coreNotebookService = coreNotebookService;
	}
	
	/* =============== Wrappers Methods for Notebook Service (Reflective Option) =============== */
	
	public Long createNotebookEntry(Long id, Integer idType, String signature,
			Integer userID, String entry) {
		return coreNotebookService.createNotebookEntry(id, idType, signature, userID, "", entry);
	}

	public NotebookEntry getEntry(Long id, Integer idType, String signature,
			Integer userID) {
		
		List<NotebookEntry> list = coreNotebookService.getEntry(id, idType, signature, userID);
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}
	
	/**
	 * @param notebookEntry
	 */
	public void updateEntry(NotebookEntry notebookEntry) {
		coreNotebookService.updateEntry(notebookEntry);
	}
}
