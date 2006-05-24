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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lamsfoundation.lams.tool.noticeboard.service;

import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardAttachment;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardAttachmentDAO;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardContentDAO;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardSessionDAO;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.springframework.dao.DataAccessException;



/**
 * An implementation of the NoticeboardService interface.
 * 
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 * @author mtruong
 *
 */
public class NoticeboardServicePOJO implements INoticeboardService, ToolContentManager, ToolSessionManager {

	private NoticeboardContent nbContent;
	private INoticeboardContentDAO nbContentDAO=null;
	
	private NoticeboardSession nbSession;
	private INoticeboardSessionDAO nbSessionDAO = null;
	
	private ILearnerService learnerService;
	private ILamsToolService toolService;
	
	private NoticeboardUser nbUser;
	private INoticeboardUserDAO nbUserDAO=null;
	
	private INoticeboardAttachmentDAO nbAttachmentDAO = null;
	private IToolContentHandler nbToolContentHandler = null;
	
	private static Logger log = Logger.getLogger(NoticeboardServicePOJO.class);

	
	/* ==============================================================================
	 * Methods for access to NoticeboardContent objects
	 * ==============================================================================
	 */
	
	
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#retrieveNoticeboard(Long)
	 */
	public NoticeboardContent retrieveNoticeboard(Long nbContentId) throws NbApplicationException
	{
	    if (nbContentId == null)
	    {
	        String error = "Unable to continue. The tool content id is missing";
	        log.error(error);
	        throw new NbApplicationException(error);
	    }
	       
		try
		{
			nbContent = nbContentDAO.findNbContentById(nbContentId);
						
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("An exception has occured when trying to retrieve noticeboard content: "
                                                         + e.getMessage(),
														   e);
		}
		
		return nbContent;
	}
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#retrieveNoticeboardBySessionID(Long)
	 */
	public NoticeboardContent retrieveNoticeboardBySessionID(Long nbSessionId)
	{
	    if (nbSessionId == null)
	    {
	        String error = "Unable to continue. The tool session id is missing";
	        log.error(error);
	        throw new NbApplicationException(error);
	    }
	    
	    try
		{
			nbContent = nbContentDAO.getNbContentBySession(nbSessionId);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("An exception has occured when trying to retrieve noticeboard content: "
                                                         + e.getMessage(),
														   e);
		}
		
		return nbContent;
	}
	
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#saveNoticeboard(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent)
	 */
	public void saveNoticeboard(NoticeboardContent nbContent)
	{
		try
		{
			if ( nbContent.getUid() == null ) {
				nbContentDAO.saveNbContent(nbContent);
			} else {
				nbContentDAO.updateNbContent(nbContent);
			}
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to save the noticeboard content object: "
														+ e.getMessage(), e);
		}
	}
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#removeNoticeboardSessions(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent)
	 */
	public void removeNoticeboardSessionsFromContent(NoticeboardContent nbContent)
	{
		try
		{
		    nbContent.getNbSessions().clear();
		    //updateNoticeboard(nbContent);
		    
			nbContentDAO.removeNbSessions(nbContent);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to remove the sessions associated with this noticeboard content object: "
														+ e.getMessage(), e);
		}
		
	}
	 
	/**
     * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#removeNoticeboard(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent)
     */
	public void removeNoticeboard(Long nbContentId)
	{
	    if (nbContentId == null)
	    {
	        String error = "Unable to continue. The tool content id is missing";
	        log.error(error);
	        throw new NbApplicationException(error);
	    }
	    
		try
		{
			nbContentDAO.removeNoticeboard(nbContentId);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to remove this noticeboard content object: "
					+ e.getMessage(), e);
		}
	}
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#removeNoticeboard(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent)
	 */
	public void removeNoticeboard(NoticeboardContent nbContent)
	{
	    try
		{
			nbContentDAO.removeNoticeboard(nbContent);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to remove this noticeboard content object: "
					+ e.getMessage(), e);
		}
	}
	
		
	/* ==============================================================================
	 * Methods for access to NoticeboardSession objects
	 * ==============================================================================
	 */
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#retrieveNoticeboardSession(Long)
	 */
	public NoticeboardSession retrieveNoticeboardSession(Long nbSessionId)
	{
	    if (nbSessionId == null)
	    {
	        String error = "Unable to continue. The tool session id is missing";
	        log.error(error);
	        throw new NbApplicationException(error);
	    }
	    
	    try
	    {
	        nbSession = nbSessionDAO.findNbSessionById(nbSessionId);
	    }
	    catch (DataAccessException e)
		{
			throw new NbApplicationException("An exception has occured when trying to retrieve noticeboard session object: "
                                                         + e.getMessage(),
														   e);
		}
		
		return nbSession;
	}
	
	
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#saveNoticeboardSession(org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession)
	 */
	public void saveNoticeboardSession(NoticeboardSession nbSession)
	{
	    try
	    {
	        NoticeboardContent content = nbSession.getNbContent();
	     //   content.getNbSessions().add(nbSession);
	       // content.
	        
	     /*   updateNoticeboard(content); */
	        nbSessionDAO.saveNbSession(nbSession);
	    }
	    catch(DataAccessException e)
	    {
	        throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to save this noticeboard session: "
	                	+e.getMessage(), e);
	    }
	}
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#updateNoticeboardSession(org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession)
	 */
	public void updateNoticeboardSession(NoticeboardSession nbSession)
	{
	    try
	    {
	        nbSessionDAO.updateNbSession(nbSession);
	    }
	    catch (DataAccessException e)
	    {
	        throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to update this noticeboard session: "
                	+e.getMessage(), e);
	    }
	}
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#removeSession(Long)
	 */
	public void removeSession(Long nbSessionId)
	{
	    if (nbSessionId == null)
	    {
	        String error = "Unable to continue. The tool session id is missing";
	        log.error(error);
	        throw new NbApplicationException(error);
	    }
	    
	    try
		{
	        NoticeboardSession sessionToDelete = retrieveNoticeboardSession(nbSessionId);
	        NoticeboardContent contentReferredBySession = sessionToDelete.getNbContent();
	        //un-associate the session from content
	        contentReferredBySession.getNbSessions().remove(sessionToDelete);
	        nbSessionDAO.removeNbSession(nbSessionId);
	      //  updateNoticeboard(contentReferredBySession);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to remove this noticeboard session object: "
					+ e.getMessage(), e);
		}
	}
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#removeSession(org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession)
	 */
	public void removeSession(NoticeboardSession nbSession)
	{
	    try
		{
	        NoticeboardContent contentReferredBySession = nbSession.getNbContent();
	        //un-associate the session from content
	        contentReferredBySession.getNbSessions().remove(nbSession);
	              
			nbSessionDAO.removeNbSession(nbSession);
		//	updateNoticeboard(contentReferredBySession);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to remove this noticeboard session object: "
					+ e.getMessage(), e);
		}
	}
	
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#removeNoticeboardUsersFromSession(org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession)
	 */
	public void removeNoticeboardUsersFromSession(NoticeboardSession nbSession)
	{
		try
		{
		    nbSession.getNbUsers().clear();
		//    updateNoticeboardSession(nbSession);
		    
			nbSessionDAO.removeNbUsers(nbSession);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to remove the users associated with this noticeboard session instance: "
														+ e.getMessage(), e);
		}
		
	}
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#retrieveNbSessionByUserID(java.lang.Long)
	 */
	public NoticeboardSession retrieveNbSessionByUserID(Long userId)
	{
	    if (userId == null)
	    {
	        String error = "Unable to continue. The tool session id is missing";
	        log.error(error);
	        throw new NbApplicationException(error);
	    }
	    
		try
		{
			nbSession = nbSessionDAO.getNbSessionByUser(userId);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to retrieve noticeboard session instance "
														+ e.getMessage(), e);
		}
		return nbSession;
		
	}
	
	/** @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#getSessionIdsFromContent(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent) */
	public List getSessionIdsFromContent(NoticeboardContent content)
	{
	    List list = null;
	    try
	    {
	        list = nbSessionDAO.getSessionsFromContent(content);
	    }
	    catch(DataAccessException e)
	    {
	        throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to the list of session ids from content "
					+ e.getMessage(), e);
	    }
	    return list;
	}
	
	/* ==============================================================================
	 * Methods for access to NoticeboardUser objects
	 * ==============================================================================
	 */
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#retrieveNoticeboardUser(java.lang.Long)
	 */
	public NoticeboardUser retrieveNoticeboardUser(Long nbUserId)
	{
	    if (nbUserId == null)
	    {
	        String error = "Unable to continue. The user id is missing";
	        log.error(error);
	        throw new NbApplicationException(error);
	    }
	    
	    try
		{
			nbUser = nbUserDAO.getNbUserByID(nbUserId);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("An exception has occured when trying to retrieve noticeboard user: "
                                                         + e.getMessage(),
														   e);
		}
		
		return nbUser;
	}
	
	
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#saveNoticeboardUser(org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser)
	 */
	public void saveNoticeboardUser(NoticeboardUser nbUser)
	{
		try
		{
		    NoticeboardSession session = nbUser.getNbSession();
		    session.getNbUsers().add(nbUser);
		  //  updateNoticeboardSession(session);
		    
			nbUserDAO.saveNbUser(nbUser);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to save the noticeboard user object: "
														+ e.getMessage(), e);
		}
	}
	
	/** org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#retrieveNbUserBySession(java.lang.Long, java.lang.Long) */
	public NoticeboardUser retrieveNbUserBySession(Long userId, Long sessionId)
	{
		try
		{
		  nbUser = nbUserDAO.getNbUserBySession(userId, sessionId);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to retrive the noticeboard user object: "
					+ e.getMessage(), e);
		}
		
		return nbUser;
	}
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#updateNoticeboardUser(org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser)
	 */
	public void updateNoticeboardUser(NoticeboardUser nbUser)
	{
		try
		{
			nbUserDAO.updateNbUser(nbUser);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to update the noticeboard user object: "
														+ e.getMessage(), e);
		}
	}
	
		
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#removeUser(org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser)
	 */
	public void removeUser(NoticeboardUser nbUser)
	{
		try
		{
		    NoticeboardSession session = nbUser.getNbSession();
		    session.getNbUsers().remove(nbUser);
		    
			nbUserDAO.removeNbUser(nbUser);
			
		//	updateNoticeboardSession(session);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to remove the noticeboard user object: "
														+ e.getMessage(), e);
		}
	}
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#removeUser(java.lang.Long)
	 */
	public void removeUser(Long nbUserId)
	{
	    if (nbUserId == null)
	    {
	        String error = "Unable to continue. The user id is missing";
	        log.error(error);
	        throw new NbApplicationException(error);
	    }
		try
		{
		    NoticeboardUser user = retrieveNoticeboardUser(nbUserId);
		    NoticeboardSession session = user.getNbSession();
		    session.getNbUsers().remove(user);
			nbUserDAO.removeNbUser(nbUserId);
			
		//	updateNoticeboardSession(session);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to remove the noticeboard user object: "
														+ e.getMessage(), e);
		}
	}
	
	/** @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#addSession(java.lang.Long, org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession) */
	public void addSession(Long nbContentId, NoticeboardSession session)
    {

	    if (nbContentId == null || session == null)
	    {
	        String error = "Unable to continue. The tool content id is missing";
	        log.error(error);
	        throw new NbApplicationException(error);
	    }
	    
        try
		{
		    nbContentDAO.addNbSession(nbContentId, session);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to create session: "
														+ e.getMessage(), e);
		}
    }	
	
	/** @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#addUser(java.lang.Long, org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession) */
	public void addUser(Long nbSessionId, NoticeboardUser user)
	{

	    if (nbSessionId == null)
	    {
	        String error = "Unable to continue. The tool session id is missing";
	        log.error(error);
	        throw new NbApplicationException(error);
	    } 
	    try
	    {
	        nbSessionDAO.addNbUsers(nbSessionId, user);
	    }
	    catch (DataAccessException e)
	    {
	        throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to create user: "
					+ e.getMessage(), e);
	    }
	}
	
	/** @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#getNumberOfUsersInSession(org.lamsfoundation.lams.tool.noticeboard.oticeboardSession) */
	public int getNumberOfUsersInSession(NoticeboardSession session)
	{
	    int numberOfUsers;
	    try
	    {
	        numberOfUsers = nbUserDAO.getNumberOfUsers(session);
	    }
	    catch (DataAccessException e)
	    {
	        throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to get the number of users in the session: "
					+ e.getMessage(), e);
	    }
	    return numberOfUsers;
	}
	
	/** @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#calculateTotalNumberOfUsers(java.lang.Long) */
	public int calculateTotalNumberOfUsers(Long toolContentId)
	{

	    if (toolContentId == null)
	    {
	        String error = "Unable to continue. The tool content id is missing";
	        log.error(error);
	        throw new NbApplicationException(error);
	    }
	    
	    int totalNumberOfUsers = 0;
	    try
	    {
	        nbContent = retrieveNoticeboard(toolContentId);
	        List listOfSessionIds = getSessionIdsFromContent(nbContent);
	        
	        Iterator i = listOfSessionIds.iterator();
	        
	        while(i.hasNext())
	        {
	            Long sessionId = (Long)i.next();
	            int usersInThisSession = getNumberOfUsersInSession(retrieveNoticeboardSession(sessionId));
	            totalNumberOfUsers = totalNumberOfUsers + usersInThisSession;
	        }
	    }
	    catch (DataAccessException e)
	    {
	        throw new NbApplicationException("EXCEPTION: An exception has occurred while calculating the total number of users in tool activity "
					+ e.getMessage(), e);
	    }
	    return totalNumberOfUsers;
	}
	
	/* ==============================================================================
	 * Methods for access to NoticeboardUser objects
	 * ==============================================================================
	 */
	
	/** @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#retrieveAttachment(java.lang.Long) */
	public NoticeboardAttachment retrieveAttachment(Long attachmentId)
	{
	    if (attachmentId == null)
	    {
	        String error = "Unable to continue. The attachment id is missing";
	        log.error(error);
	        throw new NbApplicationException(error);
	    }
	    
	    try
	    {
	        return nbAttachmentDAO.retrieveAttachment(attachmentId);
	    }
	    catch (DataAccessException e)
	    {
	        throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to retrieve the attachment "
	                + e.getMessage(), e);
	    }
	}
	
	/** @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#retrieveAttachmentByUuid(java.lang.Long) */
	public NoticeboardAttachment retrieveAttachmentByUuid(Long uuid)
	{
	    if (uuid == null)
	    {
	        String error = "Unable to continue. The uuid is missing";
	        log.error(error);
	        throw new NbApplicationException(error);
	    }
	    try
	    {
	        return nbAttachmentDAO.retrieveAttachmentByUuid(uuid);
	    }
	    catch (DataAccessException e)
	    {
	        throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to retrieve the attachment "
	                + e.getMessage(), e);
	    }
	}
	/** @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#retrieveAttachment(java.lang.String) */
	public NoticeboardAttachment retrieveAttachmentByFilename(String filename)
	{
	    if (filename == null || filename.trim().length() == 0)
	    {
	        String error = "Unable to continue. The filename is missing";
	        log.error(error);
	        throw new NbApplicationException(error);
	    }
	    try
	    {
	        return nbAttachmentDAO.retrieveAttachmentByFilename(filename);
	    }
	    catch (DataAccessException e)
	    {
	        throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to retrieve the attachment with filename " + filename + " "
	                + e.getMessage(), e);
	    }
	}
	
	
	/** @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#getAttachmentIdsFromContent(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent) */
	public List getAttachmentIdsFromContent(NoticeboardContent nbContent)
	{
	    try
	    {
	        return nbAttachmentDAO.getAttachmentIdsFromContent(nbContent);
	    }
	    catch (DataAccessException e)
	    {
	        throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to retrieve the list of attachment ids "
	                + e.getMessage(), e);
	    }
	}
	
	/** @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#saveAttachment(org.lamsfoundation.lams.tool.noticeboard.NoticeboardAttachment) */
	public void saveAttachment(NoticeboardContent content, NoticeboardAttachment attachment)
	{
	    try
	    {
	    	content.getNbAttachments().add(attachment);
			attachment.setNbContent(content);
			saveNoticeboard(content);
	    }
	    catch (DataAccessException e)
	    {
	        throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to save the attachment "
	                + e.getMessage(), e);
	    }
	}
	
	/** @throws RepositoryCheckedException 
	 * @throws  
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#removeAttachment(org.lamsfoundation.lams.tool.noticeboard.NoticeboardAttachment) */
	public void removeAttachment(NoticeboardContent content, NoticeboardAttachment attachment) throws RepositoryCheckedException
	{
	    try
	    {
			attachment.setNbContent(null);
			content.getNbAttachments().remove(attachment);
			nbToolContentHandler.deleteFile(attachment.getUuid());
			saveNoticeboard(content);
	    }
	    catch (DataAccessException e)
	    {
	        throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to remove this attachment"
	                + e.getMessage(), e);
	    }
	}
	
	/** @throws RepositoryCheckedException 
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#uploadFile(java.io.InputStream, java.lang.String, java.lang.String, java.lang.String) */
	public NodeKey uploadFile(InputStream istream, String filename, String contentType, String fileType) throws RepositoryCheckedException
	{
	    return nbToolContentHandler.uploadFile(istream, filename, contentType, fileType); 
	}
	
	/* ===============Methods implemented from ToolContentManager =============== */
	
	/** @see org.lamsfoundation.lams.tool.ToolContentManager#copyToolContent(java.lang.Long, java.lang.Long)*/
	public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	
	    if (toContentId == null)
		    throw new ToolException("Failed to copy Noticeboard tool content. Missing parameter: toContentId");
		if (fromContentId == null)
		{
		    //use the default content Id
		    //fromContentId = NoticeboardConstants.DEFAULT_CONTENT_ID;
		    fromContentId = getToolDefaultContentIdBySignature(NoticeboardConstants.TOOL_SIGNATURE);
		}
		
		//fromContentId might not have any content, in this case use default content
		//default content id might not have any contnet, throw exception
		NoticeboardContent originalNb = null;
		
		try {
			if ((originalNb = retrieveNoticeboard(fromContentId))== null) //the id given does not have content, use default content
			{
			    //use default content id to grab contents
			    NoticeboardContent defaultContent = retrieveNoticeboard(getToolDefaultContentIdBySignature(NoticeboardConstants.TOOL_SIGNATURE));
			    
			    if (defaultContent != null)
			    {
			        NoticeboardContent newContent = NoticeboardContent.newInstance(defaultContent, toContentId, nbToolContentHandler);
			        saveNoticeboard(newContent);
			    }
			    else
			    {
			        throw new ToolException("Default content is missing. Unable to copy tool content");
			    }
			}
			else
			{
			    NoticeboardContent newNbContent = NoticeboardContent.newInstance(originalNb, toContentId, nbToolContentHandler);
				saveNoticeboard(newNbContent);
			}
		} catch (RepositoryCheckedException e) {
			log.error("Unable to copy the tool content due to a content repository error. fromContentId "+fromContentId+" toContentId "+toContentId);
			throw new ToolException(e);
		}
			
		
	}
	
	/** @see org.lamsfoundation.lams.tool.ToolContentManager#setAsDefineLater(java.lang.Long)*/
	public void setAsDefineLater(Long toolContentId) throws DataMissingException, ToolException
	{
	    NoticeboardContent nbContent = getAndCheckIDandObject(toolContentId);
		
	    nbContent.setDefineLater(true);
	    //nbContent.setContentInUse(false); //if define later is set to true, then contentInUse flag should be false
	    saveNoticeboard(nbContent);
		
	}
	
	/** @see org.lamsfoundation.lams.tool.ToolContentManager#setAsRunOffline(java.lang.Long)*/
	public void setAsRunOffline(Long toolContentId) throws DataMissingException, ToolException
	{
	    NoticeboardContent nbContent = getAndCheckIDandObject(toolContentId);
	    
		nbContent.setForceOffline(true);
		saveNoticeboard(nbContent);
	}
	   
	/** @see org.lamsfoundation.lams.tool.ToolContentManager#removeToolContent(java.lang.Long)*/
	public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException, ToolException
	{	
	    NoticeboardContent nbContent =  getAndCheckIDandObject(toolContentId);
	   //if session data exist and removeSessionData=false, throw an exception
	    if ((!nbContent.getNbSessions().isEmpty()) && !removeSessionData)
	        throw new SessionDataExistsException("Delete failed: There is session data that belongs to this tool content id");
	    
	   //remove any attachments that belong to this tool entry
	   Set attachments = nbContent.getNbAttachments();
	   Iterator i = attachments.iterator();
	   while(i.hasNext())
	   {
		   try
		   {
			   removeAttachment(nbContent, (NoticeboardAttachment)i.next());
		   }
		   catch(RepositoryCheckedException e)
		   {
			   //TODO: not sure if suppose to throw another type of exception or not
		   }
	   }
	    
	   removeNoticeboard(toolContentId);
	}

	private NoticeboardContent getAndCheckIDandObject(Long toolContentId) throws ToolException, DataMissingException
	{
	    if (toolContentId == null)
		    throw new ToolException("Tool content ID is missing. Unable to continue");
	   
	    NoticeboardContent nbContent = retrieveNoticeboard(toolContentId);
	    if (nbContent == null)
	        throw new DataMissingException("No tool content matches this tool content id");
	    
	    return nbContent;
	}
	
	private NoticeboardSession getAndCheckSessionIDandObject(Long toolSessionId) throws ToolException, DataMissingException
	{
	    if (toolSessionId == null)
		    throw new ToolException("Tool session ID is missing. Unable to continue");
	   
	    NoticeboardSession nbSession = retrieveNoticeboardSession(toolSessionId);
	    if (nbSession == null)
	        throw new DataMissingException("No tool session matches this tool session id");
	    
	    return nbSession;
	} 
	
	/*private void checkSessionIDandObject(Long toolSessionId) throws ToolException, DataMissingException
	{
	    if (toolSessionId == null)
		    throw new ToolException("Tool session ID is missing. Unable to continue");
	   
	    NoticeboardSession nbSession = retrieveNoticeboardSession(toolSessionId);
	    if (nbSession == null)
	        throw new DataMissingException("No tool session matches this tool session id");
	} */

	/**
     * Export the XML fragment for the tool's content, along with any files needed
     * for the content.
     * @throws DataMissingException if no tool content matches the toolSessionId 
     * @throws ToolException if any other error occurs
     */
 	public String exportToolContent(Long toolContentId) throws DataMissingException, ToolException {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * Import the XML fragment for the tool's content, along with any files needed
     * for the content.
     * @throws ToolException if any other error occurs
     */
	public String exportToolContent(List toolContentId) throws ToolException {
		// TODO Auto-generated method stub
		return null;
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
	    

	    if ((nbContent = retrieveNoticeboard(toolContentId)) == null)
	    {
	        //use default content
		    NoticeboardContent defaultContent = retrieveNoticeboard(getToolDefaultContentIdBySignature(NoticeboardConstants.TOOL_SIGNATURE));
		   
		    if (defaultContent != null)
		    {
		        NoticeboardSession newSession = new NoticeboardSession(toolSessionId, 
		        														toolSessionName,
		                												defaultContent,
		                												new Date(System.currentTimeMillis()),
		                												NoticeboardSession.NOT_ATTEMPTED);
		        //saveNoticeboardSession(newSession);
		        defaultContent.getNbSessions().add(newSession);
		        saveNoticeboard(defaultContent);
		       
		    }
		    else
		    {
		        throw new ToolException("Default content is missing. Unable to create tool session");
		    }
	    }
	    else
	    {
	        NoticeboardSession nbSession = new NoticeboardSession(toolSessionId,
	        									  toolSessionName,
												  nbContent,
												  new Date(System.currentTimeMillis()),
												  NoticeboardSession.NOT_ATTEMPTED);
	        
	        nbContent.getNbSessions().add(nbSession);
	        saveNoticeboard(nbContent);
	        //saveNoticeboardSession(nbSession);
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
        NoticeboardSession session = getAndCheckSessionIDandObject(toolSessionId);
        removeSession(session);
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
    	    throw new NbApplicationException(error);
    	}
	    return contentId;
    }
  
	/* getter setter methods to obtain the service bean */
	/*public INoticeboardContentDAO getNbContentDAO()
	{
		return nbContentDAO;
	}
	*/
	public void setNbContentDAO(INoticeboardContentDAO nbContentDAO)
	{
		this.nbContentDAO = nbContentDAO;
	}
	
	/*public INoticeboardSessionDAO getNbSessionDAO()
	{
	    return nbSessionDAO;
	}
	*/
	public void setNbSessionDAO(INoticeboardSessionDAO nbSessionDAO)
	{
	    this.nbSessionDAO = nbSessionDAO;
	}
	
	/*public INoticeboardUserDAO getNbUserDAO()
	{
	    return nbUserDAO;
	}
	*/
	public void setNbUserDAO(INoticeboardUserDAO nbUserDAO)
	{
	    this.nbUserDAO = nbUserDAO;
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
 /*   public INoticeboardAttachmentDAO getNbAttachmentDAO() {
        return nbAttachmentDAO;
    } */
   
    public void setNbAttachmentDAO(INoticeboardAttachmentDAO nbAttachmentDAO) {
        this.nbAttachmentDAO = nbAttachmentDAO;
    }
    /**
     * @param toolService The toolService to set.
     */
    public void setToolService(ILamsToolService toolService) {
        this.toolService = toolService;
    }

	public IToolContentHandler getNbToolContentHandler() {
		return nbToolContentHandler;
	}

	public void setNbToolContentHandler(IToolContentHandler nbToolContentHandler) {
		this.nbToolContentHandler = nbToolContentHandler;
	}
}
