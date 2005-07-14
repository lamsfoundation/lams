/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */

package org.lamsfoundation.lams.tool.noticeboard.service;

import java.util.Date;
import java.util.List;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardContentDAO;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardSessionDAO;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO;

import org.springframework.dao.DataAccessException;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;

import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionManager;

import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;

import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;

import org.lamsfoundation.lams.usermanagement.User;

import org.lamsfoundation.lams.learning.service.ILearnerService;


/**
 * An implementation of the NoticeboardService interface.
 * @author mtruong
 *
 */
public class NoticeboardServicePOJO implements INoticeboardService, ToolContentManager, ToolSessionManager {

	private NoticeboardContent nbContent;
	private INoticeboardContentDAO nbContentDAO=null;
	
	private NoticeboardSession nbSession;
	private INoticeboardSessionDAO nbSessionDAO = null;
	private ILearnerService learnerService;
	
	private NoticeboardUser nbUser;
	private INoticeboardUserDAO nbUserDAO=null;
	
	private static Logger log = Logger.getLogger(NoticeboardServicePOJO.class);

	
	/* ==============================================================================
	 * Methods for access to NoticeboardContent objects
	 * ==============================================================================
	 */
	
	
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#retrieveNoticeboard(Long)
	 */
	public NoticeboardContent retrieveNoticeboard(Long nbContentId)
	{
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
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#updateNoticeboard(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent)
	 */
	public void updateNoticeboard(NoticeboardContent nbContent)
	{
		try
		{
			nbContentDAO.updateNbContent(nbContent);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("An exception has occured while trying to update a noticeboard content object: "
                                                         + e.getMessage(),
														   e);
		}

	}
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#saveNoticeboard(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent)
	 */
	public void saveNoticeboard(NoticeboardContent nbContent)
	{
		try
		{
			nbContentDAO.saveNbContent(nbContent);
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
	
	
	/* ==============================================================================
	 * Methods for access to NoticeboardUser objects
	 * ==============================================================================
	 */
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#retrieveNoticeboardUser(java.lang.Long)
	 */
	public NoticeboardUser retrieveNoticeboardUser(Long nbUserId)
	{
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
	
	/* ===============Methods implemented from ToolContentManager =============== */
	
	/** @see org.lamsfoundation.lams.tool.ToolContentManager#copyToolContent(java.lang.Long, java.lang.Long)*/
	public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	
	    if (toContentId == null)
		    throw new ToolException("Failed to copy Noticeboard tool content. Missing parameter: toContentId");
		if (fromContentId == null)
		{
		    //use the default content Id
		    fromContentId = NoticeboardConstants.DEFAULT_CONTENT_ID;
		}
		
		//fromContentId might not have any content, in this case use default content
		//default content id might not have any contnet, throw exception
		NoticeboardContent originalNb = null;
		
		if ((originalNb = retrieveNoticeboard(fromContentId))== null) //the id given does not have content, use default content
		{
		    //use default content id to grab contents
		    NoticeboardContent defaultContent = retrieveNoticeboard(NoticeboardConstants.DEFAULT_CONTENT_ID);
		    
		    if (defaultContent != null)
		    {
		        NoticeboardContent newContent = NoticeboardContent.newInstance(defaultContent, toContentId);
		        saveNoticeboard(newContent);
		    }
		    else
		    {
		        throw new ToolException("Default content is missing. Unable to copy tool content");
		    }
		}
		else
		{
		    NoticeboardContent newNbContent = NoticeboardContent.newInstance(originalNb, toContentId);
			saveNoticeboard(newNbContent);
		}
			
		
	}
	
	/** @see org.lamsfoundation.lams.tool.ToolContentManager#setAsDefineLater(java.lang.Long)*/
	public void setAsDefineLater(Long toolContentId) throws DataMissingException, ToolException
	{
	    NoticeboardContent nbContent = getAndCheckIDandObject(toolContentId);
		
	    nbContent.setDefineLater(true);
	    //nbContent.setContentInUse(false); //if define later is set to true, then contentInUse flag should be false
	    updateNoticeboard(nbContent);
		
	}
	
	/** @see org.lamsfoundation.lams.tool.ToolContentManager#setAsRunOffline(java.lang.Long)*/
	public void setAsRunOffline(Long toolContentId) throws DataMissingException, ToolException
	{
	    NoticeboardContent nbContent = getAndCheckIDandObject(toolContentId);
	    
		nbContent.setForceOffline(true);
		updateNoticeboard(nbContent);
	}
	   
	/** @see org.lamsfoundation.lams.tool.ToolContentManager#removeToolContent(java.lang.Long)*/
	public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException, ToolException
	{	
	    NoticeboardContent nbContent =  getAndCheckIDandObject(toolContentId);
	   //if session data exist and removeSessionData=false, throw an exception
	    if ((!nbContent.getNbSessions().isEmpty()) && !removeSessionData)
	        throw new SessionDataExistsException("Delete failed: There is session data that belongs to this tool content id");
	    
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

	/* ===============Methods implemented from ToolSessionManager =============== */
	
	/** @see org.lamsfoundation.lams.tool.ToolSessionManager#createToolSession(java.lang.Long, java.lang.Long) */
	public void createToolSession(Long toolSessionId, Long toolContentId) throws ToolException
	{
	    if (toolSessionId == null || toolContentId == null)
	    {
	        String error = "Failed to create tool session. The tool session id or tool content id is invalid";
	        throw new ToolException(error);
	    }
	    

	    if ((nbContent = retrieveNoticeboard(toolContentId)) == null)
	    {
	        //use default content
		    NoticeboardContent defaultContent = retrieveNoticeboard(NoticeboardConstants.DEFAULT_CONTENT_ID);
		   
		    if (defaultContent != null)
		    {
		        NoticeboardSession newSession = new NoticeboardSession(toolSessionId, 
		                												defaultContent,
		                												new Date(System.currentTimeMillis()),
		                												NoticeboardSession.NOT_ATTEMPTED);
		        saveNoticeboardSession(newSession);
		       
		    }
		    else
		    {
		        throw new ToolException("Default content is missing. Unable to create tool session");
		    }
	    }
	    else
	    {
	        NoticeboardSession nbSession = new NoticeboardSession(toolSessionId,
												  nbContent,
												  new Date(System.currentTimeMillis()),
												  NoticeboardSession.NOT_ATTEMPTED);

	        saveNoticeboardSession(nbSession);
	    }
	    
	    
	    
	}
	
	/** @see org.lamsfoundation.lams.tool.ToolSessionManager#leaveToolSession(java.lang.Long, org.lamsfoundation.lams.usermanagement.User)*/
	public String leaveToolSession(Long toolSessionId, User learner) throws DataMissingException, ToolException
	{
	   getAndCheckIDandObject(toolSessionId);
	    
	   return learnerService.completeToolSession(toolSessionId, learner);
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
       
    
    
    
    
/*	 public boolean isToolContentIdMissing(Long id) throws ToolException
	{
	    boolean isMissing = true;
	    if(id == null)
	    {
	        throw new ToolException("Tool content id is missing. Unable to continue");
	    }
	    else
	    {
	        isMissing = false;
	    }
	    return isMissing;
	}  */
	
	/* getter setter methods to obtain the service bean */
	public INoticeboardContentDAO getNbContentDAO()
	{
		return nbContentDAO;
	}
	
	public void setNbContentDAO(INoticeboardContentDAO nbContentDAO)
	{
		this.nbContentDAO = nbContentDAO;
	}
	
	public INoticeboardSessionDAO getNbSessionDAO()
	{
	    return nbSessionDAO;
	}
	
	public void setNbSessionDAO(INoticeboardSessionDAO nbSessionDAO)
	{
	    this.nbSessionDAO = nbSessionDAO;
	}
	
	public INoticeboardUserDAO getNbUserDAO()
	{
	    return nbUserDAO;
	}
	
	public void setNbUserDAO(INoticeboardUserDAO nbUserDAO)
	{
	    this.nbUserDAO = nbUserDAO;
	}
}
